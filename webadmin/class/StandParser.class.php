<?php

require_once __DIR__.'/../views/HtmlViewGenerator.class';

require_once "DataParser.class.php";

/**
 * Génère un bilan contenant les informations de ventes de produit pour
 * un stand particulier.
 *
 * @author ibekwe
 */
class StandParser extends DataParser{
    public static $FIELDS = array('name'=>'','ventes'=>'Ventes', 'caisse'=>'Caisse', 'gains'=>'C.A.', 'stand'=>'Dû');
    protected static $PRODUCT_FIELDS = array('name' => '', 'price' => 'prix','ventes'=>'Ventes', 'earnPercentage'=>'%Agep', 'caisse'=>'Caisse', 'gains'=>'C.A.', 'stand'=>'Dû');
    protected static $TABLE = 'stand';


    protected $products;
    protected $event_tag;
    protected $event_name;
    
    public function __construct($stand_id) {
        $this->setStand($stand_id);
        $this->setProducts();
    }
    
    private function setStand($stand_id){
        $stand = Stand::getElementWithId($stand_id);
        $this->eventTag = $stand->eventTag;
        $standTag = $stand->tag;
        
        $query = 'SELECT s.name AS name, e.name AS eventName, COUNT(t.productTag) AS ventes, 
            ROUND(SUM(p.price),2) as caisse, ROUND(SUM(p.price*p.earnPercentage)/100.00,2) as gains, 
            ROUND((SUM(p.price*(100-p.earnPercentage)))/100.00,2) as stand 
            FROM stands s, events e, transactions t 
            INNER JOIN products p 
            ON p.tag=t.productTag 
            WHERE p.eventTag=\''.$this->eventTag.'\' AND p.standTag=\''.$standTag.'\' 
            AND p.standTag = s.tag AND p.eventTag=e.tag
            GROUP BY s.tag';
        
	$req = submit($query, array(), true);
	$this->header = $req[0];
	$this->event_name = $this->header['eventName'];
    }
    
    public function get_html_display() {
	$html = $this->get_html_content();

	return $html;
    }
    
    private function setProducts() {
        $query = 'SELECT p.name AS name, p.unit AS unit, 
            p.earnPercentage as earnPercentage, ROUND(p.price,2) as price, 
            COUNT(t.productTag) AS ventes, ROUND(SUM(p.price),2) AS caisse,
            ROUND((p.earnPercentage*SUM(p.price))/100.00,2) as gains, 
            ROUND(((100-p.earnPercentage)*SUM(p.price))/100.00,2) as stand
            FROM products p 
            INNER JOIN transactions t 
            ON p.tag=t.productTag
            WHERE p.eventTag=\''.$this->eventTag.'\'
            GROUP BY t.productTag';
        
         $this->products = submit($query, array(), true);
    }
    
    public function get_html_content() {
	
	$header = static::$PRODUCT_FIELDS;
	$StandRow = static::mergeFirstArraysKeysWithValues($header, $this->header);
	$array_values = array($header, $StandRow);
	
	$subProducts = $this->get_chunks($this->products);
	
	$html = null;
	foreach($subProducts as $products)
	    $html .= $this->get_sub_array ($header, $products, $array_values);
	
	return $html;
    }
    
    protected function get_sub_array($header, $products, $array_values) {
	foreach($products as $product)
	    $array_values[] = static::mergeFirstArraysKeysWithValues($header, $product);
	
	$html = HtmlViewGenerator::getArray($array_values, true);
	
	$html = '<page footer="date" backtop="10mm" backbottom="10mm" backleft="10mm" backright="10mm">
		    <page_header> 
			<h4 style="border-bottom: 1px solid;">Bilan <em>'.$this->header['name'].'</em> durant '.$this->event_name.'</h4>
		    </page_header> 
		    <page_footer> 
			© PecuniamExactam
		    </page_footer> 
		    '.static::getCSS().$html.'
		</page>';
	
	return $html;
    }
    
}

?>
