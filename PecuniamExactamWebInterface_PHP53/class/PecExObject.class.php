<?php

/*

	Classe mère de toute les Classe de table.
	
	Les méthodes de cette classe permette de récupérer et de modifier les entrées dans tout les tables.
	Toute Classe associé à une table SQL (Classe de table) doit hériter de cette Classe.
	
*/

abstract class PecExObject {
	protected static $NON_USER_FIELDS = array('digest','tag','id', 'productTag');
	protected $id;
	public	$isUpdated	=	false;
	
	// attribut de table devant être redéfinie pour chaque classe fille.
	public static $table 		=   'aucune';
	public static $predicat 	=   'name';
	public static $header_name = false;
	public static $header_precision = false;
	
	/*
		Méthode static de construction, redéfinie dans chaques classe.
		
		Cette méthode permet de faire une instance d'une Classe table,
		en spécifiant uniquement sa table.
		
		Ex: User::init($data) <-> Va construire une instance de la table users.
	*/
	public static function init($data) {
		return new static($data);
	}
	
	
	/*
		Constructeur d'une classe de table.
		
		Construit une instance d'entrée de table à partie des valeurs de champs
		spécifiée.
		
		(!) Mise à part l'id, l'intégralité des autres champs de la table doivent être spécifiés
		
		Ex: new User(array('name', 'digest', '0', 'eventTag'));
	*/
	public function __construct($data) {
	    foreach(static::getFields() as $field)
		$this->$field = $data[$field];
	}
	
	/*
		Optention d'attribut.
		
		Permet de spécifier ce qui doit être fait lorsque l'on essaie,
		d'accéder à un attribut private, protected, ou qui n'existe pas.
		
		A été implémenté afin de retourner l'attribut si il existe.
	*/
	public function __get($property) {
		if(property_exists($this, $property))
			return $this->$property;
                
                return false;
	}
	
	/* 
		Définition d'attribut.
		
		Permet de définir la valeur d'un attribut private, protected.
		Ou encore de spécifier ce qui doit être fait lorsque cette attribut
		n'existe pas.
		
		A été implémenté afin de définir la valeur, après avoir nettoyé l'objet,
		à l'aide la méthode 'cleanInput', voir 'init.php'.
	*/
	public function __set($property, $value) {
	    $clean = cleanInput($value);
	    if($this->property_updated($property, $clean))
		$this->$property = ContentFilter::elementParsing($property, $clean, true);
	    if($property == 'date' || $property == 'startTime' || $property == 'endTime')
		error_log($this->$property);
	}
	
	public function property_updated($property, $value) {
		
		if(!property_exists($this, $property)) return true;
		
		$change	= ($this->$property != $value);
		if($change)
			$this->isUpdated = true;
		
		return $change;
	}
	
	public static function valid_inputs(Array $inputs) {
		if(isset($inputs['eventTag'])) {
			if($inputs['eventTag'] == '') 
				return false;
			
			$eventTag	=	$inputs['eventTag'];
			if(!Event::eventWithTag_exists($eventTag))
				return false;
		}
		if(isset($inputs['tag'])) {
			if($inputs['tag'] == '') 
				return false;
		
			$tag	=	$inputs['tag'];
			if(static::search_exist($tag, 'tag')) 
				return false;
		}
		if(static::element_exist($inputs))
		    return false;
		foreach($inputs as $input) {
		    if($input == '' || $input == null)
			return false;
		}
		
		return true;
	}
	
	/**
	Insertion des valeurs dans la table.
	
	Permet d'insérer une nouvelle entrée dans la table,
	et retourne une instance de cette entrée de la Classe de table associée.
	
	(!) Tout les champs doivent être spécifiée, voir ----> 'init()'
	*/
	public static function insert($inputFields) {
		// vérification de la validité des données
		if(!static::valid_inputs($inputFields))
			throw new Exception(get_class().'Input: '.implode(', ', $inputFields));
		
		$queryString = '';
		foreach(static::getFields() as $field) {
                    if($field == 'id')
			$queryString .= "''";
                    else if(isset($inputFields[$field])){
			$queryString.= ',:'.$field;
			$inputFields[$field] = ContentFilter::elementParsing($field, $inputFields[$field], true);
		    }
                    else $queryString .=",''";
		}
		
		$queryString = '('.$queryString.')';
	
		// Insertion des nouvelles valeurs.
		$query = 'INSERT INTO '.static::$table.' VALUES '.$queryString;
		submit($query,$inputFields);
	
		// Récupération de la dernière entrée.
		$query = 'SELECT * FROM '.static::$table.' ORDER BY id DESC LIMIT 0, 1';
		$lastInsertion = submit($query, array(), true);
                $lastValue = new static($lastInsertion[0]);
                
                //Définition du tag si nécessaire, voir ----> setTag()
                $lastValue->setTag(true,true);
                
		return $lastValue;
	}
	
