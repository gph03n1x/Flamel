import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Flamel {
	public static Frame framePanel;
	
	public static void main(String args[]) {
		visualGraph canvasPanel = new visualGraph();
		
		DefaultComboBoxModel<String> startNodesList = new DefaultComboBoxModel<String>();
		DefaultComboBoxModel<String> endNodesList = new DefaultComboBoxModel<String>();
		JComboBox<String> startPoint = new JComboBox<String>(startNodesList);
		JComboBox<String> endPoint = new JComboBox<String>(endNodesList);
		JFileChooser fileDialog = new JFileChooser("graphs/");
	    JButton showFileDialogButton = new JButton("Open File");
	    JButton updateGraph = new JButton("Update Graph");
	    
	    showFileDialogButton.addActionListener(new ActionListener() {
	    	@Override
	    	public void actionPerformed(ActionEvent e) {
	    		int returnVal = fileDialog.showOpenDialog(framePanel);
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
	    updateGraph.addActionListener(new ActionListener() {
	    	@Override
	    	public void actionPerformed(ActionEvent e) {
	    		canvasPanel.readGraph(fileDialog.getSelectedFile().getPath());
	    		canvasPanel.doSearch(startPoint.getSelectedItem().toString(), endPoint.getSelectedItem().toString());
	    		canvasPanel.constructGraph();
	    		
	    		canvasPanel.repaint();
	    	}
	    });
	    
	    JPanel controlPanel = new JPanel();
	    GridLayout layout = new GridLayout(9,3);
	    controlPanel.setLayout(layout);
	    layout.setHgap(10);
	    layout.setVgap(10);
	    controlPanel.add(startPoint);
	    controlPanel.add(endPoint);
	    controlPanel.add(showFileDialogButton);
	    controlPanel.add(updateGraph);
	    
	    
	    
	    framePanel = new Frame();
		framePanel.setSize(600, 600);
		framePanel.setLayout(new GridLayout(0,2));
		framePanel.add(controlPanel);
		framePanel.add(canvasPanel);
		framePanel.setVisible(true); 
		framePanel.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				System.exit(0);
			}
		});

		

	}
}
