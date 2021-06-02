package etf.openpgp.ma170420ddv170455d;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyRing;

public class ChoosePublicKeyGUI extends JFrame {
	private JPanel contentPanel;

	public ChoosePublicKeyGUI(KeyGenerator keyGenerator, MessageSender messageSender) {
		super("PGP");
		
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(600, 300, 600, 450);
		contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPanel);
		
		ArrayList<ArrayList<String>> lista = new ArrayList<>();
	    
	    for (int i = 0; i < 100; i++) {
	    	PGPPublicKeyRing keyRing = keyGenerator.getPublicRing(i);
	    	if (keyRing == null) break;
	    	java.util.Iterator<PGPPublicKey> iterPrivate = keyRing.getPublicKeys();
	    	PGPPublicKey key = iterPrivate.next();
			ArrayList<String> row = new ArrayList<String>();
			row.add(String.valueOf(key.getKeyID()));
			row.add(key.getUserIDs().next());
			lista.add(row);
	    }	    
	    
	    String column[] = {"ID", "User ID"};  
	    String[][] data = lista.stream().map(u -> u.toArray(new String[0])).toArray(String[][]::new);
		JTable jt = new JTable(data, column);    
		jt.setBounds(30, 40, 200, 50);          
		JScrollPane sp = new JScrollPane(jt);    
		contentPanel.add(sp); 
		
		JButton button = new JButton("Gotovo");
		contentPanel.add(button, BorderLayout.SOUTH);
		
		button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	int column = 0;
            	int[] rows = jt.getSelectedRows();
            	
            	ArrayList<PGPPublicKey> list = new ArrayList<PGPPublicKey>();            	
            	for (int row: rows) {
            		long keyId = Long.parseLong(jt.getModel().getValueAt(row, column).toString());
            		
            		PGPPublicKeyRing ring = keyGenerator.findPublicRing(keyId);
            		Iterator<PGPPublicKey> iterKey = ring.getPublicKeys();
        			PGPPublicKey key = iterKey.next();
        			key = iterKey.next();
        			list.add(key);
            	}
            	
            	PGPPublicKey[] keys = new PGPPublicKey[list.size()];
            	keys = list.toArray(keys);
            	messageSender.setEncryptionKeys(keys);
            	ChoosePublicKeyGUI.this.setVisible(false);
            }
        });            
				
	}
}
