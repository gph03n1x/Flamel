import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JProgressBar;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import java.awt.GridLayout;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;

public class Flamel {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Flamel window = new Flamel();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Flamel() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 512, 416);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenuItem mntmFile = new JMenuItem("File");
		menuBar.add(mntmFile);
		
		JMenuItem mntmOptions = new JMenuItem("Options");
		menuBar.add(mntmOptions);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		menuBar.add(mntmAbout);
		frame.getContentPane().setLayout(null);
		
		Panel controlPanel = new Panel();
		controlPanel.setBounds(0, 0, 150, 350);
		frame.getContentPane().add(controlPanel);
		controlPanel.setLayout(new GridLayout(10, 0, 0, 0));
		
		Panel panel = new Panel();
		controlPanel.add(panel);
		panel.setLayout(new GridLayout(2, 2, 0, 0));
		
		DefaultComboBoxModel<String> startNodesList = new DefaultComboBoxModel<String>();
		DefaultComboBoxModel<String> endNodesList = new DefaultComboBoxModel<String>();
		
		JLabel lblStartingPoint = new JLabel("Starting Node: ");
		panel.add(lblStartingPoint);
		
		
		JComboBox<String> startPoint = new JComboBox<String>(startNodesList);
		panel.add(startPoint);
		
		JLabel lblEndingNode = new JLabel("Ending Node:");
		panel.add(lblEndingNode);
		
		JComboBox<String> endPoint = new JComboBox<String>(endNodesList);
		panel.add(endPoint);
		
		JButton graphreader = new JButton("Open Graph");
		controlPanel.add(graphreader);
		
		JButton btnCreatePath = new JButton("Create Path");
		controlPanel.add(btnCreatePath);
		
		
		
		
		Panel graphicPanel = new Panel();
		graphicPanel.setBounds(152, 0, 350, 350);
		frame.getContentPane().add(graphicPanel);
		
		visualGraph canvasPanel = new visualGraph();
		graphicPanel.add(canvasPanel);
		
		Panel panel_1 = new Panel();
		graphicPanel.add(panel_1);
		
		JProgressBar progressBar = new JProgressBar();
		panel_1.add(progressBar);
		progressBar.setValue(0);
		
		JFileChooser fileDialog = new JFileChooser("graphs/");
		
		graphreader.addActionListener(new ActionListener() {
	    	@Override
	    	public void actionPerformed(ActionEvent e) {
	    		int returnVal = fileDialog.showOpenDialog(frame);
	    		if (returnVal == JFileChooser.APPROVE_OPTION) {
	    			canvasPanel.readGraph(fileDialog.getSelectedFile().getPath());
	    			canvasPanel.constructGraph();
	    			startNodesList.removeAllElements();
	    			endNodesList.removeAllElements();
	    			for (String nodeLabel :canvasPanel.graph.graph.keySet()) {
	    				startNodesList.addElement(nodeLabel);
		    			endNodesList.addElement(nodeLabel);
	    			}
	    			
	            }   
	    	}
	    });
		
		btnCreatePath.addActionListener(new ActionListener() {
	    	@Override
	    	public void actionPerformed(ActionEvent e) {
	    		canvasPanel.readGraph(fileDialog.getSelectedFile().getPath());
	    		canvasPanel.doSearch(startPoint.getSelectedItem().toString(), endPoint.getSelectedItem().toString());
	    		canvasPanel.constructGraph();
	    		
	    		canvasPanel.repaint();
	    	}
	    });
		
	}
}
