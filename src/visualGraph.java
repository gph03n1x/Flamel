import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;


class visualEdge {
	Node node1;
	Node node2;
}


class visualNode {
	Node actualNode;
	int x, y, nodeNumberId, nodeCount;
	Color nodeColor = Color.blue;
	
	
	public void calcX(double wideX, double offsetX){
		double rads = nodeNumberId * 2  * Math.PI/nodeCount;
		this.x = (int)(Math.cos(rads) * wideX + offsetX);
	}
	
	public void calcY(double wideY, double offsetY){
		double rads = nodeNumberId * 2  * Math.PI/nodeCount;
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
	

	private static final long serialVersionUID = 2084845050702797718L;
	public static HashMap<Node, visualNode> visualNodes = new HashMap<Node, visualNode>();
	public static ArrayList<visualEdge> visualEdges = new ArrayList<visualEdge>();
	public graphParser graph;
	//public String currentFile;
	
	public visualGraph(){ 
		setSize(300, 300); 
		setBackground(Color.white); 
	} 
	
	
	public void doSearch(String start, String end) {
		//Class<?> getSe = Class.forName("Astar");
		Astar getSe = new Astar();
		getSe.setGraph(graph.graph);
		getSe.setHeuristics(graph.heuristic);
		getSe.setOptions(true, true);
		getSe.lookForPath(start, end);
	}
	public void readGraph(String File){
		graph = new graphParser();
		graph.readGraph(File);
	}
	
	public void constructGraph(){
		visualNodes.clear();
		visualEdges.clear();
		int count = 1;
		for (String name: graph.graph.keySet()){
			visualNode currentNode = new visualNode();
			currentNode.actualNode = graph.graph.get(name);
			currentNode.nodeNumberId = count;
			currentNode.nodeCount = graph.graph.size();
			currentNode.nodeColor = graph.graph.get(name).nodeColor;
			visualNodes.put(graph.graph.get(name), currentNode);
	        count +=1;
	        for (Node neighbor: graph.graph.get(name).costs.keySet()){
	        	visualEdge edge = new visualEdge();
	        	edge.node1 = graph.graph.get(name);
	        	edge.node2 = neighbor;
	        	
	        	visualEdge edgeAlt = new visualEdge();
	        	edgeAlt.node1 = neighbor;
	        	edgeAlt.node2 = graph.graph.get(name);
	        	
	        	if (!visualEdges.contains(edge) && !visualEdges.contains(edgeAlt) ) {
	        		visualEdges.add(edge);
	        	}
	        	
	        	//g.drawLine(node1.x, node1.y, node2.x, node2.y);
	        }
	        
		}
	}
	
	public void paint(Graphics g){ 
		double offsetX = this.getWidth()/2;
		double offsetY = this.getHeight()/2;
		int circleDiameter = 10;
		double wideX = 75;
		double wideY = 75;
		g.setColor(Color.black);

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
	} 
}
