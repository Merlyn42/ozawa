package ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import utility.CardImagerMapperUtil;

public class HomePage extends JFrame {
	
	JTextField textFieldHexDir = new JTextField(20);
	JTextField textFieldSaveDir = new JTextField(20);
	String[] qualities = {"10", "20", "30", "40", "50", "60", "70", "80", "90", "100"};
	JComboBox<String> comboQuality = new JComboBox<String>(qualities);
	public HomePage(){
		initUI();
	}
	
	private void initUI() {

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1));
        JPanel panelHexDir = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelHexDir.setMaximumSize(new Dimension(650, 0));
        JLabel labelHexDir = new JLabel("Hex Directory: ");
        JButton selectHexDir = new JButton("Select Hex Directory");
        selectHexDir.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser.setAcceptAllFileFilterUsed(false);
				if (fileChooser.showOpenDialog(HomePage.this) == JFileChooser.APPROVE_OPTION) {
					textFieldHexDir.setText(fileChooser.getSelectedFile().toString());
				}
				
			}
		});        
        
        panelHexDir.add(labelHexDir);
        panelHexDir.add(textFieldHexDir);
        panelHexDir.add(selectHexDir);
        
        JPanel panelSaveDir = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelSaveDir.setMaximumSize(new Dimension(650, 0));
        JLabel labelSaveDir = new JLabel("Save Directory: ");
        JButton selectSaveDir = new JButton("Select Save Directory");
        selectSaveDir.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser.setAcceptAllFileFilterUsed(false);
				if (fileChooser.showOpenDialog(HomePage.this) == JFileChooser.APPROVE_OPTION) {
					textFieldSaveDir.setText(fileChooser.getSelectedFile().toString());
				}
				
			}
		});
        
        
        panelSaveDir.add(labelSaveDir);
        panelSaveDir.add(textFieldSaveDir);
        panelSaveDir.add(selectSaveDir);
        
        JPanel panelQuality = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelQuality.setMaximumSize(new Dimension(650, 0));
        JLabel labelQuality = new JLabel("Image Quality %: ");
        
        
        panelQuality.add(labelQuality);
        panelQuality.add(comboQuality);
        
        JPanel panelStart = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelStart.setMaximumSize(new Dimension(650, 0));
        
        JButton buttonStart = new JButton("Start");
       
        buttonStart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				CardImagerMapperUtil.generateImageAndCardJSONData(new File(textFieldHexDir.getText(), "\\Data\\"), 
																	new File(textFieldSaveDir.getText()), Integer.parseInt((String)comboQuality.getSelectedItem()));
				
			}
		});
        panelStart.add(buttonStart);
        
        panel.add(panelHexDir);
        panel.add(panelSaveDir);
        panel.add(panelQuality);
        panel.add(panelStart);
        add(panel);

        setTitle("Hex TCG - Card Image Generator");
        setSize(new Dimension(650, 300));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
	
	public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	HomePage ex = new HomePage();
                ex.setVisible(true);
            }
        });
    }
}
