<?php
include_once 'PecExObject.class.php';


// Obsolète !!!!
/*
	Classe d'instance de la table 'Stock'.
	
	Un instance d'élément de 'stock', est toujours associé
	à un produit défini dans 'productTag'.
	
	(!)Pour supprimer un stock, il faut supprimer le produit associé.
*/
class Stock extends PecExObject {

	public static	$table	 	= 	"stock";
	public static	$predicat	=	'productTag';
	
	protected 		$productTag;
	protected 		$stock;
	protected 		$standTag;
	
	protected 		$standsStock;
	
	
	/*
		Constructeur d'instance de la table 'stock'.
		
		Idrate les attributs du stocks.
		
		Les stocks des stands sont regroupé dans un mapping,
		selon 'standTag' => 'quantité en stock', voir ----> 'Stand.class.php'.
	*/
	public function __construct($data) {
		foreach ($data as $field => $value) {
			if(property_exists($this, $field))
				$this->$field 				= 	$value;
			else
				$this->$field	= 	$value;
		}
	}
	
	public function delete() {
		return;
	}
	
}