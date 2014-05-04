package ui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;

import utility.CardImagerMapperUtil;

@SuppressWarnings("serial")
public class HomePage extends JFrame implements PropertyChangeListener{
	
	JTextField textFieldHexDir = new JTextField(20);
	JTextField textFieldSaveDir = new JTextField(20);
	Integer[] qualities = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
	JComboBox<Integer> comboQuality = new JComboBox<Integer>(qualities);
	JButton buttonStart;
	private JPanel panelLie;
	
	private JProgressBar progressBar;
	
	private boolean success = true;
	
	public HomePage(){
		initUI();
	}
	
	private void initUI() {
		try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e){

        }
		
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1));
        
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
        comboQuality.setSelectedItem(qualities[7]);
        
        panelQuality.add(labelQuality);
        panelQuality.add(comboQuality);
        
        JPanel panelStart = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelStart.setMaximumSize(new Dimension(650, 0));
        
        buttonStart = new JButton("Start");
       
        buttonStart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonStart.setEnabled(false);
				progressBar.setVisible(true);
		        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		        Task task = new Task();
		        task.addPropertyChangeListener(HomePage.this);
		        task.execute();
				
			}
		});
        panelStart.add(buttonStart);
        
        panelLie = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel labelLie = new JLabel("The progress bar is lying... Still generating cards...");
        panelLie.add(labelLie);
        panelLie.setVisible(false);
        
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setVisible(false);
        
        panel.add(panelHexDir);
        panel.add(panelSaveDir);
        panel.add(panelQuality);
        panel.add(panelStart);
        panel.add(panelLie);
        panel.add(progressBar);
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
	
	class Task extends SwingWorker<Void, Void> {
        @Override
        public Void doInBackground() {
        	Random random = new Random();
            int progress = 0;
            //Initialize progress property.
            setProgress(0);
            Thread thread = new Thread(new CardGeneratorRunnable());
            thread.start();
        	while(thread.isAlive()){
        		//Sleep for up to one second.
        		if(progress < 100 && success){
	                try {
	                    Thread.sleep(random.nextInt(5000));
	                } catch (InterruptedException ignore) {}
	                //Make random progress.
	                progress += random.nextInt(5);
	                setProgress(Math.min(progress, 100));
        		}else if(success){
        			panelLie.setVisible(true);
        		}
        	};
        	if(progress < 100){
        		setProgress(100);
        	}
            return null;
        }

        /*
         * Executed in event dispatching thread
         */
        @Override
        public void done() {
            Toolkit.getDefaultToolkit().beep();
            buttonStart.setEnabled(true);
            progressBar.setVisible(false);
            setCursor(null); //turn off the wait cursor
            if(success){
            	JOptionPane.showMessageDialog(null, "Hex cards generated!");
            }else{
            	success = true;
            }
            panelLie.setVisible(false);
        }
    }

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();
            progressBar.setValue(progress);
        }	
	}
	
	public class CardGeneratorRunnable implements Runnable {

	    public CardGeneratorRunnable() {
	    }

	    public void run() {
	    	try {
				CardImagerMapperUtil.generateCardImages(new File(textFieldHexDir.getText(), "\\Data\\"), 
															new File(textFieldSaveDir.getText()), (int)comboQuality.getSelectedItem());
			} catch (Exception e) {
				success = false;
				JOptionPane.showMessageDialog(null, "Could not find card files. Check path is HEX root directory.");				
			}
	    }
	}
}
