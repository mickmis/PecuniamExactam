package client;

import java.awt.EventQueue;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gui.main.MainFrame;
import messages.*;

/**
 * Classe principale qui va lancer le client.
 * Contient également de nombreux utilisés un peu partout.
 * 
 * @author Mickaël Misbach
 *
 */
public class Client {
	
	/**
	 * Champs statiques pour accès extérieur.
	 */
	public static Client client;
	public static MainFrame mainFrame;


	/**
	 * Champs partagés pour accès depuis extérieur.
	 */
	private ClientProtocol clientProtocol;
	private SharedEvent currentEvent;
	private int productListVersion;
	private boolean isDataUpToDate;
	private String clientUsername;
	private String clientPassword;
	private double cash;
	private boolean currentlySellingToStaff;
	private List<SharedProduct> currentSellingProducts;
	private List<SharedTransaction> cashBook;
	
	/**
	 * Adresse du serveur.
	 */
	private InetSocketAddress serverAdress;

	/**
	 * Lancement du client.
	 * @param serverAddress l'adresse et port du serveur
	 */
	public Client(InetSocketAddress serverAddress) {
		this.serverAdress = serverAddress;
		this.clientPassword = "";
		this.clientUsername = "";
		this.cashBook = new ArrayList<SharedTransaction>();
		this.currentSellingProducts = new ArrayList<SharedProduct>();
		this.productListVersion = -1;
		this.isDataUpToDate = false;
		this.currentlySellingToStaff = false;
		this.clientProtocol = new ClientProtocol();
	}
	
	/**
	 * Méthode de lancement main.
	 * 
	 * @param args
	 * @throws IllegalArgumentException si les arguments passés au programme ne sont pas bons
	 * @throws NullPointerException si il y a moins de deux arguments passés
	 */
	public static void main(String[] args) {
		
		// on parse la ligne de commande
		final InetSocketAddress serverAdress = new InetSocketAddress(args[0], Integer.parseInt(args[1]));
		System.out.println("Adresse serveur : " + serverAdress.toString());
        
		// on crée le client
		Client.client = new Client(serverAdress);
		
		// on lance la fenêtre principale
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Client.mainFrame = new MainFrame();
				Client.mainFrame.setVisible(true); }});
	}
	
	/**
	 * Recherche le nom du stand associé au produit dans la liste.
	 * 
	 * @param prod
	 * @return
	 */
	public String getStandNameByProduct(SharedProduct prod) {
		SharedStand[] stands = this.currentEvent.getStands();
		
		for (int i = 0 ; i < stands.length ; i++)
			for (int j = 0 ; j < stands[i].getProducts().length ; j++)
				if (stands[i].getProducts()[j].getName().equals(prod.getName()))
					return stands[i].getName();
			
		throw new IllegalArgumentException("Recherche d'un stand associé à un produit pas fructueuse, quelque chose qui ne doit pas arriver.");
	}


	
	
	/**
	 * @return toutes les transactions effectuées par l'utilisateur, additionne leur prix, y ajoute le fond de caisse et renvoie le résultat.
	 * @throws IOException si erreur IO
	 */
	public double getLiquidites() throws IOException {
		
		this.clientProtocol.retrieveCashBook(new Date(0), new java.util.Date());
		double total = 0;
		for (SharedTransaction trans : this.cashBook) 
			if (!trans.isSellingToStaff())// si on a vendu à un staff ça ne compte pas
				total += trans.getProduct().getPrice();
		return total + this.cash;
	}
	
	/**
	 * @return le prix total de la liste des produits actuellement en vente.
	 * EN CENTIMES !
	 */
	public int getTotalPrice() {
		if (this.isCurrentlySellingToStaff())// si c'est une transaction staff on ne compte pas
			return 0;
		
		double returnValue = 0;
		for (SharedProduct prod : this.currentSellingProducts)
			returnValue += prod.getPrice();
		
		return (int)(returnValue*100);
	}
	
	public InetSocketAddress getServerAdress() {
		return serverAdress;
	}

	public ClientProtocol getClientProtocol() {
		return clientProtocol;
	}

	public String getClientUsername() {
		return clientUsername;
	}

	public String getClientPassword() {
		return clientPassword;
	}

	public SharedEvent getCurrentEvent() {
		return currentEvent;
	}
	
	public List<SharedTransaction> getCashBook() {
		return cashBook;
	}

	public double getCash() {
		return cash;
	}

	public int getProductListVersion() {
		return productListVersion;
	}
	
	public boolean isDataUpToDate() {
		return isDataUpToDate;
	}
	
	public List<SharedProduct> getCurrentSellingProducts() {
		return currentSellingProducts;
	}
	
	public boolean isCurrentlySellingToStaff() {
		return this.currentlySellingToStaff;
	}
	
	public void setCurrentEvent(SharedEvent currentEvent) {
		this.currentEvent = currentEvent;
	}
	
	public void setProductListVersion(int productListVersion) {
		this.productListVersion = productListVersion;
	}
	
	public void setDataUpToDate(boolean isDataUpToDate) {
		this.isDataUpToDate = isDataUpToDate;
	}

	public void setClientUsername(String clientUsername) {
		this.clientUsername = clientUsername;
	}

	public void setClientPassword(String clientPassword) {
		this.clientPassword = clientPassword;
	}
	
	public void setCurrentlySellingToStaff(boolean currentlySellingToStaff) {
		this.currentlySellingToStaff = currentlySellingToStaff;
	}
	
	public void setCash(double cash) {
		this.cash = cash;
	}


}