	/**
	 Met à jour l'entrée sql associé avec les valeurs actuelle de l'instance d'entrée de table.
	 */
	public function update() {
            
		$fields 		= 	static::getFields();
		unset($fields[0]); // remove of the id
                
		$queryString	= 	null;
		// construction de la requête.
		foreach ($fields as $property) {
			if(property_exists($this, $property))
			$queryString	.=	 ', `'.$property.'` = \''.$this->$property.'\'';
		}
		$queryString	=	substr($queryString, 1);
	
		$query	 =	'UPDATE '.static::$table.' SET'.$queryString.' ';
		$query	.=	'WHERE id=\''.$this->id.'\'';
	
		submit($query, null);
	}
	
	/**
		Efface l'entrée sql associé à l'instance d'entrée de table.
	 * 
	 *	@return Boolean la suppression c'est bien déroulé.
	*/
	public function delete() {
		$query 		= 	'DELETE FROM '.static::$table.' WHERE id=? ';
		$values 	= 	array($this->id);
		submit($query, $values);
		
		return true;
	}
        
        /**
         * Méthode de définition de tag, permet de générer un identifiant unique
         * pour une entrée.

           Le tag est composé du nom de l'objet, ainsi que de sont id, généré automatiquement
         * par la base sql.
            
         * La génération de tag est transparente pour l'utilisateur et doit absolument être utilisée,
         * après l'insertion de l'objet dans la base de donnée.
         * @see PecExObject::insert()
	 * 
	 * Attention: En cas de bug ou de corruption de la bdd,
	 * la base de donnée est protégé face à 100 colision, ce chiffre est volontairement
	 * écrit en dure.
         * 
         * @param BOOL $updateAfter
         * @throws PecException
         */
        protected function setTag($checkIfTagExist=false, $updateAfter = false) {
            if(!property_exists($this, 'tag'))
                    return;
            
            if($this->name == null)
                throw new PecException('Cette objet n\'a pas de nom, impossible de lui générer un tag');
            
            
            $tag = static::$table.'_'.$this->name.date('Y').'_'.$this->id;
	    
	    //vérification du tag
	    if($checkIfTagExist) {
		do{
		    $tag .= '_'.mt_rand(0,100);
		}while(static::tag_exist($tag));
	    }
	    
	    $this->tag = $tag;
            
            if($updateAfter)
                $this->update();
        }
	
	/**
	Méthode statique de recherche.
	
	Retourne l'intégralité des résultats correspondant à un prédicats.
	
	(Sauf redéfinition, cette méthode cherche vis à vis de l'attribut "name".)
	
	Return: un tableau d'instance d'entrée de table.
	
	Ex: User::search('name') <-> Retourne tout les utilisateurs dont le nom est 'name'.
	*/
	public static function search($data , $desc = true, $predicat = null, $precision = null, $similar = false, $start=0, $nbr = 10){
	    if($predicat == null) $predicat = static::$predicat;

	    if($predicat == 'startTime' || $predicat=='endTime' || $predicat == 'date'){
		    $cond =($predicat == 'endTime')? '<= ?' :'>= ?';

		    $query 	= 	'SELECT * FROM '.static::$table.' WHERE '.$predicat.' '.$cond.' '.$precision;
		    $value 	= 	array($data);
	    }
	    elseif(!$similar) {
		    $query 	= 	'SELECT * FROM '.static::$table.' WHERE '.$predicat.' = ? '.$precision;
		    $value 	= 	array($data);
	    }
	    else {
		    $query 	= 	'SELECT * FROM '.static::$table.' WHERE '.$predicat.' LIKE \'%'.$data.'%\' '.$precision;
		    $value 	= 	array();
	    }
	    if($desc)	$query .=	' ORDER BY id DESC LIMIT '.($start).','.$nbr;
	    
	    $req 	= 	submit($query, $value, true);
	
	    if(empty($req))
		return false;
	    
	    return array_map('static::init', $req);	
	}
	
