<?php

/*
 * Classe Exception générique
 * 
 * Permet d'afficher explicitement une errreur.
 * 
 * Une exception peut être affiché direction dans un echo (exemple: echo MonException)
 * Les classes filles doivents simplement redéfinir leur constructeur.
 * 
 * 
 */
class PecException extends Exception{
    protected $DEFAULT_MESSAGE = "Il y'a une erreur quelque part, veuillez contrôler les informations envoyés";
    
    public function __construct($message = NULL) {
        if(isset($message)) 
            $this->message = $message;
        else
            $this->message = $this->DEFAULT_MESSAGE;
    }
    
    public function __toString() {
        return $this->message;
    }
}

?>
