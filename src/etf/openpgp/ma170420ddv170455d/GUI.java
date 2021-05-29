package etf.openpgp.ma170420ddv170455d;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.bouncycastle.openpgp.PGPException;

public class GUI extends JFrame {
	
	private JFrame frame;
	private KeyGeneratorGUI keyGeneratorGUI;
	private ShowKeysGUI showKeysGUI;

	public GUI(String path) throws IOException, PGPException {
		super("PGP");
		
		keyGeneratorGUI = new KeyGeneratorGUI(path);
		showKeysGUI = new ShowKeysGUI(path);
		
	    frame = this;
	    frame.setVisible(true);
	    initialize();
	}
	
	private void initialize() {
		frame.setBounds(600, 300, 600, 450);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.X_AXIS));
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{587, 0};
		gbl_panel.rowHeights = new int[]{16, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel lblNewLabel = new JLabel("Izaberite opciju:");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel.anchor = GridBagConstraints.NORTH;
		gbc_lblNewLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 1;
		panel.add(lblNewLabel, gbc_lblNewLabel);
		
		JButton keyGeneratorButton = new JButton("Generisanje novog para klju\u010Deva");
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton_1.gridx = 0;
		gbc_btnNewButton_1.gridy = 3;
		panel.add(keyGeneratorButton, gbc_btnNewButton_1);
		
		keyGeneratorButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
            	keyGeneratorGUI.setVisible(true);
            }
        });
		
		JButton btnNewButton_2 = new JButton("Brisanje postoje\u0107eg para klju\u010Deva");
		GridBagConstraints gbc_btnNewButton_2 = new GridBagConstraints();
		gbc_btnNewButton_2.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton_2.gridx = 0;
		gbc_btnNewButton_2.gridy = 4;
		panel.add(btnNewButton_2, gbc_btnNewButton_2);
		
		JButton showKeysButton = new JButton("Prikaz prstena javnih i privatnih klju\u010Deva");
		GridBagConstraints gbc_showKeysButton = new GridBagConstraints();
		gbc_showKeysButton.insets = new Insets(0, 0, 5, 0);
		gbc_showKeysButton.gridx = 0;
		gbc_showKeysButton.gridy = 5;
		panel.add(showKeysButton, gbc_showKeysButton);
		
		showKeysButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
            	showKeysGUI.setVisible(true);
            }
        });
		
		JButton btnNewButton_4 = new JButton("Uvoz i izvoz klju\u010Deva u .asc formatu");
		GridBagConstraints gbc_btnNewButton_4 = new GridBagConstraints();
		gbc_btnNewButton_4.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton_4.gridx = 0;
		gbc_btnNewButton_4.gridy = 6;
		panel.add(btnNewButton_4, gbc_btnNewButton_4);
		
		JButton btnNewButton = new JButton("Slanje poruke");
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton.gridx = 0;
		gbc_btnNewButton.gridy = 7;
		panel.add(btnNewButton, gbc_btnNewButton);
		
		JButton btnNewButton_5 = new JButton("Primanje poruke");
		GridBagConstraints gbc_btnNewButton_5 = new GridBagConstraints();
		gbc_btnNewButton_5.gridx = 0;
		gbc_btnNewButton_5.gridy = 8;
		panel.add(btnNewButton_5, gbc_btnNewButton_5);
	}
}