	public static function search_exist($data, $predicat = null, $precision = null) {
	    $results = static::search($data, false, $predicat, $precision);

	    return !empty($results);
	}
	
	public static function searchElementWith($inputFields) {
	    $predicat = null;
	    foreach($inputFields as $field => $value)
		$predicat .= ' AND '.$field.'=\''.$value.'\'';
	    $predicat = substr($predicat, 5);
	    $query = 'SELECT * FROM '.static::$table.' WHERE '.$predicat;
	    $req = submit($query, array(), true);
	    
	    if(empty($req))
		return false;
	    
	    return static::init($req[0]);
	}
	
	public static function element_exist($inputFields) {
	    return (static::searchElementWith($inputFields) != false);
	}
        
        public static function tag_exist($tag) {
            return static::search_exist($tag, 'tag');
        }
	
	/*
		Recherche de toute les entrées de la table.
		
		Retourne toute les entrée de la table.
		
		Return: un tableau d'instance d'entrée de table.
	*/
	public static function searchAll($precision = false) {
		if($precision)
		    $precision = 'WHERE '.$precision;
		$query 	= 	'SELECT * FROM '.static::$table.' '.$precision.' ORDER BY id DESC';
		$value 	=	array();
		$req 	= 	submit($query, $value, true);
		
		$values = array_map('static::init', $req);
		
		return $values;
	}
	
	public static function relativeCopy($tag_name, $tag_value, $copyTag_value, $sql_class) {
	    
	    $predicat = $tag_name.'=\''.$tag_value.'\'';
	    $elements = $sql_class::searchAll($predicat);
	    $elements_fields = $sql_class::getFields();
	    
	    foreach($elements as $element) {
		$copyElement = null;
		foreach($elements_fields as $field)
		    $copyElement[$field] = $element->$field;
		$copyElement[$tag_name] = $copyTag_value;
		unset($copyElement['tag']);
		unset($copyElement['id']);
		
		$sql_class::insert($copyElement);
	    }
	}
	
	public static function isRelativeCopyable() {
	    return false;
	}
	
	public static function getNbrOfElementsWith($predicat=null, $value=null) {
	    
	    
		
		$query	=	'SELECT COUNT(*) AS nbr_elements FROM '.static::$table;
		if($predicat != null){
		    $query .= ' WHERE '.$predicat.' LIKE \'%'.$value.'%\'';
		}
		
		$req = submit($query, array(), true);
		
		return $req[0]['nbr_elements'];
	}
	/* 
	 * Recherche les éléments associé à l'évènement en paramètre.
	 */
	public static function getAllFromEvent($event){
		$query = 'SELECT*FROM '.static::$table.' WHERE eventTag=?';
		$value = array($event);
		$req = submit($query, $value, true);
	
		return array_map('static::init', $req);
	}
	
	/*
	 * Recherche l'élément avec l'id en paramètre.
	 */
	public static function getElementWithId($id) {
		$query = 'SELECT*FROM '.static::$table.' WHERE id=?';
		$value = array($id);
		$req = submit($query, $value, true);
                
		return new static($req[0]);
	}

	/*
		Retourne les champs de la table associé.
		
		Ex: User::getFields();
	*/
	public static function getFields() {
		return static::getFieldsFrom(static::$table);
	}
	
	public static function getUserFields() {
	    $fields = array_flip(static::getFields());
	    foreach(static::$NON_USER_FIELDS as $toRemoveField){
		if(isset($fields[$toRemoveField]))
		    unset($fields[$toRemoveField]);
	    }
	    
	    return array_reverse(array_reverse(array_flip($fields)));
	}


	/*
		Retourne le nombre de champs dans une table.
	*/
	public static function getNbrOfFields() {
		return count(static::getFields());
	}
	
	/*
		Retourne les champs de la table spécifié.
		
		Ex: PecExObject::getFieldsFrom('users');
	*/
	public static function getFieldsFrom($table) {
		$query 	= 	'SHOW COLUMNS FROM '.$table;
		$req 	= 	submit($query, array(), true);
		$fields = null;
		if($req == null) return false;
		foreach($req as $key)
			$fields[] = $key['Field'];
			
		return $fields;
	}
}
?>