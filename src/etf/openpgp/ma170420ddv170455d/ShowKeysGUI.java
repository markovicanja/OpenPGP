package etf.openpgp.ma170420ddv170455d;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ListIterator;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSecretKeyRing;

public class ShowKeysGUI extends JFrame {
	
	private KeyGenerator keyGenerator;
	private PGPSecretKeyRing currentPrivateRing;
	private PGPPublicKeyRing currentPublicRing;
	
	private int currentPrivateIndex = -1;
	private int currentPublicIndex = -1;
	
	private JPanel contentPanel;
	private JLabel privateKeyInfo[][];
	private JLabel publicKeyInfo[][];
	
	public ShowKeysGUI(String path) {
		super("PGP");
		
		try {
			keyGenerator = new KeyGenerator(path);
		} catch (Exception e1) {
			e1.printStackTrace();
		}  
		
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(600, 300, 700, 500);
		contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPanel);
		
		contentPanel.setLayout(new GridLayout(2, 1));
		
		JPanel upper = new JPanel();
		upper.setLayout(new BorderLayout());
		upper.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		JLabel labelPrivate = new JLabel("Privatni klju\u010Devi");
		labelPrivate.setHorizontalAlignment(SwingConstants.CENTER);
		labelPrivate.setFont(new Font("Tahoma", Font.BOLD, 13));
		upper.add(labelPrivate, BorderLayout.NORTH);
		
		JPanel keyInfoPrivate = new JPanel();
		keyInfoPrivate.setLayout(new GridLayout(3, 3));
		keyInfoPrivate.setBorder(new EmptyBorder(0, 30, 0, 0));
		
		privateKeyInfo = new JLabel[3][2];
		for (int i = 0; i < 3; i++) {
	    	for (int j = 0; j < 2; j++)
	    	{
	    		privateKeyInfo[i][j] = new JLabel();
	    		privateKeyInfo[i][j].setText("/");
	    		keyInfoPrivate.add(privateKeyInfo[i][j]);
	    		if (j == 1) keyInfoPrivate.add(new JLabel(""));
	    	}
	    }
		privateKeyInfo[0][0].setText("ID: "); 
		privateKeyInfo[1][0].setText("User IDs: "); 
		privateKeyInfo[2][0].setText("Time: ");
		
		upper.add(keyInfoPrivate, BorderLayout.CENTER);
		
		JPanel buttonsPrivate = new JPanel();
		JButton prevPrivate = new JButton("Prethodni");
		JButton deletePrivate = new JButton("Obrisi");
		JButton nextPrivate = new JButton("Sledeci");
		buttonsPrivate.add(prevPrivate);
		buttonsPrivate.add(deletePrivate);
		buttonsPrivate.add(nextPrivate);
		
		upper.add(buttonsPrivate, BorderLayout.SOUTH);
		
		contentPanel.add(upper);
		
		JPanel lower = new JPanel();
		lower.setLayout(new BorderLayout());
		lower.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		JLabel labelPublic = new JLabel("Javni klju\u010Devi");
		labelPublic.setHorizontalAlignment(SwingConstants.CENTER);
		labelPublic.setFont(new Font("Tahoma", Font.BOLD, 13));
		lower.add(labelPublic, BorderLayout.NORTH);
		
		JPanel keyInfoPublic = new JPanel();
		keyInfoPublic.setLayout(new GridLayout(3, 3));
		keyInfoPublic.setBorder(new EmptyBorder(0, 30, 0, 0));
		
		publicKeyInfo = new JLabel[3][2];
		for (int i = 0; i < 3; i++) {
	    	for (int j = 0; j < 2; j++)
	    	{
	    		publicKeyInfo[i][j] = new JLabel();
	    		publicKeyInfo[i][j].setText("/");
	    		keyInfoPublic.add(publicKeyInfo[i][j]);
	    		if (j == 1) keyInfoPublic.add(new JLabel(""));
	    	}
	    }
		publicKeyInfo[0][0].setText("ID: "); 
		publicKeyInfo[1][0].setText("User IDs: "); 
		publicKeyInfo[2][0].setText("Time: ");
		
