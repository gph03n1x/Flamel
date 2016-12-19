import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JTextPane;


class visualEdge {
	Node node1;
	Node node2;
	Color edgeColor = Color.black;
}


class visualNode {
	Node actualNode;
	int x, y;
	double Phase;
	
	public void moveX(double totalOffsetX){
		this.x += totalOffsetX;
	}
	
	public void moveY(double totalOffsetY){
		this.y += totalOffsetY;
	}
	
	public void calcX(double wideX, double offsetX, double deg){
		//double rads = 2  * Math.PI * (deg + Phase);
		double rads = 2  * Math.PI * (deg);
		this.x = (int)(Math.cos(rads) * wideX + offsetX);
	}
	
	public void calcY(double wideY, double offsetY, double deg){
		//double rads = 2  * Math.PI * (deg + Phase);
	    double rads = 2  * Math.PI * (deg);
		this.y = (int)(Math.sin(rads) * wideY + offsetY);
	}
	
	public int getCenterX(int diameter){
		return this.x + diameter/2;
	}
	
	public int getCenterY(int diameter){
		return this.y + diameter/2;
	}
}

class visualNodeComparator implements Comparator<visualNode> {
    public int compare(visualNode nodeFirst, visualNode nodeSecond) {
        return Double.compare(nodeFirst.actualNode.h, nodeSecond.actualNode.h);
    }
} 


public class visualGraph extends Component implements MouseListener, MouseMotionListener{ 
	private static final long serialVersionUID = 8533078139841015261L;
	public static HashMap<Node, visualNode> visualNodes = new HashMap<Node, visualNode>();
	public static ArrayList<visualEdge> visualEdges = new ArrayList<visualEdge>();
	public graphParser graph;
	//public String currentFile;
	double clickedX, clickedY, draggedOffsetX=0, draggedOffsetY=0;
	double newPosX=0, newPosY=0;
	boolean createImage=false;
	int heightAndWidth = 800;
	
	BufferedImage image;
	JFrame frame;
	JTextPane textPanel;
	Panel controlPanel;
	
	public visualGraph(){ 
		addMouseListener(this);
		addMouseMotionListener(this);
		
	} 
	
	public void addReferences(JFrame frame, Panel controlPanel, JTextPane textPanel) {
		this.frame = frame;
		this.controlPanel = controlPanel;
		this.textPanel = textPanel;
		this.setBackground(Color.white);
		this.resizeComponents();
	}
	
	public void doSearch(String start, String end) {
		//Class<?> getSe = Class.forName("Astar");
		Astar getSe = new Astar();
		getSe.setGraph(graph.graph);
		getSe.setHeuristics(graph.heuristic);
		getSe.setOptions(true, true);
		
		this.textPanel.setText(getSe.lookForPath(start, end));
	}
	
	public void resizeComponents() {
		// Bit hardcoded 
		this.frame.setSize(heightAndWidth+400, heightAndWidth+100);
		this.setBounds(this.controlPanel.getWidth(), 0, heightAndWidth+this.controlPanel.getWidth(), heightAndWidth);
		this.textPanel.setBounds(this.getWidth()+10, 0,
				this.textPanel.getWidth(), this.textPanel.getHeight());
	}
	
	public void readGraph(String File){
		graph = new graphParser();
		graph.readGraph(File);
	}

