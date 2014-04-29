<?php

/**
 * Classe génératire de code html, afin de minimiser la redondance du code,
 * tout en permettant une redéfinition simple de ce même code html.
 */
class HtmlViewGenerator {
    
    protected static $PARITY_VALUES = array('paire' => 'impaire', 'impaire' => 'paire');
    protected static $parity_val = 'paire';
    
    /**
     * Génère une url html et la retourne sous forme de string.
     * Si l'url n'est pas précisé un lien vide sera généré.
     * 
     * @param string $content
     * @param string $url
     * @return string
     */
    public static function getLink($content, $url=null){
	if($url == null)
	    $url = '#';
	
	$html = '<a href="'.$url.'">'.$content.'</a>';
	
	return $html;
    }
    
    /**
     * Généère le code html d'un tableau à partir d'un tableau à plusieur dimensions,
     * les sous tableau représente des lignes.
     * 
     * ( ! 
     * ) Attention: La première dimension est considérée comme étant une ligne d'en-tête,
     * à moins que le paramètre optionnel $hasHeader soit défini comme true.
     * 
     * @param array(array, ...) $array
     * @param bool $hasHeader
     * @return string
     * @throws PecException
     */
    public static function getArray($array, $hasHeader=true) {
	if(empty($array[0]))
	    throw new PecException("Ce tableau n'est pas un tableau à 2 dimension");
	
	if($hasHeader) {
	    $html = static::getArrayRow($array[0], true);
	    unset($array[0]);
	}
	else
	    $html = '';
	
	foreach($array as $row)
	    $html .= static::getArrayRow($row);
	
	return '<table>'.$html.'</table>';
	
    }
    
    /**
     * Retourne le code html de la ligne d'une tableau.
     * 
     * 
     * @param array $row
     * @param BOOL $header Ligne d'en-tête ?
     * @return string
     */
    protected static function getArrayRow($row, $header=false) {
	$htmlRow = '';
	
	$bal = ($header)? 'th': 'td';
	
	foreach ($row as $key=>$element)
	    $htmlRow .= '<'.$bal.' class="'.$key.'_'.$bal.'">'.$element.'</'.$bal.'>';
	
	$parity = static::$parity_val;
	static::$parity_val = static::$PARITY_VALUES[static::$parity_val];
	
	return '<tr class="'.$parity.'">'.$htmlRow.'</tr>';
    }
    
    public static function getItemList($list_items, $ordered_list = false, $class=false){
	
	$class = ($class)? 'class="'.$class.'"': '';
	$balise = ($ordered_list)? 'ol': 'ul';
	
	$html = null;
	foreach($list_items as $li_class => $item) {
	    $html .= '<li class="'.$li_class.'">'.$item.'</li>';
	}
	
	$html = '<'.$balise.' '.$class.'>'.$html.'</'.$balise.'>';
	
	return $html;
    }
    
    public static function getElementFieldView($field, $data){
	$html = '<span class="element_field_view"><label>'.$field.'</label>'.ContentFilter::elementParsing($field, $data).'</span>';
	
	return $html;
    }
    
    public static function getElementView($element) {
	$class = get_class($element);
	$fields = ContentFilter::getFields($class);
	$header_html = null;
	$header_name = $class::$header_name;
	$header_name = ($header_name)? $element->$header_name: $element->$fields[0];
	$header_precison = $class::$header_precision;
	$header_precision = ($header_precison)? ContentFilter::elementParsing($header_precison, $element->$header_precison)
		: ContentFilter::elementParsing($fields[1], $element->$fields[1]);
	$header_html = $header_name.' - '.$header_precision.' - ';
	$header_html = '<a href="#" class="element_field_view_header lead well well-small">'.$header_html.'</a>';
	
	if(!$header_name)
	    unset($fields[0]);
	if(!$header_precision)
	    unset($fields[1]);
	$html = $header_html.static::getElementExtendedView($element, $fields);
	
	return $html;
    }
    
    public static function getElementExtendedView($element, $needed_fields) {
	$html = null;
	foreach($needed_fields as $field)
	    $html .= '<p><em>'.$field.':</em>'.ContentFilter::elementParsing($field, $element->$field).'</p>';
	
	$html = '<div class="element_field_extended_view well hide">'.$html.'</div>';
	
	return $html;
    }
}

?>
