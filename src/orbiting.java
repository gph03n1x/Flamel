
public class orbiting extends graphPlacement{
	public void graphCreate() {
		visualNodes.clear();
		visualEdges.clear();
		double wideX = 200;
		double wideY = 200;
		double count = 1;
		double nodeCount = graph.graph.size();
		for (String name: graph.graph.keySet()){
			visualNode currentNode = new visualNode();
			currentNode.actualNode = graph.graph.get(name);
			currentNode.calcX(wideX, offsetX, count/nodeCount);
			currentNode.calcY(wideY, offsetY, count/nodeCount);
			visualNodes.put(graph.graph.get(name), currentNode);
			count +=1;
		    for (Node neighbor: graph.graph.get(name).costs.keySet()){
		    	visualEdge edge = new visualEdge();
		    	edge.node1 = graph.graph.get(name);
		    	edge.node2 = neighbor;
		    	
		    	visualEdge edgeAlt = new visualEdge();
		    	edgeAlt.node1 = neighbor;
		    	edgeAlt.node2 = graph.graph.get(name);
		        	
		    	if (!visualEdges.contains(edge) && !visualEdges.contains(edgeAlt) ) {
		    		visualEdges.add(edge);
		    	}
		    }
		}
	}
}