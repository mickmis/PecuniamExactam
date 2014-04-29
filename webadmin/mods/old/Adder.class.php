<?php

/*
	Module d'ajout d'élément.
	
	Les éléments sont ajouté via la méthode 'insert()' de la Classe 'PecExObject', voir ----> PecEexObject.class
*/

class Adder {
	private $class;
	private $fields;
	
	
	/*
		Le module d'ajout se contruit vis à vis d'une Classe de table, voir ----> PecExObject.class.php
		
		Une hydration des champs de table est ensuite effectué.
		
		(!) La classe doit être une fille de 'PecExObject'.
	*/
	public function __construct($class) {
		$this->class = $class;
		$this->fields = $class::getFields();
		unset($this->fields[0]);
	}
	
	/*
		Affiche le formulaire d'ajout
		
		Il est contenu dans un tableau dont la ligne d'en tête correspond au champs de la table associées
		
		Ajoute une valeur précédemment postée.
		
		Si le formulaire à été intégralement remplie les valeurs sont regroupés dans un tableau
		et insérées dans la méthode 'insert()' de la classe spécifiée en instance.
	*/
	public function show(){
		$insert = false;
		
		echo '<table>';
		$header = '';
		foreach ($this->fields as $key) {
			$header.='<th>'.$key.'</th>';
		}
		echo '<tr>'.$header.'</tr>';
		
		echo '<tr><form method ="POST" name="adder" action ="index.php">';
		
		$insert = true;
		foreach ($this->fields as $key) {
		
			/*
				Les dates sont enregistré en tant que DATETIME,
				ce qui signifie que la date et l'heure doivent être précisé.
				
				Au format 'YYYY-MM-DD HH:MM'
			*/
			
			$current_dateTime	=	new DateTime();
			$current_date		=	$current_dateTime->format('Y-m-d');
			$current_time		=	$current_dateTime->format('H:i');
			
			if($key == 'startTime' || $key == 'endTime' || $key == 'date'){
				$addDate	=	'<input type="date" value="'.$current_date.'" name="addDate'.$key.'"/>';
				$addTime	= 	'<input type="time" value="'.$current_time.'" name="addTime'.$key.'"/>';
				
				echo '<td>'.$addDate.$addTime.'</td>';
				
				$insert = $insert && isset($_POST['addDate'.$key]) && isset($_POST['addTime'.$key]);
			}
			else if($key == 'checked' || $key=='available' || $key == 'admin') {
				echo '<td><input type="checkbox" name="add'.$key.'"/></td>';
			}
			else {
				if($key == 'digest' && $this->class == 'User')
					$type 	= 	'password';
				else 
					$type 	= 	'text';
				
				echo '<td><input type = "'.$type.'" name = "add'.$key.'" value = "'.$key.'" /></td>';
				
				$insert = $insert && isset($_POST['add'.$key]);
			}
		}
		echo '<input type="submit" value ="add"/></form></tr>';
		echo '</table>';
		
		if($insert) $this->insert();
	}
	
	/**
	 * Méthode d'insertion.
		
		Si formulaire est remplie, un tableau contenant ses valeurs est donné, 
		à la méthode static 'insert' de la Classe donnée en paramètre.
		
		voir ----> PecExObject.class.php ou n'importe laquelle de ses filles
		
	 * @throws Exception si jamais l'utilisateur existe déjà
	 */
	private function insert() {
		foreach ($this->fields as $key) {
			if($key == 'startTime' || $key == 'endTime' || $key =='date')
				$values[$key] = $_POST['addDate'.$key].' '.$_POST['addTime'.$key];
			else if($key == 'checked' || $key == 'available' || $key == 'admin')
				$values[$key] = (isset($_POST['add'.$key]))? 1: 0;
			else
				$values[$key] = $_POST['add'.$key];
		}
		
		$class = $this->class;
		try{
			$class::insert($values);
		}
		catch(Exception $e) {
			echo 'ADD Exception: '.$e->getMessage();
		}
	}
}
?>