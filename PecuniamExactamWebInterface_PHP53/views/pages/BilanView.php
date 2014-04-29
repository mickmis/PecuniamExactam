<?php
require_once __DIR__.'/../../mods/PostManager.class.php';
require_once __DIR__.'/../../mods/DataParserMaker.php';
require_once __DIR__.'/../../init.php';
?>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title></title>
    </head>
    <body>
        <div class="bilan_view">
            <?php
            $for = PostManager::post('sql_class');
            $id = PostManager::post('id');
            $parser = DataParserMaker::getParser($id, $for);
            $parser->display(true);
	    
	    echo    '<p>Bilan généré avec succès: <a href="../views/pages/Bilan.pdf" about="_blank">télécharger</a><p>
		    <object type="application/pdf" data="../views/pages/Bilan.pdf" width="90%" height="90%" > </object >';
            ?>
        </div>
    </body>
</html>
