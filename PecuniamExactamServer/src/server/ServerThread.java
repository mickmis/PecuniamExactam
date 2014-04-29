package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import database.MysqlServer;

/**
 * Thread gérant la connexion à un client.
 * Les communications sont chiffrées mais il n'y a pas (encore) d'authentification du client/serveur.
 * 
 * @author Mickaël Misbach
 *
 */
public class ServerThread extends Thread {
	
	/**
	 * Les streams de communication.
	 */
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	private Socket socket;
	
	/**
	 * La BDD:
	 */
	private MysqlServer dbserver;
	
	/**
	 * Constructeur par défaut, initialise le thread.
	 * 
	 * @param socket le socket de connexion vers le client
	 * @param dbServer la BDD
	 */
	public ServerThread(Socket socket, MysqlServer dbServer) {
		super("ServerThread");
		this.socket = socket;
		this.dbserver = dbServer;
	}
	
	/**
	 * Créé les streams vers le client.
	 * 
	 * @throws IOException en cas d'erreur IO
	 */
	private void createStreams() throws IOException {
		this.outputStream = new ObjectOutputStream(this.socket.getOutputStream());
		this.inputStream = new ObjectInputStream(this.socket.getInputStream());
	}
	
	/**
	 * Fermer les sockets/streams.
	 * Peut échouer, mais silencieusement.
	 */
	private void closeStreams() {
		try {
			this.outputStream.close();
			this.inputStream.close();
			this.socket.close();
		} catch (IOException e) {
			ServerLogger.warning("Échec lors de la fermeture des sockets.");
		}
	}
	
	/**
	 * Méthode de démarrage du thread.
	 */
	@Override
	public void run() {
		
		// création des streams et lancement du protocole
		try {
			this.createStreams();
			ServerProtocol protocol = 
					new ServerProtocol(this.inputStream, this.outputStream, this.dbserver);
			protocol.processMessages();
		} catch (IOException e) {
			// erreur IO, on ne fait rien et tout sera coupé
		}
		
		// on a fini on coupe
		this.closeStreams();
	}
}