package etf.openpgp.ma170420ddv170455d;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSecretKeyRing;

public class ChoosePrivateKeyGUI extends JFrame {
	private JPanel contentPanel;

	public ChoosePrivateKeyGUI(KeyGenerator keyGenerator, MessageSender messageSender, MessageSenderGUI messageSenderGUI) {
		super("PGP");
		
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(450, 200, 600, 450);
		contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPanel);
		
		ArrayList<ArrayList<String>> lista = new ArrayList<>();
	    
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
		jt.setBounds(30, 40, 200, 50);          
		JScrollPane sp = new JScrollPane(jt);    
		contentPanel.add(sp); 
		
		JButton button = new JButton("Gotovo");
		contentPanel.add(button, BorderLayout.SOUTH);
		
		button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	int column = 0;
            	int row = jt.getSelectedRow();
            	String keyId = jt.getModel().getValueAt(row, column).toString();
            	messageSender.setPrivateKeyID(Long.parseLong(keyId));
            	messageSenderGUI.setPrivateKeyId(keyId);
            	ChoosePrivateKeyGUI.this.setVisible(false);
            }
        });
            
				
	}
}
