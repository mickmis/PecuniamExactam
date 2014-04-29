package database;

import java.util.Date;
import java.util.List;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Classe représentant la table events dans la base de donnée.
 * Assume que la BDD est correctement configurée.
 * 
 * @author Mickaël Misbach
 *
 */
public class Events extends MysqlTable {

	/**
	 * Constructeur par défaut.
	 * 
	 * @param dbServer le serveur mysql
	 */
	public Events(MysqlServer dbServer) {
		super(dbServer);
	}
	
	/** 
	 * @return true si l'évènemnt est valide dans le temps.
	 */
	public boolean isValidNow(String eventTag) {
		try {
			List<Object[]> eventsResults = this.dbServer.doQuery("SELECT startTime, endTime FROM events WHERE tag=?", eventTag);
			
			// si il n'y a pas de résultat l'évènement n'existe pas
			if (eventsResults.size() != 1)
				return false;
			
			// vérification de la validité dans le temps
			Date currentTime = new Date();
			Timestamp startTime = (Timestamp) eventsResults.get(0)[0];
			Timestamp endTime = (Timestamp) eventsResults.get(0)[1];
			return !(currentTime.before(startTime) || currentTime.after(endTime));
		} catch (SQLException e) {
			this.dbError(e);
			return false;
		}
	}

	/**
	 * Retourne le nom de l'évènement à partir de son tag.
	 * 
	 * @param eventTag le tag de l'évènement
	 * 
	 * @return le nom de l'évènement
	 */
	public String getEventName(String eventTag) {
		try {
			List<Object[]> results = this.dbServer.doQuery("SELECT name FROM events WHERE tag=?", eventTag);
			return (String) results.get(0)[0];
		} catch (SQLException e) {
			this.dbError(e);
			return "";
		}
	}
	
	/**
	 * Retourne la description de l'évènement à partir de son tag.
	 * 
	 * @param eventTag le tag de l'évènement
	 * 
	 * @return la description de l'évèvenement
	 */
	public String getEventDescription(String eventTag) {
		try {
			List<Object[]> results = this.dbServer.doQuery("SELECT description FROM events WHERE tag=?", eventTag);
			return (String) results.get(0)[0];
		} catch (SQLException e) {
			this.dbError(e);
			return "";
		}
	}	
}
