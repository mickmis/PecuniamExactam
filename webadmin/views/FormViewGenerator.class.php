<?php

require_once __DIR__.'/../mods/ContentFilter.class.php';

require_once "HtmlViewGenerator.class";

/**
 * Classe de génération de formulaire.
 * 
 * @see HtmlViewGenerator
 */
class FormViewGenerator extends HtmlViewGenerator{
    private static $FUNC_MATCH = 
	    array('description' => 'getTextAreaWithContent'
	    ,'date' => 'getDateTime'
	    ,'startTime' => 'getDateTime'
	    ,'endTime' => 'getDateTime'
	    ,'staff' => 'getCheckBox'
	    ,'admin' => 'getAuthoritySelectableList'
	    ,'eventTag' => 'getEventSelectableList'
	    ,'standTag' => 'getStandSelectableList');
    
    /**
     * Methode retournant une zone de texte d'une ligne
     * 
     * @param type $name
     * @param type $value
     * @return string
     */
    public static function getTextField( $name=null, $value=null) {
	$html = '<input class="PecEx_TextField" type="text" name="'.$name.'" value="'.$value.'"/>';
	
	return $html;
    }
    
    public static function getSelectableList($name, $elements_array, $selected_name = null) {
	$select_list = null;
	foreach($elements_array as $element_name=>$value){
	    $selected = ($element_name == $selected_name)? 'selected': '';
	    $select_list .= '<option value="'.$value.'" '.$selected.'>'.$element_name.'</option>';
	}
	
	$html = '<select name="'.$name.'">'.$select_list.'</select>';
	
	return $html;
    }
    
    public static function getAuthoritySelectableList($name, $authority = null) {
	$admin_lvls = ContentFilter::getAuthorityList();
	$select_list = null;
	$selected_name = '';
	
	foreach($admin_lvls as $lvl => $admin_lvl) {
	    if($lvl == $authority)
		$selected_name = $admin_lvl;
		
	    $select_list[$admin_lvl] = intval($lvl);
	}
	
	return static::getSelectableList($name, $select_list, $selected_name);
    }
    
    public static function getEventSelectableList($name, $eventTag = null) {
	$events = ContentFilter::searchAllFor('Event');
	$eventList = null;
	$selected_name = '';
	
	foreach($events as $event) {
	    if($eventTag == $event->tag)
		$selected_name = $event->name;
	    
	    $eventList[$event->name] = $event->tag;
	}
	
	return static::getSelectableList($name, $eventList, $selected_name);
    }
    
    public static function getStandSelectableList($name, $standTag = null) {
	$stands = ContentFilter::searchAllFor('Stand');
	$standsList = null;
	$selected_name = '';
	
	foreach($stands as $stand) {
	    if($standTag == $stand->tag)
		$selected_name = $stand->name;
	    
	    $standsList[$stand->name] = $stand->tag;
	}
	
	return static::getSelectableList($name, $standsList, $selected_name);
    }
    
    /**
     * Methode retournant une zone de texte à plusieurs lignes.
     * 
     * @param string $name
     * @param string $content
     * @return string
     */
    public static function getTextAreaWithContent($name=null,$content=null) {
	if($name==null)
	    $name="textarea";
	if($content==null)
	    $content="Contenu de la zone de texte";
	
	$html = '<textarea class="PecEx_TextArea" name="'.$name.'">'.$content.'</textarea>';
	
	return $html;
    }
    
    /**
     * Methode retournant un bouton de validation.
     * 
     * @param string $name
     * @return string
     */
    public static function getSubmitButton($name=null) {
	if($name==null)
	    $name = "button";
	
	$html = '<input type="submit" value="'.$name.'"/>';
	
	return $html;
    }
    
    public static function getDateTime($name, $value = null) {
	$html = '<input class="dateTime" type="datetime" name="'.$name.'" value="'.$value.'" />';
	
	return $html;
    }
    
    public static function getCheckBox($name, $value = null) {
	$checked = ($value)? 'checked': '';
	$html = '<input type="checkbox" name="'.$name.'" '.$checked.'/>';
	
	return $html;
    }
    
    public static function getHiddenButton($name, $value) {
	
	$html = '<input type="hidden" name="'.$name.'" value="'.$value.'"/>';
	
	return $html;
    }
    
    public static function getJsButton($name,$funcName,$values=null) {
	if($values != null){
	    $params = null;
	    foreach($values as $value)
		$params .= '\''.$value.'\',';
	    $params = substr($params, 0, -1);
	}
	else
	    $params = '';
	
	$onclick = 'onclick ="'.$funcName.'('.$params.')"';
	
	$html = '<button '.$onclick.'>'.$name.'</button>';
	
	return $html;
    }
    
    public static function getForm($content, $actionPage = null, $isGetForm = false) {
	$method = ($isGetForm)? 'method="get"': 'method="post"';
	$actionPage = ($actionPage == null)? 'action="'.PostManager::get_url().'"': 'action="'.$actionPage.'"';
	
	$html = '<form class="form-horizontal" '.$method.' '.$actionPage.'><div class="control-group">'.$content.'</div></form>';
	
	return $html;
    }
    
    public static function formLookup($name, $value = null, $label = null) {
	$lookup = static::patternMatch(static::$FUNC_MATCH, $name, $value, $label);
	
	if(!$lookup)
	    return static::getTextField ($name, $value);
	else
	    return $lookup;
    }
    
    private static function patternMatch($func_string, $name, $value, $label = false){
	$key = ($label)? $label: $name;
	
	if(!isset($func_string[$key]))
	    return false;
	
	$value = ($value == null)? '': ', "'.$value.'"';
	
	$toEval = 'return static::'.$func_string[$key].'('.'"'.$name.'"'.$value.');';
	
	return eval($toEval);
    }
}

?>
