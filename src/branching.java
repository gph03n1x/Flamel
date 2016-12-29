import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class branching extends graphPlacement{
	double shouldBeAwayDistance;
	
	public branching(){
		super();
		shouldBeAwayDistance = Double.valueOf(cfg.get("shouldBeAwayDistance"));
	}
	
	
	public void graphCreate() {
		Queue<visualNode> openQueue = new LinkedList<visualNode>();
		Set<visualNode> closedList = new HashSet<visualNode>();
		visualNode currentNode = null;
		// HardCoded stuff here
		double maxX=0, maxY=0, minX=heightAndWidth/2, minY=heightAndWidth/2;
		
		visualNodes.clear();
		visualEdges.clear();
		
		heightAndWidth = 800;
		
		for (String name: graph.graph.keySet()){
			currentNode = new visualNode();
			currentNode.actualNode = graph.graph.get(name);
			visualNodes.put(graph.graph.get(name), currentNode);
			
	        if (currentNode.actualNode.h == 0.0) {
	        	currentNode.x = heightAndWidth/2;
	        	currentNode.y = heightAndWidth/2;
	        	openQueue.add(currentNode);
	        	minX = currentNode.x; // This is wrong.
	        	minY = currentNode.y;
	        //} else if (rand.nextInt(100) + 1 <= 35){
	        //	currentNode.nodeNumberId = rand.nextInt(currentNode.nodeCount) + 1;
	        //	closedList.add(currentNode);
	        }
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
		
		while (!openQueue.isEmpty()) {
			currentNode = openQueue.poll();
			closedList.add(currentNode);
			if (currentNode.x > maxX){
				maxX = currentNode.x;
			}
			if (currentNode.y > maxY){
				maxY = currentNode.y;
			}
			if (currentNode.x < minX){
				minX = currentNode.x;
			}
			if (currentNode.y < minY){
				minY = currentNode.y;
			}
			double firstDeg = 0;
			double divisions = currentNode.actualNode.costs.size()+1;
			for (Node neighbor: currentNode.actualNode.costs.keySet()){ 
				double wideX=Double.valueOf(cfg.get("branchingWideX"));
				double wideY=Double.valueOf(cfg.get("branchingWideY"));
				firstDeg +=1;
			    visualNode currentNeighbor = visualNodes.get(neighbor);
			   
			    if (closedList.contains(currentNeighbor)) continue;
			    //if (firstDeg/divisions == startDeg) continue;
			    boolean positionedProperly = false;
			    
			    while (!positionedProperly){
			    	positionedProperly = true;
				    currentNeighbor.calcX(wideX, currentNode.x, firstDeg/divisions);
				    currentNeighbor.calcY(wideY, currentNode.y, firstDeg/divisions);
				    for (visualNode otherNode: closedList){
				    	
				    	double d = Math.sqrt(Math.pow(otherNode.x - currentNeighbor.x, 2)+Math.pow(otherNode.y - currentNeighbor.y, 2));
				    	//System.out.println(currentNeighbor.actualNode.label + ":"+ otherNode.actualNode.label);
				    	//System.out.println("X"+currentNeighbor.x + ":"+ otherNode.x);
				    	//System.out.println("Y"+currentNeighbor.y + ":"+ otherNode.y);
				    	//System.out.println(d);
				    	if (d < shouldBeAwayDistance){
				    		positionedProperly = false;
				    		break;
				    	}
				    	
				    }
				    wideX += 50;
				    wideY += 50;
				    
				}
			   
			   
			    if (!closedList.contains(currentNeighbor)) {
				    openQueue.add(currentNeighbor);
			    }
			   
			}
		}

		double polX = (minX >= 0 ? 0 : -minX);
		double polY = (minY >= 0 ? 0 : -minY);;
		// I think i might have this two confused
		imageHeight = 2 * (int)(maxX);
		imageWidth = 2 * (int)(maxY);
		//System.out.println("H"+imageHeight+"W"+imageWidth);
		polX = Math.abs((minX+(maxX-minX)/2)-(heightAndWidth/2));
		polY = Math.abs((minY+(maxY-minY)/2)-(heightAndWidth/2));
		
		//System.out.println("IX:"+imageHeight+"IY:"+imageWidth);
		//System.out.println("PX:"+polX+"PY:"+polY);
		
		
		
		for (Node n : visualNodes.keySet()) {
			visualNode s = visualNodes.get(n);
			//System.out.println("BPX:"+s.x+"BPY:"+s.y);
			if (s.x > heightAndWidth/2){
				s.x -= polX;
			} else {
				s.x += polX;
			}
			if (s.y > heightAndWidth/2){
				s.y -= polY;
			} else {
				s.y += polY;
			}
			
			//System.out.println("APX:"+s.x+"APY:"+s.y);
	    }
	    
	}

}
