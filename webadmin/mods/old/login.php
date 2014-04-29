<?php
include_once '../init.php';
include_once '../class/User.class.php';

if(isset($_POST['logout'])){
	session_destroy();
	redirectionTo(getCurrentPage());
}

function sessionStarted() {
	if(isset($_POST['name']) && isset($_POST['password'])) {
		$name 	= 	cleanInput($_POST['name']);
		$digest	= 	cleanInput($_POST['password']);
		
		if(!User::user_exist($name, $digest)) return;
		$req	= 	User::search($name);
		$user 	= 	$req[0];
		
		if(!$user->isAdmin()) return;
		
		$_SESSION['user'] 	= 	$user->name;
		$_SESSION['ip']		= 	getIp();
		
		redirectionTo(getCurrentPage());
	}
}

sessionStarted();
echo '<!DOCTYPE html>';
if(!isset($_SESSION['ip'])) {
	?>
	<html>
		<head>
			<meta charset="utf-8" />
        	<link rel="stylesheet" href="../style/administration.index.php.css" />
        	<title>Pecuniam Exactam v0.1: Administration</title>
		</head>
		<body>
			<form class="login" method="post" action = "<?php echo getCurrentPage();?>" name="login">
				<table>
					<tr>
						<td>
							<fieldset>
							<legend><input type="submit" value="Login" /></legend>
							<table>
								<tr><td>Name</td><td>Password</td></tr>
								<tr>
									<td><input type="text" name="name"/></td>
									<td><input type="password" name="password"/></td>
								</tr>
							</table>
							</fieldset>
						</td>
					</tr>
				</table>
			</form>
		</body>
	</html>		<?php
	exit;
}else {
	if(getCurrentPage() != '/PecuniamExactamWebInterface/mods/balanceSheetFactory.php' ){
		?>
		<html>
			<head>
				<meta charset="utf-8" />
			</head>
			<body>
				<form class="loged" method="post" action ="<?php echo getCurrentPage();?>" name="logoutForm">
					<label for="logout"><?php echo $_SESSION['user'];?></label>
					<input type="submit" name="logout" value="logout"/>
				</form>
			</body>
		</html>	
	<?php
	}
}
?>