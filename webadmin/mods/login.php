<?php

require_once "ContentFilter.class.php";

require_once "tequila/tequila.php";

require_once __DIR__.'/../views/HtmlViewGenerator.class';
require_once "PostManager.class.php";
require_once __DIR__.'/../init.php';

/**
 * Classe gérant les sessions de login.
 */
class login {
    private static $FILTER = '';
    private static $VAL = 
	    array('sciper'=>'uniqueid'
		,'username' => 'user');
    
    private $client;
    
    public function __construct() {
	$this->init_client();
    }
    
    private function init_client() {
	$oClient = new TequilaClient();

	$oClient->SetApplicationName('PecEx administration Login');
	$oClient->SetWantedAttributes(array('uniqueid','name','firstname','unit', 'unitid', 'where', 'group'));
	$oClient->SetWishedAttributes(array('email', 'title'));
	//$oClient->SetApplicationURL(PostManager::get_url());
	$oClient->SetCustomFilter(static::$FILTER);
	
	$this->client = $oClient;
    }
    
    private function authenticate() {
	$this->client->Authenticate();
	$user = User::getUserWithName($this->username);
	
	if(!$user OR ContentFilter::isUser($user->admin))
	    redirectionTo ('/admin/reject.php');
	
	$this->set_session($user);
    }
    
    private function set_session($user) {
	$_SESSION['user_username'] = $user->name;
	$_SESSION['user_authority'] = $user->admin;
	$_SESSION['user_event'] = $user->eventTag;
    }
    
    private function logout() {
	session_destroy();
	$this->client->Logout('admin/index.php');
    }
    
    public function __get($name) {
	if(!isset(static::$VAL[$name]))
	    return false;
	
	return $this->client->getValue(static::$VAL[$name]);
    }
    
    
    
    /// AFFICHAGE
    
    public function display($on = true) {
	echo $this->get_html_display($on);
    }
    
    public function get_html_display($on = true){
	if($on && !isset ($_SESSION ['user']))
	    $this->authenticate();
	
	if(PostManager::get('logout'))
	    $this->logout();
	
	$html = '<span class="userName">'.$this->username.'</span>';
	$logoutLink = '<span class="logout">'.HtmlViewGenerator::getLink('logout', PostManager::get_url_with_var('logout', null)).'</span>';
	
	$html = '<div class="login">'.$html.$logoutLink.'</div>';
	
	return $html;
    }
}

?>
