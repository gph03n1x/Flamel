import java.util.*;

public class Flamel {
	
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
		graphParser graph = new graphParser();
		graph.readGraph(graphFile);
		
		Astar astar = new Astar();
		astar.setGraph(graph.graph);
		astar.setHeuristics(graph.heuristic);
		astar.setOptions(true, true);
		astar.lookForPath(start, end);
		//Astar(start, end, true, true, "A*");
		//Astar(start, end, true, false, "Hill Climbing");
		//Astar(start, end, false, true, "Uniform Cost");
		//dfs(start, end);
		//bfs(start, end);
		scan.close();
		

	}
	/*
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
	}*/
}
