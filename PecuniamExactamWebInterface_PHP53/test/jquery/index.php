<!DOCTYPE html>
<?php
function getScriptDir() {
    echo __DIR__.'/../../script';
}

function include_js_file($file) {
    $html = '<script src="'.$file.'"></script>';
    
    echo $html;
}
?>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" href="test.css" />
        <title></title>
    </head>
    <body>
	<button class="afficher" onclick="show()">Afficher</button>
	<div class="firstTest">
	    <div class="container"></div>
	    <button class="simplemodal-close">Masquer</button><br/>
	</div>
    </body>
    <?php
    include_js_file('jquery.js');
    include_js_file('simpleModal.js');
    include_js_file('test.js');
    ?>
</html>
