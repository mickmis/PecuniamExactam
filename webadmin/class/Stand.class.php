<?php
include_once 'PecExObject.class.php';
include_once 'Product.class.php';


/*
	Class d'instance de la table 'stands'.
	.
	Voir ----> 'PecExObject.class.php'.
*/
class Stand extends PecExObject {

	public static 	$table 		= 	'stands';
	public static	$predicat	=	'name';
	
	protected		$name;
	protected 		$tag;
	protected 		$description;
	protected	 	$eventTag;
	protected		$startTime;
	protected 		$endTime;

	
	
	public static function valid_inputs(Array $inputs) {
		if(!isset($inputs['startTime']) || !isset($inputs['endTime'])) return false;
		
		if(!static::checkValidity($inputs['startTime'], $inputs['endTime'], $inputs['eventTag'])) return false;
		
		return parent::valid_inputs($inputs);
	}
	public static function checkValidity($startTime, $endTime, $eventTag) {
		$startTime	=	new DateTime($startTime, new DateTimeZone('Europe/Berlin'));
		$endTime	=	new DateTime($endTime, new DateTimeZone('Europe/Berlin'));
		$event		=	Event::search($eventTag);
		
		if(empty($event)) 
		    return false;
		
		if(!$event[0]->validPeriod($startTime, $endTime))
			throw new Exception('Période précisé non comprise dans la durée de l\'évènement \''.$event[0].'\'');
		
		return true;
		
	}
	
	public static function relativeCopy( $tag_name, $tag_value, $copyTag_value, $sql_class = false) {
	    $sql_class = ($sql_class)? $sql_class: 'Product';
	    parent::relativeCopy( $tag_name, $tag_value, $copyTag_value, $sql_class);
	}
	
	public static function isRelativeCopyable() {
	    return true;
	}
	
	public static function insert($args) {
		$stand = parent::insert($args);
		
		Product::updateProductListVersion();
		
		return $stand;
	}
	
	public function update() {
		static::checkValidity($this->startTime, $this->endTime, $this->eventTag);
		
		parent::update();
		
		if($this->isUpdated)
			Product::updateProductListVersion();
	}
	
	/*
		Redéfinition, mettant à jour la liste des produits.
		
		Voir ----> 'PecExObject->delete()';
	*/
	public function delete() {
		
		Product::updateProductListVersion();
		
		parent::delete();
	}
	
	/*
	 * Retourne la liste des produits vendus
	 * par ce stand.
	 */
	public function getProducts() {
		$query = 'SELECT * FROM products WHERE standTag=?';
		$value = array($this->tag);
		$req 	=	submit($query, $value, true);
		
		return array_map('Product::init', $req);
	}
}