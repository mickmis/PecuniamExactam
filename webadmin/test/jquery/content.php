<?php
require_once __DIR__.'/../../init.php';
?>
<div class="content">
	<p>
	    Nullam quis risus eget urna mollis ornare vel eu leo.
	    Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, 
	    ut fermentum massa justo sit amet risus. Donec id elit non mi porta gravida at 
	    eget metus. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus.
	</p>
	<?php
	if(isset($_POST['bonjour']))
	    echo '<h1>BONJOUR</h1>';
	?>
</div>