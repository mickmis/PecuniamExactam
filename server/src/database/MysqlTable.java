package database;

import java.sql.SQLException;

import server.ServerLogger;

public abstract class MysqlTable {
	
	/**
	 * Référence vers le serveur mysql.
	 */
	protected MysqlServer dbServer;
	
	/**
	 * 
	 * @param dbServer
	 */
	public MysqlTable(MysqlServer dbServer) {
		this.dbServer = dbServer;
	}
	
	/**
	 * Appelé lors d'une SQLException.
	 * 
	 * @param e la SQLException
	 */
	protected void dbError(SQLException e) {
		this.dbServer.setAlive(false);
		this.dbServer.shutdown();
		ServerLogger.severe("Erreur mysql, connexion down ou erreur dans une requête (code d'erreur = " + e.getErrorCode() + ") : " + e.getMessage());
	}
}
