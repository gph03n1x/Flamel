import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class breadthFirstSearch extends pathFinding{
	
	// work work work work work
	public String work() {

		Set<Node> closedList = new HashSet<Node>();
		Queue<Node> queue = new LinkedList<Node>();
		
		Node startNode = this.graph.get(this.start);
		queue.add(startNode);
		Node currentNode;
		int nodeCount=0;
		System.out.println("Breadth First Search:");
		while(!queue.isEmpty()) {
			
			currentNode = queue.remove();
			closedList.add(currentNode);
			nodeCount+=1;
			//System.out.println("Checking : "+currentNode.label );
			
			if (currentNode.label.equals(this.end)) { 
				return "Nodes visited: "+ nodeCount + "\n" + printPath(currentNode, startNode);
            }
			
			for (Node targetNode: currentNode.costs.keySet()){
				if (closedList.contains(targetNode)) continue;
				//System.out.println("Adding : "+targetNode.label );
				if (!queue.contains(targetNode))
					this.path.put(targetNode, currentNode);
				queue.add(targetNode);
				
			}

		}
		return "No path found.\n";
	}

}
