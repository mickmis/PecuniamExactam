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
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JList;

import client.Client;
import messages.SharedTransaction;

@SuppressWarnings({"rawtypes", "unchecked"})
public class CancelTicketDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -697392201209536517L;
	private final JPanel ticketListPanel = new JPanel();
	private JList ticketList;
	
	/**
	 * Create the dialog.
	 */
	public CancelTicketDialog() {
		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);
		setAlwaysOnTop(true);
		setTitle("Annuler un ticket");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		ticketListPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(ticketListPanel, BorderLayout.CENTER);
		ticketListPanel.setLayout(new BorderLayout(0, 0));
		{
			JLabel lblAttentionSupprimer = new JLabel("/!\\ supprime un TICKET /!\\");
			ticketListPanel.add(lblAttentionSupprimer, BorderLayout.NORTH);
		}
		{				
			try {
				this.ticketList = new JList(this.getTicketList());
			} catch (IOException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(ticketListPanel, "Mot de passe invalide ou erreur IO, réessayez.");
				this.ticketList = new JList();
			}
			ticketListPanel.add(this.ticketList);
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
						int[] selectedValues = ticketList.getSelectedIndices();
						
						// on ne fait rien si vide
						if (selectedValues.length == 0)
							return;
						
						try {
							for (int i = 0 ; i < selectedValues.length ; i++) {
								if (!Client.client.getClientProtocol().deleteTransaction(Client.client.getCashBook().get(selectedValues[i]).getDigestHeader(), Client.client.getCashBook().get(selectedValues[i]).getSellingTime()))
									JOptionPane.showMessageDialog(ticketListPanel, "Une transaction n'a pas été correctement supprimée (" + Client.client.getCashBook().get(selectedValues[i]).getDigestHeader() + ").");
							}
						} catch (IOException ex) {
							ex.printStackTrace();
							JOptionPane.showMessageDialog(ticketListPanel, "Erreur lors de la suppression de transactions (mot de passe invalide ou erreur IO). Vérifiez celles qui ont été supprimées ou non.");
							MainFrame.cancelTicketDialog.dispose();
							return;
						}
						JOptionPane.showMessageDialog(ticketListPanel, "Transaction(s) effacées avec succès.");
						MainFrame.cancelTicketDialog.dispose();
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
						MainFrame.cancelTicketDialog.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	private String[] getTicketList() throws IOException {
		// récupère les transactions de l'utilisateur de la journée et les retourne
		Calendar todayMidnight = Calendar.getInstance();
		todayMidnight.set(Calendar.HOUR_OF_DAY, 0);
		todayMidnight.set(Calendar.MINUTE, 0);
		todayMidnight.set(Calendar.SECOND, 0);
		todayMidnight.set(Calendar.MILLISECOND, 0);
		
		Client.client.getClientProtocol().retrieveCashBook(todayMidnight.getTime(), new Date());
		String[] returnArray = new String[Client.client.getCashBook().size()];
		
		for (int i = 0 ; i < Client.client.getCashBook().size() ; i++) {
			SharedTransaction currentTransaction = Client.client.getCashBook().get(i);
			returnArray[i] = currentTransaction.getSellingTime().toString() + " / " + currentTransaction.getProduct().getName() 
					+ " (" + currentTransaction.getProduct().getPrice() + "CHF) - staff:" +  currentTransaction.isSellingToStaff() + " - " + currentTransaction.getDigestHeader();
		}
		return returnArray;
	}

}

/**
 * Listener pour lancer le CancelTicketDialog.
 */
class CancelTicketDialogLauncher extends MouseAdapter {
	
	@Override
	public void mouseReleased(MouseEvent arg0) {
		if (Client.client.getClientUsername().isEmpty() || Client.client.getClientPassword().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Pas authentifié");
			return;
		}
		MainFrame.cancelTicketDialog = new CancelTicketDialog();
		MainFrame.cancelTicketDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		MainFrame.cancelTicketDialog.setLocationRelativeTo(null);
		MainFrame.cancelTicketDialog.setVisible(true);
	}
}
