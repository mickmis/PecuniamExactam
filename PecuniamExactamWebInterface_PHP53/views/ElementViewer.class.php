<?php

require_once __DIR__.'/../mods/DataParserMaker.php';

require_once __DIR__.'/../mods/ContentFilter.class.php';
require_once "FormViewGenerator.class";

/**
 * Gère l'affichage d'un élément.
 * 
 * CSS-Element = .element_view
 * CSS-Element Field name = .element_fied_name
 */
class ElementViewer {
    
    private $element;
   
    /**
     * Gère l'affichage d'un élément
     * 
     * @param PecExObject $element
     * @todo Repenser à l'algorithme d'affichage des éléments.
     */
    public function __construct($element) {
	$this->element = $element;
    }
    
    public function display() {
	echo $this->get_html_display();
    }
    
    public function get_html_display() {
	$class = get_class($this->element);
	$fields = ContentFilter::getFields($class);
	$html = null;
	
	//$edit_button = FormViewGenerator::getJsButton('edit', 'showEditor', $vars);
	$edit_button = '<button class="btn btn-mini showEditor" type="button" data-toggle="modal" data-target="#myModal">Edit</button>';
	$copyButton = '<button class="copyButton btn btn-mini">copier</button>'
			.FormViewGenerator::getHiddenButton('id', $this->element->id)
			.FormViewGenerator::getHiddenButton('sql_class', $class);
	if($class::isRelativeCopyable()){
	    $copyGroupButton = '<button class="btn btn-mini groupCopyBtn">copy relative</button>';
	    $copyGroupButton .= FormViewGenerator::getHiddenButton('tag_name', strtolower($class).'Tag')
				.FormViewGenerator::getHiddenButton('tag_source', $this->element->tag);
	    $copyButton .= $copyGroupButton;
	}
	$supButton = '<button class="supButton btn btn-mini">supprimer</button>';
        
	if(DataParserMaker::isParsable($class)){
            $showBilan = '<button class="showBilan btn btn-mini">bilan</button>';
            $copyButton .= $showBilan;
        }
        
	$html .= HtmlViewGenerator::getElementView($this->element);
	
//	foreach($fields as $field) {
//	    $html .= HtmlViewGenerator::getElementFieldView($field, $this->element->$field);
//	}
	
	$buttonGroup = (ContentFilter::asAddAuthorityFor($class))?'<div class="btn-group hide">'.$copyButton.$edit_button.$supButton.'</div>': null;
	$html = '<div class="element_view '.$this->element->id.'">'.$buttonGroup.$html.'</div>';
	
	return $html;
    }
}

?>
