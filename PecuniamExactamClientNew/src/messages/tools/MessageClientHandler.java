package messages.tools;

import client.Client;

import messages.SharedEvent;
import messages.SharedObject;
import messages.SharedProduct;
import messages.SharedStand;
import messages.SharedTransaction;
import messages.SharedUser;

public class MessageClientHandler implements MessageVisitor {
	
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
	
	@Override
	public void visit(SharedEvent se) {
		System.out.println("Réception message SharedEvent");
		Client.client.setCurrentEvent(se);
		Client.client.setDataUpToDate(true);
		Client.mainFrame.updateProductList();
	}

	@Override
	public void visit(SharedStand ss) { }

	@Override
	public void visit(SharedUser su) { }

	@Override
	public void visit(SharedTransaction st) {
		System.out.println("Réception message SharedTransaction");
		switch (st.getRequestType()) {
		case 0: // notre demande d'ajout a été acceptée on est content
			System.out.println("Ajout de transaction acceptée");
			break;
			
		case 1: // notre demande de suppression a été acceptée on est content
			System.out.println("Suppression transaction acceptée");
			break;
			
		case 2: // la liste des transactions arrive, on l'ajoute
			Client.client.getCashBook().add(st);
			break;
			
		case 3: // refus de quelque chose, on n'est pas content mais on ne traite pas ici
			System.out.println("Refus d'un ajout/suppression d'une transaction ou liste de transaction vide");
			break;
			
		default: 
			System.out.println("Un message SharedTransaction invalide est arrivé");
			break;
		}
		
	}

	@Override
	public void visit(SharedProduct sp) { }

}
