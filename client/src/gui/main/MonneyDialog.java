package gui.main;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Color;
import java.io.IOException;

import printing.Ticket;
import messages.SharedProduct;
import messages.SharedStand;
import client.Client;


public class MonneyDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5737643170046618043L;
	private final JPanel computeMonneyPanel = new JPanel();
	private int totalPrice;
	private int currentGivenMonney;
	private JPanel showMonneyPanel;
	private JLabel lblAtDonn;




	/**
	 * Create the dialog.
	 */
	public MonneyDialog() {
		setTitle("Rendu de monnaie");
		this.totalPrice = Client.client.getTotalPrice();
		this.currentGivenMonney = 0;
		
		setResizable(false);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setModal(true);
		setAlwaysOnTop(true);
		setBounds(100, 100, 500, 500);
		getContentPane().setLayout(new BorderLayout(0, 0));
		computeMonneyPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(computeMonneyPanel, BorderLayout.WEST);
		computeMonneyPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		{
			JPanel billPanel = new JPanel();
			computeMonneyPanel.add(billPanel);
			billPanel.setLayout(new BorderLayout(0, 0));
			{
				JLabel lblBillets = new JLabel("Billets");
				billPanel.add(lblBillets, BorderLayout.NORTH);
			}
			{
				JPanel bills = new JPanel();
				billPanel.add(bills, BorderLayout.CENTER);
				bills.setLayout(new GridLayout(0, 1, 0, 0));
				{
					JPanel ten = new JPanel();
					ten.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseReleased(MouseEvent e) {
							currentGivenMonney += 1000;
							lblAtDonn.setText("A été donné : " + currentGivenMonney/100 + "." + currentGivenMonney%100);
							lblAtDonn.revalidate();
						}
					});
					ten.setBackground(Color.ORANGE);
					ten.setPreferredSize(new Dimension(55, 55));
					bills.add(ten);
					{
						JLabel label = new JLabel("10");
						ten.add(label);
					}
				}
				{
					JPanel twenty = new JPanel();
					twenty.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseReleased(MouseEvent e) {
							currentGivenMonney += 2000;
							lblAtDonn.setText("A été donné : " + currentGivenMonney/100 + "." + currentGivenMonney%100);
							lblAtDonn.revalidate();
						}
					});
					twenty.setBackground(Color.GREEN);
					bills.add(twenty);
					{
						JLabel label = new JLabel("20");
						twenty.add(label);
					}
				}
				{
					JPanel fifty = new JPanel();
					fifty.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseReleased(MouseEvent e) {
							currentGivenMonney += 5000;
							lblAtDonn.setText("A été donné : " + currentGivenMonney/100 + "." + currentGivenMonney%100);
							lblAtDonn.revalidate();
						}
					});
					fifty.setBackground(new Color(165, 42, 42));
					bills.add(fifty);
					{
						JLabel label = new JLabel("50");
						fifty.add(label);
					}
				}
				{
					JPanel onehundred = new JPanel();
					onehundred.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseReleased(MouseEvent e) {
							currentGivenMonney += 10000;
							lblAtDonn.setText("A été donné : " + currentGivenMonney/100 + "." + currentGivenMonney%100);
							lblAtDonn.revalidate();
						}
					});
					onehundred.setBackground(new Color(204, 51, 153));
					bills.add(onehundred);
					{
						JLabel label = new JLabel("100");
						onehundred.add(label);
					}
				}
				{
					JPanel twohundreds = new JPanel();
					twohundreds.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseReleased(MouseEvent e) {
							currentGivenMonney += 20000;
							lblAtDonn.setText("A été donné : " + currentGivenMonney/100 + "." + currentGivenMonney%100);
							lblAtDonn.revalidate();
						}
					});
					twohundreds.setBackground(Color.ORANGE);
					bills.add(twohundreds);
					{
						JLabel label = new JLabel("200");
						twohundreds.add(label);
					}
				}
				{
					JPanel fivehundreds = new JPanel();
					fivehundreds.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseReleased(MouseEvent e) {
							currentGivenMonney += 50000;
							lblAtDonn.setText("A été donné : " + currentGivenMonney/100 + "." + currentGivenMonney%100);
							lblAtDonn.revalidate();
						}
					});
					fivehundreds.setBackground(Color.GREEN);
					bills.add(fivehundreds);
					{
						JLabel label = new JLabel("500");
						fivehundreds.add(label);
					}
				}
				{
					JPanel onethousand = new JPanel();
					onethousand.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseReleased(MouseEvent e) {
							currentGivenMonney += 100000;
							lblAtDonn.setText("A été donné : " + currentGivenMonney/100 + "." + currentGivenMonney%100);
							lblAtDonn.revalidate();
						}
					});
					onethousand.setBackground(new Color(255, 51, 102));
					bills.add(onethousand);
					{
						JLabel label = new JLabel("1000");
						onethousand.add(label);
					}
				}
			}
		}
		{
			JPanel coinPanel = new JPanel();
			computeMonneyPanel.add(coinPanel);
			coinPanel.setLayout(new BorderLayout(0, 0));
			{
				JLabel lblPices = new JLabel("Pièces");
				coinPanel.add(lblPices, BorderLayout.NORTH);
			}
			{
				JPanel coins = new JPanel();
				coinPanel.add(coins, BorderLayout.CENTER);
				coins.setLayout(new GridLayout(0, 1, 0, 0));
				{
					JPanel five = new JPanel();
					five.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseReleased(MouseEvent e) {
							currentGivenMonney += 500;
							lblAtDonn.setText("A été donné : " + currentGivenMonney/100 + "." + currentGivenMonney%100);
							lblAtDonn.revalidate();
						}
					});
					five.setBackground(Color.CYAN);
					five.setPreferredSize(new Dimension(55, 55));
					coins.add(five);
					{
						JLabel label = new JLabel("5");
						five.add(label);
					}
				}
				{
					JPanel two = new JPanel();
					two.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseReleased(MouseEvent e) {
							currentGivenMonney += 200;
							lblAtDonn.setText("A été donné : " + currentGivenMonney/100 + "." + currentGivenMonney%100);
							lblAtDonn.revalidate();
						}
					});
					two.setBackground(Color.LIGHT_GRAY);
					coins.add(two);
					{
						JLabel label = new JLabel("2");
						two.add(label);
					}
				}
				{
					JPanel one = new JPanel();
					one.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseReleased(MouseEvent e) {
							currentGivenMonney += 100;
							lblAtDonn.setText("A été donné : " + currentGivenMonney/100 + "." + currentGivenMonney%100);
							lblAtDonn.revalidate();
						}
					});
					one.setBackground(new Color(30, 144, 255));
					coins.add(one);
					{
						JLabel label = new JLabel("1");
						one.add(label);
					}
				}
				{
					JPanel fiftycent = new JPanel();
					fiftycent.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseReleased(MouseEvent e) {
							currentGivenMonney += 50;
							lblAtDonn.setText("A été donné : " + currentGivenMonney/100 + "." + currentGivenMonney%100);
							lblAtDonn.revalidate();
						}
					});
					fiftycent.setBackground(new Color(34, 139, 34));
					coins.add(fiftycent);
					{
						JLabel label = new JLabel("0.50");
						fiftycent.add(label);
					}
				}
				{
					JPanel twentycent = new JPanel();
					twentycent.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseReleased(MouseEvent e) {
							currentGivenMonney += 20;
							lblAtDonn.setText("A été donné : " + currentGivenMonney/100 + "." + currentGivenMonney%100);
							lblAtDonn.revalidate();
						}
					});
					twentycent.setBackground(Color.CYAN);
					coins.add(twentycent);
					{
						JLabel label = new JLabel("0.20");
						twentycent.add(label);
					}
				}
				{
					JPanel tencent = new JPanel();
					tencent.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseReleased(MouseEvent e) {
							currentGivenMonney += 10;
							lblAtDonn.setText("A été donné : " + currentGivenMonney/100 + "." + currentGivenMonney%100);
							lblAtDonn.revalidate();
						}
					});
					tencent.setBackground(Color.LIGHT_GRAY);
					coins.add(tencent);
					{
						JLabel label = new JLabel("0.10");
						tencent.add(label);
					}
				}
				{
					JPanel fivecent = new JPanel();
					fivecent.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseReleased(MouseEvent e) {
							currentGivenMonney += 5;
							lblAtDonn.setText("A été donné : " + currentGivenMonney/100 + "." + currentGivenMonney%100);
							lblAtDonn.revalidate();
						}
					});
					fivecent.setBackground(new Color(102, 153, 51));
					coins.add(fivecent);
					{
						JLabel label = new JLabel("0.05");
						fivecent.add(label);
					}
				}
			}
		}
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btnRinitialiser = new JButton("Vider");
				btnRinitialiser.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent arg0) {
						currentGivenMonney = 0;
						lblAtDonn.setText("A été donné : 0.0");
						lblAtDonn.revalidate();
						resultsPanel.removeAll();
						resultsPanel.revalidate();
					}
				});
				buttonPane.setLayout(new GridLayout(0, 4, 5, 0));
				btnRinitialiser.setActionCommand("Cancel");
				buttonPane.add(btnRinitialiser);
			}
			{
				JButton okButton = new JButton("Valider");
				okButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						doTransaction();
					}
				});
				{
					JButton btnCalculMonnaie = new JButton("Calcul ");
					btnCalculMonnaie.setActionCommand("Calcul");
					btnCalculMonnaie.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseReleased(MouseEvent e) {
							
							resultsPanel.removeAll();
							resultsPanel.revalidate();
							
							// si le client n'a pas donné assez on retourne tout de suite
							if (totalPrice >= currentGivenMonney)
								return;
							
							int[] monney = giveBack(totalPrice, currentGivenMonney);
							
							for (int i = 0 ; i < 12 ; i++) {
								if (monney[i] != 0) {
									JLabel lblMonneyToGive = new JLabel((int)(moneyUnits[i]/100) + "." + (int)(moneyUnits[i]%100) + "CHF x " + monney[i]);
									resultsPanel.add(lblMonneyToGive, BorderLayout.CENTER);
									resultsPanel.revalidate();
								}
							}
						}
					});
					buttonPane.add(btnCalculMonnaie);
				}
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Annuler");
				cancelButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						MainFrame.monneyDialog.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		{
			showMonneyPanel = new JPanel();
			getContentPane().add(showMonneyPanel);
			showMonneyPanel.setLayout(new BorderLayout(0, 0));
			{
				JLabel lblTotalRgler = new JLabel("Total à régler : " + totalPrice/100 + "." + totalPrice%100);
				showMonneyPanel.add(lblTotalRgler, BorderLayout.NORTH);
			}
			{
				lblAtDonn = new JLabel("A été donné : 0.0");
				showMonneyPanel.add(lblAtDonn, BorderLayout.SOUTH);
			}
			{
				resultsPanel = new JPanel();
				showMonneyPanel.add(resultsPanel, BorderLayout.CENTER);
				resultsPanel.setLayout(new GridLayout(0, 1, 5, 0));
			}
		}
	}
	

	public static double[] moneyUnits = {20000, 10000, 5000, 2000, 1000, 500, 200, 100, 50, 20, 10, 5};
	private JPanel resultsPanel;
	
	public static int[] giveBack(double price, double paid) {
		int[] giveback = new int[moneyUnits.length];
		double amount = paid-price;
		
		for(int i = 0; i<moneyUnits.length; i++) {
			giveback[i]	= 	getGiveBackFor(amount, moneyUnits[i]);
			amount		=	getRest(amount, moneyUnits[i]);
		}
		
		return giveback;
	}
	
	public static int getGiveBackFor(double amount, double unit) {
		return (int)(amount/unit);
	}
	
	public static double getRest(double amount, double unit) {
		return amount%unit;
	}
	

	/**
	 * Une fois la monnaie rendue on fait la transaction
	 */
	public void doTransaction() {
		// on procède stand par stand
		SharedStand[] stands = Client.client.getCurrentEvent().getStands();
		String[] standsDigest = new String[stands.length];
		Ticket[] standsTicket = new Ticket[stands.length];
		
		for (int i = 0 ; i < stands.length ; i++) {
			standsDigest[i] = Client.client.getClientProtocol().generateRandomDigestHeader();
			standsTicket[i] = new Ticket(standsDigest[i]);
								
			// on itère dans les produits vendus actuellement pour trouver ceux appartenant au stand en question
			for (SharedProduct prod : Client.client.getCurrentSellingProducts()) {
				// si le nom du stand actuel == celui du stand auquel appartient le produit actuel
				if (stands[i].getName().equals(Client.client.getStandNameByProduct(prod)))
					try {
						if (Client.client.getClientProtocol().processTransaction(prod, Client.client.isCurrentlySellingToStaff(), standsDigest[i])) 
							// si la transaction est retournée ok
							standsTicket[i].addProduct(prod);
					} catch (IOException ex) {
						System.out.println("Erreur IO durant envoi des transactions.");
						ex.printStackTrace();
						JOptionPane.showMessageDialog(null, "Erreur IO durant l'envoi des transactions. Rien ne va être imprimé, veuillez SVP contrôler la liste des transactions passées et supprimer les dernières.");
						return;
					}
			}
		}
	
		
		// les transactions ont été faites avec le serveur, on peut imprimer les tickets qui ne sont pas vides
		for (int i = 0 ; i < standsTicket.length ; i++)
			if (!standsTicket[i].isEmpty())
				standsTicket[i].printNow();
		
		// on réinitialise l'état currentlySellingToStaff
		Client.client.setCurrentlySellingToStaff(false);
		Client.mainFrame.getLblStaff().setText("Staff - NON");
		Client.mainFrame.getLblStaff().revalidate();
		
		// on vide la liste des produits
		Client.mainFrame.clearProductList();
		
		MainFrame.monneyDialog.dispose();
	}

}

/**
 * Listener pour lancer le CancelTicketDialog.
 */
class MonneyDialogLauncher extends MouseAdapter {
	
	@Override
	public void mouseReleased(MouseEvent arg0) {
		
		// vérif liste produits pas vide
		if (Client.client.getCurrentSellingProducts().size() == 0) {
			JOptionPane.showMessageDialog(Client.mainFrame, "La liste des produits est vide.");
			return;
		}
		
		// création du monneydialog
		MainFrame.monneyDialog = new MonneyDialog();
		MainFrame.monneyDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		MainFrame.monneyDialog.setLocationRelativeTo(null);
		MainFrame.monneyDialog.setVisible(true);
	}
}