	public void constructGraph(){
		Queue<visualNode> openQueue = new LinkedList<visualNode>();
		Set<visualNode> closedList = new HashSet<visualNode>();
		visualNode currentNode;
		// HardCoded stuff here
		double shouldBeAwayDistance = 100;
		currentNode = null;
		double startDeg = 0;
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
	        	currentNode.Phase = 0;
	        	openQueue.add(currentNode);
	        	minX = currentNode.x;
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
			startDeg = currentNode.Phase;
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
				double wideX=80, wideY=80;
				firstDeg +=1;
			    visualNode currentNeighbor = visualNodes.get(neighbor);
			   
			    if (closedList.contains(currentNeighbor)) continue;
			    //if (firstDeg/divisions == startDeg) continue;
			    boolean positionedProperly = false;
			    
			    while (!positionedProperly){
			    	positionedProperly = true;
				    currentNeighbor.calcX(wideX, currentNode.x, firstDeg/divisions);
				    currentNeighbor.calcY(wideY, currentNode.y, firstDeg/divisions);
				    currentNeighbor.Phase = firstDeg/divisions + 0.5;
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
		//heightAndWidth = 2 * ( maxX > maxY ? (int)maxX : (int)maxY ) ;
		//Center the graph as good as possible
		//Move inside towards the center the nodes that are outside 
		//
		double polX = (minX >= 0 ? 0 : -minX);
		double polY = (minY >= 0 ? 0 : -minY);;
		System.out.println("MX"+minX);
    	System.out.println("MY"+minY);
		System.out.println("PX"+polX);
    	System.out.println("PY"+polY);
		heightAndWidth = 2 * ( maxX > maxY ? (int)maxX : (int)maxY ) ;
		polX += Math.abs((minX+(maxX-minX)/2)-(heightAndWidth/2));
		polY += Math.abs((minY+(maxX-minY)/2)-(heightAndWidth/2));

		for (Node n : visualNodes.keySet()) {
			visualNode s = visualNodes.get(n);
			s.x += polX;
			s.y += polY;
			System.out.println("X"+s.x);
	    	System.out.println("Y"+s.y );

	    }

		resizeComponents();
		softRepaint();
		
	}
	
	public void softRepaint() {
		image = new BufferedImage(heightAndWidth, heightAndWidth, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		setSize(heightAndWidth, heightAndWidth);

		createImage=true;
		this.paint(g);
		try {
			ImageIO.write(image, "png", new File("test.png"));
		} catch (IOException ex) { }
		createImage=false;
		repaint();
	}
	
	
	public void paint(Graphics g){ 
		g.setColor(Color.white);
	    g.fillRect(0, 0, heightAndWidth, heightAndWidth);
	    
		if (!createImage){
			if (draggedOffsetX != 0 && draggedOffsetY != 0){
				g.drawImage(image, (int)draggedOffsetX, (int)draggedOffsetY, heightAndWidth, heightAndWidth, null);
				return;
			}
			g.drawImage(image, (int)newPosX, (int)newPosY, heightAndWidth, heightAndWidth, null);
			return;
		}

		int circleDiameter = 10; // Hardcoded
		
		for (visualEdge e: visualEdges){
			g.setColor(Color.black);
			if (e.node1.nodeColor != Color.blue && e.node2.nodeColor != Color.blue){
        		g.setColor(Color.magenta);
        	}
			
			g.drawLine(visualNodes.get(e.node1).getCenterX(circleDiameter),
					visualNodes.get(e.node1).getCenterY(circleDiameter),
					visualNodes.get(e.node2).getCenterX(circleDiameter),
					visualNodes.get(e.node2).getCenterY(circleDiameter));

		}
		for (Node n : visualNodes.keySet()) {
			visualNode s = visualNodes.get(n);
			g.setColor(s.actualNode.nodeColor);
			g.fillOval(s.x, s.y, circleDiameter, circleDiameter); 
			g.drawString(s.actualNode.label, s.x, s.y);
	    }
		
	}


	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		Point point = arg0.getPoint();
		draggedOffsetX = point.x - clickedX;
		draggedOffsetY = point.y - clickedY;
		repaint();
	}
	
	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		Point point = arg0.getPoint();
		clickedX = point.x;
		clickedY = point.y;
	}


	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		newPosX += draggedOffsetX;
	    newPosY += draggedOffsetY;
	    draggedOffsetX = 0;
	    draggedOffsetY = 0;
	    repaint();
	} 

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	
}
