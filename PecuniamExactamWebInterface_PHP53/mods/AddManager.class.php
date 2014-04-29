<?php

/**
 * Gère l'ajout d'un élément précis.
 *
 * @author IV
 */
class AddManager {
    
    
    public function __construct($class) {
	$this->fields = $class::getFields();
    }
}

?>
