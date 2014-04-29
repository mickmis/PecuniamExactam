package database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant la table stands dans la base de donnée.
 * Assume que la BDD est correctement configurée.
 * 
 * @author Mickaël Misbach
 *
 */
public class Stands extends MysqlTable {
	
	/**
	 * Constructeur par défaut.
	 * 
	 * @param dbServer le serveur mysql
	 */
	public Stands(MysqlServer dbServer) {
		super(dbServer);
	}
	
	/**
	 * Retourne la liste des tags des stands associés à un évènement.
	 * 
	 * @param eventTag le tag de l'évènement
	 * 
	 * @return la liste des tags des stands 
	 */
	public List<String> getStandsByEvent(String eventTag) {
		try {
			List<Object[]> results = this.dbServer.doQuery("SELECT tag FROM stands WHERE eventTag=?", eventTag);
			List<String> returnList = new ArrayList<String>();
			
			for (int i = 0 ; i < results.size() ; i++)
				returnList.add((String) results.get(i)[0]);
				
			return returnList;
		} catch (SQLException e) {
			this.dbError(e);
			return null;
		}
	}
	
	/**
	 * Retourne le nom du stand avec son tag
	 * 
	 * @param standTag le tag du stand
	 * 
	 * @return le nom du stand
	 */
	public String getStandNameByTag(String standTag) {
		try {
			List<Object[]> results = this.dbServer.doQuery("SELECT name FROM stands WHERE tag=?", standTag);
			return (String) results.get(0)[0];
		} catch (SQLException e) {
			this.dbError(e);
			return "";
		}
	}
	
	/** Retourne la description du stand avec son tag
	 * 
	 * @param standTag le tag du stand
	 * 
	 * @return la description du stand
	 */
	public String getStandDescriptionByTag(String standTag) {
		try {
			List<Object[]> results = this.dbServer.doQuery("SELECT description FROM stands WHERE tag=?", standTag);
			return (String) results.get(0)[0];
		} catch (SQLException e) {
			this.dbError(e);
			return "";
		}
	}
}
