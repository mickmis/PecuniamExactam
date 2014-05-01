package gui.main;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.Frame;

import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JLabel;

import printing.Ticket;
import messages.SharedProduct;
import messages.SharedStand;
import client.Client;
//TODO : ajout total fric, ajout texte confirmation?, line break?
public class MainFrame extends JFrame {
//TODO mettre parent au joptionpane (un élément du dialog ou autre)
	/**
	 * 
	 */
	private static final long serialVersionUID = 7467131535967100773L;
	
	public static AuthDialog authDialog;
	public static CancelTicketDialog cancelTicketDialog;
	public static CashierDialog cashierDialog;
	//public static MonneyDialog monneyDialog;
	
	private JPanel contentPane;
	private JPanel productsPanel;
	private JPanel ticketInfoPanel;
	private JLabel lblStaff;
	private JPanel buttonsPanel;
	private JLabel confirmationLbl;
	
	/**
	 * Vide la liste des produits.
	 */
	public void clearProductList() {
		Client.client.getCurrentSellingProducts().clear();
		ticketInfoPanel.removeAll();
		ticketInfoPanel.revalidate();
		System.out.println("Liste des produits sélectionnés vidée.");
	}
	
	/**
	 * Constructeur qui initalise la fenêtre uniquement.
	 */
	public MainFrame() {
		//setUndecorated(true);
		setExtendedState(Frame.MAXIMIZED_BOTH);
		setName("Pecuniam Exactam Application");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel productsAndOptionsPanel = new JPanel();
		contentPane.add(productsAndOptionsPanel, BorderLayout.CENTER);
		productsAndOptionsPanel.setLayout(new BorderLayout(0, 0));
		
		
		this.productsPanel = new JPanel();
		productsPanel.setLayout(new GridLayout(3, 4, 5, 5));
		productsAndOptionsPanel.add(productsPanel, BorderLayout.CENTER);

		
		JPanel optionsPanel = new JPanel();
		productsAndOptionsPanel.add(optionsPanel, BorderLayout.SOUTH);
		FlowLayout fl_optionsPanel = new FlowLayout(FlowLayout.CENTER, 0, 0);
		optionsPanel.setLayout(fl_optionsPanel);
		
		JPanel staffButtonPanel = new JPanel();
		staffButtonPanel.setPreferredSize(new Dimension(100, 70));
		
		staffButtonPanel.setBackground(Color.GREEN);
		//optionsPanel.add(staffButtonPanel); removes staff button
		GridBagLayout gbl_staffButtonPanel = new GridBagLayout();
		gbl_staffButtonPanel.columnWidths = new int[]{100, 0};
		gbl_staffButtonPanel.rowHeights = new int[]{70, 0};
		gbl_staffButtonPanel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_staffButtonPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		staffButtonPanel.setLayout(gbl_staffButtonPanel);
		
		lblStaff = new JLabel("Staff - NON");
		GridBagConstraints gbc_lblStaff = new GridBagConstraints();
		gbc_lblStaff.fill = GridBagConstraints.VERTICAL;
		gbc_lblStaff.gridx = 0;
		gbc_lblStaff.gridy = 0;
		staffButtonPanel.add(lblStaff, gbc_lblStaff);
		staffButtonPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				
				// permet de gérer l'état du bouton staff
				if (lblStaff.getText().equals("Staff - NON")) {
					Client.client.setCurrentlySellingToStaff(true);
					lblStaff.setText("Staff - OUI");
					lblStaff.revalidate();
				}
				else {
					Client.client.setCurrentlySellingToStaff(false);
					lblStaff.setText("Staff - NON");
					lblStaff.revalidate();
				}
			}
		});
		
		JPanel authButtonPanel = new JPanel();
		authButtonPanel.setPreferredSize(new Dimension(150, 70));
		authButtonPanel.addMouseListener(new AuthDialogLauncher());
		authButtonPanel.setBackground(Color.RED);
		optionsPanel.add(authButtonPanel);
		GridBagLayout gbl_authButtonPanel = new GridBagLayout();
		gbl_authButtonPanel.columnWidths = new int[]{150, 0};
		gbl_authButtonPanel.rowHeights = new int[]{70, 0};
		gbl_authButtonPanel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_authButtonPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		authButtonPanel.setLayout(gbl_authButtonPanel);
		
		JLabel lblAuthentification = new JLabel("Authentification/MàJ");
		GridBagConstraints gbc_lblAuthentification = new GridBagConstraints();
		gbc_lblAuthentification.fill = GridBagConstraints.VERTICAL;
		gbc_lblAuthentification.gridx = 0;
		gbc_lblAuthentification.gridy = 0;
		authButtonPanel.add(lblAuthentification, gbc_lblAuthentification);
		
		JPanel caisseButtonPanel = new JPanel();
		caisseButtonPanel.setPreferredSize(new Dimension(100, 70));
		caisseButtonPanel.addMouseListener(new CashierDialogLauncher());
		caisseButtonPanel.setBackground(Color.ORANGE);
		optionsPanel.add(caisseButtonPanel);
		GridBagLayout gbl_caisseButtonPanel = new GridBagLayout();
		gbl_caisseButtonPanel.columnWidths = new int[]{100, 0};
		gbl_caisseButtonPanel.rowHeights = new int[]{70, 0};
		gbl_caisseButtonPanel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_caisseButtonPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		caisseButtonPanel.setLayout(gbl_caisseButtonPanel);
		
		JLabel lblCaisse = new JLabel("Caisse");
		GridBagConstraints gbc_lblCaisse = new GridBagConstraints();
		gbc_lblCaisse.fill = GridBagConstraints.VERTICAL;
		gbc_lblCaisse.gridx = 0;
		gbc_lblCaisse.gridy = 0;
		caisseButtonPanel.add(lblCaisse, gbc_lblCaisse);
		
		JPanel cancelTicketButtonPanel = new JPanel();
		cancelTicketButtonPanel.setPreferredSize(new Dimension(150, 70));
		cancelTicketButtonPanel.addMouseListener(new CancelTicketDialogLauncher());
		cancelTicketButtonPanel.setBackground(Color.LIGHT_GRAY);
		optionsPanel.add(cancelTicketButtonPanel);
		GridBagLayout gbl_cancelTicketButtonPanel = new GridBagLayout();
		gbl_cancelTicketButtonPanel.columnWidths = new int[]{150, 0};
		gbl_cancelTicketButtonPanel.rowHeights = new int[]{70, 0};
		gbl_cancelTicketButtonPanel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_cancelTicketButtonPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		cancelTicketButtonPanel.setLayout(gbl_cancelTicketButtonPanel);
		
		JLabel lblAnnulationTicket = new JLabel("Annulation ticket");
		GridBagConstraints gbc_lblAnnulationTicket = new GridBagConstraints();
		gbc_lblAnnulationTicket.fill = GridBagConstraints.VERTICAL;
		gbc_lblAnnulationTicket.gridx = 0;
		gbc_lblAnnulationTicket.gridy = 0;
		cancelTicketButtonPanel.add(lblAnnulationTicket, gbc_lblAnnulationTicket);
		
		JPanel ticketAndButtonsPanel = new JPanel();
		ticketAndButtonsPanel.setPreferredSize(new Dimension(200, 200));
		contentPane.add(ticketAndButtonsPanel, BorderLayout.EAST);
		ticketAndButtonsPanel.setLayout(new BorderLayout(0, 0));
		
		buttonsPanel = new JPanel();
		ticketAndButtonsPanel.add(buttonsPanel, BorderLayout.SOUTH);
		buttonsPanel.setLayout(new BorderLayout(0, 0));
		confirmationLbl = new JLabel();
		buttonsPanel.add(confirmationLbl, BorderLayout.NORTH);
		
		JPanel validationButtonPanel = new JPanel();
		validationButtonPanel.setPreferredSize(new Dimension(200, 70));
		validationButtonPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				doTransaction();
			}
		});
		this.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					doTransaction();
				}
			}
			
			@Override
			public void keyPressed(KeyEvent arg0) {}
		});
		validationButtonPanel.setBackground(Color.GREEN);
		buttonsPanel.add(validationButtonPanel, BorderLayout.CENTER);
		GridBagLayout gbl_validationButtonPanel = new GridBagLayout();
		gbl_validationButtonPanel.columnWidths = new int[]{200, 0};
		gbl_validationButtonPanel.rowHeights = new int[]{70, 0};
		gbl_validationButtonPanel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_validationButtonPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		validationButtonPanel.setLayout(gbl_validationButtonPanel);
		
		JLabel lblValider = new JLabel("Valider (ENTER)");
		GridBagConstraints gbc_lblValider = new GridBagConstraints();
		gbc_lblValider.fill = GridBagConstraints.VERTICAL;
		gbc_lblValider.gridx = 0;
		gbc_lblValider.gridy = 0;
		validationButtonPanel.add(lblValider, gbc_lblValider);
		
		JPanel resetButtonPanel = new JPanel();
		resetButtonPanel.setPreferredSize(new Dimension(200, 50));
		resetButtonPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				clearProductList();
				confirmationLbl.setText("Liste vidée.");
			}
		});
		this.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
					clearProductList();
					confirmationLbl.setText("Liste vidée.");
				}
			}
			
			@Override
			public void keyPressed(KeyEvent arg0) {}
		});
		resetButtonPanel.setBackground(Color.YELLOW);
		buttonsPanel.add(resetButtonPanel, BorderLayout.SOUTH);
		GridBagLayout gbl_resetButtonPanel = new GridBagLayout();
		gbl_resetButtonPanel.columnWidths = new int[]{200, 0};
		gbl_resetButtonPanel.rowHeights = new int[]{50, 0};
		gbl_resetButtonPanel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_resetButtonPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		resetButtonPanel.setLayout(gbl_resetButtonPanel);
		
		JLabel lblVider = new JLabel("Vider (BACKSPACE)");
		GridBagConstraints gbc_lblVider = new GridBagConstraints();
		gbc_lblVider.fill = GridBagConstraints.VERTICAL;
		gbc_lblVider.gridx = 0;
		gbc_lblVider.gridy = 0;
		resetButtonPanel.add(lblVider, gbc_lblVider);
		
		this.ticketInfoPanel = new JPanel();
		ticketAndButtonsPanel.add(ticketInfoPanel, BorderLayout.NORTH);
		ticketInfoPanel.setLayout(new GridLayout(0, 1, 0, 0));
	}

	/**
	 * @return the lblStaff
	 */
	public JLabel getLblStaff() {
		return lblStaff;
	}

	/**
	 * Met à jour la liste des produits dans la MainFrame.
	 */
	public void updateProductList() {
		
		if (Client.client.getCurrentEvent() == null)
			return;
		
		// vider la liste affichée
		this.productsPanel.removeAll();
		
		// array des couleurs pour les différents stands
		Color[] standColors = new Color[]{Color.YELLOW, Color.GREEN, Color.MAGENTA, Color.ORANGE, Color.RED, Color.BLUE, Color.CYAN, Color.WHITE, Color.PINK};
		
		// 5 lignes, 6 colonnes
		int ithItem = 0;
		final char[] keyboardLetter = new char[]{
				'q', 'w', 'e', 'r', 
				'a', 's', 'd', 'f',
				'y', 'x', 'c', 'v',};
		
		for (int i = 0 ; i < Client.client.getCurrentEvent().getStands().length ; i++) { // boucle stands
			for (int j = 0 ; j < Client.client.getCurrentEvent().getStands()[i].getProducts().length ; j++) { // boucle products
				
				final SharedProduct currentProduct = Client.client.getCurrentEvent().getStands()[i].getProducts()[j];
				SharedStand currentStand = Client.client.getCurrentEvent().getStands()[i];
				
				Font bigFont = new Font(getFont().getName(), getFont().getStyle(), getFont().getSize()+15);
				JPanel productButton = new JPanel();
				productButton.setBackground(standColors[ithItem % 9]);
				this.productsPanel.add(productButton);
				productButton.setLayout(new BorderLayout(0, 0));
				
				JLabel lblProductName = new JLabel("<html><p>"+currentProduct.getName() + 
						" ("+currentProduct.getUnit() + ")</p></html>");
				lblProductName.setFont(bigFont);
				productButton.add(lblProductName, BorderLayout.NORTH);
				
				JLabel lblKey = new JLabel(""+keyboardLetter[ithItem]);
				lblKey.setFont(bigFont);
				productButton.add(lblKey, BorderLayout.EAST);
				
				JLabel lblPrice = new JLabel(Double.toString(currentProduct.getPrice()));
				lblPrice.setFont(bigFont);
				productButton.add(lblPrice);
				
				JLabel lblStand = new JLabel(currentStand.getName());
				productButton.add(lblStand, BorderLayout.SOUTH);
				
				final int ithItemBis = ithItem;
				this.addKeyListener(new KeyListener() {
					@Override
					public void keyTyped(KeyEvent e) {	
						if (e.getKeyChar() == keyboardLetter[ithItemBis]) {
							
							// on l'ajoute dans la liste interne
							Client.client.getCurrentSellingProducts().add(currentProduct);
							
							// on l'ajoute à l'affichage
							JLabel lblProductName = new JLabel(currentProduct.getName() + " (" + currentProduct.getUnit() + ") " + currentProduct.getPrice() + " CHF");
							ticketInfoPanel.add(lblProductName);
							ticketInfoPanel.revalidate();
							confirmationLbl.setText("Total="+((double)Client.client.getTotalPrice())/100);
							System.out.println("Produit " + currentProduct.getName() + " ajouté");
						}
					}
					
					@Override
					public void keyReleased(KeyEvent arg0) {}
					
					@Override
					public void keyPressed(KeyEvent arg0) {}});
				
				productButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						// on l'ajoute dans la liste interne
						Client.client.getCurrentSellingProducts().add(currentProduct);
						
						// on l'ajoute à l'affichage
						JLabel lblProductName = new JLabel(currentProduct.getName() + " (" + currentProduct.getUnit() + ") " + currentProduct.getPrice() + " CHF");
						ticketInfoPanel.add(lblProductName);
						ticketInfoPanel.revalidate();
						confirmationLbl.setText("Total="+((double)Client.client.getTotalPrice())/100);
						System.out.println("Produit " + currentProduct.getName() + " ajouté");
					}
				});
				ithItem++;
			}
		}
		for (int u = ithItem ; u < 12 ; u++) {
			JPanel blankProductButton = new JPanel();
			blankProductButton.setBackground(Color.BLACK);
			blankProductButton.setLayout(new BorderLayout(0, 0));
			this.productsPanel.add(blankProductButton);
		}
		
		this.productsPanel.validate();
	}
	
	public void doTransaction() {
		
		// vérif liste produits pas vide
		if (Client.client.getCurrentSellingProducts().size() == 0) {
			JOptionPane.showMessageDialog(Client.mainFrame, "La liste des produits est vide.");
			return;
		}
		
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
						confirmationLbl.setText("Erreur lors de la dernière transaction.");
						return;
					}
			}
		}
	
		/* modif : désactivation printing
		// les transactions ont été faites avec le serveur, on peut imprimer les tickets qui ne sont pas vides
		for (int i = 0 ; i < standsTicket.length ; i++)
			if (!standsTicket[i].isEmpty())
				standsTicket[i].printNow();
		*/
		// on réinitialise l'état currentlySellingToStaff
		Client.client.setCurrentlySellingToStaff(false);
		Client.mainFrame.getLblStaff().setText("Staff - NON");
		Client.mainFrame.getLblStaff().revalidate();
		
		// on vide la liste des produits
		Client.mainFrame.clearProductList();
		
		confirmationLbl.setText("Dernière transaction OK");
		//MainFrame.monneyDialog.dispose();
	}

}

