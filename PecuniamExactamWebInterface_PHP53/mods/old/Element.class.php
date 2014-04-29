<?php

/*
	Module d'affichage et de gestion d'un champs dans une table.
	
	Chaque élément est associé à une table et possède un identifiant définit par le contexte
	de résultats dont il fait partie, lui évitant ainsi d'avoir le même identifiant qu'un autre élément
	et lui permettant d'envoyer ses propres champs dans un formulaire.
	
	Lorsqu'un élément détecte une modification, il met à jour l'intégralité des champs de l'entrée à laquelle
	il est associé. (.... Je sais c'est pas très optimisé tout ça ¬ ¬).
	
	Un élément implémente aussi la méthode 'delete()' invoqué par le résultat dont il fait partit ('Result').
	Cette méthode efface simplement l'entrée dans la table à laquelle il est associé.

	
	PS: Il ne s'agit pas vraiment d'un module mais plutôt d'une Classe conteneur.
*/
class Element {
	private $classInstance;
	private $keys;
	private $id;
	
	/*
		Constructeur.
		
		Il est importance de construire un élément avec une instance de l'objet dont il est le conteneur
		et un identifiant différent pour chaque élément, si vous voulez conserver la propriété de modification simultané
		des valeurs et éviter la corruption des données d'un autre élément.
	*/
	public function __construct($classInstance, $id) {
		$this->id = $id;
		$this->classInstance = $classInstance;
				
		$class = get_class($classInstance);
		$this->keys = $class::getFields();
	}
	
	/*
		Affichage et modification de chaque champs de l'élément.
		
		La modification n'est effectué que si l'existance d'un '$_POST[$keyTag'] est détectée.
		Elle consiste à modifier les attributs de l'instance de Classe de table, puis à lancer
		la méthode 'update()' de la Classe 'PecExObject', voir ----> PecExObject.class.php
		
	*/
	public function show() {
		$contents = '';
		
		$lastKeyTag;
		
		$update_flag	=	!empty($_POST);
		foreach ($this->keys as $key) {
			
			$keyTag = $this->id.'change'.$key;
			
			if($key == 'id')
				$inputForm = '<input type="checkbox" name="delete'.$this->id.'"/>';
			else if($key == 'startTime' || $key == 'endTime' || $key =='date') {
				
				if(isset($_POST[$keyTag.'Date']) && isset($_POST[$keyTag.'Time']))
					$this->classInstance->$key = $_POST[$keyTag.'Date'].' '.$_POST[$keyTag.'Time'];
				
				$datetime 	=	new DateTime($this->classInstance->$key, new DateTimeZone('Europe/Berlin'));
				$date		=	$datetime->format('Y-m-d');
				$time 		=	$datetime->format('H:i:s');
				
				$readonly = ($key == 'date' && get_class($this->classInstance) == 'Transaction')? 'readonly': '';
				
				$addDate = '<input '.$readonly.' type="date" value="'.$date.'" name="'.$keyTag.'Date"/>';
				$addTime = '<input '.$readonly.' type="time" value="'.$time.'" name="'.$keyTag.'Time"/>';
				$inputForm = $addDate.$addTime;
			}
			else if($key == 'checked' || $key == 'available' || $key == 'admin') {
				if(isset($_POST[$keyTag]))
					$this->classInstance->$key = 1;
				else if(isset($_POST[$lastKeyTag]))
					$this->classInstance->$key = 0;
					
				$checked = ($this->classInstance->$key == 1)? 'checked': '';
				
				$inputForm = '<input type="checkbox" name ="'.$keyTag.'" '.$checked.'/>';
			}
			else if($key == 'description') {
				if(isset($_POST[$keyTag]))
					$this->classInstance->$key = $_POST[$keyTag];
					
				$inputForm = '<textarea name="'.$keyTag.'">'.$this->classInstance->$key.'</textarea>';
			}
			else {
				
				if(isset($_POST[$keyTag])) {
					if($key == 'price' && !$this->validPriceUpdate($_POST[$keyTag]))
							throw new Exception('Impossible de modifier le prix de l\'évènement \''.$this->classInstance->tag.'\' : l\'évènement à déjà commencé');
					
					$this->classInstance->$key = $_POST[$keyTag];
				}
					
				$readonly = ($key == 'tag')? 'readonly': '';
					
				if(get_class($this->classInstance) == 'Stock' && $key != 'productTag')
					$type 	= 	'number';
				else 
					$type	=	'text';
				
				$inputForm = '<input type="'.$type.'" name="'.$keyTag.'" value="'.$this->classInstance->$key.'" '.$readonly.'/>';
			}
			$contents .= '<td>'.$inputForm.'</td>';
			
			$lastKeyTag = $keyTag;
		}
		// rajout de l'option bilan si il s'agit d'un évènement
		if(get_class($this->classInstance) == 'Event') {
			$id = $this->classInstance->id;
			$bilan	= 	'<a href ="../mods/balanceSheetFactory.php?eventId='.$id.'">Bilan</a>';
			$bilan 	=	'<td>'.$bilan.'</td>';
		}else $bilan = '<td></td>';
		echo '<tr>'.$contents.$bilan.'</tr>';
		
		if($update_flag)
			$this->update();
	}
	
	private function update() {
		$this->classInstance->update();
	}
	
	public function delete() {
		$this->classInstance->delete();
	}
	
	public function validPriceUpdate($price) {
		if(get_class($this->classInstance) != 'Product') return true;
		if($price == $this->classInstance->price) return true;
		
		return !$this->classInstance->eventStarted();
	}
}
?>