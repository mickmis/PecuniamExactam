<?php
require_once("tequila.php");
session_destroy();
session_start();
$oClient = new TequilaClient();

$oClient->SetApplicationName('Les �l�ves vont � l\'�cole');
$oClient->SetWantedAttributes(array('uniqueid','name','firstname','unit', 'unitid', 'where', 'group'));
$oClient->SetWishedAttributes(array('email', 'title'));
//$oClient->SetApplicationURL(__DIR__.'/../../admin/index.php');
#$oClient->SetCustomFilter('org=EPFL&firstname=John&unit=SC-PME&where=SC-PME/SC-S/ETU/EPFL/CH&group=inbc');
$oClient->SetCustomParamaters(array ('toto' => 1));

$oClient->Authenticate ();

$org  = $oClient->getValue('org');
$user = $oClient->getValue('user');
$host = $oClient->getValue('host');
$sKey = $oClient->GetKey();

echo <<<EOT
<html>
	<head>
		<title>Test Tequila</title>
	</head>
	<body>
		<h3>Test Tequila :</h3>
		<pre>
             key = $sKey
             org = $org
            user = $user
            host = $host

EOT;

echo "\nCookies = ";
print_r($_COOKIE);
echo "\nSession = ";
print_r($_SESSION);

echo <<<EOT
		</pre>
		<p>
		<a href="{$_SERVER['PHP_SELF']}">Test session key</a><br/>
	</body>
</html>

EOT;
?>
