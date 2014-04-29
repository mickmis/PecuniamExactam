package server;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.logging.*;

import server.Server;

/**
 * Classe pour logguer des évènements.
 * Les messages INFO, WARNING et SEVERE seront imprimés dans la console.
 * <br /><br />
 * But des différents niveau de logs :<br />
 * severe : erreur bloquante <br />
 * warning : erreur non bloquante<br />
 * info : information qui devrait être communiquée<br />
 * config : informations de configurations, lorsque le programme ou ses composants se lancent<br />
 * fine : niveau de debug 1<br />
 * finer : niveau de debug 2<br />
 * finest : niveau de debug 3 <br />
 * <br />
 * À noter que les logs non pris en charge dans cette classe ne seront pas loggués (à cause de swing notamment).
 * 
 * @author Mickaël Misbach
 */
public class ServerLogger {
	
	/**
	 * Définit si la classe a été initialisée.
	 */
	private static boolean initialized = false;
	
	/**
	 * Chemin vers le fichier de log. Initialisé pour être dans le répertoire de lancement.
	 */
	public static final File LOGFILE = new File("./Server.log");
	
	/**
	 * Ne doit pas être instanciée.
	 */
	private ServerLogger() {}
	
	/**
	 * Méthode d'initialisation, définit filehandler et niveau de log de tous les loggers.
	 */
	public static void initialize(int debugLevel) {
		
		if (initialized)
			return;
		
		// enlève les handlers par défaut de tous les loggers
		Logger rootLogger = Logger.getLogger("");
		rootLogger.removeHandler(rootLogger.getHandlers()[0]);
		
		// créer nouveau filtre pour empêcher d'autres logs par swing/awt notamment
		Filter filter = new Filter() {
			@Override
			public boolean isLoggable(LogRecord record) {
				if (record.getLoggerName().startsWith("Server"))
					return true;
				else
					return false;
			}};
		
		// définit le niveau de debug
		setDebugLevel(debugLevel);
			
		try {
			// on ajoute les handlers voulus à notre logger (les loggers enfants l'auront aussi)
			FileHandler fileHandler = new FileHandler(LOGFILE.getAbsolutePath());
			fileHandler.setFormatter(new CustomFormatter());
			fileHandler.setFilter(filter);
			rootLogger.addHandler(fileHandler);

			ConsoleHandler consoleHandler = new ConsoleHandler();
			consoleHandler.setFormatter(new CustomFormatter());
			consoleHandler.setFilter(filter);
			rootLogger.addHandler(consoleHandler);
			
		} catch (IOException e) {
			// IOException (pas possible d'ouvrir le fichier) a été lancé, on arrête
			Server.endProgram(e);
		}catch (SecurityException e) {
			// SecurityException (ne devrait pas se produire) a été lancé, on arrête
			Server.endProgram(e);
		}
		
		ServerLogger.initialized = true;
	}
	
	/**
	 * Retrouve la classe appelante.
	 * 
	 * @return la classe appelante.
	 */
	private static String getCallerName() {
			Exception e = new Exception();
			return e.getStackTrace()[3].getClassName();
	}
	
	/**
	 * Loggue un message. Déclarée private à cause de l'implémentation de getCallerName().
	 * 
	 * @param level niveau de log de l'évènement
	 * @param msg the message de l'évènement
	 */
	private static void log(Level level, String msg) {
		
		// si non initialisé, défaut à CONFIG
		if (!initialized)
			initialize(0);
		
		// logger est créé si n'existe pas, ou retourné si existe déjà
		Logger logger = Logger.getLogger("Server." + ServerLogger.getCallerName());
		logger.log(level, msg);
	}
	
	/**
	 * Loggue message SEVERE.
	 * 
	 * @param msg le message à logguer
	 */
	public static void severe(String msg) {
		ServerLogger.log(Level.SEVERE, msg);
	}
	
	/**
	 * Loggue message WARNING.
	 * 
	 * @param msg le message à logguer
	 */
	public static void warning(String msg) {
		ServerLogger.log(Level.WARNING, msg);
	}
	
