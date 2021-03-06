import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.JLabel;
import java.awt.GridLayout;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.Color;
import javax.swing.JMenu;
import javax.swing.JTabbedPane;


public class Flamel {

	private JFrame frame;
	private JTextField textField;
	public static HashMap<String, visualGraph> tabbedGraphs = new HashMap<String, visualGraph>();

	/*
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

	/*
	 * Create the application.
	 */
	public Flamel() {
		initialize();
	}

	/*
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		// TODO: make it go windowed fullscreen.
		frame = new JFrame();
		frame.setBounds(100, 100, 1000, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		frame.setVisible(true);
		configParser cfg = new configParser();
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnOptions = new JMenu("Options");
		menuBar.add(mnOptions);
		frame.getContentPane().setLayout(null);
		
		Panel controlPanel = new Panel();
		controlPanel.setBounds(0, 0, 150, 350);
		frame.getContentPane().add(controlPanel);
		controlPanel.setLayout(new GridLayout(13, 0, 0, 0));
		
		DefaultComboBoxModel<String> startNodesList = new DefaultComboBoxModel<String>();
		DefaultComboBoxModel<String> endNodesList = new DefaultComboBoxModel<String>();
		DefaultComboBoxModel<String> searchAlgList = new DefaultComboBoxModel<String>();
		DefaultComboBoxModel<String> visualAlgList = new DefaultComboBoxModel<String>();
		
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
		
		JLabel lblVisualMethod = new JLabel("Visual Method");
		controlPanel.add(lblVisualMethod);
		
		JComboBox<String> visualMethod = new JComboBox<String>(visualAlgList);
		controlPanel.add(visualMethod);
		visualAlgList.addElement("Branching");
		visualAlgList.addElement("Orbiting");
		//visualMethod.setSelectedIndex(1);
		
		JLabel lblSearchMethod = new JLabel("Search Method");
		controlPanel.add(lblSearchMethod);
		
		JComboBox<String> searchMethod = new JComboBox<String>(searchAlgList);
		controlPanel.add(searchMethod);
		
		for (String part: cfg.get("algorithms").split(",")){
			searchAlgList.addElement(part);
		}
		
		JLabel Options = new JLabel("Options");
		controlPanel.add(Options);
		
		textField = new JTextField();
		controlPanel.add(textField);
		textField.setColumns(10);
		
		JButton btnCreatePath = new JButton("Create Path");
		btnCreatePath.setBackground(Color.GREEN);
		controlPanel.add(btnCreatePath);
		
		JButton btnRegenerate = new JButton("Regenerate");
		btnRegenerate.setBackground(Color.magenta);
		controlPanel.add(btnRegenerate);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(156, 0, 224, 629);
		frame.getContentPane().add(tabbedPane);


		JFileChooser fileDialog = new JFileChooser("graphs/");
		// TODO: switching tab should change startNodesList and endNodesList
		
		graphreader.addActionListener(new ActionListener() {
	    	@Override
	    	public void actionPerformed(ActionEvent e) {
	    		
	    		int returnVal = fileDialog.showOpenDialog(frame);
	    		if (returnVal == JFileChooser.APPROVE_OPTION) {
	    			visualGraph newCanvasPanel;
	    			if (tabbedGraphs.containsKey(fileDialog.getSelectedFile().getName())) {
	    				newCanvasPanel = tabbedGraphs.get(fileDialog.getSelectedFile().getName());
	    			} else {
	    				newCanvasPanel = new visualGraph(tabbedPane);
		    			tabbedPane.addTab(fileDialog.getSelectedFile().getName(), null, newCanvasPanel, null);
			    		tabbedGraphs.put(fileDialog.getSelectedFile().getName(), newCanvasPanel);
	    			}
	    			if (visualMethod.getSelectedIndex() == 0) {
	    				newCanvasPanel.useBranching();
	    			} else {
	    				newCanvasPanel.useOrbiting();
	    			}
	    			
	    			newCanvasPanel.readGraph(fileDialog.getSelectedFile().getPath());
		    		startNodesList.removeAllElements();
	    			endNodesList.removeAllElements();
	    			graphParser graph = newCanvasPanel.graphPlacement.graph;
	    			for (String nodeLabel : graph.graph.keySet()) {
	    				startNodesList.addElement(nodeLabel);
		    			endNodesList.addElement(nodeLabel);
	    			}
	    			newCanvasPanel.constructGraph();
	    			tabbedPane.setSelectedIndex(tabbedPane.indexOfTab(fileDialog.getSelectedFile().getName()));
	    			
	            }   
	    	}
	    });
		btnCreatePath.addActionListener(new ActionListener() {
	    	@Override
	    	public void actionPerformed(ActionEvent e) {
	    		System.out.println(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()));
	    		visualGraph canvasPanel = tabbedGraphs.get(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()));
	    		canvasPanel.doSearch(searchMethod.getSelectedItem().toString(),
	    				startPoint.getSelectedItem().toString(), endPoint.getSelectedItem().toString());
	    	}
	    });
		
		btnRegenerate.addActionListener(new ActionListener() {
	    	@Override
	    	public void actionPerformed(ActionEvent e) {
	    		
	    		visualGraph canvasPanel = tabbedGraphs.get(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()));
	    		String tempPath = canvasPanel.currentFile;
	    		if (visualMethod.getSelectedIndex() == 0) {
    				canvasPanel.useBranching();
    			} else {
    				canvasPanel.useOrbiting();
    			}
	    		canvasPanel.readGraph(tempPath);
	    		canvasPanel.constructGraph();
	    		
	    	}
	    });
		ChangeListener changeListener = new ChangeListener() {
			public void stateChanged(ChangeEvent changeEvent) {
				if (tabbedGraphs.containsKey(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()))){
					visualGraph newCanvasPanel  = tabbedGraphs.get(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()));

		    		startNodesList.removeAllElements();
	    			endNodesList.removeAllElements();
	    			graphParser graph = newCanvasPanel.graphPlacement.graph;
	    			for (String nodeLabel : graph.graph.keySet()) {
	    				startNodesList.addElement(nodeLabel);
		    			endNodesList.addElement(nodeLabel);
	    			}
				}
			}
		};
	    tabbedPane.addChangeListener(changeListener);
		
	}
}
