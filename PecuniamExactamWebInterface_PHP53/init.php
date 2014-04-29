<?php

/*													<----------->Couteau Suisse<----------->												*/




/**
 * Attention!
 * session_start() est toujours invoqué lorsque que le fichier 'init.php' est inclut dans
 * une page.
 */
session_start();
/** Sécurisation de la session et des pages.
*
* Redirige l'utilisateur non auotorisé vers la page en paramètre.
* (page de login par défaut, avec une variable de session comptant le nombre d'essai)
*/
if(isset($_SESSION['ip'])){
	$ip = getIP();
	if($_SESSION['ip'] != $ip)
	session_destroy();
}

/*
    TODO - Implémentation du nouveau système de login,
    L'ancien est désactivé, car obsolète.
*/
//if(!isPage('balanceSheetFactory.php'))
//	include_once '../mods/login.php';





/**
	<----------->Retourne les infos de base de la BDD<----------->
*/
function getDefaultDateBase() {
	if(!isset($_SESSION['db_connect'])){ 
		$dbFromXML	= loadXmlSettingsFrom(__DIR__.'/conf.xml');
		$_SESSION['db_connect']		=	$dbFromXML['connect'];
		$_SESSION['db_username']	=	$dbFromXML['username'];
		$_SESSION['db_password']	=	$dbFromXML['password'];
	}
	$db['connect']	=	$_SESSION['db_connect'];
	$db['username']	=	$_SESSION['db_username'];
	$db['password']	=	$_SESSION['db_password'];
	
	return $db;
}

/**
 *  Charge en session le fichier xml.
 */
function loadXmlSettingsFrom($file) {
	if($xml = simplexml_load_file($file)){
		$host 		= 	(String) $xml->mysqlServerAddress;
		$dbname		=	(String) $xml->mysqlServerDatabase;
		$username	=	(String) $xml->mysqlServerUsername;
		$password	=	(String) $xml->mysqlServerPassword;
		
		$connect = 'mysql:host='.$host.';dbname='.$dbname;
		
		return array('connect' => $connect, 'username' => $username, 'password' => $password);
	}else exit('Echec lors de l\'ouverture du fichier: '.$file);
}

/**
	<----------->Méthode de soumission de requête SQL<----------- >


Envoi une requête au serveur paramétré à l'aide d'un objet PDO. 
Si votre requête est sensé retourner une valeur, veuillez définir le champs return.
La requête se fait avec une préparation et une exécution.

(voir les méthodes prepare() et execute() de PDO.=


Valeur optionnel:. 
	- $return = false:
		Mettre true si la requête est sensé retourner une valeur

*/
function submit($query, $values, $return = false) {
	try {
		$db = getDefaultDateBase();
		//activation de la détection d'erreur de PDO
		$pdo_options[PDO::ATTR_ERRMODE]	 = 	PDO::ERRMODE_EXCEPTION;
		
		// création de la session de connection PDO avec détection d'erreur
		$pdo 	= 	new PDO( $db['connect'] , $db['username'], $db['password'], $pdo_options);
		
		$reqUTF8 = 	$pdo->prepare("SET NAMES UTF8"); // préparation de la requête
		$reqUTF8->execute(); // exécution de la requête avec les valeurs en paramètre
		
		$req 	= 	$pdo->prepare($query); // préparation de la requête
		$req->execute($values); // exécution de la requête avec les valeurs en paramètre
		
		if(!$return)
			return;
		
		$data = $req->fetchAll(PDO::FETCH_ASSOC);
		$req->closeCursor();
		
		return $data;
	}catch(PDOException $e){
		echo 'Error: '.$e->getMessage();
		error_log('Error: '.$e->getMessage());
	}
}

/**
	Sécurise une donnée.
	Pensez à supprimer empêche l'utilisation de balise html et de javascript.
*/
function cleanInput($data) {
	if(is_array($data)){
		foreach($data as $key=>$value)
			$data[$key] = cleanInput($value);
		return;
	}
	// Met un antislash devant chaque commande SQL
	$clean = mysql_real_escape_string($data);
	
	// retire les balises html et php restante
	$clean = strip_tags($clean);
	return $clean;
}

/*
	Sécurise l'intégralité de donnée de type POST.
	À l'utliser sur toute les pages de traitement de formulaire!
*/
function cleanFormInputData() {
	cleanInput($_GET);
	cleanInput($_POST);
}

function get_clean_array($array) {
    $clean_array = null;
    foreach ($array as $key => $value)
	$clean_array[cleanInput($key)] = cleanInput($value);
    
    return $clean_array;
}

function remove_posted_form() {
	if(!empty($_POST)) {
		echo '<SCRIPT language="Javascript">';
		echo 'location.assign(location.href);';
		echo '</SCRIPT>';
	}
}


/**
 * permet d'obtenir l'adresse ip
 */
function getIp() {
	if (isset($_SERVER['HTTP_X_FORWARDED_FOR']))
		$IP = $_SERVER['HTTP_X_FORWARDED_FOR'] ;
	elseif(isset($_SERVER['HTTP_CLIENT_IP']))
		$IP = $_SERVER['HTTP_CLIENT_IP'] ;
	else
		$IP = $_SERVER['REMOTE_ADDR'] ;
	
	return $IP;
}

function page_reload() {
	$headerForm = '';
	foreach($_GET as $key => $value)
		$headerForm .= '&amp;'.cleanInput($key).'='.cleanInput($value);

	$headerForm =	substr($headerForm, 5);
	$headerForm	=	getCurrentPage().'?'.$headerForm;

	redirectionTo($headerForm);
}

/**
 * Redirige vers la page en paramètre
 */
function redirectionTo($page) {
	header('location: '.$page);
	exit;
}

/**
 * Retourne la page actuelle
 */
function getCurrentPage() {
	return $_SERVER['PHP_SELF'];
}

function isPage($page) {
	return strpos(getCurrentPage(), $page) != false;
}

function getPath() {
	return $_SERVER['SCRIPT_NAME'];
}

function get_include_contents($filename) {
    if (is_file($filename)) {
        ob_start();
        include $filename;
        return ob_get_clean();
    }
    return false;
}


//	<----------->BONUS<----------- >


// auto load des pecExObject
function includePecExObj($class) {
    require_once __DIR__.'/class/'.ucfirst($class).'.class.php';
}
function includePageViews($class) {
    require_once __DIR__.'/views/pages/'.ucfirst($class).'.class.php';
}

spl_autoload_register('includePecExObj');
spl_autoload_register('includePageViews');


function miniTest($message=null) {
    
	if($message == null)
	    $message = 'YO je suis là!';
	newLine();
	echo '<<<<<<<<<<<<<<<<<-------------'.$message.'-------------->>>>>>>>>>>>>>';
	newLine();
}

function printTest($obj) {
	miniTest('Object');
	print_r($obj);
	miniTest('');
}
function newLine() {
	echo '<br/>';
}

?>