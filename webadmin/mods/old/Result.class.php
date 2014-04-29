<?php

/*
	Module d'affichage des résultats d'une recherche.
	
	Ce module affiche l'intégralité des résultats d'une table correspondant à la valeur recherché.
	La méthode 'search' de 'PecExObject' et de ses filles définit le critère de recherche, voir ----> PecExObject.php
	
	
	L'algorithme derrière le module de résultat est simple.
	La demande de suppression d'un élément est surveiller via un 'checkbox', dans le formulaire, même si l'élément
	n'est réellement supprimé qu'après l'appel à la méthode 'delete' de la Classe 'Element'.
	La modification et la surveillance des modifications des champs associer à un élément, sont délégués à 
	l'instance associé de la Classe 'Element'.
	
	(Cela permet de supprimer plusieurs valeurs tout en en éditant d'autre.)
	
	
	(!)Voir ----> Element.class.php
*/

class Result {
	private $results = null;
	private $class;
	
	
	public static $page = 0;
	public static $page_range = 10;
	public static $start;
	
	
	/*
		Constructeur.
		
		Nécessite une Classe de table et la valeur à rechercher dedant.
		
		Le module de résultat est composé d'un tableau contenant l'intégralité des éléments,
		correspondant à la recherche. 
		
		voir ----> Element.class.php
	*/
	public function __construct($class, $data) {
		$this->class = $class;
		
		if($data == null)
			$values		=	 $class::searchAll();
		else
			$values		=	 $class::search($data, true, null, null, true);
		
		if(empty($values)) return;		
		foreach ($values as $value) {
		
			$id 					= 	$value->id;
			$this->results[$id] 	= 	new Element($value, $id);
		}
		
		if(isset($_GET['page']))
			static::$page	= 	$_GET['page'];
		
		Result::$start = static::$page * static::$page_range;
	}
	
	/*
		Affiche le formulaire de visualisation et de modification des résultats.
		
		Permet de supprimer une entrée, la modification étant délégué aux élément, voir ----> Element.class.php
		
		Les résultats sont afficher dans un tableau.
	*/
	public function show() {
		if(empty($this->results)) return; // pour éviter d'afficher quoi que ce soit si il n'y a aucun résultats.
		
		$class = $this->class;
		$keys = $class::getFields();
		
		echo '<form method="post" name="result" action="index.php?page='.static::$page.'"><table><tr>';
		foreach ($keys as $key) {
			echo '<th>'.$key.'</th>';
		}
		echo '<th></th>';
		echo '</tr>';
		
		$this->showResults();
		
		echo '</table><input type="submit" value="Appliquer"/></form>';
		
		$this->showPages();
	}
	
	
	/*
		Affichage des résultats.
		
		Itère parmi les éléments et les affichages à l'aide de la méthode 'show()' de la Classe 'Element'.
		
		Suppression des éléments sélectionné.
		
		Si le champs 'deleteId', de type 'checkbox' est posté la valeur est supprimée
		via un appel à la méthode 'delete()' de la Classe 'Element'.
		
		(!) Voir ----> Element.class.php
		
	*/
	private function showResults() {
		$resultInPage = array_slice($this->results, Result::$start, Result::$page_range, true);
		
		foreach ($resultInPage as $id => $element) {
			if(isset($_POST['delete'.$id])){
				try{
					$element->delete();
					unset($this->results[$id]);
				}
				catch(Exception $e) {
					echo 'Delete Exception: '.$e->getMessage();
				}
			}
			else {
				try{
					$element->show();
				}
				catch(Exception $e) {
					echo 'Update Exception: '.$e->getMessage();
				}
			}
		}
	}
	
	private function showPages() {
		$nbrPages = count($this->results) / Result::$page_range;
		
		$pages = '';
		for($i = 0; $i < $nbrPages; $i++) {
			$pages .= '<td><a href="'.getCurrentPage().'?page='.$i.'">page '.($i+1).'</a></td>';
		}
		$pages = '<table><tr>'.$pages.'</tr></table>';
		
		echo $pages;
	}
}
?>