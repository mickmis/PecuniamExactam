<?php
require_once __DIR__.'/../../mods/ContentFilter.class.php';

require_once __DIR__.'/../../init.php';
require_once __DIR__.'/../FormViewGenerator.class';
require_once __DIR__.'/../../mods/PostManager.class.php';

function include_js_file($file) {
    $html = '<script src="'.$_SERVER['HTTP_REFERER'].'/../../script/'.$file.'"></script>';
    
    echo $html;
}
?>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>
	<div class="editor_view submitForm">
	    <?php
	    $id = (PostManager::post('id'))? PostManager::post('id'): 8;
	    $class = (PostManager::post('sql_classe'))? (PostManager::post('sql_classe')): 'Event';
	    $fields = ContentFilter::getFields($class);
	    $element = $class::getElementWithId($id);
	    
	    $hidden_class = FormViewGenerator::getHiddenButton('sql_classe', $class);
	    $hidden_id = FormViewGenerator::getHiddenButton('id', $id);
	    $editForm = $hidden_class.$hidden_id;
	    
	    foreach ($fields as $field) {
		if(PostManager::post($field))
		    $element->$field = PostManager::translateData(PostManager::post($field), $field);
		
		$value = ContentFilter::elementParsing($field, $element->$field);
		$inputField = FormViewGenerator::formLookup($field, $value);
		$editForm .= '<label for="'.$field.'">'.$field.': </label>'.$inputField.'<br/>';
	    }
	    $element->update();
	    
	    $editForm = FormViewGenerator::getForm($editForm);
	    
	    $html = '<div class="submitFormReload">'.$editForm.'</div>';
	    echo $html;
	    ?>
	</div>
    </body>
    <?php
//    include_js_file('jquery.js');
//    echo '<script src="'.$_SERVER['HTTP_REFERER'].'/../../mods/bootstrap/js/bootstrap.js"></script>';
    ?>
</html>