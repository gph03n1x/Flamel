import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class breadthFirstSearch extends pathFinding{
	
	public void work() {

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
				System.out.println("Nodes visited: "+ nodeCount);
				this.printPath(currentNode, startNode);
            	break;
            }
			
			for (Node targetNode: currentNode.costs.keySet()){
				if (closedList.contains(targetNode)) continue;
				//System.out.println("Adding : "+targetNode.label );
				if (!queue.contains(targetNode))
					this.path.put(targetNode, currentNode);
				queue.add(targetNode);
				
			}

		}
	}

}
