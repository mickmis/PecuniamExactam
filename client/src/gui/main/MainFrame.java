package gui.main;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Frame;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import java.awt.Dimension;
import javax.swing.JLabel;

import messages.SharedProduct;
import messages.SharedStand;
import client.Client;

public class MainFrame extends JFrame {
//TODO mettre parent au joptionpane (un élément du dialog ou autre)
	/**
	 * 
	 */
	private static final long serialVersionUID = 7467131535967100773L;
	
	public static AuthDialog authDialog;
	public static CancelTicketDialog cancelTicketDialog;
	public static CashierDialog cashierDialog;
	public static MonneyDialog monneyDialog;
	
	private JPanel contentPane;
	private JPanel productsPanel;
	private JPanel ticketInfoPanel;
	private JLabel lblStaff;
	
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
		setUndecorated(true);
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
		
		JScrollPane productsScrollPane = new JScrollPane();
		productsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		productsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		productsAndOptionsPanel.add(productsScrollPane, BorderLayout.CENTER);
		
		this.productsPanel = new JPanel();
		productsScrollPane.setViewportView(productsPanel);
		productsPanel.setLayout(new GridLayout(5, 6, 5, 5));
		
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
		
		JPanel buttonsPanel = new JPanel();
		ticketAndButtonsPanel.add(buttonsPanel, BorderLayout.SOUTH);
		buttonsPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel validationButtonPanel = new JPanel();
		validationButtonPanel.setPreferredSize(new Dimension(200, 70));
		validationButtonPanel.addMouseListener(new MonneyDialogLauncher());
		validationButtonPanel.setBackground(Color.GREEN);
		buttonsPanel.add(validationButtonPanel, BorderLayout.NORTH);
		GridBagLayout gbl_validationButtonPanel = new GridBagLayout();
		gbl_validationButtonPanel.columnWidths = new int[]{200, 0};
		gbl_validationButtonPanel.rowHeights = new int[]{70, 0};
		gbl_validationButtonPanel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_validationButtonPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		validationButtonPanel.setLayout(gbl_validationButtonPanel);
		
		JLabel lblValider = new JLabel("Valider");
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
			}
		});
		resetButtonPanel.setBackground(Color.YELLOW);
		buttonsPanel.add(resetButtonPanel, BorderLayout.SOUTH);
		GridBagLayout gbl_resetButtonPanel = new GridBagLayout();
		gbl_resetButtonPanel.columnWidths = new int[]{200, 0};
		gbl_resetButtonPanel.rowHeights = new int[]{50, 0};
		gbl_resetButtonPanel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_resetButtonPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		resetButtonPanel.setLayout(gbl_resetButtonPanel);
		
		JLabel lblVider = new JLabel("Vider");
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
		Color[] standColors = new Color[]{Color.YELLOW, Color.GREEN, Color.MAGENTA, Color.ORANGE, Color.RED, Color.BLUE, Color.CYAN, Color.WHITE};
		
		for (int i = 0 ; i < Client.client.getCurrentEvent().getStands().length ; i++) // boucle stands
			for (int j = 0 ; j < Client.client.getCurrentEvent().getStands()[i].getProducts().length ; j++) { // boucle products
				final SharedProduct currentProduct = Client.client.getCurrentEvent().getStands()[i].getProducts()[j];
				SharedStand currentStand = Client.client.getCurrentEvent().getStands()[i];
				
				JPanel productButton = new JPanel();
				productButton.setBackground(standColors[i % 8]);
				this.productsPanel.add(productButton);
				productButton.setLayout(new BorderLayout(0, 0));
				
				JLabel lblProductName = new JLabel(currentProduct.getName());
				productButton.add(lblProductName, BorderLayout.NORTH);
				
				JLabel lblUnit = new JLabel(currentProduct.getUnit());
				productButton.add(lblUnit, BorderLayout.EAST);
				
				JLabel lblPrice = new JLabel(Double.toString(currentProduct.getPrice()));
				productButton.add(lblPrice);
				
				JLabel lblStand = new JLabel(currentStand.getName());
				productButton.add(lblStand, BorderLayout.SOUTH);
				
				productButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						// on l'ajoute dans la liste interne
						Client.client.getCurrentSellingProducts().add(currentProduct);
						
						// on l'ajoute à l'affichage
						JLabel lblProductName = new JLabel(currentProduct.getName() + " (" + currentProduct.getUnit() + ") " + currentProduct.getPrice() + " CHF");
						ticketInfoPanel.add(lblProductName);
						ticketInfoPanel.revalidate();
						System.out.println("Produit " + currentProduct.getName() + " ajouté");
					}
				});
			}
		
		this.productsPanel.validate();
	}
}
