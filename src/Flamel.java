import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
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
import javax.swing.JTextPane;
import javax.swing.JTextField;
import java.awt.Color;

public class Flamel {

	private JFrame frame;
	private JTextField textField;

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
		frame.setBounds(100, 100, 1000, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		frame.getContentPane().setLayout(null);
		
		Panel controlPanel = new Panel();
		controlPanel.setBounds(0, 0, 150, 350);
		frame.getContentPane().add(controlPanel);
		controlPanel.setLayout(new GridLayout(10, 0, 0, 0));
		
		DefaultComboBoxModel<String> startNodesList = new DefaultComboBoxModel<String>();
		DefaultComboBoxModel<String> endNodesList = new DefaultComboBoxModel<String>();
		
		JButton graphreader = new JButton("Open Graph");
		
		controlPanel.add(graphreader);
		
		
		
		JLabel lblStartingPoint = new JLabel("Starting Node: ");
		controlPanel.add(lblStartingPoint);
		
		
		JComboBox<String> startPoint = new JComboBox<String>(startNodesList);
		controlPanel.add(startPoint);
		
		JLabel lblEndingNode = new JLabel("Ending Node:");
		controlPanel.add(lblEndingNode);
		
		JComboBox<String> endPoint = new JComboBox<String>(endNodesList);
		controlPanel.add(endPoint);
		
		JLabel lblSearchMethod = new JLabel("Search Method");
		controlPanel.add(lblSearchMethod);
		
		JComboBox searchMethod = new JComboBox();
		controlPanel.add(searchMethod);
		
		JLabel Options = new JLabel("Options");
		controlPanel.add(Options);
		
		textField = new JTextField();
		controlPanel.add(textField);
		textField.setColumns(10);
		
		JButton btnCreatePath = new JButton("Create Path");
		btnCreatePath.setBackground(Color.GREEN);
		controlPanel.add(btnCreatePath);
		
		visualGraph canvasPanel = new visualGraph();
		canvasPanel.setBounds(0, 0, 1, 1);
		frame.getContentPane().add(canvasPanel);
		
		
		JTextPane textPane = new JTextPane();
		textPane.setBounds(763, 0, 211, 454);
		textPane.setEditable(false);
		frame.getContentPane().add(textPane);
		
		canvasPanel.addReferences(frame, controlPanel, textPane);
		
		JFileChooser fileDialog = new JFileChooser("graphs/");
		graphreader.addActionListener(new ActionListener() {
	    	@Override
	    	public void actionPerformed(ActionEvent e) {
	    		int returnVal = fileDialog.showOpenDialog(frame);
	    		if (returnVal == JFileChooser.APPROVE_OPTION) {
	    			canvasPanel.readGraph(fileDialog.getSelectedFile().getPath());
	    			
	    			startNodesList.removeAllElements();
	    			endNodesList.removeAllElements();
	    			for (String nodeLabel :canvasPanel.graph.graph.keySet()) {
	    				startNodesList.addElement(nodeLabel);
		    			endNodesList.addElement(nodeLabel);
	    			}
	    			canvasPanel.constructGraph();
	    			
	            }   
	    	}
	    });
		btnCreatePath.addActionListener(new ActionListener() {
	    	@Override
	    	public void actionPerformed(ActionEvent e) {
	    		canvasPanel.readGraph(fileDialog.getSelectedFile().getPath());
	    		canvasPanel.doSearch(startPoint.getSelectedItem().toString(), endPoint.getSelectedItem().toString());
	    		canvasPanel.constructGraph();
	    		
	    		//canvasPanel.repaint();
	    	}
	    });
		
	}
}
