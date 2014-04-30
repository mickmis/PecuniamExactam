package database;

import java.sql.SQLException;
import java.util.List;

import server.ServerLogger;

/**
 * Classe représentant la table users dans la base de donnée.
 * Assume que la BDD est correctement configurée.
 * 
 * @author Mickaël Misbach
 *
 */
public class Users extends MysqlTable {

	/**
	 * Constructeur par défaut.
	 * 
	 * @param dbServer le serveur mysql
	 */
	public Users(MysqlServer dbServer) {
		super(dbServer);
	}
	
	/**
	 * Retourne le cash que possède l'utilisateur.
	 * 
	 * @param username le nom d'utilisateur
	 * 
	 * @return le cash de l'utilisateur
	 */
	public double getCash(String username) {
		try {
			List<Object[]> results = this.dbServer.doQuery("SELECT cash FROM users WHERE name=?", username);
			return ((Double) results.get(0)[0]).doubleValue();
		} catch (SQLException e) {
			this.dbError(e);
			return 0;
		}
	}
	
	/**
	 * Met à jour les informations sur le cash de l'utilisateur.
	 * 
	 * @param username le nom d'utilisateur
	 */
	public void processCash(String username, double relativeCash) {
		try {
			if (relativeCash != 0)
				this.dbServer.doUpdate("UPDATE users SET cash=cash+? WHERE name=?", relativeCash, username);
		} catch (SQLException e) {
			this.dbError(e);
		}
	}
	
	/**
	 * Authentifie un utilisateur à partir.
	 * 
	 * @param username l'utilisateur
	 * @param password son mot de passe
	 * 
	 * @return true si l'authentification est bonne
	 */
	public boolean authentification(String username, String password) {
		try {
			// ajouter vérif de l'event
			List <Object[]> usersResults = this.dbServer.doQuery("SELECT eventTag FROM users WHERE name=? AND digest=SHA1(?)", username, password);
			
			if (usersResults.size() != 1) {
				ServerLogger.info("L'utilisateur " + username + " n'existe pas, authentification rejetée");
				return false; // il doit y avoir exactement une entrée qui corresponde à l'utilisateur
			}
			
			if (!this.dbServer.getEvents().isValidNow((String) usersResults.get(0)[0])) {
				ServerLogger.info("L'utilisateur " + username + " est associé à un évènement inconnu ou non valide, authentification rejetée");
				return false;
			}
			
			ServerLogger.info("L'utilisateur " + username + " a été authentifié");
			return true;
		} catch (SQLException e) {
			this.dbError(e);
			return false;
		}
	}
	
	/**
	 * Retourne l'évènement auquel appartient l'utilisateur.
	 * 
	 * @param username le nom d'utilisateur
	 * 
	 * @return le tag de l'évènement
	 */
	public String getEventTagByUsername(String username) {
		try {
			List <Object[]> usersResults = this.dbServer.doQuery("SELECT eventTag FROM users WHERE name=?", username);
			return (String) usersResults.get(0)[0];
		} catch (SQLException e) {
			this.dbError(e);
			return "";
		}
	}
}
