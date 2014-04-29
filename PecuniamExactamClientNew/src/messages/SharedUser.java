package messages;

/**
 * Classe représentant un utilisateur échangée via un ObjectStream.
 * Attention : le cash est défini de façon relative.
 * 
 * @author Mickaël Misbach
 *
 */
public class SharedUser extends SharedObject {
	
	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 8564391741992488501L;
	
	/**
	 * Champs échangés.
	 */
	private String username;
	private String password;
	private double cash;
	private boolean authenticated;
	private int productListVersion;
	
	/**
	 * Constructeur générique.
	 * 
	 * @param lastMessage
	 * @param username
	 * @param password
	 * @param cash
	 * @param authenticated
	 * @param productListVersion
	 */
	private SharedUser(boolean lastMessage, String username, String password, double cash, boolean authenticated, int productListVersion) {
		super(lastMessage);
		
		if (username.isEmpty() || productListVersion < -1)
			throw new IllegalArgumentException("Erreur lors de l'initialisation du SharedUser.");
		
		this.username = username;
		this.password = password;
		this.cash = cash;
		this.authenticated = authenticated;
		this.productListVersion = productListVersion;
	}
	
	/**
	 * Utilisé par le client pour authentification et modification fond de caisse.
	 * 
	 * @param lastMessage
	 * @param username
	 * @param password
	 * @param cash
	 * @param productListVersion
	 */
	public SharedUser(boolean lastMessage, String username, String password, double cash, int productListVersion) {
		this(lastMessage, username, password, cash, false, productListVersion);

		if (password.isEmpty())
			throw new IllegalArgumentException("Erreur lors de l'initialisation du SharedUser.");
	}
	
	/**
	 * Utilisé par le serveur pour réponse à l'authentification et renvoi du fond de caisse.
	 * 
	 * @param lastMessage
	 * @param username
	 * @param cash
	 * @param authenticated
	 * @param productListVersion
	 */
	public SharedUser(boolean lastMessage, String username, double cash, boolean authenticated, int productListVersion) {
		this(lastMessage, username, "", cash, authenticated, productListVersion);
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return the cash
	 */
	public double getCash() {
		return cash;
	}

	/**
	 * @return the authenticated
	 */
	public boolean isAuthenticated() {
		return authenticated;
	}

	/**
	 * @return the productListVersion
	 */
	public int getProductListVersion() {
		return productListVersion;
	}
	
	
}
