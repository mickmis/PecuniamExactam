<?php

require_once __DIR__.'/../views/HtmlViewGenerator.class';

require_once "PostManager.class.php";

/**
 * Classe gérant simplement l'affichage des liens de changement de page.
 * 
 */
class NavManager {
    private static $MIN_PAGE = 0;
    
    private $currentPage;
    private $nbrPages;
    
    public function __construct($nbrPages) {
	$this->nbrPages = $nbrPages;
	$this->currentPage = (PostManager::get('nav'))? PostManager::get('nav'): 0;
    }
    
    public function display() {
	echo $this->get_html_display();
    }
    
    public function get_html_display() {
	
	//Avancer, reculer d'une page
	$leftArrow = $this->get_leftArrow_display();
	$rightArrow = $this->get_rightArrowDisplay();
	
	$html = '<div class="PageArrow"><ul class="pager"><li>'.$leftArrow.'</li><li>'.$rightArrow.'</li></ul></div>';
	
	return $html;
    }
    
    public function get_leftArrow_display() {
	if($this->currentPage == static::$MIN_PAGE)
	    return;
	
	$nav_to = ($this->currentPage-1);
	$html = '<span class="arrow">'.HtmlViewGenerator::getLink('<',$this->get_link($nav_to)).'</span>';
	
	return $html;
    }
    
    public function get_rightArrowDisplay() {
	if($this->currentPage == $this->nbrPages)
	    return;
	
	$nav_to = ($this->currentPage+1);
	$html = '<span class="arrow">'.HtmlViewGenerator::getLink('>',$this->get_link($nav_to)).'</span>';
	
	return $html;
    }
    
    private function get_link($page) {
	$link = PostManager::get_url_with_var('nav', $page);
	
	return $link;
    }
}

?>
