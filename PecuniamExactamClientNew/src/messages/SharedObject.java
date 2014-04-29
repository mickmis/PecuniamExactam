package messages;

import java.io.Serializable;

/**
 * Classe mère de tous les objets.
 * 
 * @author Mickaël Misbach
 *
 */
public abstract class SharedObject implements Serializable {
	
	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = -4468625514277893617L;
	
	/**
	 * Définit si le message est le dernier à être envoyé.
	 */
	private boolean lastMessage;
	
	/**
	 * Constructeur par défaut.
	 * 
	 * @param lastMessage si le message est le dernier à être envoyé
	 */
	public SharedObject(boolean lastMessage) {
		this.lastMessage = lastMessage;
	}
	
	/**
	 * @return si le message est le dernier à être envoyé
	 */
	public boolean isLastMessage() {
		return this.lastMessage;
	}
}
