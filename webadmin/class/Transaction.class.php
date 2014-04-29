<?php
include_once 'PecExObject.class.php';


/*
	Classe d'instance de la table 'Transaction'.
	
	(Simple conteneur)
*/
class Transaction extends PecExObject {

	public static 	$table 		= 	'transactions';
	public static 	$predicat	=	'eventTag';
	public static	$header_precision=	'date';
	
	protected 		$productTag;
	protected 		$eventTag;
	protected 		$userName;
	protected 		$digest;
	protected 		$date;
	protected 		$staff;
	protected 		$checked;
	
	
	public function delete() {
		$query = "DELETE FROM transactions WHERE id=?";
		$value = array($this->id);
		submit($query,$value);
	}
	
	public function __set($property, $value) {
		if($property != 'date') {
			parent::__set($property, $value);
			return;
		}
		
		if($this->property_updated($property, $value))
			throw new Exception('Il est interdit de modifier la date d\transaction!');
	}
	
	/*
		La recherche se fait avec le 'digest'.
	*/
	public static function searchWithDigest($digest){
		$query = "SELECT*FROM transactions WHERE digest = ? ORDER BY id DESC";
		$value = array($digest);
		$req = submit($query, $value, true);
		
		return array_map('Transaction::init', $req);
	}
		
	public function check() {
		$this->checked = true;
		$this->update();
	}
}
?>