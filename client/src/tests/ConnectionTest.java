package tests;


import gui.main.MainFrame;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Calendar;


import org.junit.Test;

import client.Client;

public class ConnectionTest {

	@Test
	public void test() throws IOException {
		
		
		final InetSocketAddress serverAdress = new InetSocketAddress("192.168.2.102", 8027);
		Client.client = new Client(serverAdress);

				Client.mainFrame = new MainFrame();
				Client.mainFrame.setVisible(true);
				
		Client.client.setClientUsername("admintest2"); Client.client.setClientPassword("test");
		Client.client.getClientProtocol().processEventInformationsRetrieving();
		
		System.out.println(Client.client.getCurrentEvent().getName());
		System.out.println(Client.client.getCurrentEvent().getStands()[0].getName());
		System.out.println(Client.client.getCurrentEvent().getStands()[0].getProducts()[0].getName());

		//------------
		Client.client.getClientProtocol().processCash(-65.23);
		Client.client.getClientProtocol().processTransaction(Client.client.getCurrentEvent().getStands()[0].getProducts()[0], true, "TEST");
		//Date date = new Date();
		Calendar cal1 = Calendar.getInstance();
		cal1.set(2012, 2, 23, 3, 59, 28);
		//Date date1 = cal1.getTime();
		Calendar cal2 = Calendar.getInstance();
		cal2.set(2012, 2, 23, 3, 59, 30);
		//Date date2 = cal2.getTime();
		//Client.client.getClientProtocol().retrieveCashBook(date1, date2);
	}

}
