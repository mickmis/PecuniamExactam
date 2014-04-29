<?php

require_once "login.php";

require_once "NavManager.class.php";

require_once "ResearchManager.class.php";

require_once "PostManager.class.php";

/**
 * Gestionnaire de page,
 * Se charge d'afficher les page.
 * 
 * Pour des souci de sécurité l'intégralité des pages affichable par le PageManager
 * sont définie dans la variable privé VALID_PAGES
 * 
 * @author IV
 */
class PageManager {
    private static $VALID_PAGES = array('Result','Adder','Editor');
    private static $BASIC_TABLE = 'Event';
    private static $NBR_ELEMENT_PER_PAGE = 10;
    
    private $postMan;
    
    
    /**
     * 
     * @param type $postMan
     */
    public function __construct($postMan = null) {
	$this->postMan = ($postMan == null)? new PostManager(): $postMan;
    }
    
    public function display() {
	echo $this->get_html_display();
    }
    
    public function get_html_display() {
	$html = $this->getLogin().$this->getSearchArea().$this->getMenu()
		.$this->getAdderArea().$this->getPageContent().$this->getPageNavigator();
	
	return '<div class="hero-unit container pecEx_body">'.$html.'</div>';
    }
    
    public function getLogin() {
	$login = new login();
	
	return $login->get_html_display();
    }
    
    public function getSearchArea() {
	$class = (PostManager::get('for'))? PostManager::get('for'): static::$BASIC_TABLE;
	
	$search_area = '<div class="Page_SearchArea container">'.ResearchManager::get_search_area_for($class).'</div>';
	
	return $search_area;
    }
    
    public function getMenu() {
	$menu = get_include_contents(__DIR__.'/../views/MenuView.php');
	
	return $menu;
    }
    
    public function getAdderArea() {
	$sql_class = (PostManager::get('for'))? PostManager::get('for'): 'Event';
	$adder_view_toggle = (ContentFilter::asAddAuthorityFor($sql_class))?'<button class="showHide showAdder btn well">Ajouter</button>': null;
	$container = '<div class="adder_area_content empty"></div>';
	$adder_view = '<div class="adder_area">'.$adder_view_toggle.$container.'</div>';
	
	return $adder_view;
    }
    
    public function getPageContent() {
	$page = (PostManager::get('page'))? PostManager::get('page'): static::$VALID_PAGES[0];
	if($this->isValid($page)){
	    $pageContent = get_include_contents(__DIR__.'/../views/pages/'.$page.'View.php');
	    return '<div class="Page_Content container">'.$pageContent.'</div>';
	}
    
	return '';
    }
    
    public function getPageNavigator() {
	$nav = new NavManager($this->getNbrPages());
	
	return '<div classe="page_nav container">'.$nav->get_html_display().'</div>';
    }
    
    private function isValid($page) {
	return in_array($page, static::$VALID_PAGES);
    }
    
    /**
     * Retourne le nombre de pages, pour un prédicat et une valeur de prédicat précise.
     * 
     * @return int
     */
    private function getNbrPages() {
	$class = (PostManager::get('for'))? (PostManager::get('for')): static::$BASIC_TABLE;
	
	if(PostManager::post('predicat')){
	    $predicat = PostManager::post('predicat');
	    $value = PostManager::post('searchValue');
	}
	else{
	    $predicat = null;
	    $value = null;
	}
	
	$div = ($class::getNbrOfElementsWith($predicat, $value)/static::$NBR_ELEMENT_PER_PAGE);
	
	return intval($div);
    }
}

?>
