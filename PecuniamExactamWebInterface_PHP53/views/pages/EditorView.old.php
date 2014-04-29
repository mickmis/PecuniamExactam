<?php
require_once __DIR__.'/../FormViewGenerator.class';
require_once __DIR__.'/../../mods/PostManager.class.php';
?>
<div class="editor_view">
    <?php
    $id = PostManager::post('id');
    $class = PostManager::post('sql_classe');
    $fields = $class::getUserFields();
    $element = $class::getElementWithId($id);
    
    $editForm = null;
    foreach ($fields as $field) {
	if(PostManager::post($field))
	    $element->$field = PostManager::post($field);
	$textField = FormViewGenerator::getTextField($field, $element->$field);
	$editForm .= '<label for="'.$field.'">'.$field.': </label>'.$textField.'<br/>';
    }
    $editForm = FormViewGenerator::getForm($editForm);
    $actionPage = PostManager::get_url();
    $submitButton = FormViewGenerator::getJsButton('valider', 'submitForm', array($actionPage));
    
    $html = $editForm.$submitButton;
    echo $html;
    ?>
</div>