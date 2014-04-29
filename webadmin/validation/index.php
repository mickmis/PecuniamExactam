<?php
include_once '../init.php';
include_once '../mods/TicketValidation.class.ph';
?>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8" />
        <link rel="stylesheet" href="../style/validation.index.php.css" />
        <title>Pecuniam Exactam v0.1: Validation</title>
	</head>
	<body>
		<header>
			<!-- Tite -->
		</header>
		<section>
			<?php showIndex();?>
		</section>
	</body>
</html>

<?php 
function showIndex() {
	?>
	<table>
		<tr>
			<td>
				<fieldset>
				<legend>Valider un ticket</legend>
				<?php 
				$ticketValidation = new TicketValidation();
				$ticketValidation->show();
				?>
				</fieldset>
			</td>
		</tr>
	</table>
	<?php
}
?>