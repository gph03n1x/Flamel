import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;


class NodeComparator implements Comparator<Node> {
    public int compare(Node nodeFirst, Node nodeSecond) {
        return Double.compare(nodeFirst.f, nodeSecond.f);
    }
} 


public class Astar extends pathFinding{
	
	boolean useHeur, useCost;
	
	public void setOptions(boolean useHeur, boolean useCost) {
		this.useHeur = useHeur;
		this.useCost = useCost;
	}
	
	@Override
	public void work(){
		Queue<Node> openQueue = new PriorityQueue<Node>(11, new NodeComparator()); 
		Set<Node> closedList = new HashSet<Node>();
		
		Node startNode = this.graph.get(this.start);
		startNode.setCost(0);
		startNode.getF(this.useHeur, this.useCost);
		openQueue.add(startNode);
		int nodeCount=0;
		while (!openQueue.isEmpty()) {
            Node currentNode = openQueue.poll();
            closedList.add(currentNode);
            nodeCount+=1;

            if (currentNode.label.equals(this.end)) { 
            	System.out.println("Nodes visited: "+ nodeCount);
            	printPath(currentNode, startNode);
            	break;
            }
            
            for (Node targetNode: currentNode.costs.keySet()){
            	if (closedList.contains(targetNode)) continue;
            	double value = currentNode.g+currentNode.costs.get(targetNode);  

                targetNode.setCost(value);
                targetNode.getF(this.useHeur, this.useCost);
                
                this.path.put(targetNode, currentNode);
                
                if (!openQueue.contains(targetNode)) {
                    openQueue.add(targetNode);
                }
    		} 
		}
	}
}
