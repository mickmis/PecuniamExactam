<?php
	include_once("../init.php");
	
	//nclusion des table
//        require_once '../class/BalanceSheet.class.php';
//        require_once '../class/Event.class.php';
//        require_once '../class/Product.class.php';
//        require_once '../class/Stand.class.php';
	
	// inclusion du menu
	require_once '../views/menu/Menu.class.php';
//        require_once '../class/Stock.class.php';
        require_once '../class/Transaction.class.php';
        require_once '../class/User.class.php';
	// définition des session
	set_session();
	// destruction des formulaires postés!
	remove_posted_form();
	
	// inclusion de l'intégralité des classes de module
	include_once('../mods/Search.class.php');
	include_once('../mods/Adder.class.php');
	include_once('../mods/Result.class.php');
	include_once('../mods/Element.class.php');

//	include_once '../mods/login.php';
	$class_tables = array('Event','User','Product','Stand','Transaction');
?>
<!DOCTYPE html>
<html>
	<head>
		<head>
			<meta charset="utf-8" />
        	<link rel="stylesheet" href="../style/administration.index.php.css" />
        	<link rel="stylesheet" href="../style/toggleOver.css" />
        	<title>Pecuniam Exactam v0.1: Administration</title>
		</head>
	</head>
	<body>
	    <header>
		    <?php
			$menu = new Menu($class_tables);
			$menu->display();
		    ?>
		</header>
		<section>
			<table>
				<tr>
					<td>
						<?php
						echo '<fieldset><legend>Search for an entry</legend>';
						show_search();
						echo '</fieldset>';
					?>
					</td>
				</tr>
					<td>
						<?php
						echo '<fieldset><legend>Add an entry</legend>';
						show_adder();
						echo '</fieldset>';
					?>
					</td>
				<tr>
				</tr>
					<td>
						<?php
						echo '<fieldset><legend>Results</legend>';
						show_result();
						echo '</fieldset>';
						?>
					</td>
				<tr>
				</tr>
			</table>
		</section>
	</body>
</html>
<?php
	function show_search() {
		// Module de recherche, voir ------> Search.class.php
		$searchOptions	= 	array('Event','User','Product','Stand','Transaction');
		$finder			= 	new Search($searchOptions);
		$finder->show();
	}
	
	function show_adder() {
		$search = get_search_context();
		// Module de rajout, voir ------> Adder.class.php
		$adder 	= 	new Adder($search['in']);
		$adder->show();
	}
	
	function show_result() {
		$search = get_search_context();
		// Module d'affichage et de modification des résultats, voir ------> Result.class.php
		$resultFetcher 	= 	new Result($search['in'], $search['for']);
		$resultFetcher->show();
	}
	
	/**
	Vérification de l'existence d'une précédente recherche.
	Si une recherche n'a pas été effecté, mais qu'une variable de session existe,
	alors le contexte de recherche est défini par les variables de session.
	Les valeurs par défaut sont une recherche sous 'Event' de la valeur ''
	*/
	function get_search_context() {
		if(!isset($_SESSION['searchIn']) || !isset($_SESSION['searchFor']))
			throw new Exception('Impossible de charger le contexte de recherche! Ligne'.__LINE__);
		
		return array('in' => $_SESSION['searchIn'], 'for' => $_SESSION['searchFor']);
	}
	
	function set_session() {
		if(isset($_POST['defined_predicat']))
			$_SESSION['defined_predicat']	=  $_POST['defined_predicat'];
		
		if(isset($_POST['searchIn'])) {
			$searchIn	=	$_POST['searchIn'];
			$searchFor	=	$_POST['searchFor'];
		}
		else if(isset($_SESSION['searchIn'])) {
			$searchIn 	=	$_SESSION['searchIn'];
			if(!isset($_POST['searchAll']))
				$searchFor	=	$_SESSION['searchFor'];
			else
				$searchFor	= '';
		}
		else {
			$searchIn 	=	'Event';
			$searchFor	=	'';
		}
		
		$_SESSION['searchIn'] 	=	$searchIn;
		$_SESSION['searchFor']	=	$searchFor;
	}
?>