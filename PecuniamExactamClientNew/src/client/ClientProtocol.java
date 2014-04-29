package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.net.SocketFactory;

import messages.tools.MessageClientHandler;
import messages.*;

public class ClientProtocol {
	
	/**
	 * Les streams de communication.
	 */
	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;
	private Socket socket;
	
	/**
	 * Le handler pour les messages reçus.
	 */
	private MessageClientHandler messageHandler;
	
	/**
	 * Les réponses au client.
	 */
	private List<SharedObject> responses;
	
	/**
	 * L'utilisateur actuel.
	 */
	private SharedUser currentUser;
	
	/**
	 * Constructeur par défaut.
	 * 
	 * @param inputStream le stream entrant
	 * @param outputStream le stream sortant
	 * 
	 */
	public ClientProtocol() {
		this.messageHandler = new MessageClientHandler();
		this.responses = new ArrayList<SharedObject>();
	}
	
	/**
	 * établissement de la connexion au serveur
	 * @throws IOException 
	 */
	private void connect() throws IOException {
		SocketFactory factory = (SocketFactory) SocketFactory.getDefault();
		
		this.socket = (Socket) factory.createSocket(Client.client.getServerAdress().getAddress(), Client.client.getServerAdress().getPort());
		this.socket.setSoTimeout(5000); // 5 secondes de timeout
		this.outputStream = new ObjectOutputStream(this.socket.getOutputStream());
		this.inputStream = new ObjectInputStream(this.socket.getInputStream());
	}
	
	/**
	 * Fermer les sockets/streams.
	 * Peut échouer, mais silencieusement.
	 */
	private void disconnect() {
		try {
			this.outputStream.close();
			this.inputStream.close();
			this.socket.close();
		} catch (IOException e) {
			// déconnexion silencieuse, on ne fait rien
		}
	}
	
	/**
	 * Envoie un message et stocke la/les réponses dans this.responses.
	 * 
	 * @param so
	 * @throws IOException 
	 */
	private void sendMessage(SharedObject so, boolean doAuth) throws IOException {
		// connection et vider responses
		this.connect();
		this.responses.clear();
		
		// si authentification demandé envoi auth
		if (doAuth) {
			this.outputStream.writeObject(new SharedUser(false, Client.client.getClientUsername(), Client.client.getClientPassword(), 0, Client.client.getProductListVersion()));
			System.out.println("Envoi du message : SharedUser");
		}
		// envoi de l'objet
		this.outputStream.writeObject(so);
		System.out.println("Envoi du message : " + so.getClass());
		
		// récupération des messages entrants
		SharedObject nextMessage = null;
		do {
			try {
				nextMessage = (SharedObject) this.inputStream.readObject();
				System.out.println("Réception du message : " + nextMessage.getClass());
				this.responses.add(nextMessage);
			} catch (ClassNotFoundException e) {
				// ça ça ne devrait pas se passer ... on arrête le client
				System.out.println("Erreur de cast lors de la lecture de l'objet entrant : " + e.getMessage());
				e.printStackTrace();
				System.exit(-1);
			} catch (SocketTimeoutException e) {
				System.out.println("Timeout atteint : " + e.getMessage());
				throw new IOException(e.getMessage());
			}
		} while (!nextMessage.isLastMessage());

		this.disconnect();
	}
	/**
	 * Génère un nombre aléatoire qui fera office de digestHeader, 8 caractères (parfois moins si le random est 0.0...).
	 * -->> COMMUN AUX TRANSACTIONS D'UN MÊME TICKET <<-- 
	 * @return
	 */
	public String generateRandomDigestHeader() {
		int random = Math.round(Math.round(Math.random() * Math.pow(10, 8)));
		System.out.println("random = " + random);
		return Integer.toString(random);
	}
	/**
	 * Envoie les messages vers le MessageHandler.
	 */
	private void processReceivedMessages() {
		for (SharedObject so : this.responses)
			this.messageHandler.visit(so);
		this.responses.clear();
	}
	
	/**
	 * Lit le premier message et si c'est un SharedUser lit si il est identifié.
	 * 
	 * @return si l'utilisateur a été identifié
	 */
	private boolean readSharedUserForAuth() {
		SharedUser su = null;
		try {
			su = (SharedUser) this.responses.get(0);
			this.responses.remove(0);
		} catch (ClassCastException e) {
			System.out.println("Erreur lors de la lecture du SharedUser : " + e.getMessage());
			return false;
		} catch (IndexOutOfBoundsException e) {
			System.out.println("Erreur lors de la lecture du SharedUser : " + e.getMessage());
			return false;
		}
		
		// vérification version liste de produits
		if (Client.client.getProductListVersion() < su.getProductListVersion())
			Client.client.setDataUpToDate(false);
		
		// on le stocke
		this.currentUser = su;
		
		// on update le cash
		Client.client.setCash(su.getCash());
		
		System.out.println("Authentication utilisateur : " + su.isAuthenticated() + ", version liste produits sur serveur : " + su.getProductListVersion());
		return su.isAuthenticated();
	}
	
