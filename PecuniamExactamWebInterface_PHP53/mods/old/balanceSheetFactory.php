<?php
include_once '../init.php';
include_once '../class/BalanceSheet.class.php';

if(!empty($_GET['eventId'])) {
	// création d'un objet Bilan
	$id = cleanInput($_GET['eventId']);
	$balanceSheet = new BalanceSheet($id);
	
	//génération du PDF
	$balanceSheet->showPDF();
}
?>