	/**
	 * Loggue message INFO.
	 * 
	 * @param msg le message à logguer
	 */
	public static void info(String msg) {
		ServerLogger.log(Level.INFO, msg);
	}
	
	/**
	 * Loggue message CONFIG.
	 * 
	 * @param msg le message à logguer
	 */
	public static void config(String msg) {
		ServerLogger.log(Level.CONFIG, msg);
	}

	/**
	 * Loggue message FINE.
	 * 
	 * @param msg le message à logguer
	 */
	public static void fine(String msg) {
		ServerLogger.log(Level.FINE, msg);
	}
	
	/**
	 * Loggue message FINER.
	 * 
	 * @param msg le message à logguer
	 */
	public static void finer(String msg) {
		ServerLogger.log(Level.FINER, msg);
	}
	
	/**
	 * Loggue message FINEST.
	 * 
	 * @param msg le message à logguer
	 */
	public static void finest(String msg) {
		ServerLogger.log(Level.FINEST, msg);
	}
	
	/**
	 * Retourne le niveau de debug utilisé. Niveau par défaut 0.<br />
	 * 0 : CONFIG<br />
	 * 1 : FINE ;<br />
	 * 2 : FINER ;<br />
	 * 3 : FINEST.
	 * 
	 * @return niveau de log (0, 1, 2, 3)
	 */
	public static int getDebugLevel() {
		Logger logger = Logger.getLogger("");
		
		if (logger.getLevel().equals(Level.CONFIG))
			return 0;
		else if (logger.getLevel().equals(Level.FINE))
			return 1;
		else if (logger.getLevel().equals(Level.FINER))
			return 2;
		else if (logger.getLevel().equals(Level.FINEST))
			return 3;
		else {
			Server.endProgram(new RuntimeException("Le niveau de debug est inconrrect."));
			return -1;
		}
	}
	
	/**
	 * Définit le niveau de debug utilisé. Niveau par défaut 0.<br />
	 * 0 : CONFIG<br />
	 * 1 : FINE ;<br />
	 * 2 : FINER ;<br />
	 * 3 : FINEST.
	 * 
	 * @return niveau de log (0, 1, 2, 3)
	 */
	public static void setDebugLevel(int level) {
		if (level < 0 || level > 3)
			throw new IllegalArgumentException("Le niveau de debug est " + level + " mais devrait être 0, 1, 2, ou 3.");
	
		Logger logger = Logger.getLogger("");
		
		switch (level) {
			case 0 : 
				logger.setLevel(Level.CONFIG);
				break;
				
			case 1 :
				logger.setLevel(Level.FINE);
				break;
				
			case 2 :
				logger.setLevel(Level.FINER);
				break;
				
			case 3 :
				logger.setLevel(Level.FINEST);
				break;
		}	
	}
}

/**
 * Classe de formattage.
 * c/c de <code>SimpleFormatter</code> et modifié.
 * 
 * @author JDK 1.6
 * @author Mickaël Misbach
 */
class CustomFormatter extends Formatter {
	
    Calendar cal = Calendar.getInstance();

    // Line separator string.  This is the value of the line.separator
    // property at the moment that the CustomFormatter was created.
    private String lineSeparator = java.security.AccessController.doPrivileged(
    		new sun.security.action.GetPropertyAction("line.separator"));

    /**
     * Format the given LogRecord.
     * This method can be overridden in a subclass.
     * 
     * @param record the log record to be formatted.
     * @return a formatted log record
     */
    public synchronized String format(LogRecord record) {
        StringBuffer sb = new StringBuffer();
        sb.append(cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH));
        sb.append(" ");
        sb.append(cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND));
        sb.append(" ");
        sb.append(record.getLoggerName());
        sb.append(" -- ");
        String message = formatMessage(record);
        sb.append(record.getLevel().getName());
        sb.append(": ");
        sb.append(message);
        sb.append(lineSeparator);
        if (record.getThrown() != null) {
            try {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                record.getThrown().printStackTrace(pw);
                pw.close();
                sb.append(sw.toString());
            } catch (Exception ex) {
            }
        }
        return sb.toString();
    }
}