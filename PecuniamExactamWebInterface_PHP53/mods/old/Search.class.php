<?php
/*
	Module de recherche d'un élément.
	
	Il s'agit enfaite d'un simple formulaire, permettant de choisir
	une table dans la quelle effectuer une recherche.
*/

class Search {
	private $options; // liste des tables de recherche
	public 	static	$DEFAULT_SEARCH_TABLE = 'Event'; 
	private static 	$NO_SEARCH_TABLES 	= 	array();
	private static 	$fields;
	
	/*
		Le constructeur construit la liste des tables.
		
		L'objet search doit être construit via un tableau contenant
		le nom de chaque Classe représentant une table, sur laquelle une recherche est désirée.
		
		Pour empêcher la recherche dans une table, 
		il suffit de rajouter son nom dans la liste de tables interdites ---> '$NO_SEARCH_TABLES'.
	*/
	
	public function __construct($options) {
		foreach ($options as $key) {
			if(!isset(static::$NO_SEARCH_TABLES[$key]))
				$this->options[$key]	 = 	$key::$table;
		}
		
	}
	
	/*
		Affichage du formulaire de recherche.
		
	*/
	public function show() {
	
		// List déroulante
		$options = "";
		
		if(!isset($_SESSION['searchIn']))
			$_SESSION['searchIn']	=	Search::DEFAULT_SEARCH_TABLE;
		
		foreach ($this->options as $className => $option) {
			
			$selected 	= 	($className==$_SESSION['searchIn'])?'selected':'';

			$options	.=	'<option value="'.$className.'" '.$selected.'>'.$option.'</option>';
		}
		
		$predicatSelect = '';
		
		if(isset($_SESSION['searchIn'])){
			$fields = $_SESSION['searchIn']::getFields();
			
			if(!isset($_SESSION['defined_predicat']))
				$_SESSION['defined_predicat'] = $_SESSION['searchIn']::$predicat;
			
			if(in_array($_SESSION['defined_predicat'], $fields))
				$_SESSION['searchIn']::$predicat	=	 $_SESSION['defined_predicat'];
			
			foreach ($fields as $field) {
				
				if($_SESSION['searchIn']::$predicat == $field) {
					if(true)
						$selected = 'selected';
				}
				else
					$selected = '';
				
				$predicatSelect .= '<option value="'.$field.'" '.$selected.'>'.$field.'</option>';
			}
			
			$predicatSelect	=	'<select onChange="document.search0.submit()" name="defined_predicat">'.$predicatSelect.'</select>';
		}
	
		echo '<form name="search0" method="post" action="index.php">';		
		echo '<select OnChange="document.search0.submit()" name="searchIn">'.$options.'</select>';
		echo $predicatSelect;
		
		
		// définition d'une éventuelle précédente valeur de recherche 'searchFor'
		
		if(isset($_POST['searchAll']))
			$value 	=	'';
		else if(isset($_SESSION['searchFor']))
			$value 	= 	$_SESSION['searchFor'];
		else
			$value 	= 	'';
		
		echo 	'<input type="text" name="searchFor" value="'.$value.'"/>';
		echo	'</form>';
		echo	'<form method="post" >';
		echo	'<input type="submit" name="searchAll" value="Afficher toutes les entrées" />';
		echo	'</form>';
	}
}
?>