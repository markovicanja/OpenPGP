package etf.openpgp.ma170420ddv170455d;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.bouncycastle.bcpg.SymmetricKeyAlgorithmTags;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPublicKey;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridBagConstraints;
import javax.swing.SwingConstants;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.NoSuchProviderException;

import javax.swing.JTextField;
import javax.swing.JRadioButton;
import java.awt.Font;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;

public class MessageSenderGUI extends JFrame {
	private JPanel contentPanel;
	
	private JTextField messagePathTextField;
	private JTextField pathTextField;
	private JTextField keyIDField;
	private JTextField passwordField;
	
	private MessageSender messageSender;

	public void setPrivateKeyId(String keyId) {
		keyIDField.setText(keyId);
	}

	public MessageSenderGUI(KeyGenerator keyGenerator) {
		super("PGP");
		
		messageSender = new MessageSender(keyGenerator);
		
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(450, 200, 600, 450);
		contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPanel);
		
		BorderLayout borderLayout = new BorderLayout();
		contentPanel.setLayout(borderLayout);
		
		JPanel contentPane = new JPanel();
		contentPanel.add(contentPane, BorderLayout.CENTER);
		
		JLabel lblNewLabel = new JLabel("Po\u0161alji poruku");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		contentPanel.add(lblNewLabel, BorderLayout.NORTH);

		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		ButtonGroup group = new ButtonGroup();
		
		JLabel lblNewLabel_1 = new JLabel("Putanja poruke za slanje:");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 5;
		gbc_lblNewLabel_1.gridy = 5;
		contentPane.add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		messagePathTextField = new JTextField();
		GridBagConstraints gbc_messagePathTextField = new GridBagConstraints();
		gbc_messagePathTextField.insets = new Insets(0, 0, 5, 5);
		gbc_messagePathTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_messagePathTextField.gridx = 7;
		gbc_messagePathTextField.gridy = 5;
		contentPane.add(messagePathTextField, gbc_messagePathTextField);
		messagePathTextField.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("Putanja na koju se poruka \u0161alje:");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.gridx = 5;
		gbc_lblNewLabel_2.gridy = 6;
		contentPane.add(lblNewLabel_2, gbc_lblNewLabel_2);
		
		pathTextField = new JTextField();
		GridBagConstraints gbc_textField_1_1_1 = new GridBagConstraints();
		gbc_textField_1_1_1.insets = new Insets(0, 0, 5, 5);
		gbc_textField_1_1_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1_1_1.gridx = 7;
		gbc_textField_1_1_1.gridy = 6;
		contentPane.add(pathTextField, gbc_textField_1_1_1);
		pathTextField.setColumns(10);
		
		JCheckBox authCheckBox = new JCheckBox("Autentikacija");
		GridBagConstraints gbc_authCheckBox = new GridBagConstraints();
		gbc_authCheckBox.anchor = GridBagConstraints.WEST;
		gbc_authCheckBox.insets = new Insets(0, 0, 5, 5);
		gbc_authCheckBox.gridx = 5;
		gbc_authCheckBox.gridy = 7;
		contentPane.add(authCheckBox, gbc_authCheckBox);
		
		JButton authKeysButton = new JButton("Izaberi klju\u010D");
		GridBagConstraints gbc_button = new GridBagConstraints();
		gbc_button.anchor = GridBagConstraints.WEST;
		gbc_button.insets = new Insets(0, 0, 5, 5);
		gbc_button.gridx = 7;
		gbc_button.gridy = 7;
		contentPane.add(authKeysButton, gbc_button);
		
		authKeysButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	ChoosePrivateKeyGUI choosePrivateKeyGUI = new ChoosePrivateKeyGUI(keyGenerator, messageSender, MessageSenderGUI.this);
            	choosePrivateKeyGUI.setVisible(true);
            }
        });	
		
		JLabel lblNewLabel_5 = new JLabel("ID izabranog klju\u010Da:");
		GridBagConstraints gbc_lblNewLabel_5 = new GridBagConstraints();
		gbc_lblNewLabel_5.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_5.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_5.gridx = 5;
		gbc_lblNewLabel_5.gridy = 8;
		contentPane.add(lblNewLabel_5, gbc_lblNewLabel_5);
		
		keyIDField = new JTextField();
		GridBagConstraints gbc_keyIDField = new GridBagConstraints();
		gbc_keyIDField.fill = GridBagConstraints.HORIZONTAL;
		gbc_keyIDField.insets = new Insets(0, 0, 5, 5);
		gbc_keyIDField.gridx = 7;
		gbc_keyIDField.gridy = 8;
		contentPane.add(keyIDField, gbc_keyIDField);
		keyIDField.setColumns(10);
		
		JLabel lblNewLabel_4 = new JLabel("Lozinka za privatni klju\u010D:");
		GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
		gbc_lblNewLabel_4.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_4.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_4.gridx = 5;
		gbc_lblNewLabel_4.gridy = 9;
		contentPane.add(lblNewLabel_4, gbc_lblNewLabel_4);
		
		passwordField = new JTextField();
		GridBagConstraints gbc_passwordField = new GridBagConstraints();
		gbc_passwordField.insets = new Insets(0, 0, 5, 5);
		gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordField.gridx = 7;
		gbc_passwordField.gridy = 9;
		contentPane.add(passwordField, gbc_passwordField);
		passwordField.setColumns(10);
		
		JCheckBox encryptCheckBox = new JCheckBox("Tajnost");
		GridBagConstraints gbc_encryptCheckBox = new GridBagConstraints();
		gbc_encryptCheckBox.anchor = GridBagConstraints.WEST;
		gbc_encryptCheckBox.insets = new Insets(0, 0, 5, 5);
		gbc_encryptCheckBox.gridx = 5;
		gbc_encryptCheckBox.gridy = 10;
		contentPane.add(encryptCheckBox, gbc_encryptCheckBox);
		
		JButton encryptKeysButton = new JButton("Izaberi klju\u010Deve");
		GridBagConstraints gbc_encryptKeysButton = new GridBagConstraints();
		gbc_encryptKeysButton.anchor = GridBagConstraints.WEST;
		gbc_encryptKeysButton.insets = new Insets(0, 0, 5, 5);
		gbc_encryptKeysButton.gridx = 7;
		gbc_encryptKeysButton.gridy = 10;
		contentPane.add(encryptKeysButton, gbc_encryptKeysButton);
		
		encryptKeysButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	ChoosePublicKeyGUI choosePublicKeyGUI = new ChoosePublicKeyGUI(keyGenerator, messageSender);
            	choosePublicKeyGUI.setVisible(true);
            }
        });	
		
		JLabel lblNewLabel_3 = new JLabel("Algoritam za enkripciju:");
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_3.gridx = 5;
		gbc_lblNewLabel_3.gridy = 11;
		contentPane.add(lblNewLabel_3, gbc_lblNewLabel_3);
		
		JRadioButton radioButton3DES = new JRadioButton("3DES");
		GridBagConstraints gbc_radioButton3DES = new GridBagConstraints();
		gbc_radioButton3DES.anchor = GridBagConstraints.WEST;
		gbc_radioButton3DES.insets = new Insets(0, 0, 5, 5);
		gbc_radioButton3DES.gridx = 7;
		gbc_radioButton3DES.gridy = 11;
		contentPane.add(radioButton3DES, gbc_radioButton3DES);
		group.add(radioButton3DES);
		
		JRadioButton radioButtonCAST5 = new JRadioButton("CAST5");
		GridBagConstraints gbc_radioButtonCAST5 = new GridBagConstraints();
		gbc_radioButtonCAST5.anchor = GridBagConstraints.WEST;
		gbc_radioButtonCAST5.insets = new Insets(0, 0, 5, 5);
		gbc_radioButtonCAST5.gridx = 7;
		gbc_radioButtonCAST5.gridy = 12;
		contentPane.add(radioButtonCAST5, gbc_radioButtonCAST5);
		group.add(radioButtonCAST5);
		
		JCheckBox zipCheckBox = new JCheckBox("Zip kompresija");
		GridBagConstraints gbc_zipCheckBox = new GridBagConstraints();
		gbc_zipCheckBox.anchor = GridBagConstraints.WEST;
		gbc_zipCheckBox.insets = new Insets(0, 0, 5, 5);
		gbc_zipCheckBox.gridx = 5;
		gbc_zipCheckBox.gridy = 13;
		contentPane.add(zipCheckBox, gbc_zipCheckBox);
		
		JCheckBox radixCheckBox = new JCheckBox("Radix64 konverzija");
		GridBagConstraints gbc_radixCheckBox = new GridBagConstraints();
		gbc_radixCheckBox.anchor = GridBagConstraints.WEST;
		gbc_radixCheckBox.insets = new Insets(0, 0, 5, 5);
		gbc_radixCheckBox.gridx = 5;
		gbc_radixCheckBox.gridy = 14;
		contentPane.add(radixCheckBox, gbc_radixCheckBox);
		
		JButton button = new JButton("Po\u0161alji");
		GridBagConstraints gbc_button1 = new GridBagConstraints();
		gbc_button1.anchor = GridBagConstraints.WEST;
		gbc_button1.insets = new Insets(0, 0, 5, 5);
		gbc_button1.gridx = 7;
		gbc_button1.gridy = 15;
		contentPane.add(button, gbc_button1);						
		
		button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	int tag = -1;
            	if (radioButton3DES.isSelected()) tag = SymmetricKeyAlgorithmTags.TRIPLE_DES;
            	else if (radioButtonCAST5.isSelected()) tag = SymmetricKeyAlgorithmTags.CAST5;
            	
            	if (tag == -1 && encryptCheckBox.isSelected()) {
            		JOptionPane.showMessageDialog(null, "Morate izabrati algotiram za sifrovanje");
            		return;
            	}

            	try {
            		char[] password = passwordField.getText().toCharArray();         			
					messageSender.sendMessage(messagePathTextField.getText(), pathTextField.getText(), 
							authCheckBox.isSelected(), encryptCheckBox.isSelected(), tag,
							zipCheckBox.isSelected(), radixCheckBox.isSelected(), password);
				} catch (PGPException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (NoSuchProviderException e1) {
					e1.printStackTrace();
				}
            }
        });		
	}

}
