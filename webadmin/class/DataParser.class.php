<?php

require_once __DIR__.'/../mods/html2pdf/html2pdf.class.php';

//require_once __DIR__.'/../mods/tcpdf/tcpdf.php';
//require_once __DIR__.'/../mods/tcpdf/config/lang/eng.php';

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Le rôle d'un data parser est de générer une version formater des informations 
 * d'une table. Format HTML et PDF si demandé.
 *
 * @author ibekwe
 */
abstract class DataParser {
    protected static $type = 'General';
    protected static $FIELDS = null;
    protected static $TABLE;
    protected static $NBR_ELEMENT_PERS_PAGES = 36;
    
    protected $header;
    
    public function display( $pdf = false) {
        if($pdf) {
            $this->get_pdf_display()->Output('Bilan.pdf', 'F');
	}
        else
            echo $this->get_html_display();
	
	echo $this->get_html_display();
    }
    
    public function get_html_display() {
	
//	$html = '<!DOCTYPE html>
//		<html>
//		    <head>
//			<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
//		    </head>
//		    <body>
//			<style>
//			    .bilan_view {
//				background-color: blue;
//			    }
//			</style>
//			'.$this->get_html_content().'
//		    </body>
//		</html>';
//	
	$html = static::getCSS().$this->get_html_content();
	
	$html = '<page footer="date" backtop="10mm" backbottom="10mm" backleft="10mm" backright="10mm">
		    <page_header"> 
			<h4>Bilan <em>'.$this->header['name'].'</em></h4>
		    </page_header> 
		    <page_footer> 
			© PecuniamExactam 
		    </page_footer> 
		    '.$html.'
		</page>';
	return $html;
    }
    
    public static function getCSS($content=false){
	$style =   'h4{
			border-bottom: 1px solid;
		    }
		    th {
			border: 1px solid;
			border-top: none;
			text-align: center;
			padding: 5px;
			background-color: white;
		    }
		    td {
			border: 1px solid;
			border-top: none;
			border-bottom: none;
			padding: 5px;
		    }
		    
		    .impaire {
			background-color: whitesmoke;
		    }
		    .paire {
			background-color: #c7d5fe;
		    }
		    table {
			text-align: left;
			border-collapse: collapse;
			border: 1px solid;
			border-top: none;
			margin-top: 40px;
			width: 21cm;
			margin:auto;
		    }
		    body {
			width: 21cm;
		     }
		    ';
	
	if($content)
	    return $style;
	
	$style = '<style>'.$style.'</style>';
	
	return $style;
    }
    
    abstract public function get_html_content();
    
    public function get_header_html_display(){
        $html = null;
	$tableName = static::$TABLE;
        foreach(static::$FIELDS as $field => $field_display){
            $html .= ' | <span class="bilan_'.$tableName.' header_element">'.$field_display
		    .' <em>'.$this->header[$field].'</em></span>';
        }
	$html = substr($html, 3);
        
        $html = '<div class="bilan_'.$tableName.' header">'.$html.'</div>';
        
        return $html;
    }
    public function get_footer_html_display(){
        return null;
    }
    
    public function get_pdf_display() {
	ini_set('max_execution_time',0);
	$margin = array(10, 10, 10, 10);
	$html2pdf = new HTML2PDF('P','A4','fr', true, 'UTF-8', $margin);
	$html2pdf->setDefaultFont('Arial');
	$html2pdf->pdf->SetTitle('LITE-PDF!!!');
	$html2pdf->pdf->SetDisplayMode('real');
	$html2pdf->pdf->AcceptPageBreak();
	$html2pdf->writeHTML($this->get_html_display());
	
	return $html2pdf;
    }
    
    private function get_html_doc_info() {
	$head = '<h4> Pecuniam Exactam le '.date('d/m/Y').'</h4>';
	
	$html = $head;
	return $html;
    }
    
    protected static function mergeFirstArraysKeysWithValues($array_first_array, $values, $defaultValue = false) {
	if(!$defaultValue)
	    $defaultValue = '';
	
	$merged_array = null;
	foreach($array_first_array as $key => $value)
	    $merged_array[$key] = (isset($values[$key]))? $values[$key]: $defaultValue;
	
	return $merged_array;
    }
    
    protected function get_chunks($source) {
	$nbr_products = static::$NBR_ELEMENT_PERS_PAGES;
	
	return array_chunk($source, $nbr_products, true);
    }
    
}

?>
