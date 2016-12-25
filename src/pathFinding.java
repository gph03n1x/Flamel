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
	
	public String lookForPath(String start, String end){
		/*
		 * Clears the path and then calls work()
		 */
		this.path.clear();
		this.start = start;
		this.end = end;
		return this.work();
	}

	public String printPath(Node currentNode, Node startNode) {
		for (String label : graph.keySet()) {
			graph.get(label).nodeColor = Color.blue;
		}
    	double cost = 0.00;
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
    	return "Path: \n" + pathResult + "\nTotal path cost: "+ cost + "\n";
    	
	}
	
	abstract String work();

}
