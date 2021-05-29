package etf.openpgp.ma170420ddv170455d;

import java.awt.BorderLayout;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.bouncycastle.crypto.generators.RSAKeyPairGenerator;
import org.bouncycastle.crypto.params.RSAKeyGenerationParameters;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPKeyPair;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPSignatureSubpacketGenerator;
import org.bouncycastle.openpgp.operator.bc.BcPGPKeyPair;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.SwingConstants;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;

import javax.swing.JTextField;
import javax.swing.JRadioButton;
import java.awt.Font;
import javax.swing.JButton;

public class KeyGeneratorGUI extends JFrame {
	private JPanel contentPanel;
	private JTextField nameTextField;
	private JTextField mailTextField;
	private JTextField passwordTextField;
	
	private KeyGenerator keyGenerator;

	public KeyGeneratorGUI(String path) {
		super("PGP");
		
		try {
			keyGenerator = new KeyGenerator(path);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(600, 300, 600, 450);
		contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPanel);
		
		BorderLayout borderLayout = new BorderLayout();
		contentPanel.setLayout(borderLayout);
		
		JPanel contentPane = new JPanel();
		contentPanel.add(contentPane, BorderLayout.CENTER);
		
		JLabel lblNewLabel = new JLabel("Generisanje novog para klju\u010Deva");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		contentPanel.add(lblNewLabel, BorderLayout.NORTH);

		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JLabel nameLabel = new JLabel("Ime");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 5;
		gbc_lblNewLabel_1.gridy = 2;
		contentPane.add(nameLabel, gbc_lblNewLabel_1);
		
		nameTextField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.gridx = 6;
		gbc_textField.gridy = 2;
		contentPane.add(nameTextField, gbc_textField);
		nameTextField.setColumns(5);
		
		JLabel mailLabel = new JLabel("Mejl");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.gridx = 5;
		gbc_lblNewLabel_2.gridy = 3;
		contentPane.add(mailLabel, gbc_lblNewLabel_2);
		
		mailTextField = new JTextField();
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.insets = new Insets(0, 0, 5, 5);
		gbc_textField_1.gridx = 6;
		gbc_textField_1.gridy = 3;
		contentPane.add(mailTextField, gbc_textField_1);
		mailTextField.setColumns(5);
		
		JLabel passwordLabel = new JLabel("Lozinka");
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_3.gridx = 5;
		gbc_lblNewLabel_3.gridy = 4;
		contentPane.add(passwordLabel, gbc_lblNewLabel_3);
		
		passwordTextField = new JTextField();
		passwordTextField.setColumns(5);
		GridBagConstraints gbc_textField_2 = new GridBagConstraints();
		gbc_textField_2.insets = new Insets(0, 0, 5, 5);
		gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_2.gridx = 6;
		gbc_textField_2.gridy = 4;
		contentPane.add(passwordTextField, gbc_textField_2);
		
		JLabel algorithmLabel = new JLabel("Algoritam");
		GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
		gbc_lblNewLabel_4.anchor = GridBagConstraints.ABOVE_BASELINE;
		gbc_lblNewLabel_4.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_4.gridx = 5;
		gbc_lblNewLabel_4.gridy = 5;
		contentPane.add(algorithmLabel, gbc_lblNewLabel_4);

		ButtonGroup group = new ButtonGroup();
		
		JRadioButton rsa1024 = new JRadioButton("RSA 1024");
		GridBagConstraints gbc_rdbtnNewRadioButton = new GridBagConstraints();
		gbc_rdbtnNewRadioButton.anchor = GridBagConstraints.WEST;
		gbc_rdbtnNewRadioButton.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnNewRadioButton.gridx = 6;
		gbc_rdbtnNewRadioButton.gridy = 5;
		contentPane.add(rsa1024, gbc_rdbtnNewRadioButton);
		group.add(rsa1024);
		
		JRadioButton rsa2048 = new JRadioButton("RSA 2048");
		GridBagConstraints gbc_rdbtnNewRadioButton_1 = new GridBagConstraints();
		gbc_rdbtnNewRadioButton_1.anchor = GridBagConstraints.WEST;
		gbc_rdbtnNewRadioButton_1.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnNewRadioButton_1.gridx = 6;
		gbc_rdbtnNewRadioButton_1.gridy = 6;
		contentPane.add(rsa2048, gbc_rdbtnNewRadioButton_1);
		group.add(rsa2048);
		
		JRadioButton rsa4096 = new JRadioButton("RSA 4096");
		GridBagConstraints gbc_rdbtnNewRadioButton_2 = new GridBagConstraints();
		gbc_rdbtnNewRadioButton_2.anchor = GridBagConstraints.WEST;
		gbc_rdbtnNewRadioButton_2.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnNewRadioButton_2.gridx = 6;
		gbc_rdbtnNewRadioButton_2.gridy = 7;
		contentPane.add(rsa4096, gbc_rdbtnNewRadioButton_2);
		group.add(rsa4096);
		
		JRadioButton tripleDes = new JRadioButton("3DES");
		GridBagConstraints gbc_rdbtnNewRadioButton_3 = new GridBagConstraints();
		gbc_rdbtnNewRadioButton_3.anchor = GridBagConstraints.WEST;
		gbc_rdbtnNewRadioButton_3.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnNewRadioButton_3.gridx = 6;
		gbc_rdbtnNewRadioButton_3.gridy = 8;
		contentPane.add(tripleDes, gbc_rdbtnNewRadioButton_3);
		group.add(tripleDes);
		
		JRadioButton cast5 = new JRadioButton("CAST5");
		GridBagConstraints gbc_rdbtnNewRadioButton_4 = new GridBagConstraints();
		gbc_rdbtnNewRadioButton_4.anchor = GridBagConstraints.WEST;
		gbc_rdbtnNewRadioButton_4.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnNewRadioButton_4.gridx = 6;
		gbc_rdbtnNewRadioButton_4.gridy = 9;
		contentPane.add(cast5, gbc_rdbtnNewRadioButton_4);
		group.add(cast5);
		
		JButton button = new JButton("Generi\u0161i");
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.anchor = GridBagConstraints.WEST;
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton.gridx = 6;
		gbc_btnNewButton.gridy = 10;
		contentPane.add(button, gbc_btnNewButton);
		
		button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if (tripleDes.isSelected()) {
            		keyGenerator.generate3DES();
            		return;
            	}
            	if (cast5.isSelected()) {
            		keyGenerator.generateCAST5();
            		return;
            	}
            	int keySize = (rsa1024.isSelected()? 1024 : (rsa2048.isSelected()? 2048 : 4096));
				try {
					keyGenerator.generateKeys(
							nameTextField.getText(), mailTextField.getText(), passwordTextField.getText(), keySize);
				} catch (Exception exception) {
					exception.printStackTrace();
				}
            }
        });
	}

}
