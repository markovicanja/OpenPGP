package etf.openpgp.ma170420ddv170455d;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.bouncycastle.openpgp.PGPException;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.SwingConstants;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.JButton;

public class MessageReceiverGUI extends JFrame {
	private JPanel contentPanel;
	
	private KeyGenerator keyGenerator;
	
	private JTextField messagePathTextField;
	private JTextField destPathTextField;

	public MessageReceiverGUI(KeyGenerator keyGenerator) {
		super("PGP");
		
		this.keyGenerator = keyGenerator;
		
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(600, 300, 600, 526);
		contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPanel);
		
		BorderLayout borderLayout = new BorderLayout();
		contentPanel.setLayout(borderLayout);
		
		JPanel contentPane = new JPanel();
		contentPanel.add(contentPane, BorderLayout.CENTER);
		
		JLabel lblNewLabel = new JLabel("Primi poruku");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		contentPanel.add(lblNewLabel, BorderLayout.NORTH);

		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JLabel lblNewLabel_1 = new JLabel("Putanja do poruke za dekripciju");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 5;
		gbc_lblNewLabel_1.gridy = 2;
		contentPane.add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		messagePathTextField = new JTextField();
		GridBagConstraints gbc_messagePathTextField = new GridBagConstraints();
		gbc_messagePathTextField.insets = new Insets(0, 0, 5, 5);
		gbc_messagePathTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_messagePathTextField.gridx = 7;
		gbc_messagePathTextField.gridy = 2;
		contentPane.add(messagePathTextField, gbc_messagePathTextField);
		messagePathTextField.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("Destinacija:");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.gridx = 5;
		gbc_lblNewLabel_2.gridy = 3;
		contentPane.add(lblNewLabel_2, gbc_lblNewLabel_2);
		
		destPathTextField = new JTextField();
		GridBagConstraints gbc_destPathTextField = new GridBagConstraints();
		gbc_destPathTextField.insets = new Insets(0, 0, 5, 5);
		gbc_destPathTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_destPathTextField.gridx = 7;
		gbc_destPathTextField.gridy = 3;
		contentPane.add(destPathTextField, gbc_destPathTextField);
		destPathTextField.setColumns(10);
		
		JButton btnPrimi = new JButton("Primi");
		GridBagConstraints gbc_btnPrimi = new GridBagConstraints();
		gbc_btnPrimi.anchor = GridBagConstraints.WEST;
		gbc_btnPrimi.insets = new Insets(0, 0, 5, 5);
		gbc_btnPrimi.gridx = 7;
		gbc_btnPrimi.gridy = 4;
		contentPane.add(btnPrimi, gbc_btnPrimi);				
		
		btnPrimi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	MessageReceiver messageReceiver = new MessageReceiver(keyGenerator);
            	try {
					messageReceiver.receiveMessage(messagePathTextField.getText(), destPathTextField.getText());
				} catch (IOException | PGPException e1) {
					e1.printStackTrace();
				}
            }
        });		
	}

}
