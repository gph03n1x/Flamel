import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class depthFirstSearch extends pathFinding {
	
	public void work() {
		Set<Node> closedList = new HashSet<Node>();
		Stack<Node> stack = new Stack<Node>();

		Node startNode = this.graph.get(this.start);
		stack.push(startNode);
		closedList.add(startNode);
		Node currentNode;
		int nodeCount=0;
		
		while(!stack.isEmpty()) {
			
			currentNode = stack.peek();
			nodeCount+=1;
			if (currentNode.label.equals(this.end)) { 
				System.out.println("Nodes visited: "+ nodeCount);
				this.printPath(currentNode, startNode);
            	break;
            }
			boolean addedToS = false;
			for (Node targetNode: currentNode.costs.keySet()){
				if (closedList.contains(targetNode)) continue;
				stack.push(targetNode);
				closedList.add(targetNode);
				this.path.put(targetNode, currentNode);
				addedToS = true;
			}
			if (!addedToS) {
				stack.pop();
			}
			
		}
	}
	
}
