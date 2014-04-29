<?php

require_once __DIR__.'/../mods/PostManager.class.php';
require_once "HtmlViewGenerator.class";


$SQL_CLASS = array('Event', 'User','Stand', 'Product', 'Transaction');
$currentFor = (PostManager::get('for'))? PostManager::get('for'): 'Event';

$links = null;
foreach($SQL_CLASS as $i => $name) {
    $url = PostManager::get_url_with_vars(array('page'=>'Result','for'=>$name));
    $li_class = ($currentFor==$name)? 'active lead': $i.' lead';
    $links[$li_class] = HtmlViewGenerator::getLink($name, $url);
}
$list = '<div class="">'.HtmlViewGenerator::getItemList($links, false, 'nav nav-tabs').'</div>';
$html = '<div class="menu navbar">'.$list.'</div>';

echo $html;
?>