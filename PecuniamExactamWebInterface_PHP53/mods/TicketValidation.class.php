<?php

/*
 * Valid un ticket
 */
class TicketValidation {
	
	
	public function digest($ids,$code){
		
		$digest = $ids.'/'.$code;
		return Transaction::searchWithDigest($digest);
	}
	
	public function show() {
		//affichage
		$keyTag = 'transactionValid';
		echo '<form method = "post" action = "index.php">';
		echo '<input type="text" name = "'.$keyTag.'ids"/>';
		echo '/';
		echo '<input type="text" name = "'.$keyTag.'code"/>';
		echo '</form>';
		
		if(!isset($_POST[$keyTag.'ids']) || !isset($_POST[$keyTag.'code'])) return;
		// récupération de toute les transactions du tickets
		$ticketTransactions = $this->digest($_POST[$keyTag.'ids'],$_POST[$keyTag.'code']);
		
		if($ticketTransactions == null) throw new Exception('INVALID ticket digest!');
		
		foreach ($ticketTransactions as $transaction)
			$transaction->check();
	}
}
?>