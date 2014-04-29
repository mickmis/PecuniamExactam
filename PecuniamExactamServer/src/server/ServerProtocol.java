package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import messages.*;
import messages.tools.MessageServerHandler;
import database.MysqlServer;

/**
 * Implémente le protocole de communication entre client et serveur.
 * 
 * @author Mickaël Misbach
 *
 */
public class ServerProtocol {
	
	/**
	 * Les streams de communication.
	 */
	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;
	
	/**
	 * La BDD.
	 */
	private MysqlServer dbServer;
	
	/**
	 * Le handler pour les messages reçus.
	 */
	private MessageServerHandler messageHandler;
	
	/**
	 * Les réponses au client.
	 */
	private List<SharedObject> responses;
	
	/**
	 * L'utilisateur à qui on dialogue.
	 */
	private SharedUser currentUser;
	
	/**
	 * Constructeur par défaut.
	 * Lance immédiatement le traitement des messages.
	 * 
	 * @param inputStream le stream entrant
	 * @param outputStream le stream sortant
	 * @param dbServer la BDD
	 * 
	 * @throws IOException en cas d'erreur IO
	 */
	public ServerProtocol(ObjectInputStream inputStream, ObjectOutputStream outputStream, MysqlServer dbServer) throws IOException {
		this.outputStream = outputStream;
		this.inputStream = inputStream;
		this.dbServer = dbServer;
		this.responses = new ArrayList<SharedObject>();
	}
	
	/**
	 * Récupère et traite tous les messages qui arrivent.
	 * 
	 * @throws IOException en cas d'erreur IO
	 */
	protected void processMessages() throws IOException {
		
		SharedObject nextMessage = null;
		try {
			// lecture et process des messages entrants
			do {
				
				// lecture du message
				nextMessage = (SharedObject) this.inputStream.readObject();
				
				// authentification + fond de caisse + prélèvement si besoin
				if (nextMessage instanceof SharedUser) // premiere condition pour voir si c'est un SharedUser, ou au un autre message
					if (!this.processAuth((SharedUser) nextMessage)) { // deuxième condition pour l'authentification
						// authentification échouée, on arrête là
						this.responses.add(new SharedUser(true, ((SharedUser) nextMessage).getUsername(), 0, false, -1));
						break;
					}
					else { // authenfication réussie
						
						// on sotck le currentUser
						this.currentUser = (SharedUser) nextMessage;
						
						// on détermine si il y a encore des messages après
						boolean isLastMessage = this.currentUser.isLastMessage();
						
						// on peut créer le messagehandler
						this.messageHandler = new MessageServerHandler(this.dbServer, this.responses, this.currentUser);
						
						// on envoie la réponse
						this.responses.add(new SharedUser(isLastMessage, ((SharedUser) nextMessage).getUsername(), 
							this.dbServer.getUsers().getCash(((SharedUser) nextMessage).getUsername()), true, this.dbServer.getProducts().getProductListVersion()));
					}
				else
					this.messageHandler.visit(nextMessage);
			} while (!nextMessage.isLastMessage());
			
			// on écrit les réponses
			for (SharedObject response : this.responses)
				this.outputStream.writeObject(response);
				
		} catch (ClassCastException e) {
			// si le message envoyé n'est pas un message valide ou qu'une erreur IO survient, on coupe la connexion
			ServerLogger.warning("Connexion coupée avec le client : " + e.getMessage());
			return;
		} catch (ClassNotFoundException e) {
			// ne doit pas se produire, erreur fatale
			Server.endProgram(e);
		}
	}
	
	/**
	 * Authentifie l'utilisateur et modifie au besoin le fond de caisse ou fait un prélèvement.
	 * Pas de modification du fond de caisse si authentification échoue.
	 * 
	 * @return si l'authentification a réussi
	 * 
	 * @param su l'objet à traiter
	 */
	private boolean processAuth(SharedUser su) {
		boolean auth = this.dbServer.getUsers().authentification(su.getUsername(), su.getPassword());
		if (auth)
			this.dbServer.getUsers().processCash(su.getUsername(), su.getCash());
		
		return auth;
	}
}
