package gui.main;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import client.Client;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.io.IOException;

public class CashierDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5177555624189653773L;
	private final JPanel contentPanel = new JPanel();
	private JTextField fondDeCaisseTextField;
	private JTextField liquiditesTextField;
	private JTextField ajoutFondDeCaisseTextField;
	private JTextField prelevementTextField;

	/**
	 * Create the dialog.
	 * @throws IOException si erreur IO lors récupération du total de liquidités
	 */
	public CashierDialog() {
		setResizable(false);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setModal(true);
		setTitle("Gestion caisse");
		setAlwaysOnTop(true);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(4, 2, 10, 10));
		{
			JLabel lblFondDeCaisse = new JLabel("Fond de caisse actuel");
			contentPanel.add(lblFondDeCaisse);
		}
		{
			fondDeCaisseTextField = new JTextField(Double.toString(Client.client.getCash()));
			fondDeCaisseTextField.setEditable(false);
			contentPanel.add(fondDeCaisseTextField);
			fondDeCaisseTextField.setColumns(10);
		}
		{
			JLabel lblTotalLiquidits = new JLabel("Total liquidités");
			contentPanel.add(lblTotalLiquidits);
		}
		{
			try {
				liquiditesTextField = new JTextField(Double.toString(Client.client.getLiquidites()));
			} catch (IOException e) {
				System.out.println("Erreur IO lors tentative récupération cash, stacktrace suit :");
				e.printStackTrace();
				liquiditesTextField = new JTextField("Erreur IO ou pas authentifié");
				JOptionPane.showMessageDialog(null, "Mot de passe invalide ou erreur IO, réessayez.");
			}
			liquiditesTextField.setEditable(false);
			contentPanel.add(liquiditesTextField);
			liquiditesTextField.setColumns(10);
		}
		{
			JLabel lblAjouterFondDe = new JLabel("Ajouter fond de caisse");
			contentPanel.add(lblAjouterFondDe);
		}
		{
			ajoutFondDeCaisseTextField = new JTextField();
			contentPanel.add(ajoutFondDeCaisseTextField);
			ajoutFondDeCaisseTextField.setColumns(10);
		}
		{
			JLabel lblFaireUnPrlvement = new JLabel("Faire un prélèvement");
			contentPanel.add(lblFaireUnPrlvement);
		}
		{
			prelevementTextField = new JTextField();
			contentPanel.add(prelevementTextField);
			prelevementTextField.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						try {
							if (!ajoutFondDeCaisseTextField.getText().isEmpty()) {
								double ajoutFondDeCaisse = Double.parseDouble(ajoutFondDeCaisseTextField.getText());
								System.out.println("Ajout de fond de caisse : " + ajoutFondDeCaisse);
								if (ajoutFondDeCaisse >= 0)
									if (!Client.client.getClientProtocol().processCash(ajoutFondDeCaisse))
										JOptionPane.showMessageDialog(null, "Ajout de fond de caisse échoué, réessayez.");
							}
							
							if (!prelevementTextField.getText().isEmpty()) {
								double prelevement = Double.parseDouble(prelevementTextField.getText());
								System.out.println("Prélèvement :" + prelevement);
								if (prelevement >= 0)
									if (!Client.client.getClientProtocol().processCash(-prelevement))
										JOptionPane.showMessageDialog(null, "Prélèvement échoué, réessayez.");
							}
						} catch (NumberFormatException ex) {
							
						} catch (IOException ex) {
							ex.printStackTrace();
							JOptionPane.showMessageDialog(null, "Mot de passe invalide ou erreur IO, réessayez.");
						}
						MainFrame.cashierDialog.dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Annuler");
				cancelButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						MainFrame.cashierDialog.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}

/**
 * Listener pour lancer le CashierDialog.
 */
class CashierDialogLauncher extends MouseAdapter {

	@Override
	public void mouseReleased(MouseEvent arg0) {
		if (Client.client.getClientUsername().isEmpty() || Client.client.getClientPassword().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Pas authentifié");
			return;
		}
		MainFrame.cashierDialog = new CashierDialog();
		MainFrame.cashierDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		MainFrame.cashierDialog.setLocationRelativeTo(null);
		MainFrame.cashierDialog.setVisible(true);
	}
}
