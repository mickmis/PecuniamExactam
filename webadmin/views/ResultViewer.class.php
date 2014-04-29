<?php
require_once "ElementViewer.class.php";


/**
 * Affiche l'intégralité des resultats dans le cadre d'une recherche.
 */
class ResultViewer {
    private $results;
    
    /**
     * Gère l'affichage d'une multitude d'éléments
     * 
     * @param Array[ PecExObject ] $results_array
     */
    public function __construct($results_array) {
	if(empty($results_array)) return;
	foreach ($results_array as $id => $result)
	    $this->results[$id] = new ElementViewer($result);
    }
    
    public function display(){
	echo $this->get_html_display();
    }
    
    public function get_html_display() {
	$html = '';
	
	if(empty($this->results))
	    return;
	foreach($this->results as $element){
	   $html .= $element->get_html_display();
	}
	if(PostManager::get('searchValue'))
	    $deleteAll = '<button class="deleteAll">Tout supprimer</button>';
	else
	    $deleteAll = '';
	$html = '<div class="result_view">'.$html.$deleteAll.'</div>';
	
	return $html;
    }
}

?>
