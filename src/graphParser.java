import java.awt.Color;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.NoSuchElementException;


class Node {
    String label;
    HashMap<Node, Double> costs = new HashMap<Node, Double>();
    
    double g, h, f;
    Color nodeColor = Color.blue;
    
    public void setCost(double g) {
    	this.g = g;
    }
    
    public double getF(boolean useHeur, boolean useCost){
    	double heur = 0, cost = 0;
    	if (useHeur) heur = this.h;
    	if (useCost) cost = this.g;
    	this.f = heur+cost;
    	return this.f;
    }
    
    public String toString() {
    	String value = label+" # ";
    	
    	for (Node name: this.costs.keySet()){
            String node =name.label;
            double costs = this.costs.get(name);  
            value += node + ":" + costs;  
		} 
    	
    	return value;
    }
}


public class graphParser {
	public HashMap<String, Node> graph = new HashMap<String, Node>();
	public HashMap<String, Double> heuristic = new HashMap<String, Double>();
	
	public void readGraph(String graphFile){
		try {
			FileInputStream fstream = new FileInputStream(graphFile);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				if (strLine.charAt(0) == '#')
					continue;
				String[] parts = strLine.split(" ");
				
				if (parts.length == 2) {
					if (!heuristic.containsKey(parts[0])) {
						heuristic.put(parts[0], Double.parseDouble(parts[1]));
					}

				} else if (parts.length == 3) {
					Node firstNode = null;
					Node secondNode = null;
					if (!heuristic.containsKey(parts[0]) || !heuristic.containsKey(parts[1]))
						throw new NoSuchElementException("Some heuristic values are missing.");
					
					
					if (!graph.containsKey(parts[0])) {
						firstNode = new Node();
						firstNode.label = parts[0];
						firstNode.h = heuristic.get(parts[0]);
						graph.put(parts[0], firstNode);
					}
					
					if (!graph.containsKey(parts[1])) {
						secondNode = new Node();
						secondNode.label = parts[1];
						secondNode.h = heuristic.get(parts[1]);
						graph.put(parts[1], secondNode);
					}
					
					firstNode = graph.get(parts[0]);
					secondNode = graph.get(parts[1]);
					firstNode.costs.put(secondNode, Double.parseDouble(parts[2]));
					secondNode.costs.put(firstNode, Double.parseDouble(parts[2]));
				} else {
					System.out.println("Line :\""+strLine+"\" is incomplete");
				}
			}
			br.close();
			in.close();
			fstream.close();
		} catch (Exception e) {//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
		
	}

}
