<?php

require_once __DIR__.'/../class/StandParser.class';
require_once __DIR__.'/../class/EventParser.class.php';
/**
 * Classe static permettant de généré un DataParser en fonction
 * du type de Data (Du nom de la classe sql).
 *
 * @author Vic I-Lite
 */
class DataParserMaker {
    private static $DATA_PARSERS = array('Event' => 'EventParser', 'Stand' => 'StandParser');
    
    /**
     * DataParser matcher.
     * 
     * @param string $id
     * @param string $class
     * @return DataParser Retourne le bon type de parser.
     * @see StandParser
     * @see EventParser
     * @see DataParser
     */
    public static function getParser($id, $class){
        if(!isset(static::$DATA_PARSERS[$class]))
            return false;
        
        $toEval = 'return new '.static::$DATA_PARSERS[$class].'('.$id.');';
        return eval($toEval);
    }
    
    public static function isParsable($class){
        $data_parsables_array = array_flip(static::$DATA_PARSERS);
        
        return in_array($class, $data_parsables_array);
    }
}

?>
