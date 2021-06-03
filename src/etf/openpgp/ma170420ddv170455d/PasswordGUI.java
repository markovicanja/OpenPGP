package etf.openpgp.ma170420ddv170455d;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class PasswordGUI extends JFrame {
	
	public static int DELETE = 1;
	
	private KeyGenerator keyGenerator;
	
	public PasswordGUI(long id, int action, ShowKeysGUI showKeysGUI, KeyGenerator keyGenerator) {
		super("Lozinka za privatni kljuc");
		
		this.keyGenerator = keyGenerator;
		
		setBounds(550, 300, 400, 150);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		JPanel upper = new JPanel();
		JTextField passwordInput = new JTextField(25);
		upper.add(passwordInput);
		
		JPanel lower = new JPanel();
		JButton buttonOK = new JButton("OK");
		lower.add(buttonOK);
		
		setLayout(new BorderLayout());
		
		add(upper, BorderLayout.NORTH);
		add(lower, BorderLayout.CENTER);
		
		buttonOK.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				if (action == DELETE) {
					boolean deleted;
					try {
						deleted = keyGenerator.deleteKey(id, passwordInput.getText(), true);
						if (!deleted) JOptionPane.showMessageDialog(null, "Nije dobra sifra");
						else {
							setVisible(false);
							showKeysGUI.reset();
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		
	}
	
	public PasswordGUI(MessageReceiver messageReceiver) {	
		super("Lozinka za privatni kljuc");
		
		setBounds(550, 300, 400, 150);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		
		JPanel upper = new JPanel();
		JTextField passwordInput = new JTextField(25);
		upper.add(passwordInput);
		
		JPanel lower = new JPanel();
		JButton buttonOK = new JButton("OK");
		lower.add(buttonOK);
		
		setLayout(new BorderLayout());
		
		add(upper, BorderLayout.NORTH);
		add(lower, BorderLayout.CENTER);
		
		buttonOK.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				messageReceiver.setPassphrase(passwordInput.getText().toCharArray());
				PasswordGUI.this.setVisible(false);
			}
		});
		
	}
	
}
