<?php

require_once 'PecException.php';

/**
 * classe d'erreur SQL_PecException
 * 
 * Définit l'intégralité des exceptions susceptible d'être levé par un objet de type PDO
 */
class SQL_PecException extends PecException{
    protected $DEFAULT_MESSAGE = "Il y'a eu une erreur de connection veuillez recommencer.";
}

?>
