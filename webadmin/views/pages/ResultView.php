
<?php

require_once __DIR__.'/../../mods/ContentFilter.class.php';
require_once __DIR__.'/../ResultViewer.class.php';


$results = ContentFilter::searchContent();

$resultView = new ResultViewer($results);
$resultView->display();

?>