<?php


/**
 * Description of EditManager
 *
 * @author IV
 */
class EditManager {
    private $fields;
    
    public function __construct(PecExObject $element) {
	$class = get_class($element);
	$this->fields = $class::getFields();
    }
}

?>
