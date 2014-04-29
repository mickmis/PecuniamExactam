<?php

require_once __DIR__.'/../init.php';

/**
 * Classe gérant les infos reçu par méthode Post où get.
 * 
 * Il suffit d'inclure cette classe dans un code pour qu'elle joue le role
 * de parseur d'information.
 */
class PostManager {
    
    public $post;
    public $get;
    
    public function __construct() {
	
	$this->post = get_clean_array($_POST);
	$this->get = get_clean_array($_GET);
    }
    
    public static function get($name) {
	if(isset($_GET[$name]))
	    return cleanInput ($_GET[$name]);
	
	return false;
    }
    
    public static function post($name) {
	if(isset($_POST[$name]))
	    return cleanInput ($_POST[$name]);
	
	return false;
    }
    
    public static function session_var($name){
	if(isset($_SESSION[$name]))
	    return $_SESSION[$name];
	
	return false;
    }
    
    /**
     * Retourne une url formaté contenant l'intégralité des info get courant,
     * Pour la page concerné
     * 
     * @param string $page
     */
    public static function get_url_with_page($page) {
	
	return static::get_unique_url($page, $this->get);
    }
    
    /**
     * Construit une url pour une page précise contenant une liste de variable précise.
     * 
     * @param string $page La page en question
     * @param array(string) $mapped_vars tableau mappé contenant les nom => valeur de variables quelconques.
     */
    public static function get_unique_url($page, $mapped_vars){
	$headerForm = null;
	
	if(empty($mapped_vars))
	    return $page;
	
	foreach($mapped_vars as $name => $value)
	    $headerForm .= '&amp;'.cleanInput($name).'='.cleanInput($value);
	
	$headerForm = '?'.substr($headerForm, 5);
	$headerForm = $page.$headerForm;
	
	return $headerForm;
    }
    
    public static function get_url_with_var($name, $value) {
	$get = get_clean_array($_GET);
	$get[$name] = $value;
	
	return static::get_url_with_vars($get);
    }
    
    public static function get_url_with_vars($mapped_vars) {
	return static::get_unique_url(getCurrentPage(), $mapped_vars);
    }
    
    /**
     * Retourne simplement l'url courant.
     */
    public static function get_url() {
	return static::get_unique_url(getCurrentPage(), $_GET);
    }
    
    public static function translateData($data, $field=null) {
	
	$TRANSLATION = array ('on' => 1, ''=>0);
	
	if(isset($TRANSLATION[$data]))
	    return $TRANSLATION[$data];
	
	return $data;
    }
    
}

?>
