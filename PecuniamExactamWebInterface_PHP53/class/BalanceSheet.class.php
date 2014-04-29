<?php
include_once 'Event.class.php';
include_once 'StandBalance.class.php';
include_once '../mods/FPDF/fpdf.php';

class BalanceSheet {
	
	private $event;
	private $bank;
	private $earning;
	private $totalSold;
	private $standBalances;
	
	private $pdf;
	
	public function __construct($id) {
		$this -> pdf	=	new FPDF();
		
		$this->event = Event::getElementWithId($id);
		if($this->event == null) return;
		
		// définition des gains absolue
		$query 		 	 = 		'SELECT SUM(p.price) AS total FROM products p ';
		$query			.= 		'INNER JOIN transactions t ON t.productTag = p.tag ';
		$query			.=		'WHERE t.eventTag = ?';
		$value  	 	 = 		array($this->event->tag);
		$req 			 = 		submit($query, $value, true);
		$this->bank		 = 		(int)$req[0]['total'];
		
		// récupération de la liste des stands
		foreach(Stand::getAllFromEvent($this->event->tag) as $stand)
			$this->standBalances[] = new StandBalance($stand);
		
		// récupération de la bank et du gains total
		$query 	=	'SELECT COUNT(*) AS total FROM transactions WHERE eventTag = ?';
		$value	=	array($this->event->tag);
		$req	=	submit($query, $value,	true);
		$this->totalSold = $req[0]['total'];
		$this->earning = $this->totalSold;
		
		if(empty($this->standBalances))	return;
		
		foreach ($this->standBalances as $standBalance) {
			$this->earning -= $standBalance ->getGiveBack();
		}
		$this->earning += $this->bank;
	}
	
	public function getBank() {
		$txt 	 = 	'Félicitation vous avez engrangé '.$this->bank.'.- ';
		$txt	.= 	'ce qui vous fait un gains total de '.$this->earning.'.- !';
		
		return $txt;
	}
	
	public function getStandsSellingsList() {
		$txt = '';
		if(empty($this->standBalances)) return '';
		foreach($this->standBalances as $standBalance) {
			$txt	.=	'
			';
			$txt	.=	$standBalance->name.'('.$standBalance->tag.') :.'.$standBalance->getSellingList();
		}
		
		return $txt;
	}
	
	public function getStandsIOStock() {
		$txt = '';
		if(empty($this->standBalances)) return '';
		foreach($this->standBalances as $standBalance) {
			$txt	.=	'
			';
			$txt	.=	$standBalance->name.'('.$standBalance->tag.') :.'.$standBalance->getIOStock();
		}
		
		return $txt;
	}
	
	public function showPDF() {
		$title = 'Bilan de l\'évènement '.$this->event->name.' <'.$this->event->tag.'>';
		$this->pdf->SetAuthor('PecuniamExactam v0', true);
		
		$this->pdf -> AddPage();
		$this->writeTitle($title);
		
		$this->write($this->getBank());
		$this->pdf -> Ln(10);
		
		$this->writeSubTitle('Liste des ventes');
		$this->write($this->getStandsSellingsList());
		
		$this->pdf -> AddPage();
		$this->writeSubTitle('Entrée et Sortie du stock');
		$this -> write($this->getStandsIOStock());
		
		$this->pdf -> Output('Bilan.pdf','I');
		
	}
	
	public function writeTitle($title) {
		$this->pdf -> SetFont('Arial','BU', 15);
		//obtention de la largeur du titre
		$w = $this->pdf->GetStringWidth($title);
		
		$this->pdf -> Cell($w, 10, utf8_decode($title));
		
		$this->pdf -> Ln(18);
	}
	
	public function writeSubTitle($title) {
		$this->pdf -> SetFont('Arial','U', 10);
		
		//obtention de la largeur du titre
		$w = $this->pdf->GetStringWidth($title);
		
		$this->pdf -> Cell($w, 10, utf8_decode($title));
		
		$this->pdf -> Ln();
	}
	
	public function write($txt) {
		$this->pdf -> SetFont('Arial','', 7);
		
		$this->pdf -> write(4, utf8_decode($txt));
		
		$this->pdf -> Ln();
	}
}
?>