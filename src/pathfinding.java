import java.awt.Color;
import java.util.HashMap;

public abstract class pathFinding {
	public HashMap<String, Node> graph = new HashMap<String, Node>();
	public HashMap<String, Double> heuristic = new HashMap<String, Double>();
	public HashMap<Node, Node> path = new HashMap<Node, Node>();
	String start, end;
	
	public void setGraph(HashMap<String, Node> graph){
		this.graph = graph;
	}
	public void setHeuristics(HashMap<String, Double> heuristic){
		this.heuristic = heuristic;
	}
	
	public void lookForPath(String start, String end){
		this.start = start;
		this.end = end;
		this.work();
	}

	public void printPath(Node currentNode, Node startNode) {

    	System.out.print("Path: ");
    	double cost = 0.0;
    	String pathResult = "";
    	currentNode.nodeColor = Color.orange;
    	pathResult = "->"+currentNode.label;
    	
    	while (path.get(currentNode) != startNode){
    		cost += this.graph.get(this.path.get(currentNode).label).costs.get(currentNode);
    		
    		currentNode = this.path.get(currentNode);
    		currentNode.nodeColor = Color.green;
    		pathResult = "->"+currentNode.label + pathResult;
    	}
    	cost += this.graph.get(this.path.get(currentNode).label).costs.get(currentNode);
    	this.path.get(currentNode).nodeColor = Color.orange;
    	pathResult = this.path.get(currentNode).label + pathResult;
    	System.out.println(pathResult);
    	System.out.println("Total path cost: "+ cost + "\n");
	}
	
	abstract void work();

}
