package server;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.sql.SQLException;

import javax.net.ServerSocketFactory;

import org.apache.commons.configuration.ConfigurationException;

import database.MysqlServer;
import server.ServerLogger;

/**
 * Classe principale, prépare l'environnement et lance le serveur.
 * 
 * @author Mickaël Misbach
 * 
 * @see http://docs.oracle.com/javase/tutorial/networking/sockets/examples/KKMultiServer.java
 *
 * TODO : gérer les envois lorsque certaines choses sont vides
 * TODO : ajouter du logguing
 */
public class Server {
	
	/**
	 * Configurations du serveur.
	 */
	public static ServerConfiguration serverConfiguration;
	
	/**
	 * Serveur de base de données mysql.
	 */
	private static MysqlServer dbServer;
	
	/**
	 * Méthode main de lancement du serveur.
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) {
		
		// prise en charge d'une interruption externe
		Runtime.getRuntime().addShutdownHook(new ShutdownInterceptor());
		
		// chargement config
		Server.loadConfig();
	
		// lancement de la connexion à la BDD
		if (!Server.dbConnect())
			Server.endProgram(new RuntimeException("Connexion à la BDD pas possible."));
		
		// création du socket d'écoute
		ServerSocket serverSocket = Server.getServerSocket();
		
		// boucle infini pour l'acceptation des connexions
		int dbReconnectCount = 10; // nombre maxi de tentative de reconnexion à la BDD
		boolean listening = true;
		while (listening) {

			// check si mysql est alive
			if (!Server.dbServer.isAlive() && dbReconnectCount >= 0) {
				Server.dbConnect();
				dbReconnectCount--;
			}
			else {
				// db connecté on réinitialise le compteur
				dbReconnectCount = 10;
				
				// attend la connexion et démarre un thread quand une arrive
				try {
					ServerLogger.info("En attente d'un nouveau client ...");
					new ServerThread(serverSocket.accept(), dbServer).start();
				} catch (IOException e) {
					// on ne fait rien, on réessaie simplement
					ServerLogger.warning("Échec de la connexion avec un client : " + e.getMessage());
					// TODO : si problème réseau, on est infiment bloqué
				}
				
				// un peu de repos pour le proco dans le cas ou il y des IOException à l'infini
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) { 
					// quelque chose qui se produit si l'arrêt est demandé, on arrête l'écoute
					listening = false;
				}
			}
		}
	}
	
	/**
	 * Lance la connexion au serveur mysql.
	 * 
	 * @return true si la connexion est bonne.
	 */
	private static boolean dbConnect() {
		ServerLogger.config("Tentative de connexion à la DB");
		Server.dbServer = null;
		try {
			Server.dbServer = new MysqlServer();
		} catch (SQLException e) {
			// pas de connexion
			return false;
		}
		
		if (Server.dbServer == null)
			return false;
		else
			return Server.dbServer.isAlive();
	}
	
	/**
	 * Créé le socket d'écoute.
	 * 
	 * @return le socket d'écoute
	 */
	private static ServerSocket getServerSocket() {
		try {
			ServerLogger.config("Création du socket d'écoute");
			ServerSocketFactory factory = (ServerSocketFactory) ServerSocketFactory.getDefault();
			ServerSocket serverSocket = (ServerSocket) factory.createServerSocket(Server.serverConfiguration.getListeningPort(), 15, Inet4Address.getByName((Server.serverConfiguration.getListeningAddress())));
			return serverSocket;
		}
		catch (IOException e) {
			// impossible d'écouter sur le port choisi on arrête
			Server.endProgram(e);
			return null;
		}
	}
	
	/**
	 * Chargement de la serverConfiguration.
	 */
	private static void loadConfig() {
		try {
			//Server.serverConfiguration = new ServerConfiguration("../conf.xml");
			Server.serverConfiguration = new ServerConfiguration("conf/PecuniamExactamServerConf.xml");
		} catch (ConfigurationException e) {
			// échec du chargement de la config on s'arrête là
			ServerLogger.severe("Erreur lors du chargement de la serverConfiguration.");
			Server.endProgram(e);
		} catch (UnknownHostException e) {
			// échec du chargement de la config on s'arrête là
			ServerLogger.severe("Erreur lors du chargement de la serverConfiguration.");
			Server.endProgram(e);
		}
	}
	
	/**
	 * Déconnecter proprement toutes les connexions.
	 */
	static void niceEnding() {
		ServerLogger.info("Arrêt des connexions ...");
		Server.dbServer.shutdown();
	}
	
	/**
	 * Arrêter le serveur en cas d'exception bloquante.
	 * 
	 * @param e l'exception qui a fait s'arrêter le serveur
	 */
	public static void endProgram(Exception e) {
		ServerLogger.severe("Une exception a fait s'arrêter le serveur de manière inattendue : " + e.getMessage());
		e.printStackTrace();
		Server.niceEnding();// on essaie quand même d'arrêter les connexions
		System.exit(-1);
	}
	
	/**
	 * Arrêter le serveur en se déconnectant proprement.
	 */
	public static void endProgram() {
		Server.niceEnding();
		ServerLogger.info("Extinction du programme.");
		System.exit(0);
	}
}

/**
 * Classe permettant de prendre en charge une interruption externe du programme.
 * 
 * @author Mickaël Misbach
 * 
 * @see http://twit88.com/blog/2007/09/27/do-a-graceful-shutdown-of-your-java-application-when-ctr-c-kill/
 *
 */
class ShutdownInterceptor extends Thread {
	
	/**
	 * Constructeur par défaut.
	 */
	public ShutdownInterceptor() { }
	
	/**
	 * Sera lancé lorsque l'interruption sera demandée.
	 */
	public void run() {
		ServerLogger.info("Interruption externe demandée.");
		Server.niceEnding();
	}
}
