<?php
include_once 'Stand.class.php';

class StandBalance{
	
	public $name;
	public $tag;
	private $products;
	private $productsSellings;
	private $productEarnPercentage;
	private $productsInitialStock;
	
	public function __construct($stand) {
		
		$this->name		=	$stand->name;
		$this->tag		=	$stand->tag;
		
		//obtention de la liste des produits vendu par ce stand
		foreach($stand->getProducts() as $product)
			$this->products[$product->tag] 	= 	$product;
		
		// définition du nombre total de vente par produits
		// définition du pourcentage de gains par produit
		// définition du stock initiale accordé par produit
		foreach($this->products as $tag => $product) {
			$this->productsSellings[$tag] 		= 	count($product->transactions);
			$this->productEarnPercentage[$tag] 	=	$product->earnPercentage;
			$this->productsInitialStock[$tag]	=	0;  //TODO stock initial hors d'usage
		}
	}
	
	public function getBank() {
		$bank = 0;
		foreach ($this->products as $tag => $product)
			$bank += $product->price*$this->productsSellings[$tag];
		
		return $bank;
	}
	
	public function getGiveBack() {
		$giveBack = 0;
		if(empty($this->products)) return;
		foreach ($this->products as $tag => $product)
			$giveBack += $product->price*$this->productsSellings[$tag]* (1-$product->earnPercentage);
		
		return $giveBack;
	}
	
	public function getSellingList() {
		$txt = '';
		
		if(empty($this->products)) return;
		foreach($this->products as $tag => $product) {
			$sells = $this->productsSellings[$tag];
			$giveBack = $product->price*$sells*(1-$product->earnPercentage);
			$vendu = ($sells>1)?'vendus':'vendu';
			$txt .= '
			';
			$txt .= $product->name.'('.$tag.') : '.$sells.' unités '.$vendu.' ---> '.$giveBack.'.- dû';
		}
		
		return $txt;
	}
	
	public function getIOStock() {
		$txt = '';
		if(empty($this->productsInitialStock)) return;
		foreach($this->productsInitialStock as $tag => $stock) {
			$left = $stock - $this->productsSellings[$tag];
			$txt	.=	'
			';
			$txt	.=	$this->products[$tag]->name.': '.$stock.' donné '.$left.' à rendre.';
		}
		
		return $txt;
	}
}