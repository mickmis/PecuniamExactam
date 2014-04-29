package messages;

/**
 * Classe représentant un évènement échangée via un ObjectStream.
 * 
 * @author Mickaël Misbach
 *
 */
public class SharedEvent extends SharedObject {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 834070464505678246L;
	
	/**
	 * Champs échangés.
	 */
	private boolean askForEventInformations;
	private String name;
	private String tag;
	private String description;
	
	/**
	 * Stands appartenant à l'évènement.
	 */
	private SharedStand[] stands;
	
	/**
	 * Constructeur générique.
	 * 
	 * @param lastMessage
	 * @param askForEventInformations
	 * @param name
	 * @param tag
	 * @param description
	 * @param stands
	 */
	private SharedEvent(boolean lastMessage, boolean askForEventInformations,
			String name, String tag, String description, SharedStand[] stands) {
		super(lastMessage);
		
		this.askForEventInformations = askForEventInformations;
		this.name = name;
		this.tag = tag;
		this.description = description;
		this.stands = stands;
	}
	
	/**
	 * Utilisé par le client pour demander les informations sur l'évènement.
	 * 
	 * @param lastMessage
	 */
	public SharedEvent(boolean lastMessage) {
		this(lastMessage, true, "", "", "", null);
	}
	
	/**
	 * Utilisé par le serveur pour envoyer les informations de l'évènement (produits, stands ...).
	 * 
	 * @param lastMessage
	 * @param name
	 * @param tag
	 * @param description
	 * @param stands
	 */
	public SharedEvent(boolean lastMessage, String name, String tag, String description, SharedStand[] stands) {
		this(lastMessage, false, name, tag, description, stands);
		
		if (name.isEmpty() || tag.isEmpty() || stands == null)
			throw new IllegalArgumentException("Erreur lors de l'initialisation du SharedEvent.");
	}

	/**
	 * @return the askForEventInformations
	 */
	public boolean isAskForEventInformations() {
		return askForEventInformations;
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
	 * @return the stands
	 */
	public SharedStand[] getStands() {
		return stands;
	}
}
