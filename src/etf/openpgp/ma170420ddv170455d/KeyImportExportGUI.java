package etf.openpgp.ma170420ddv170455d;


import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.SwingConstants;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import java.awt.Font;
import javax.swing.ButtonGroup;
import javax.swing.JButton;

public class KeyImportExportGUI extends JFrame {
	private JPanel contentPanel;
	
	private KeyGenerator keyGenerator;
	private JTextField idTextField;
	private JTextField pathTextField;

	public KeyImportExportGUI(String path) {
		super("PGP");
		
		try {
			keyGenerator = new KeyGenerator(path);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(600, 300, 600, 450);
		contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPanel);
		
		BorderLayout borderLayout = new BorderLayout();
		contentPanel.setLayout(borderLayout);
		
		JPanel contentPane = new JPanel();
		contentPanel.add(contentPane, BorderLayout.CENTER);
		
		JLabel lblNewLabel = new JLabel("Uvoz i izvoz klju\u010Deva");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		contentPanel.add(lblNewLabel, BorderLayout.NORTH);

		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		ButtonGroup group = new ButtonGroup();
		
		JLabel lblNewLabel_1 = new JLabel("ID:");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 5;
		gbc_lblNewLabel_1.gridy = 3;
		contentPane.add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		idTextField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 6;
		gbc_textField.gridy = 3;
		contentPane.add(idTextField, gbc_textField);
		idTextField.setColumns(10);
		
		JLabel lblNewLabel_4 = new JLabel("(ID klju\u010Da je potreban samo za izvoz)");
		lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 10));
		GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
		gbc_lblNewLabel_4.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_4.gridx = 6;
		gbc_lblNewLabel_4.gridy = 4;
		contentPane.add(lblNewLabel_4, gbc_lblNewLabel_4);
		
		JLabel lblNewLabel_2 = new JLabel("Putanja");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.gridx = 5;
		gbc_lblNewLabel_2.gridy = 5;
		contentPane.add(lblNewLabel_2, gbc_lblNewLabel_2);
		
		pathTextField = new JTextField();
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.insets = new Insets(0, 0, 5, 5);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 6;
		gbc_textField_1.gridy = 5;
		contentPane.add(pathTextField, gbc_textField_1);
		pathTextField.setColumns(10);
		
		JLabel lblNewLabel_3 = new JLabel("Tip:");
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_3.gridx = 5;
		gbc_lblNewLabel_3.gridy = 6;
		contentPane.add(lblNewLabel_3, gbc_lblNewLabel_3);
		
		JRadioButton publicRB = new JRadioButton("Javni");
		publicRB.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_rdbtnNewRadioButton = new GridBagConstraints();
		gbc_rdbtnNewRadioButton.anchor = GridBagConstraints.WEST;
		gbc_rdbtnNewRadioButton.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnNewRadioButton.gridx = 6;
		gbc_rdbtnNewRadioButton.gridy = 6;
		contentPane.add(publicRB, gbc_rdbtnNewRadioButton);
		group.add(publicRB);
		
		JRadioButton privateRB = new JRadioButton("Privatni");
		privateRB.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_rdbtnNewRadioButton_1 = new GridBagConstraints();
		gbc_rdbtnNewRadioButton_1.anchor = GridBagConstraints.WEST;
		gbc_rdbtnNewRadioButton_1.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnNewRadioButton_1.gridx = 6;
		gbc_rdbtnNewRadioButton_1.gridy = 7;
		contentPane.add(privateRB, gbc_rdbtnNewRadioButton_1);
		group.add(privateRB);
		
		JButton buttonImport = new JButton("Uvoz");
		GridBagConstraints gbc_btnNewButton1 = new GridBagConstraints();
		gbc_btnNewButton1.anchor = GridBagConstraints.WEST;
		gbc_btnNewButton1.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton1.gridx = 6;
		gbc_btnNewButton1.gridy = 8;
		contentPane.add(buttonImport, gbc_btnNewButton1);
		
		JButton buttonExport = new JButton("Izvoz");
		GridBagConstraints gbc_btnNewButton2 = new GridBagConstraints();
		gbc_btnNewButton2.anchor = GridBagConstraints.WEST;
		gbc_btnNewButton2.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton2.gridx = 6;
		gbc_btnNewButton2.gridy = 9;
		contentPane.add(buttonExport, gbc_btnNewButton2);
		
		buttonImport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String path = pathTextField.getText();
				if (privateRB.isSelected()) keyGenerator.importPrivateKey(path);
            	else keyGenerator.importPublicKey(path);
			}
		});
		
		buttonExport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	String path = pathTextField.getText();
            	long id = Long.parseLong(idTextField.getText());
            	if (privateRB.isSelected()) keyGenerator.exportPrivateKey(id, path);
            	else keyGenerator.exportPublicKey(id, path);
            }
        });
	}

}
