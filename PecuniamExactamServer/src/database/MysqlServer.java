package database;

import java.sql.*;
import java.util.List;

import org.apache.commons.dbutils.*;
import org.apache.commons.dbutils.handlers.ArrayListHandler;

import server.ServerConfiguration;
import server.Server;
import server.ServerLogger;

/**
 * This class represents a database server (MySQL) and provides methods to connect, disconnect it and do some queries.
 * Start the thread to connect to the database.
 * 
 * @author Mickaël Misbach
 *
 */
public class MysqlServer {
	
	/**
	 * Définit si la connexion est vivante.
	 */
	private boolean alive;
	
	/**
	 * Représente la connexion à la BDD.
	 */
	private Connection connection;
	
	/**
	 * Éxecute les requètes-
	 */
	private QueryRunner queryRunner;
	
	/**
	 * Objets représentants nos tables.
	 */
	private Events events;
	private Stands stands;
	private Users users;
	private Transactions transactions;
	private Products products;
	
	/**
	 * @throws SQLException si la connexion à la BDD échoue
	 */
	public MysqlServer() throws SQLException {
		
		this.alive = false;
		
		// chargement du driver et quitte si échoué
		if (!DbUtils.loadDriver("org.gjt.mm.mysql.Driver"))
			Server.endProgram(new RuntimeException("Échec lors du chargement du driver JDBC mysql."));
		
		ServerLogger.fine("Driver mysql JDBC chargé avec succès");
		
		// connexion à la BDD
		this.connection = DriverManager.getConnection(this.getJdbcConnectionUrl(), 
				Server.serverConfiguration.getMysqlServerUsername(), 
				Server.serverConfiguration.getMysqlServerPassword());
		
		this.queryRunner = new QueryRunner();
		this.initializeTables();
		this.alive = true;
		
		ServerLogger.config("Connexion à la BDD Mysql faite avec succès");
	}
	
	/**
	 * Initialise les objets représentants les tables de la BDD.
	 */
	private void initializeTables() {
		this.events = new Events(this);
		this.stands = new Stands(this);
		this.users = new Users(this);
		this.transactions = new Transactions(this);
		this.products = new Products(this);
	}
	
	/**
	 * Exécute une requete de type select.
	 * 
	 * @return la liste des résultats
	 * 
	 * @throws SQLException 
	 */
	protected List<Object[]> doQuery(String query, Object... parameters) throws SQLException {
		return this.queryRunner.query(this.connection, query, new ArrayListHandler(), parameters);
	}
	
	/**
	 * Exécute une requete de type insert ou autre.
	 * 
	 * @return le nombres d'entrées modifiées
	 * 
	 * @throws SQLException 
	 */
	protected int doUpdate(String query, Object... parameters) throws SQLException {
		return this.queryRunner.update(this.connection, query, parameters);
	}
	
	/**
	 * Construit l'URL de connection JDBC en allant chercher les informations dans la serverConfiguration.
	 * 
	 * @return URL de connection JDBC
	 */
	private String getJdbcConnectionUrl() {
		ServerConfiguration conf = Server.serverConfiguration;
		return "jdbc:mysql://" + conf.getMysqlServerAddress().getHostAddress()
				+ ":" + conf.getMysqlServerPort() 
				+ "/" + conf.getMysqlServerDatabase();
	}
	
	/**
	 * Arrête la connexion à la BDD silencieusement (ne renvoie rien si il y une erreur).
	 */
	public void shutdown() {
		DbUtils.closeQuietly(this.connection);
		this.alive = false;
		ServerLogger.config("Connexion BDD fermée.");
	}
	
	/**
	 * @return vraie si la connexion est active.
	 */
	public boolean isAlive() {
		return this.alive;
	}
	
	/**
	 * Définit si la connexion est active, accessible dans package seulement.
	 */
	protected void setAlive(boolean alive) {
		this.alive = alive;
	}

	/**
	 * @return the events
	 */
	public Events getEvents() {
		return events;
	}

	/**
	 * @return the stands
	 */
	public Stands getStands() {
		return stands;
	}

	/**
	 * @return the users
	 */
	public Users getUsers() {
		return users;
	}

	/**
	 * @return the transactions
	 */
	public Transactions getTransactions() {
		return transactions;
	}

	/**
	 * @return the products
	 */
	public Products getProducts() {
		return products;
	}
}
