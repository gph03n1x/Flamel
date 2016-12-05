
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;


class visualEdge {
	Node node1;
	Node node2;
}


class visualNode {
	Node actualNode;
	int x, y, nodeNumberId;
	Color nodeColor = Color.blue;
	
	public void calcX(double wideX, double offsetX){
		double rads = nodeNumberId * 2  * Math.PI/Flamel.graph.size();
		this.x = (int)(Math.cos(rads) * wideX + offsetX);
	}
	
	public void calcY(double wideY, double offsetY){
		double rads = nodeNumberId * 2  * Math.PI/Flamel.graph.size();
		this.y = (int)(Math.sin(rads) * wideY + offsetY);
	}
	
	public int getCenterX(int diameter){
		return this.x + diameter/2;
	}
	
	public int getCenterY(int diameter){
		return this.y + diameter/2;
	}
}


public class visualGraph extends Canvas { 
	public static Frame framePanel;
	public static HashMap<Node, visualNode> visualNodes = new HashMap<Node, visualNode>();
	public static ArrayList<visualEdge> visualEdges = new ArrayList<visualEdge>();
	
	
	public visualGraph(){ 
		setSize(200, 200); 
		setBackground(Color.white);
	} 
	
	
	public static void main(String[] argS){ 
		visualGraph canvasPanel = new visualGraph();
		Flamel.readGraph("graph2.txt");
		Flamel.Astar("Oradea", "Hirsova", true, true, "A*");

		int count = 1;
		for (String name: Flamel.graph.keySet()){
			visualNode currentNode = new visualNode();
			currentNode.actualNode = Flamel.graph.get(name);
			currentNode.nodeNumberId = count;
			currentNode.nodeColor = Flamel.graph.get(name).nodeColor;
			visualNodes.put(Flamel.graph.get(name), currentNode);
	        count +=1;
	        for (Node neighbor: Flamel.graph.get(name).costs.keySet()){
	        	visualEdge edge = new visualEdge();
	        	edge.node1 = Flamel.graph.get(name);
	        	edge.node2 = neighbor;
	        	
	        	visualEdge edgeAlt = new visualEdge();
	        	edgeAlt.node1 = neighbor;
	        	edgeAlt.node2 = Flamel.graph.get(name);
	        	
	        	if (!visualEdges.contains(edge) && !visualEdges.contains(edgeAlt) ) {
	        		visualEdges.add(edge);
	        	}
	        	
	        	//g.drawLine(node1.x, node1.y, node2.x, node2.y);
	        }
	        
		}
		
		
		framePanel = new Frame(); 
		framePanel.setSize(600, 600); 
		framePanel.add(canvasPanel); 
		framePanel.setVisible(true); 
		framePanel.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				System.exit(0);
			}
		});
		
	} 
	public void paint(Graphics g){ 
		double offsetX = this.getWidth()/2;
		double offsetY = this.getHeight()/2;
		int circleDiameter = 20;
		double wideX = 150;
		double wideY = 150;
		
		g.setColor(Color.black);
		System.out.println(visualEdges.size());
		for (visualEdge e: visualEdges){
			visualNodes.get(e.node1).calcX(wideX, offsetX);
			visualNodes.get(e.node1).calcY(wideY, offsetY);
			visualNodes.get(e.node2).calcX(wideX, offsetX);
			visualNodes.get(e.node2).calcY(wideY, offsetY);
			
			g.drawLine(visualNodes.get(e.node1).getCenterX(circleDiameter),
					visualNodes.get(e.node1).getCenterY(circleDiameter),
					visualNodes.get(e.node2).getCenterX(circleDiameter),
					visualNodes.get(e.node2).getCenterY(circleDiameter));

		}
		
		for (Node n : visualNodes.keySet()) {
			visualNode s = visualNodes.get(n);
			g.setColor(s.nodeColor);
			g.fillOval(s.x, s.y, circleDiameter, circleDiameter); 
			g.drawString(s.actualNode.label, s.x, s.y);
	    }
		// TODO: fixit

		
		

	} 
	
}
