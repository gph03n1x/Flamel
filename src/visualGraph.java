import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;


class visualEdge {
	Node node1;
	Node node2;
	Color edgeColor = Color.black;
}


class visualNode {
	Node actualNode;
	int x, y;
	
	public void moveX(double totalOffsetX){
		this.x += totalOffsetX;
	}
	
	public void moveY(double totalOffsetY){
		this.y += totalOffsetY;
	}
	
	public void calcX(double wideX, double offsetX, double deg){
		double rads = 2  * Math.PI * (deg);
		this.x = (int)(Math.cos(rads) * wideX + offsetX);
	}
	
	public void calcY(double wideY, double offsetY, double deg){
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
	int circleDiameter = 10;
	int imageHeight, imageWidth;
	
	
	BufferedImage image;
	JFrame frame;
	Panel controlPanel;
	JTabbedPane tabbedPane;
	
	public visualGraph(){ 
		addMouseListener(this);
		addMouseMotionListener(this);
		
	} 
	
	public void addReferences(JFrame frame, Panel controlPanel, JTabbedPane tabbedPane) {
		this.frame = frame;
		this.controlPanel = controlPanel;
		this.tabbedPane = tabbedPane;
		
		this.resizeComponents();
	}
	
	public void doSearch(String start, String end) {
		//Class<?> getSe = Class.forName("Astar");
		Astar getSe = new Astar();
		getSe.setGraph(graph.graph);
		getSe.setHeuristics(graph.heuristic);
		getSe.setOptions(true, true);
		getSe.lookForPath(start, end);
		//this.textPanel.setText(getSe.lookForPath(start, end));
	}
	
	public void resizeComponents() {
		// Bit hardcoded 
		this.frame.setSize(heightAndWidth+400, heightAndWidth+100);
		this.tabbedPane.setBounds(this.controlPanel.getWidth(), 0, heightAndWidth+this.controlPanel.getWidth(), heightAndWidth);
		this.setSize(this.tabbedPane.getWidth(), this.tabbedPane.getHeight());
		heightAndWidth = this.tabbedPane.getWidth() > this.tabbedPane.getHeight() ? this.tabbedPane.getWidth() : this.tabbedPane.getHeight(); 
		
	}
	
	public void readGraph(String File){
		graph = new graphParser();
		graph.readGraph(File);
	}

	public void constructGraph(){
		Queue<visualNode> openQueue = new LinkedList<visualNode>();
		Set<visualNode> closedList = new HashSet<visualNode>();
		visualNode currentNode = null;
		// HardCoded stuff here
		double shouldBeAwayDistance = 100;
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
		
		imageHeight = 2 * (int)(maxX-minX);
		imageWidth = 2 * (int)(maxY-minY) ;
		//System.out.println("H"+imageHeight+"W"+imageWidth);
		polX += Math.abs((minX+(maxX-minX)/2)-(heightAndWidth/2));
		polY += Math.abs((minY+(maxY-minY)/2)-(heightAndWidth/2));

		for (Node n : visualNodes.keySet()) {
			visualNode s = visualNodes.get(n);
			s.x += polX;
			s.y += polY;
	    }

		resizeComponents();
		softRepaint();
		
	}
	
	public void softRepaint() {
		System.out.println("SOFT");
		image = new BufferedImage(Math.abs(imageHeight), Math.abs(imageWidth), BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		setSize(imageHeight, imageWidth);

		createImage=true;
		this.paint(g);
		try {
			ImageIO.write(image, "png", new File("images/test.png"));
		} catch (IOException ex) { }
		createImage=false;
		setSize(heightAndWidth, heightAndWidth);
		repaint();
	}
	
	
	public void paint(Graphics g){ 
		g.setColor(Color.white);
	    
	    
		if (!createImage){
			g.fillRect(0, 0, heightAndWidth, heightAndWidth);
			g.drawImage(image, (int)(newPosX+draggedOffsetX), (int)(newPosY+draggedOffsetY), imageHeight, imageWidth, null);
			return;
		}
		
		System.out.println("CALLED");
		g.fillRect(0, 0, imageHeight, imageWidth);
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
			System.out.println("X:"+s.x+"Y:"+s.y);
			g.drawString(s.actualNode.label, s.x, s.y);
	    }
		
	}


	@Override
	public void mouseDragged(MouseEvent arg0) {
		Point point = arg0.getPoint();
		draggedOffsetX = point.x - clickedX;
		draggedOffsetY = point.y - clickedY;
		repaint();
	}
	
	@Override
	public void mousePressed(MouseEvent arg0) {
		Point point = arg0.getPoint();
		clickedX = point.x;
		clickedY = point.y;
	}


	@Override
	public void mouseReleased(MouseEvent arg0) {
		Point point = arg0.getPoint();
		draggedOffsetX = 0;
		draggedOffsetY = 0;
		newPosX += (point.x - clickedX);
		newPosY += (point.y - clickedY);
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
