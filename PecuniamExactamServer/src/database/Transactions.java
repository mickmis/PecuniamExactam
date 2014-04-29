package database;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Date;

import server.ServerLogger;
import messages.SharedTransaction;

public class Transactions extends MysqlTable {
	
	/**
	 * Constructeur par défaut.
	 * 
	 * @param dbServer la base de donnée sur laquelle on éxécute les requètes.
	 */
	public Transactions(MysqlServer dbServer) {
		super(dbServer);
	}
	
	/**
	 * Ajoute une transaction dans la base de donnée.
	 * 
	 * @param productTag
	 * @param eventTag
	 * @param sellerUsername
	 * @param digest
	 * @param sellingTime
	 * @param sellingToStaff
	 */
	public void addTransaction(String productTag, String eventTag, String sellerUsername,
			String digest, Date sellingTime, boolean sellingToStaff) {
		try {
			this.dbServer.doUpdate("INSERT INTO transactions(productTag, eventTag, userName, digest, date, staff) " +
					"VALUES (?, ?, ?, ?, ?, ?)", productTag, eventTag, sellerUsername, digest, new Timestamp(sellingTime.getTime()), sellingToStaff);
		} catch (SQLException e) {
			this.dbError(e);
		}
		ServerLogger.fine("Transaction ajoutée : " + digest);
	}
	
	/**
	 * Supprimer des transactions.
	 * Le digestHeader permet de sélectionner les transactions à supprimer (le digestHeader est commun à toutes les transactions du ticket).
	 * 
	 * @param digestHeader
	 * @param sellingTime 
	 * @param username
	 * 
	 * @return si des transactions ont été supprimées.
	 */
	public boolean deleteTransactions(String digestHeader, Date sellingTime, String username) {
		int returnValue = 0;
		try {
			// retourne le nombre d'enregistrements supprimés
			returnValue = this.dbServer.doUpdate("DELETE FROM transactions WHERE digest LIKE ? AND userName=? AND date=?", digestHeader+"%", username, new Timestamp(sellingTime.getTime()));
		} catch (SQLException e) {
			this.dbError(e);
		}
		
		if (returnValue != 0) { // transactions supprimées ok
			ServerLogger.fine("Transaction supprimée : " + digestHeader);
			return true;
		}
		else { // erreur, ou refusé
			ServerLogger.warning("Une transaction n'a pas été supprimée : " + digestHeader);
			return false;
		}
	}
	
	/**
	 * Récupère la liste des transactions d'une date à une autre.
	 * 
	 * @param requestedStartTime
	 * @param requestedEndTime
	 * @param username
	 * @return null si pas de résultats, sinon la liste demandée
	 */
	public SharedTransaction[] getTransactions(Date requestedStartTime, Date requestedEndTime, String username) {
		List<Object[]> results = null;
		try {
			results = this.dbServer.doQuery("SELECT * FROM transactions WHERE date >=? AND date <=?", new Timestamp(requestedStartTime.getTime()), new Timestamp(requestedEndTime.getTime()));
		} catch (SQLException e) {
			this.dbError(e);
		}
		
		// on retourne null si y'a une erreur
		if (results == null)
			return null;
		
		// création de l'array de retour à partir des résultats
		SharedTransaction[] returnArray = new SharedTransaction[results.size()];
		for (int i = 0 ; i < results.size() ; i++)
			returnArray[i] = new SharedTransaction(false, this.dbServer.getProducts().getProductByTag((String) results.get(i)[1]), (String) results.get(i)[4], ((Timestamp) results.get(i)[5]), (Boolean) results.get(i)[6]);
		
		ServerLogger.fine("Requète sur les transaction allant de " + requestedStartTime.getTime() + " à " + requestedEndTime.getTime() + " traitée");
		return returnArray;
	}

}
