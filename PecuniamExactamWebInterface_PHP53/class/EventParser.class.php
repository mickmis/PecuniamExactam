<?php

require_once __DIR__.'/../views/HtmlViewGenerator.class';

require_once "StandParser.class";
require_once "DataParser.class.php";

/**
 * Génère un bilan de l'évènement en y incluant les informations relative au Stands
 *
 * @author ibekwe
 */
class EventParser extends DataParser {
    protected static $FIELDS = array('name'=>'', 'ventes'=>'Ventes', 'caisse'=>'Caisse', 'gains'=>'C.A.');
    protected static $TABLE = 'event';
    
    protected $eventTag;
    protected $standParsers;
    
    public function __construct($event_id) {
        $this->setEvent($event_id);
        $this->setStandParsers();
    }
    
    private function setEvent($event_id) {
        $event = Event::getElementWithId($event_id);
        $this->eventTag = $event->tag;
        $query = 'SELECT  
            e.name AS name,COUNT(t.productTag) AS ventes, 
            ROUND(SUM(p.price),2) as caisse, 
            ROUND(SUM(p.price*p.earnPercentage)/100.00,2) as gains 
            FROM events e, transactions t
            INNER JOIN products p 
            ON p.tag=t.productTag 
            WHERE p.eventTag=\''.$this->eventTag.'\' AND p.eventTag=e.tag
            GROUP BY e.tag';
	
        $req = submit($query, array(), true);
        $this->header = $req[0];
    }
    
    private function setStandParsers(){
        $query = 'SELECT  s.id as id
            FROM stands s, transactions t 
            INNER JOIN products p
            ON p.tag=t.productTag 
            WHERE p.eventTag=\''.$this->eventTag.'\'
            GROUP BY id';
        
        $req = submit($query, array(), true);
	$stand_ids = $req;
        foreach($stand_ids as $stand_id)
            $this->standParsers[] = new StandParser($stand_id['id']);
    }
    
//    public function get_html_content() {
//        $html = $this->get_header_html_display();
//        foreach($this->standParsers as $standParser)
//            $html .= $standParser->get_html_content();
//        
//        $html .= $this->get_footer_html_display();
//        
//        return $html;
//    }
    
    public function get_html_content() {
	$html = $this->get_resume();
	
	foreach($this->standParsers as $standParser)
	    $html .= $standParser->get_html_content();
	
	return $html;
    }
    
    private function get_resume() {
	$header = StandParser::$FIELDS;
	$EventRow = static::mergeFirstArraysKeysWithValues($header, $this->header);
	$array_values = array($header, $EventRow);
	
	$standParsers_chunks = $this->get_chunks($this->standParsers);
	
	$html = null;
	foreach($standParsers_chunks as $standParsers)
	    $html .= $this->get_sub_array ($header, $standParsers, $array_values);
	
	return $html;
    }
    
    protected function get_sub_array($header, $standParsers, $array_values) {
	foreach($standParsers as $standParser)
	    $array_values[] = static::mergeFirstArraysKeysWithValues($header, $standParser->header);
	
	$html = HtmlViewGenerator::getArray($array_values, true);
	
	return $html;
    }
}

?>
