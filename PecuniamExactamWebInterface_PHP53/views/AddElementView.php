<?php
require_once __DIR__.'/../mods/ContentFilter.class.php';
require_once "FormViewGenerator.class";
require_once __DIR__."/../mods/PostManager.class.php";


if(PostManager::post('submitForm')){
    inputData();
    exit();
}else if(PostManager::post('toDelete')){
    $class = PostManager::post('sql_class');
    $id = PostManager::post('toDelete');
    $element = $class::getElementWithId($id);
    try {
	$element->delete();
    } catch (PecException $e) {
	echo $e->getMessage();
    }

    exit;
}
?>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title></title>
    </head>
    <body>
	<div class="add_element_view submitForm">
	    <?php
	    
	    if(PostManager::post('for'))
		$class = PostManager::post('for');
	    else
		$class = (PostManager::get ('for'))? PostManager::get('for'): 'Event';
	    
	    $fields = ContentFilter::getFields($class);
	    $values = parsePage($fields, $class);
	    
	    $inputFields = null;
	    foreach($fields as $field) {
		$value = ContentFilter::elementParsing($field, $values[$field]);
		$inputFields .= '<label for="'.$field.'">'.$field.'</label> '
				.FormViewGenerator::formLookup($field, $value).'<br/>';
	    }
	    $submitButton = FormViewGenerator::getSubmitButton('envoyer');
	    $copyButton = '<button class="copyButton btn">copier</button>';
	    $deleteButton = '<button class="deleteButton btn">supprimer</button>';
	    $hiddenButton = FormViewGenerator::getHiddenButton('copy_source', 'form')
		    .FormViewGenerator::getHiddenButton('sql_class', $class);
	    
	    $html = $copyButton.$deleteButton.FormViewGenerator::getForm($hiddenButton.$inputFields.$submitButton);
	    
	    echo $html;
	    ?>
	</div>
    </body>
    <script src="../script/jquery.forms.js"></script>
    <script src="../script/jquery.pecEx.js"></script>
</html>

<?php

function parsePage($fields, $class) {
    $copy_source = PostManager::post('copy_source');
    if($copy_source == 'empty')
	return parseFromEmpty($fields);
    else if ($copy_source == 'form')
	return parseFromForm($fields);
    else{
	$id = PostManager::post('id');
	return parseFromId($id, $fields, $class);
    }
}

function parseFromId($id, $fields, $class) {
    $values = null;
    $element = $class::getElementWithId($id);
    foreach($fields as $field)
	$values[$field] = $element->$field;
    
    return $values;
}

function parseFromForm($fields) {
    $values = null;
    foreach($fields as $field)
	$values[$field] = PostManager::translateData(PostManager::post($field));
    
    return $values;
}

function parseFromEmpty($fields) {
    $values = null;
    foreach($fields as $field)
	$values[$field] = '';
    
    return $values;
}

function inputData() {
    $class = PostManager::post('sql_class');
    $fields = $class::getUserFields();
    
    $values = null;
    foreach($fields as $field)
	$values[$field] = PostManager::post($field);
    
    if(ContentFilter::isAdmin()){
	$user = User::getUserWithName(PostManager::session_var('user_username'));
	foreach(ContentFilter::$NON_ADMIN_FIELDS as $field) {
	    if(in_array($field, $fields))
		$values[$field] = $user->$field;
	}
    }
    
    return ($class::valid_inputs($values) && $class::insert($values));
}
?>