package tests;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.UIManager;
import org.junit.Test;


public class PrintTest {

	@Test
	public void test() {
		UIManager.put("swing.boldMetal", Boolean.FALSE);
        JFrame f = new JFrame("Hello World Printer");
        f.addWindowListener(new WindowAdapter() {
           public void windowClosing(WindowEvent e) {System.exit(0);}
        });
        JButton printButton = new JButton("Print Hello World");
        //printButton.addActionListener(new Ticket());
        f.add("Center", printButton);
        f.pack();
        f.setVisible(true);
        
	}

}
