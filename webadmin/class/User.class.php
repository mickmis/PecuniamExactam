<?php
include_once 'PecExObject.class.php';


/*
	Simple instance de table, représentant un utilisateur de la plateforme.
	
	(!) Merci de créer un seul compte par 'stand'.
*/
class User extends PecExObject {

	public static 	$table		=	'users';
	public static	$predicat	=	'name';
	
	protected 		$admin 	= 	false;
	protected 		$name;
	protected 		$digest;
	protected 		$eventTag;
	protected 		$cash;
	
	private			$old_digest;
	
	
	public function __construct($data) {
		parent::__construct($data);
		
		$this->old_digest = $this->digest;
	}
	
	public static function insert($inputFields) {
		if(self::user_exist($inputFields['name']))
			throw new Exception('Cette utilisateur existe déjà');
		
		if(!self::valid_inputs($inputFields))
			throw new Exception('Invalid '.get_class().' insertion with inputs: '.implode(',', $inputFields));
		
		parent::insert($inputFields);
	}
	
	/**
	 Met à jour l'entrée sql associé avec les valeurs actuelle de l'instance d'entrée de table.
	 */
	public function update() {
		$fields 		= 	self::getFields();
		unset($fields[0]); // remove of the id
	
		$queryString	= 	'';
		// construction de la requête.
		foreach ($fields as $property) {
			if(property_exists($this, $property)){
				if ($property == 'digest' && $this->digest_changed())
					$queryString	.=	 ', `'.$property.'` = SHA1(\''.$this->$property.'\')';
				else
					$queryString	.=	 ', `'.$property.'` = \''.$this->$property.'\'';
			}
		}
		$queryString	=	substr($queryString, 1);
	
		$query	 =	'UPDATE '.self::$table.' SET'.$queryString.' ';
		$query	.=	'WHERE id=\''.$this->id.'\'';
	
		submit($query, null);
	}
	
	public static function getUserWithName($username) {
	    $user = static::search($username);
	    
	    if(!$user)
		return false;
	    
	    return $user[0];
	}
	
	public function isAdmin() {
		return $this->admin == 1;
	}
	
	
	public static function user_exist($name, $password = null) {
		if($password != null)
			$precision	= 	' AND digest = SHA1(\''.$password.'\')';
		else
			$precision	=	'';
		
		$user	=	self::search($name, false, 'name', $precision);
		
		return !empty($user);
	}
	
	public function toString() {
		return 'name = '.$this->name.' and admin = '.$this->admin;
	}
	
	public function digest_changed() {
		return $this->old_digest != $this->digest;
	}
}
?>