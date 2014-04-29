<?php
require_once __DIR__."/../mods/PageManager.class.php";
require_once __DIR__.'/../init.php';

function include_js_file($file) {
    $html = '<script src="../script/'.$file.'"></script>';
    
    echo $html;
}
?>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>PecEx Admin</title>
	<meta charset="utf-8" />
	<link rel="stylesheet" href="../mods/bootstrap/css/bootstrap.css" />
	<link rel="stylesheet" href="../mods/bootstrap/css/bootstrap-responsive.css" />
	<link rel="stylesheet" href="../script/Source/datepicker_dashboard/datepicker_dashboard.css">
	<link rel="stylesheet" href="../script/Source/datepicker.css">
	<link rel="stylesheet" href="../style/admin.bootstrap.css" />
    </head>
    <header>
	<h4 class="well">PECUNIAM EXACTAM ADMINISTRATION</h4>
    </header>
    <body>
	<div class="alert alert-block alert-error fade in hide">
	    <a class="close" data-dismiss="alert" href="#">&times;</a>
	    <div class="alert_content">TU PU</div>
	</div>
	<div class="alert alert-block fade in hide">
	    <a class="close" data-dismiss="alert" href="#">&times;</a>
	    <div class="alert_content">TU PU</div>
	</div>
	<?php
	    $pageMan = new PageManager();
	    $pageMan->display();
	?>
	<div class="modal hide" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	    <div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
		<h3 id="myModalLabel">Modal header</h3>
	    </div>
	    <div class="modal-body">
                
	    </div>
	    <div class="modal-footer">
		<button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
		<button class="btn btn-primary submitForm">Valider</button>
	      </div>
	</div>
        <div class="modal hide" id="pdfModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	    <div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
		<h3 id="myModalLabel">Modal header</h3>
	    </div>
	    <div class="modal-body">
                
	    </div>
	    <div class="modal-footer">
		<button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
	      </div>
	</div>
    </body>
    <?php
    include_js_file('jquery.js');
    include_js_file('mootools-core-1.4.5.js');
    include_js_file('mootools-more-1.4.0.1.js');
    ?>
    <script src="../mods/bootstrap/js/bootstrap.js"></script>
    <?php
    include_js_file('Source/Locale.fr-FR.DatePicker.js');
    include_js_file('Source/Picker.js');
    include_js_file('Source/Picker.Attach.js');
    include_js_file('Source/Picker.Date.js');
    include_js_file('modale_launcher.js');
    include_js_file('jquery.pecEx.js');
    ?>
</html>
