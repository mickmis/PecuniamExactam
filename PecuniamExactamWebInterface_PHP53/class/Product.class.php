<?php
include_once 'PecExObject.class.php';
include_once 'Stock.class.php';
include_once 'Transaction.class.php';


/*
	Classe de la table 'products'.
	
	Voir ----> 'PecExObject.class.php';
	
	Un produit est associé à une entrée dans la table stock,
	et aux transaction dont il fait l'objet.
	
	Le produit de tag 'product_list_version' sert signaler l'ajout,
	ou la modification d'un produit dans la table sql.
	Cela permet de maintenant différent client à jour.
*/
class Product extends PecExObject {

	public static 	$table = 'products';
	public static 	$predicat	=	'name';
	
	protected 		$name;
	protected 		$tag;
	protected 		$unit;
	protected 		$description;
	protected		$price;
	protected 		$earnPercentage;
	protected 		$eventTag;
	protected 		$standTag;
	protected 		$available;
	
	
	/*
		Redéfintion du constructeur, voir ---> 'PecExObject::__construct()'.
		
         *	Attention, un produit ayant fait l'objet de transactions, ne peut plus être
                supprimé.
         *      C'est pourquoi un tableau d'instance de la table 'transactions',
		fait aussi partie de ses attributs.
	*/
	public function __construct($data) {
		parent::__construct($data);
	}
	
	/*
		Met à jour la liste des produits.
		
		En augmentant l'attribut price de la liste des produits.
	*/
	public static function updateProductListVersion() {
		$query = "UPDATE products SET price = price+1 WHERE tag = 'product_list_version'";
		$value = array();
		submit($query, $value);
	}
	
	
	/*
		Redéfiniton insert une entrée dans la table, voir ----> 'PecExObject::insert()'.
		
		Redéfinie afin de signaler immédiatement la modification à l'aide du numéro de liste de produit.
		voir ----> 'updateProductListVersion()'
	*/
	public static function insert($args) {
		$product = parent::insert($args);
		
		Product::updateProductListVersion();
		return $product;
	}
	
	/*
		Redéfinition de la mise à jour
                -----> voir ''update()' dans 'PecExObject.class.php'.
		
		Ajout de la mise à jour de la liste des produits.
	*/
	public function update() {
		parent::update();
		
		if($this->isUpdated)
			Product::updateProductListVersion();
	}
	
	/*
		Redéfinition de la méthode 'delete()', voir ----> 'PecExObject.class.php';
		
		Un produit ne peux pas être supprimé si il a déjà fait l'objet de transaction.
	*/
	public function delete() {
		if(!empty($this->transaction))
			return;
                
		parent::delete();
		
		static::updateProductListVersion();
	}
	
	/*
		Redéfinition du prédicat de recherche totale afin de ne pas inclure
		l'élément incrément au tag 'product_list_version'.
	*/
	public static function searchAll($precision = false) {
	    
	    $predicat = 'tag != \'product_list_version\'';
	    $precision =(!$precision)? $predicat:  $precision.' AND '.$predicat;
	    return parent::searchAll($precision);
	}
	
	public function eventStarted() {
		$event = Event::search($this->eventTag);
		
		return $event[0]->eventStarted();
	}
}
?>