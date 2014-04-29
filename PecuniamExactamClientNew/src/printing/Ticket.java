package printing;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashAttributeSet;
import javax.print.attribute.standard.PrinterName;

import client.Client;
import messages.SharedProduct;

/**
 * Classe représentant un ticket, et qui va l'imprimer.
 * 
 * @author Mickaël Misbach
 */
public class Ticket implements Printable {
	
	/**
	 * Les produits qui sont vendus et imprimés sur le ticket.
	 */
	private List<SharedProduct> products;
	private String digest;
	
	/**
	 * Constructeur par défaut.
	 */
	public Ticket(String digest) {
		this.digest = digest;
		this.products = new ArrayList<SharedProduct>();
	}
	
	/**
	 * Implémentation de printable, définit ce qui va être imprimée sur le ticket.
	 */
	public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
		if (page > 0) { // une seule page à imprimer
			return NO_SUCH_PAGE;
		}
		
		// on se translate dans la partie imprimable
		Graphics2D g2d = (Graphics2D)g;
		g2d.translate(pf.getImageableX(), pf.getImageableY());
		
		// écriture logo agepoly
		g.drawImage(Client.logoAgepoly, 0, 0, new ImageObserver() { //ne fait rien, on assume que l'image est toujours chargée (car elle l'est dans Client)
			@Override
			public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
				return false;
			}});
		
		// écriture du nom de l'évènement et celui du stand
		g.drawString(Client.client.getCurrentEvent().getName() + " - " + Client.client.getStandNameByProduct(this.products.get(0)), 0, 80);
		
		// on écrit l'en-tête
		g.drawString("Nom produit", 0, 105);
		g.drawString("Unit", 135, 105);
		g.drawString("Prix", 162, 105);
		
		// on écrit les infos sur les produits
		int i;
		for (i = 0 ; i < this.products.size() ; i++) {
			g.drawString(this.products.get(i).getName(), 0, 120 + i*15);
			g.drawString(this.products.get(i).getUnit(), 135, 120 + i*15);
			g.drawString(Double.toString(this.products.get(i).getPrice()), 162, 120 + i*15);
        }
		
		// on écrit le total
		g.drawString("Total : " + Double.toString(this.getTotalPrice()) + " CHF", 0, 140 + i*15);
		
		// on écrit la date
		Calendar now = Calendar.getInstance();
		g.drawString(now.get(Calendar.DAY_OF_MONTH) + "/" + now.get(Calendar.MONTH) + " - " + now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE) + ":" + now.get(Calendar.SECOND), 0, 155 + i*15);
		
		// on écrit le digest
		g.drawString(this.digest, 0, 170 + i*15);

		// on met le staff
		if (Client.client.isCurrentlySellingToStaff())
			g.drawString("STAFF", 0, 185 + i*15);
		
		// on met 6 lignes
		g.drawString(".", 0, 275 + i*15);
		
        // on dit que cette page existe
        return PAGE_EXISTS;
    }
	
    /**
     * Ajoute un produit à la liste à imprimer.
     * 
     * @param prod le produit à ajouter
     */
	public void addProduct(SharedProduct prod) {
		this.products.add(prod);
	}
	
	/**
	 * 
	 * @return si la liste des produits est vide
	 */
	public boolean isEmpty() {
		return this.products.isEmpty();
	}
	
	/**
	 * @return le prix total de la liste des produits actuellement en vente.
	 */
	public double getTotalPrice() {
		double returnValue = 0;
		for (SharedProduct prod : this.products)
			returnValue += prod.getPrice();
		return returnValue;
	}
	
	/**
	 * Lancer l'impression.
	 */
	public void printNow() {
		// sélection de l'imprimante correcte
		PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
		AttributeSet aset = new HashAttributeSet();
		aset.add(new PrinterName("OKIPOS 411-412 (200dpi)", null));
		services = PrintServiceLookup.lookupPrintServices(null, aset);
		if (services.length != 1) {
			System.out.println("Problème de détection de l'imprimante, impression annulée.");
			return;
		}
		PrinterJob job = PrinterJob.getPrinterJob();

		//contournement d'un bug dans l'impression, on définit un PageFormat et un Paper personnalisé
		PageFormat pf = new PageFormat();
		Paper pp = new Paper();
		//pp.setSize(3.1496063/72, 21.496063/72);
		//pp.setImageableArea(0.0787401575, 0.0984251969, 2.99212598,	21.496063);
		pp.setSize(3.1496063/72, 0);
		pp.setImageableArea(0.0787401575, 0, 2.99212598,	0);
		pf.setPaper(new Paper());
		
		//job.printDialog();
		job.setPrintable(this, pf);
     	try {
			job.setPrintService(services[0]);
		} catch (PrinterException e) {
			System.out.println("Impression échouée, stacktrace suit:");
			e.printStackTrace();
			return;
		}
     	
     	// lancement de l'impression
     	try {
     		job.print();
     	} catch (PrinterException e) {
     		System.out.println("Impression échouée, stacktrace suit:");
     		e.printStackTrace();
     		return;
     	}
	}
}