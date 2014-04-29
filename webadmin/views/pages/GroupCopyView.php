<?php

require_once __DIR__.'/../../init.php';
require_once __DIR__.'/../../mods/PostManager.class.php';
require_once __DIR__.'/../FormViewGenerator.class';

if(PostManager::post('tagToCopy')) {
    inputData();
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
	<div class="container group_copy_view">
	    <?php
	    $tag_name = PostManager::post('tag_name');
	    $tag_source = PostManager::post('tag_source');
	    $sql_class = PostManager::post('sql_class');
	    
	    $select_list = FormViewGenerator::formLookup('tagToCopy', null, $tag_name);
	    $hidden_tagname = FormViewGenerator::getHiddenButton('tag_name', $tag_name);
	    $hidden_tagsource = FormViewGenerator::getHiddenButton('tag_source', $tag_source);
	    $hidden_sqlClass = FormViewGenerator::getHiddenButton('sql_class', $sql_class);
	    
	    $hiddenButtons = $hidden_tagname.$hidden_tagsource.$hidden_sqlClass;
	    
	    $html = FormViewGenerator::getForm($select_list.$hiddenButtons);
	    
	    echo $html;
	    ?>
	</div>
    </body>
</html>

<?php
function inputData() {
    $class = PostManager::post('sql_class');
    $tag_name = PostManager::post('tag_name');
    $tag_value = PostManager::post('tag_source');
    $copyTag_value = PostManager::post('tagToCopy');
    
    $class::relativeCopy($tag_name, $tag_value, $copyTag_value, false);
}
?>
