<?php

require_once __DIR__."/../views/FormViewGenerator.class";
require_once __DIR__.'/../init.php';


/**
 * Classe de recherche permettant d'effectuer une recherche précise,
 * dans le cadre d'un prédicat.
 *
 * @author IV
 */
class ResearchManager {
     private static $BASIC_TABLE = 'Event';
    
    private $table;
    
    /**
     * Construit un gestionnaire de recherche, capable de déterminer des résultats précis.
     * 
     * @param PostManager $postManager
     */
    public function __construct(PostManager $postManager = null) {
	if($postManager == null)
	    $this->table = static::$BASIC_TABLE;
	else {
	    $this->table = $postManager->get['page'];
	}
	
    }
    
    public static function get_search_area_for($class) {
	
	$predicats = static::getPredicatsFrom($class);
	$old_predicat = static::get_old_value('predicat', ContentFilter::getPredicatFor($class));
	$select_list = FormViewGenerator::getSelectableList('predicat', $predicats, $old_predicat);
	$old_searchValue = static::get_old_value('searchValue', '');
	$inputField = '<span class="inputField">'
			    .FormViewGenerator::formLookup('searchValue', $old_searchValue, $old_predicat)
			.'</span>';
	
	$hiddenInfo = null;
	$searchContext = static::getSearchContext();
	if(!empty($searchContext)){
	    foreach($searchContext as $key => $value)
		$hiddenInfo .= FormViewGenerator::getHiddenButton ($key, $value);
	}
	$submit = FormViewGenerator::getSubmitButton('GO');
	$reset = '<a href="'.  PostManager::get_url_with_vars($searchContext).'">reset</a>';
	
	$html = FormViewGenerator::getForm($hiddenInfo.$select_list.$inputField.$submit.$reset, PostManager::get_url(), true);
	
	return $html;
    }
    
    private static function getSearchContext() {
	$get = get_clean_array($_GET);
	if(isset($get['searchValue']))
	    unset($get['searchValue']);
	
	if(isset($get['predicat']))
	    unset($get['predicat']);
	
	if(isset($get['nav']))
	    unset($get['nav']);
	
	return $get;
    }
    
    public static function getPredicatsFrom($class) {
	$predicats = null;
	foreach(ContentFilter::getFields($class) as $field){
	    $predicats[$field] = $field;
	}
	
	return $predicats;
    }
    
    private static function get_old_value($name, $defaultValue = null) {
	
	if(PostManager::post($name))
	    return PostManager::post($name);
	else if(PostManager::get($name))
	    return PostManager::get($name);
	
	return $defaultValue;
    }
    
//    /**
//     * 
//     * @return boolean
//     * @todo Permettre de conserver le prédicat et sa valeur de page en page!
//     */
//    public static function page_as_changed() {
//	if(!PostManager::session_var('page') || $_SESSION['page'] != PostManager::get('page')) {
//	    $_SESSION['page'] = PostManager::get('page');
//	    
//	    return true;
//	}
//	
//	return false;
//    }
    
}

?>
