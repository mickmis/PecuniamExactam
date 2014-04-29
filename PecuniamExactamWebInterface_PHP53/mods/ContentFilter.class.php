<?php

/**
 * Permet d'obtenir des informations concernant le contenu
 * à afficher en fonction d'une session d'utilisateur.
 * 
 * Un login doit être effecter et les variables de sesssion user_authority et
 * event doivent exister!
 */
class ContentFilter {
    public static $NON_ADMIN_FIELDS = array('eventTag');
    public static $NON_ADMIN_ADD_AUTHORITY = array('Event');
    public static $AUTHORITY = array(1=>'user', 2=>'admin', 3=>'super admin');
    
    public static function searchContent() {
	return static::searchWithRestriction();
    }
    
    private static function searchWithRestriction() {
	$class = (PostManager::get('for'))? (PostManager::get('for')): 'Event';
	if(static::isSuperAdmin())
	    $restriction = '';
	else if($class == 'Event')
	    $restriction = 'AND tag=\''.static::event().'\'';
	else
	    $restriction = 'AND eventTag=\''.static::event ().'\'';
	
	$desc = true;
	$predicat = (PostManager::get('predicat'))? PostManager::get('predicat'): $class::$predicat;
	$data = static::elementParsing($predicat, PostManager::get('searchValue'), true);
	$similar = true;
	$start = (PostManager::get('nav'))? intVal(PostManager::get('nav')*10): 0;
	$nbr = 10;

	$results = $class::search($data, $desc, $predicat, $restriction, $similar, $start, $nbr);
	
	return $results;
    }
    
    public static function searchAllFor($class){
	if(static::isSuperAdmin())
	    $restriction = false;
	else if($class == 'Event')
	    $restriction = 'tag=\''.static::event().'\'';
	else
	    $restriction = 'eventTag=\''.static::event ().'\'';
	
	return $class::searchAll($restriction);
    }

    public static function getPredicatFor($class) {
	$predicat = $class::$predicat;
	if(in_array($predicat, static::$NON_ADMIN_FIELDS) && static::isAdmin())
		return 'name';
	
	return $predicat;
    }
    public static function getFields($class) {
	if(static::isSuperAdmin())
	    return $class::getUserFields();
	
	$fields = array_flip($class::getUserFields());
	foreach(static::$NON_ADMIN_FIELDS as $toRemoveField)
	    unset($fields[$toRemoveField]);
	
	return array_flip($fields);
    }
    
    public static function getAuthorityList() {
	$authority = static::$AUTHORITY;
	
	if(static::isSuperAdmin())
	    return $authority;
	
	unset($authority[3]);
	
	return $authority;
    }
    
    public static function asAddAuthorityFor($class) {
	if(static::isSuperAdmin())
	    return true;
	
	return !in_array($class, static::$NON_ADMIN_ADD_AUTHORITY);
    }
    
    public static function isSuperAdmin($val = false) {
	return static::is(3, $val);
    }
    public static function isAdmin($val = false) {
	return static::is(2, $val);
    }
    public static function isUser($val = false) {
	return static::is(1, $val);
    }
    
    private static function is($toCompare, $val = false) {
	$val = ($val)? $val: static::authority();
	
	return ($val == $toCompare);
    }
    
    private static function authority() {
	return $_SESSION['user_authority'];
    }
    
    private static function event() {
	
	return $_SESSION['user_event'];
    }
    
    // parsing
    private static $ELEMENT_PARSERS = array('date' => 'dateParsing', 'startTime' => 'dateParsing'
				, 'endTime' => 'dateParsing', 'admin' => 'adminParsing');
    
    public static function elementParsing($field, $value, $reverse = false) {
	if(!isset(static::$ELEMENT_PARSERS[$field]) || $value == null)
	    return $value;
	
	$func_name = ($reverse)? 'reverse'.ucfirst(static::$ELEMENT_PARSERS[$field]): static::$ELEMENT_PARSERS[$field];
	
	$toEval = 'return static::'.$func_name.'("'.$value.'");';
	
	return eval($toEval);
    }
    
    public static function reverseDateParsing($date) {
	error_log($date);
	$date = DateTime::createFromFormat('d/m/Y H:i', $date);
	return $date->format('Y-m-d H:i:s');
    }
    
    public static function dateParsing($date) {
	$date_object = DateTime::createFromFormat('Y-m-d H:i:s', $date);
	return $date_object->format('d/m/Y H:i');
    }
    
    
    public static function adminParsing($adminVal) {
	if(!isset(static::$AUTHORITY[$adminVal]))
	    return 'user';
	
	return static::$AUTHORITY[$adminVal];
    }
}
?>
