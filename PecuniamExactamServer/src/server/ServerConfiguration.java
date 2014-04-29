package server;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.configuration.*;

/**
 * Classe servant à charger, conserver et si besoin modifier la serverConfiguration du serveur.
 * 
 * @author Mickaël Misbach
 *
 */
public class ServerConfiguration extends XMLConfiguration {

	/**
	 * Doit être sérialisable à cause de l'héritage.
	 */
	private static final long serialVersionUID = -6966953129797875825L;
	
	/**
	 * Les champs suivants servent à se connecter à la BDD mysql.
	 */
	private InetAddress mysqlServerAddress;
	private int mysqlServerPort;
	private String mysqlServerDatabase;
	private String mysqlServerUsername;
	private String mysqlServerPassword;
	
	/**
	 * Le port et adresse sur lequel le serveur va écouter.
	 */
	private int listeningPort;
	private String listeningAddress;
	
	/**
	 * Niveau de debug. Entre 0 et 3 inclus.
	 */
	private int debugLevel;
	
	/**
	 * Constructeur par défaut, définit le fichier de serverConfiguration et le charge.
	 * Peut être uniquement instancié dans le package.
	 * 
	 * @throws ConfigurationException si erreur de serverConfiguration
	 * @throws UnknownHostException si l'adresse mysql n'est pas valide
	 */
	public ServerConfiguration(String filePath) throws ConfigurationException, UnknownHostException {
		super(filePath);
		this.loadConfig();
	}
	
	/**
	 * Loads serverConfiguration from file.
	 * 
	 * @throws ConfigurationException si erreur de serverConfiguration
	 * @throws UnknownHostException si l'adresse mysql n'est pas valide
	 */
	private void loadConfig() throws ConfigurationException, UnknownHostException {
		
		ServerLogger.config("Chargement de la serverConfiguration ...");
		
		this.mysqlServerAddress = InetAddress.getByName(this.getString("mysqlServerAddress"));
		ServerLogger.config("Adresse serveur mysql chargée : " + this.mysqlServerAddress.toString());
		
		this.mysqlServerPort = this.getInt("mysqlServerPort");
		ServerLogger.config("Port serveur mysql chargé: " + this.mysqlServerPort);
		
		this.mysqlServerDatabase = this.getString("mysqlServerDatabase");
		ServerLogger.config("Base de donnée serveur mysql chargé: " + this.mysqlServerDatabase);
		
		this.mysqlServerUsername = this.getString("mysqlServerUsername");
		ServerLogger.config("Nom d'utilisateur serveur mysql chargé: " + this.mysqlServerUsername);

		this.mysqlServerPassword = this.getString("mysqlServerPassword");
		ServerLogger.config("Mot de passe serveur mysql chargé");
		
		this.setListeningPort(this.getInt("listeningPort"));
		ServerLogger.config("Port serveur chargé: " + this.listeningPort);

		this.listeningAddress = this.getString("listeningAddress");
		ServerLogger.config("Adresse écoute serveur chargé: " + this.listeningAddress);
		
		this.setDebugLevel(this.getInt("debugLevel"));
		ServerLogger.config("Niveau de debug chargé: " + this.debugLevel);
	}
	
	/**
	 * @return l'adresse du serveur mysql
	 */
	public InetAddress getMysqlServerAddress() {
		return this.mysqlServerAddress;
	}

	/**
	 * @return the le port de connexion au serveur mysql
	 */
	public int getMysqlServerPort() {
		return mysqlServerPort;
	}

	/**
	 * @return la base de donnée mysql sur laquelle on se connecte
	 */
	public String getMysqlServerDatabase() {
		return mysqlServerDatabase;
	}

	/**
	 * @return le nom d'utilisateur pour la connexion au serveur mysql
	 */
	public String getMysqlServerUsername() {
		return this.mysqlServerUsername;
	}

	/**
	 * @return le mot de passe pour la connexion au serveur mysql
	 */
	public String getMysqlServerPassword() {
		return this.mysqlServerPassword;
	}
	
	/**
	 * @return le port d'écoute du serveur
	 */
	public int getListeningPort() {
		return this.listeningPort;
	}

	/**
	 * @return le niveau de debug
	 */
	public int getDebugLevel() {
		return this.debugLevel;
	}

	/**
	 * Définit le port d'écoute du serveur
	 * 
	 * @param listeningPort le port à définir
	 * 
	 * @throws ConfigurationException si le port n'est pas valide
	 */
	private void setListeningPort(int listeningPort) throws ConfigurationException {
		if (listeningPort < 1 || listeningPort > 65535)
			throw new ConfigurationException("Port non valide");
		
		this.listeningPort = listeningPort;
	}

	/**
	 * Définit le niveau de debug.
	 * 
	 * @param debugLevel le niveau de debug
	 * 
	 * @throws ConfigurationException si le niveau de debug n'est pas valide
	 */
	private void setDebugLevel(int debugLevel) throws ConfigurationException {
		if (debugLevel < 0 || debugLevel > 3)
			throw new ConfigurationException("Le niveau de debug n'est pas valide");
		
		this.debugLevel = debugLevel;
		ServerLogger.initialize(debugLevel);
	}
	
	/**
	 * @return l'adresse d'écoute
	 */
	public String getListeningAddress() {
		return this.listeningAddress;
	}
}
