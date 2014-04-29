package messages;

/**
 * Classe représentant un stand échangée via un ObjectStream.
 * 
 * @author Mickaël Misbach
 *
 */
public class SharedStand extends SharedObject {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = -1055322419100499829L;
	
	/**
	 * Champs échangés.
	 */
	private String name;
	private String tag;
	private String description;
	
	/**
	 * Produits appartenant au stands.
	 */
	private SharedProduct[] products;

	/**
	 * Utilisé par le serveur pour envoyer des stands.
	 * 
	 * @param lastMessage
	 * @param name
	 * @param tag
	 * @param description
	 * @param products
	 */
	public SharedStand(boolean lastMessage, String name, String tag, String description, SharedProduct[] products) {
		super(lastMessage);
		
		if (name.isEmpty() || tag.isEmpty() || products == null)
			throw new IllegalArgumentException("Erreur lors de l'initialisation du SharedStand.");
		
		this.name = name;
		this.tag = tag;
		this.description = description;
		this.products = products;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the tag
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the products
	 */
	public SharedProduct[] getProducts() {
		return products;
	}
	
}