		lower.add(keyInfoPublic, BorderLayout.CENTER);
		
		JPanel buttonsPublic = new JPanel();
		JButton prevPublic = new JButton("Prethodni");
		JButton deletePublic = new JButton("Obrisi");
		JButton nextPublic = new JButton("Sledeci");
		buttonsPublic.add(prevPublic);
		buttonsPublic.add(deletePublic);
		buttonsPublic.add(nextPublic);
		
		lower.add(buttonsPublic, BorderLayout.SOUTH);
		
		contentPanel.add(lower);
		
		showPrivateKey('+');
		showPublicKey('+');
		
		nextPrivate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showPrivateKey('+');
			}
		});
		
		prevPrivate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showPrivateKey('-');
			}
		});
		
		nextPublic.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showPublicKey('+');
			}
		});
		
		prevPublic.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showPublicKey('-');
			}
		});
	}
	
	public void showPrivateKey(char sign) {
		PGPSecretKeyRing oldRing = currentPrivateRing;
		if (sign == '+') {
			currentPrivateIndex++;
			currentPrivateRing = keyGenerator.getPrivateRing(currentPrivateIndex);
			if (currentPrivateRing == null) {
				currentPrivateRing = oldRing;
				currentPrivateIndex--;
			}
		} else {
			currentPrivateIndex--;
			currentPrivateRing = keyGenerator.getPrivateRing(currentPrivateIndex);
			if (currentPrivateRing == null) {
				currentPrivateRing = oldRing;
				currentPrivateIndex++;
			}
		}
		
		if (currentPrivateRing != null) {
			
			/* nije mi jasno sta je sa prikazom javnih kljuceva
			 * ako idem currentPrivateKey.getPublicKey() ID mu je isti pa ne znam da li
			 * ti kljucevi treba da se prikazuju ili ne
			 * ona devojka cudno nesto radi ovaj prikaz u njenom projektu
			 * meni deluje da samo treba ovde ovi asimetricni tajni, a kod javnih samo oni simetricni
			 * koje izgenerisemo za 3des, moramo ovo da proverimo 
			 */
			
			java.util.Iterator<PGPSecretKey> iterPrivate = currentPrivateRing.getSecretKeys();
			PGPSecretKey currentPrivateKey = iterPrivate.next();
			privateKeyInfo[0][1].setText(String.valueOf(currentPrivateKey.getKeyID()));
			privateKeyInfo[1][1].setText(currentPrivateKey.getUserIDs().next());
			privateKeyInfo[2][1].setText(String.valueOf(currentPrivateKey.getPublicKey().getCreationTime()));
		}
	}
	
	public void showPublicKey(char sign) {
		PGPPublicKeyRing oldRing = currentPublicRing;
		if (sign == '+') {
			currentPublicIndex++;
			currentPublicRing = keyGenerator.getPublicRing(currentPublicIndex);
			if (currentPublicRing == null) {
				currentPublicRing = oldRing;
				currentPublicIndex--;
			}
		} else {
			currentPublicIndex--;
			currentPublicRing = keyGenerator.getPublicRing(currentPublicIndex);
			if (currentPublicRing == null) {
				currentPublicRing = oldRing;
				currentPublicIndex++;
			}
		}
		
		if (currentPublicRing != null) {
			java.util.Iterator<PGPPublicKey> iterPrivate = currentPublicRing.getPublicKeys();
			PGPPublicKey currentPublicKey = iterPrivate.next();
			publicKeyInfo[0][1].setText(String.valueOf(currentPublicKey.getKeyID()));
			publicKeyInfo[1][1].setText(currentPublicKey.getUserIDs().next());
			publicKeyInfo[2][1].setText(String.valueOf(currentPublicKey.getCreationTime()));
		}
	}
	
}
