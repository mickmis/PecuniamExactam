package gui.main;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.GridLayout;
import javax.swing.JPasswordField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import client.Client;

public class AuthDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2533205719830586409L;
	private final JPanel infoPanel = new JPanel();
	private JTextField txtUsername;
	private JPasswordField passwordField;

	
	

	/**
	 * Constructeur qui initalise la fenêtre uniquement.
	 */
	public AuthDialog() {
		setResizable(false);
		setUndecorated(false);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setTitle("Authentification");
		setModal(true);
		setBounds(100, 100, 400, 175);
		getContentPane().setLayout(new BorderLayout());
		infoPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(infoPanel, BorderLayout.CENTER);
		infoPanel.setLayout(new GridLayout(0, 1, 0, 0));
		{
			JPanel userPanel = new JPanel();
			infoPanel.add(userPanel);
			userPanel.setLayout(new BorderLayout(0, 0));
			{
				JLabel lblNomDutilisateur = new JLabel("Nom d'utilisateur");
				userPanel.add(lblNomDutilisateur, BorderLayout.WEST);
			}
			{
				txtUsername = new JTextField();
				userPanel.add(txtUsername);
				txtUsername.setColumns(10);
			}
		}
		{
			JPanel passwordPanel = new JPanel();
			infoPanel.add(passwordPanel);
			passwordPanel.setLayout(new BorderLayout(0, 0));
			{
				JLabel lblMotDePasse = new JLabel("Mot de passe");
				passwordPanel.add(lblMotDePasse, BorderLayout.WEST);
			}
			{
				passwordField = new JPasswordField();
				passwordPanel.add(passwordField);
			}
		}
		{
			JPanel buttonPanel = new JPanel();
			buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPanel, BorderLayout.SOUTH);
			{
				final JButton okButton = new JButton("OK");
				okButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						try {
							if (txtUsername.getText().isEmpty() || new String(passwordField.getPassword()).isEmpty())
								return; // on fait rien si un champ est pas rempli
							
							Client.client.setClientUsername(txtUsername.getText());
							Client.client.setClientPassword(new String(passwordField.getPassword()));
							if (!Client.client.getClientProtocol().processEventInformationsRetrieving())
								throw new IOException("Authentification rejetée");
							MainFrame.authDialog.dispose();
							Client.mainFrame.clearProductList();
						} catch (IOException ex) {
							ex.printStackTrace();
							JOptionPane.showMessageDialog(null, "Mot de passe invalide ou erreur IO, réessayez.");
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPanel.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				final JButton cancelButton = new JButton("Annuler");
				cancelButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						MainFrame.authDialog.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPanel.add(cancelButton);
			}
		}
	}

}

/**
 * Listener pour lancer l'AuthDialog.
 */
class AuthDialogLauncher extends MouseAdapter {

	@Override
	public void mouseReleased(MouseEvent arg0) {
		MainFrame.authDialog = new AuthDialog();
		MainFrame.authDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		MainFrame.authDialog.setLocationRelativeTo(null);
		MainFrame.authDialog.setVisible(true);
	}
}
