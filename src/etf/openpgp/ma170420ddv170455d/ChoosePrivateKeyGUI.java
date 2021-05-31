package etf.openpgp.ma170420ddv170455d;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSecretKeyRing;

public class ChoosePrivateKeyGUI extends JFrame {
	private JPanel contentPanel;

	public ChoosePrivateKeyGUI(KeyGenerator keyGenerator) {
		super("PGP");
		
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(600, 300, 600, 450);
		contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPanel);
		
		ArrayList<ArrayList<String>> lista = new ArrayList<>();
	    
	    // Kako ovde da dohvatim sve privatne kljuceve
	    for (int i = 0; i < 100; i++) {
	    	PGPSecretKeyRing keyRing = keyGenerator.getPrivateRing(i);
	    	if (keyRing == null) break;
	    	java.util.Iterator<PGPSecretKey> iterPrivate = keyRing.getSecretKeys();
			PGPSecretKey key = iterPrivate.next();
			ArrayList<String> row = new ArrayList<String>();
			row.add(String.valueOf(key.getKeyID()));
			row.add(key.getUserIDs().next());
			lista.add(row);
	    }	    
	    
	    String column[] = {"ID", "User ID"};  
	    String[][] data = lista.stream().map(u -> u.toArray(new String[0])).toArray(String[][]::new);
		JTable jt = new JTable(data, column);    
		jt.setBounds(30, 40, 200, 300);          
		JScrollPane sp = new JScrollPane(jt);    
		contentPanel.add(sp);    
		
	}
}
