package messages.tools;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import server.Server;
import server.ServerLogger;

import database.MysqlServer;

import messages.*;

/**
 * Implémente la pattern visitor servant à prendre en charge les messages.
 * 
 * @author Mickaël Misbach
 *
 */
public class MessageServerHandler implements MessageVisitor {
	
	/**
	 * Les réponses envoyés en client, créé dans le ServerProtocol
	 */
	private List<SharedObject> responses;
	
	/**
	 * L'utilisateur à qui on dialogue.
	 */
	private SharedUser currentUser;
	
	/**
	 * La BDD.
	 */
	private MysqlServer dbServer;
	
	/**
	 * Constructeur par défaut.
	 * 
	 * @param dbServer la BDD
	 * @param responses le pool de réponses au client
	 * @param su l'utilisateur à qui on dialogue
	 */
	public MessageServerHandler(MysqlServer dbServer, List<SharedObject> responses, SharedUser su) {
		this.responses = responses;
		this.currentUser = su;
		this.dbServer = dbServer;
	}
	
	/**
	 * Calcule la clé de vérification des tickets.
	 * 
	 * @param digestHeader le header du digest (xx-yy-zz sont les numéros des transactions).
	 * 
	 * @return la clé (4 caractères)
	 */
	private String computeVerifKey(String digestHeader) {
		
		// calcul du hash
		byte[] sha1hash = new byte[40];
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(digestHeader.getBytes("UTF-8"), 0, digestHeader.length());
			sha1hash = md.digest();
		} catch (UnsupportedEncodingException e) {
			Server.endProgram(e); // quelque chose qui ne doit pas se produire
		} catch(NoSuchAlgorithmException e)  {
			Server.endProgram(e); // quelque chose qui ne doit pas se produire
		}
        
        // conversion byte -> string
        StringBuilder string = new StringBuilder();
        for (byte b : sha1hash) {
            String hexString = Integer.toHexString(0x00FF & b);
            string.append(hexString.length() == 1 ? "0" + hexString : hexString);
        }
        
        // retour des caractères 0-8-16-24
        return new String(new char[]{string.charAt(0), string.charAt(8), string.charAt(16), string.charAt(24)});
	}
	
	/**
	 * Méthode pour faciliter la prise en charge des différents objets.
	 * 
	 * @param so l'objet à traiter
	 */
	public void visit(SharedObject so) {
		
		if (so instanceof SharedTransaction)
			this.visit((SharedTransaction) so);
		
		else if (so instanceof SharedProduct)
			this.visit((SharedProduct) so);
		
		else if (so instanceof SharedEvent)
			this.visit((SharedEvent) so);
		
		else if (so instanceof SharedStand)
			this.visit((SharedStand) so);
		
		else throw new IllegalArgumentException("L'objet n'est pas une instance d'un type supporté.");
	}
	
	/**
	 * S'occupe de gérer une transaction, ou de générer un journal de caisse.
	 */
	@Override
	public void visit(SharedTransaction st) {
		ServerLogger.fine("Traitement SharedTransaction");
		switch (st.getRequestType()) {
		
		case 0: // ajout d'une transaction par le client
			if (this.currentUser.getProductListVersion() == this.dbServer.getProducts().getProductListVersion()) { // verif version liste produits client à jour
				this.dbServer.getTransactions().addTransaction(st.getProduct().getTag(), this.dbServer.getUsers().getEventTagByUsername(this.currentUser.getUsername()), this.currentUser.getUsername(), st.getDigestHeader() + "/" + this.computeVerifKey(st.getDigestHeader()), st.getSellingTime(), st.isSellingToStaff());
				this.responses.add(new SharedTransaction(0));
			} else
				this.responses.add(new SharedTransaction()); // refus si pas à jour
				
			break;
			
		case 1: // demande de suppression d'une transaction par le client
			if (this.dbServer.getTransactions().deleteTransactions(st.getDigestHeader(), st.getSellingTime(), this.currentUser.getUsername())) // tentative de suppression
				this.responses.add(new SharedTransaction(1));
			else
				this.responses.add(new SharedTransaction()); // si a échoué on avertit le client
			break;
			
		case 2: // demande de liste de transactions
			if (this.currentUser.getProductListVersion() == this.dbServer.getProducts().getProductListVersion()) { // verif version liste produits client à jour
				SharedTransaction[] transactions = this.dbServer.getTransactions().getTransactions(
						st.getRequestedStartTime(), st.getRequestedEndTime(), this.currentUser.getUsername());
				
				if (transactions.length == 0) // si il n'y a pas de transaction, on envoie un refus
					this.responses.add(new SharedTransaction());
				else {
					// on prend la dernière transaction et on lui ajoute le flag dernier message
					transactions[transactions.length - 1] = new SharedTransaction(true, transactions[transactions.length - 1].getProduct(), transactions[transactions.length - 1].getDigestHeader(), transactions[transactions.length - 1].getSellingTime(), transactions[transactions.length - 1].isSellingToStaff());
					
					for (int i = 0 ; i < transactions.length ; i++)
						this.responses.add(transactions[i]);
				}
			} else
				this.responses.add(new SharedTransaction()); // refus si pas à jour
			break;
			
		default: // ne devrait pas se produire, on envoie un refus
			this.responses.add(new SharedTransaction());
			break;
		}
	}
	
	/**
	 * S'occupe de gérer une demande d'informations sur un évènement (produits, stands, évènement).
	 */
	@Override
	public void visit(SharedEvent se) {
		ServerLogger.fine("Traitement SharedEvent");
		if (!se.isAskForEventInformations())
			return;
		
		// on récupère l'eventTag de l'utilisateur actuel
		String currentEventTag = this.dbServer.getUsers().getEventTagByUsername(this.currentUser.getUsername());
		
		// on récupère la liste des stands de l'évènement
		List<String> standsTag = this.dbServer.getStands().getStandsByEvent(currentEventTag);
		
		// on crée les différents SharedStand
		SharedStand[] stands = new SharedStand[standsTag.size()];
		int i = 0;
		for (String standTag : standsTag) {
			
			// on récupère la liste des produits du stand
			List<String> productsTag = this.dbServer.getProducts().getProductsByStand(standTag);
			
			// on crée les différents SharedProduct
			SharedProduct[] products = new SharedProduct[productsTag.size()];
			int j = 0;
			for (String productTag : productsTag) {
				products[j] = new SharedProduct(false, this.dbServer.getProducts().getProductNameByTag(productTag), productTag, this.dbServer.getProducts().getProductUnitByTag(productTag), this.dbServer.getProducts().getProductDescriptionByTag(productTag), this.dbServer.getProducts().getProductPriceByTag(productTag));
				j++;
			}
			
			stands[i] = new SharedStand(false, this.dbServer.getStands().getStandNameByTag(standTag), standTag, this.dbServer.getStands().getStandDescriptionByTag(standTag), products);
			i++;
		}
		
		// envoi des objets
		this.responses.add(new SharedEvent(true, this.dbServer.getEvents().getEventName(currentEventTag), 
				currentEventTag, this.dbServer.getEvents().getEventDescription(currentEventTag), stands));
	}
	
	/**
	 * N'est pas utilisé dans cette implémentation.
	 */
	@Override
	public void visit(SharedUser su) {}
	
	/**
	 * N'est pas utilisé dans cette implémentation.
	 */
	@Override
	public void visit(SharedStand ss) { }
	
	/**
	 * N'est pas utilisé dans cette implémentation.
	 */
	@Override
	public void visit(SharedProduct sp) { }

}
