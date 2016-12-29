import java.util.Comparator;
import java.util.HashMap;
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
	
	public void Invoke(HashMap<String, Node> graph, HashMap<String, Double> heuristic,
			String start, String end) {
		System.out.println("Invoked.");
		System.out.println("Start:");
		System.out.println(start);
		System.out.println("End:");
		System.out.println(end);
		//for (String label: graph.keySet()){
		//	System.out.println(label);
		//}
		this.setGraph(graph);
		this.setHeuristics(heuristic);
		this.setOptions(true, true);
		this.lookForPath(start, end);
		this.work();
	}
	
	
	public void setOptions(boolean useHeur, boolean useCost) {
		this.useHeur = useHeur;
		this.useCost = useCost;
	}
	
	@Override
	public String work(){
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
            	System.out.println("Nodes visited: "+ nodeCount + "\n" + printPath(currentNode, startNode));
            	return "Nodes visited: "+ nodeCount + "\n" + printPath(currentNode, startNode);
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
		return "No path found.\n";
	}
}
