import java.io.*;
import java.util.*;
import java.awt.Color;

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


class NodeComparator implements Comparator<Node> {
    public int compare(Node nodeFirst, Node nodeSecond) {
        return Double.compare(nodeFirst.f, nodeSecond.f);
    }
} 

public class Flamel {
	
	public static HashMap<String, Node> graph = new HashMap<String, Node>();
	public static HashMap<String, Double> heuristic = new HashMap<String, Double>();
	
	public static void main(String args[]) {
		
		Scanner scan = new Scanner(System.in);
		System.out.print("Give the path to your graph file>");
		String graphFile = scan.next();
		System.out.print("Give the starting point>");
		String start = scan.next();
		System.out.print("Give the ending point>");
		String end = scan.next();
		graphFile = "graph.txt";
		start = "A";
		end = "E";
		readGraph(graphFile);
		
		Astar(start, end, true, true, "A*");
		//Astar(start, end, true, false, "Hill Climbing");
		//Astar(start, end, false, true, "Uniform Cost");
		//dfs(start, end);
		//bfs(start, end);
		scan.close();
		

	}
	
	public static void readGraph(String graphFile){
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
					throw new NoSuchElementException("Line :\""+strLine+"\" is incomplete");
				}
			}
			
			//for (String name: graph.keySet()){
	        //    String value = graph.get(name).toString();  
	        //    System.out.println(value+ " Heuristic:"+heuristic.get(name));  
			//} 
			br.close();
			in.close();
		} catch (Exception e) {//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
		
	}
	
	public static void printPath(HashMap<Node, Node> path, Node currentNode, Node startNode) {
		//System.out.println("Debug: ");
    	//for (Node name: path.keySet()){
    	//	String key = name.label;
        //    String value = path.get(name).label;  
        //    System.out.println(key+ " "+value);  
    	//}
    	System.out.print("Path: ");
    	double cost = 0.0;
    	String pathResult = "";
    	currentNode.nodeColor = Color.orange;
    	pathResult = "->"+currentNode.label;
    	
    	while (path.get(currentNode) != startNode){
    		cost += graph.get(path.get(currentNode).label).costs.get(currentNode);
    		
    		currentNode = path.get(currentNode);
    		currentNode.nodeColor = Color.green;
    		pathResult = "->"+currentNode.label + pathResult;
    	}
    	cost += graph.get(path.get(currentNode).label).costs.get(currentNode);
    	path.get(currentNode).nodeColor = Color.orange;
    	pathResult = path.get(currentNode).label + pathResult;
    	System.out.println(pathResult);
    	System.out.println("Total path cost: "+ cost + "\n");
	}
	
	public static void bfs(String start, String end) {
		// BFS uses queue data structure
		Set<Node> closedList = new HashSet<Node>();
		Queue<Node> queue = new LinkedList<Node>();
		HashMap<Node, Node> path = new HashMap<Node, Node>();
		
		Node startNode = graph.get(start);
		queue.add(startNode);
		Node currentNode;
		int nodeCount=0;
		System.out.println("Breadth First Search:");
		while(!queue.isEmpty()) {
			
			currentNode = queue.remove();
			closedList.add(currentNode);
			nodeCount+=1;
			//System.out.println("Checking : "+currentNode.label );
			
			if (currentNode.label.equals(end)) { 
				System.out.println("Nodes visited: "+ nodeCount);
				printPath(path, currentNode, startNode);
            	break;
            }
			
			for (Node targetNode: currentNode.costs.keySet()){
				if (closedList.contains(targetNode)) continue;
				//System.out.println("Adding : "+targetNode.label );
				if (!queue.contains(targetNode))
					path.put(targetNode, currentNode);
				queue.add(targetNode);
				
			}

		}
	}
	
	public static void dfs(String start, String end) {
		// DFS uses Stack data structure
		Set<Node> closedList = new HashSet<Node>();
		Stack<Node> stack = new Stack<Node>();

		HashMap<Node, Node> path = new HashMap<Node, Node>();
		Node startNode = graph.get(start);
		stack.push(startNode);
		closedList.add(startNode);
		Node currentNode;
		int nodeCount=0;
		
		System.out.println("Depth First Search:");
		while(!stack.isEmpty()) {
			
			currentNode = stack.peek();
			nodeCount+=1;
			//System.out.println("Checking : "+currentNode.label );
			if (currentNode.label.equals(end)) { 
				System.out.println("Nodes visited: "+ nodeCount);
				printPath(path, currentNode, startNode);
            	break;
            }
			boolean addedToS = false;
			for (Node targetNode: currentNode.costs.keySet()){
				if (closedList.contains(targetNode)) continue;
				//System.out.println("Adding : "+targetNode.label );
				stack.push(targetNode);
				closedList.add(targetNode);
				path.put(targetNode, currentNode);
				addedToS = true;
			}
			if (!addedToS) {
				//System.out.println("Removing : "+currentNode.label );
				stack.pop();
			}
			
		}
	}

	public static void Astar(String start, String end, boolean useHeur, boolean useCost, String AlgName){
		Queue<Node> openQueue = new PriorityQueue<Node>(11, new NodeComparator()); 
		Set<Node> closedList = new HashSet<Node>();
		HashMap<Node, Node> path = new HashMap<Node, Node>();

		Node startNode = graph.get(start);
		startNode.setCost(0);
		startNode.getF(useHeur, useCost);
		openQueue.add(startNode);
		System.out.println(AlgName+":");
		int nodeCount=0;
		while (!openQueue.isEmpty()) {
            Node currentNode = openQueue.poll();
            closedList.add(currentNode);
            nodeCount+=1;

            if (currentNode.label.equals(end)) { 
            	System.out.println("Nodes visited: "+ nodeCount);
            	printPath(path, currentNode, startNode);
            	break;
            }
            
            for (Node targetNode: currentNode.costs.keySet()){
            	if (closedList.contains(targetNode)) continue;
            	double value = currentNode.g+currentNode.costs.get(targetNode);  
            	//System.out.println(currentNode.g + " : "+ currentNode.costs.get(targetNode) + " : " + value);
                targetNode.setCost(value);
                targetNode.getF(useHeur, useCost);
                
                path.put(targetNode, currentNode);
                
                if (!openQueue.contains(targetNode)) {
                    openQueue.add(targetNode);
                }
    		} 
		}
        

	}
}
