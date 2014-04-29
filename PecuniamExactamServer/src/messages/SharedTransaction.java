package messages;

import java.util.Date;

/**
 * Classe représentant une transaction échangée via un ObjectStream.
 * Attention lors de l'utilisation des constructeurs, ils ont des sens différents.
 * 
 * @author Mickaël Misbach
 *
 */
public class SharedTransaction extends SharedObject {
	
	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = -6956800257955596269L;
	
	/**
	 * Type de requête : <br />
	 * 0 -> ajouter une transaction (mode client), transaction ajoutée avec succés (mode serveur) <br />
	 * 1 -> supprimer une transaction (mode client), transaction supprimée avec succès (mode serveur) <br />
	 * 2 -> demander une liste de transaction (mode client), envoi des transactions (mode serveur)<br />
	 * 3 -> demande de n'importe quel type refusée
	 */
	private int requestType;
	
	/**
	 * Produit vendu.
	 */
	private SharedProduct product;
	
	/**
	 * Champs échangés.
	 */
	private Date sellingTime;
	private boolean sellingToStaff;
	private Date requestedStartTime;
	private Date requestedEndTime;
	private String digestHeader; // de la forme xx-yy-zz, xx, yy et zz étant les numéros de transaction
	
	/**
	 * Constructeur générique.<br />
	 * Type de requête : <br />
	 * 0 -> ajouter une transaction (mode client), transaction ajoutée avec succés (mode serveur) <br />
	 * 1 -> supprimer une transaction (mode client), transaction supprimée avec succès (mode serveur) <br />
	 * 2 -> demander une liste de transaction (mode client), envoi des transactions (mode serveur)<br />
	 * 3 -> demande de n'importe quel type refusée
	 * 
	 * @param lastMessage
	 * @param requestType
	 * @param product
	 * @param sellingTime
	 * @param sellingToStaff
	 * @param requestedEndTime
	 * @param requestedStartTime
	 */
	private SharedTransaction(boolean lastMessage, int requestType, SharedProduct product, String digestHeader,
			Date sellingTime, boolean sellingToStaff, Date requestedStartTime, Date requestedEndTime) {
		super(lastMessage);
		
		if (requestType < 0 || requestType > 3)
			throw new IllegalArgumentException("Erreur lors de l'initialisation du SharedTransaction.");
		
		this.requestType = requestType;
		this.product = product;
		this.digestHeader = digestHeader;
		this.sellingTime = sellingTime;
		this.sellingToStaff = sellingToStaff;
		this.requestedStartTime = requestedStartTime;
		this.requestedEndTime = requestedEndTime;
	}
	
	/**
	 * Constructeur utilisé par le serveur pour refuser une demande.
	 */
	public SharedTransaction() {
		this(true, 3, null, null, null, false, null, null);
	}
	
	/**
	 * Constructeur utilisé par le client pour demander une liste de transactions.
	 * 
	 * @param lastMessage
	 * @param requestedStartTime
	 * @param requestedEndTime
	 */
	public SharedTransaction(boolean lastMessage, Date requestedStartTime, Date requestedEndTime) {
		this(lastMessage, 2, null,null, null, false, requestedStartTime, requestedEndTime);
		
		if (requestedEndTime == null || requestedStartTime == null)
			throw new IllegalArgumentException("Erreur lors de l'initialisation du SharedTransaction.");
	}
	
	/**
	 * Constructeur utilisé par le serveur pour envoyer des transactions.
	 * 
	 * @param lastMessage
	 * @param product
	 * @param sellingTime
	 * @param sellingToStaff
	 */
	public SharedTransaction(boolean lastMessage, SharedProduct product, String digestHeader, Date sellingTime, boolean sellingToStaff) {
		this(lastMessage, 2, product, digestHeader, sellingTime, sellingToStaff, null, null);
		
		if (product == null || sellingTime == null || digestHeader.isEmpty())
			throw new IllegalArgumentException("Erreur lors de l'initialisation du SharedTransaction.");
	}
	
	/**
	 * Constructeur utilisé par le client pour effectuer (requestType = 0) ou supprimer (requestType = 1) une transaction.
	 * 
	 * @param lastMessage
	 * @param requestType
	 * @param product
	 * @param sellingTime
	 * @param sellingToStaff
	 */
	public SharedTransaction(boolean lastMessage, int requestType, SharedProduct product, String digestHeader, Date sellingTime, boolean sellingToStaff) {
		this(lastMessage, requestType, product, digestHeader, sellingTime, sellingToStaff, null, null);
		
		if (sellingTime == null || digestHeader.isEmpty() || (requestType != 0 && requestType != 1))
			throw new IllegalArgumentException("Erreur lors de l'initialisation du SharedTransaction.");
	}
	
	/**
	 * Constructeur utilisé par le serveur pour confirmer l'ajout (requestType = 0) ou la suppression (requestType = 1) d'une transaction.
	 * 
	 * @param lastMessage
	 * @param requestType
	 * @param product
	 * @param sellingTime
	 * @param sellingToStaff
	 */
	public SharedTransaction(int requestType) {
		this(true, requestType, null, null, null, false, null, null);
		
		if (requestType != 0 && requestType != 1)
			throw new IllegalArgumentException("Erreur lors de l'initialisation du SharedTransaction.");
	}

	/**
	 * @return the requestType
	 */
	public int getRequestType() {
		return requestType;
	}

	/**
	 * @return the product
	 */
	public SharedProduct getProduct() {
		return product;
	}

	/**
	 * @return the digestHeader
	 */
	public String getDigestHeader() {
		return digestHeader;
	}

	/**
	 * @return the sellingTime
	 */
	public Date getSellingTime() {
		return sellingTime;
	}

	/**
	 * @return the sellingToStaff
	 */
	public boolean isSellingToStaff() {
		return sellingToStaff;
	}

	/**
	 * @return the requestedStartTime
	 */
	public Date getRequestedStartTime() {
		return requestedStartTime;
	}

	/**
	 * @return the requestedEndTime
	 */
	public Date getRequestedEndTime() {
		return requestedEndTime;
	}
}
