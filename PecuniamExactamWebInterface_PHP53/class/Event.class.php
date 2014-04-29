<?php
include_once 'PecExObject.class.php';
require_once 'PecException.class.php';


/*
	Classe de la table events.
	
	voir ----> 'PecExObject.class.php'
*/
class Event extends PecExObject {
	public static $table 	= 'events';
	public static $predicat	=	'name';
	
	public static $link  = array('User','Product', 'Stand', 'Transaction');

	protected $name;
	protected $description;
	protected $startTime;
	protected $endTime;
	
	public static function insert($inputFields) {
		parent::insert($inputFields);
		
		Product::updateProductListVersion();
	}
	
	public function update() {
		parent::update();
		
		if($this->isUpdated)
			Product::updateProductListVersion();
	}
	
	public function delete() {
		foreach (static::$link as $table) {
			if($table::getNbrOfElementsWith('eventTag', $this->tag) > 0)
				throw new PecException('Impossible de supprimer un évènement tant qu\'il reste un(e) '.$table.' associé à cet évènement!');
		}
		
		Product::updateProductListVersion();
		
		parent::delete();
	}
	
	public static function relativeCopy( $tag_name, $tag_value, $copyTag_value, $sql_class = false) {
	    $sql_class = ($sql_class)? $sql_class: 'Stand';
	    parent::relativeCopy( $tag_name, $tag_value, $copyTag_value, $sql_class);
	}
	
	public static function isRelativeCopyable() {
	    return true;
	}
	
	public static function getEventWithTag($tag) {
	    $event = static::search($tag);
	    
	    if(!$event)
		return false;
	    
	    return $event[0];
	}
	
	public static function eventWithTag_exists($tag) {
		$req	=	static::search($tag, true, 'tag');
		
		return !empty($req);
	}
	
	public function eventStarted() {
		$eventStartTime		=	new DateTime($this->startTime, new DateTimeZone('Europe/Berlin'));
		$currentTime		=	new DateTime();
		
		return ($currentTime >= $eventStartTime);
	}
	
	public function validPeriod(DateTime $startTime, DateTime $endTime) {
		$eventStart	=	new DateTime($this->startTime, new DateTimeZone('Europe/Berlin'));
		$eventEnd	=	new DateTime($this->endTime, new DateTimeZone('Europe/Berlin'));
		
		return ($eventStart <= $startTime && $eventEnd >= $endTime);
	}
}
?>