	/**
	 * Authentifie auprès du serveur et envoie la modif fond de caisse / prélèvement.
	 * Pour authentification uniquement mettre cash à 0.
	 * 
	 * @param relativeCash la valeur relative du cash à modifier
	 * 
	 * @return si l'authentification a réussi
	 * 
	 * @throws IOException 
	 * 
	 */
	public boolean processCash(double relativeCash) throws IOException {
		
		// envoi du message
		this.sendMessage(new SharedUser(true, Client.client.getClientUsername(), Client.client.getClientPassword(), relativeCash, Client.client.getProductListVersion()), false);
		
		// le serveur ne doit nous renvoyer qu'un seul SharedUser
		boolean auth = false;
		try {
			auth = ((SharedUser) this.responses.get(0)).isAuthenticated();
		} catch (ClassCastException e) {
			throw new IOException("Erreur lors traduction message : " + e.getMessage());
		} catch (IndexOutOfBoundsException e) {
			throw new IOException("Erreur lors traduction message : " + e.getMessage());
		}
		
		if (auth) {
			Client.client.setCash(((SharedUser) this.responses.get(0)).getCash());
			System.out.println("Modification cash : " + relativeCash);
		}
			return auth;
	}
	
	/**
	 * Authentifie et rafraichit les données dispo.
	 * @param username
	 * @param password
	 * @param relativeCash
	 * @return
	 * @throws IOException 
	 */
	public boolean processEventInformationsRetrieving() throws IOException {
		
		// envoi du message
		this.sendMessage(new SharedEvent(true), true);
		
		// vérif authentification
		if (!this.readSharedUserForAuth())
			return false;
		
		// vérification message du bon type
		if (!this.responses.get(0).getClass().equals(SharedEvent.class)) {
			System.out.println("Reçu classe invalide (" + this.responses.get(0).getClass().getName() + ")");
			this.responses.clear();
			return false;
		}
		
		// traitement du message SharedEvent en retour
		this.processReceivedMessages();
		
		// si tout est bon on peut updater la version de la liste des produits
		Client.client.setProductListVersion(this.currentUser.getProductListVersion());
		
		return true;
	}
	
	/**
	 * 
	 * @param digestHeader le digestHeader commun aux transactions d'un même ticket
	 * @return
	 * @throws IOException 
	 */
	public boolean processTransaction(SharedProduct product, boolean sellingToStaff, String digestHeader) throws IOException {
		
		// vérif liste est à jour
		if (!Client.client.isDataUpToDate()) {
			System.out.println("Liste des produits pas à jour.");
			return false;
		}
		
		// envoi du message
		this.sendMessage(new SharedTransaction(true, 0, product, digestHeader, new Date(), sellingToStaff), true);
		
		// vérif authentification
		if (!this.readSharedUserForAuth())
			return false;
		
		// vérification message du bon type et pas sharedtransaction type 3
		if (!this.responses.get(0).getClass().equals(SharedTransaction.class) || ((SharedTransaction)this.responses.get(0)).getRequestType() == 3) {
			System.out.println("Reçu refus (type 3) ou classe invalide (" + this.responses.get(0).getClass().getName() + ")");
			this.responses.clear();
			return false;
		}
		
		// traitement du message SharedEvent en retour
		this.processReceivedMessages();
		
		return true;
	}
	

	/**
	 * 
	 * @param digestHeader
	 * @param sellingTime
	 * @return
	 * @throws IOException
	 */
	public boolean deleteTransaction(String digestHeader, Date sellingTime) throws IOException {
		
		// envoi du message
		this.sendMessage(new SharedTransaction(true, 1, null, digestHeader, sellingTime, false), true);
		
		// vérif authentification
		if (!this.readSharedUserForAuth())
			return false;
		
		// vérification message du bon type et pas sharedtransaction type 3
		if (!this.responses.get(0).getClass().equals(SharedTransaction.class) || ((SharedTransaction)this.responses.get(0)).getRequestType() == 3) {
			System.out.println("Reçu refus (type 3) ou classe invalide (" + this.responses.get(0).getClass().getName() + ")");
			this.responses.clear();
			return false;
		}
		
		// traitement du message SharedEvent en retour
		this.processReceivedMessages();
		
		return true;
	}
	
	/**
	 * @throws IOException 
	 * 
	 */
	public boolean retrieveCashBook(Date requestedStartTime, Date requestedEndTime) throws IOException {
		
		// suppression ancienne liste
		Client.client.getCashBook().clear();
		
		// envoi du message
		this.sendMessage(new SharedTransaction(true, requestedStartTime, requestedEndTime), true);
		
		// vérif authentification
		if (!this.readSharedUserForAuth())
			return false;
		
		// vérification du type de message faite dans le message handler (car nombre indéterminé)
		
		// traitement du message SharedEvent en retour
		this.processReceivedMessages();
		
		return true;
	}
}

