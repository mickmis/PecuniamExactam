package messages;

/**
 * Classe représentant un produit échangée via un ObjectStream.
 * 
 * @author Mickaël Misbach
 *
 */
public class SharedProduct extends SharedObject {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 7032286044577309028L;
	
	/**
	 * Champs échangés.
	 */
	private String name;
	private String tag;
	private String unit;
	private String description;
	private double price;
	
	/**
	 * Constructeur générique.
	 * 
	 * @param lastMessage
	 * @param askForProductList
	 * @param name
	 * @param tag
	 * @param unit
	 * @param description
	 * @param price
	 */
	public SharedProduct(boolean lastMessage, String name, String tag, String unit, String description, double price) {
		super(lastMessage);
		
		if (name.isEmpty() || tag.isEmpty())
			throw new IllegalArgumentException("Erreur lors de l'initialisation du SharedProduct.");
		
		this.name = name;
		this.tag = tag;
		this.unit = unit;
		this.description = description;
		this.price = price;
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
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}
}
