package messages.tools;

import messages.*;

/**
 * Interface participant à l'implémentation du visitor pattern.
 * 
 * @author Mickaël Misbach
 */
public interface MessageVisitor {
	
	/**
	 * Prend en charge message de type SharedEvent (demande / échange d'infos).
	 * 
	 * @param su le message SharedEvetn 
	 */
	public void visit(SharedEvent se);
	
	/**
	 * Prend en charge message de type SharedStand (échange d'infos).
	 * 
	 * @param su le message SharedUser 
	 */
	public void visit(SharedStand ss);
	
	/**
	 * Prend en charge message de type SharedUser (authentification, fond de caisse, prélèvement).
	 * 
	 * @param su le message SharedUser 
	 */
	public void visit(SharedUser su);
	
	/**
	 * Prend en charge message de type SharedTransaction (effectuer, annuler une transaction, envoyer journal de caisse).
	 * 
	 * @param su le message SharedTransaction 
	 */
	public void visit(SharedTransaction st);
	
	/**
	 * Prend en charge message de type SharedProduct (récupérer liste des produits).
	 * 
	 * @param su le message SharedProduct
	 */
	public void visit(SharedProduct sp);

}