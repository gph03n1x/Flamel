import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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
	int x, y, nodeNumberId, nodeCount;
	Color nodeColor = Color.blue;
	
	
	public void calcX(double wideX, double offsetX){
		double rads = nodeNumberId * 2  * Math.PI/nodeCount;
		this.x = (int)(Math.cos(rads) * wideX + offsetX);
	}
	
	public void calcY(double wideY, double offsetY){
		double rads = nodeNumberId * 2  * Math.PI/nodeCount;
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
		visualNodes.clear();
		visualEdges.clear();
		int count = 1;
		double maxHeuristic = 0;
		for (String name: graph.graph.keySet()){
			visualNode currentNode = new visualNode();
			currentNode.actualNode = graph.graph.get(name);
			if (currentNode.actualNode.h > maxHeuristic)
				maxHeuristic = currentNode.actualNode.h;
			currentNode.nodeNumberId = count;
			currentNode.nodeCount = graph.graph.size();
			currentNode.nodeColor = graph.graph.get(name).nodeColor;
			visualNodes.put(graph.graph.get(name), currentNode);
	        count +=1;
	        for (Node neighbor: graph.graph.get(name).costs.keySet()){
	        	
	        	visualEdge edge = new visualEdge();
	        	edge.node1 = graph.graph.get(name);
	        	edge.node2 = neighbor;

	        	visualEdge edgeAlt = new visualEdge();
	        	edgeAlt.node1 = neighbor;
	        	edgeAlt.node2 = graph.graph.get(name);
	        	
	        	if (edge.node1.nodeColor != Color.blue && edge.node2.nodeColor != Color.blue){
	        		edge.edgeColor = Color.magenta;
	        		edgeAlt.edgeColor = Color.magenta;
	        	}
	        	
	        	if (!visualEdges.contains(edge) && !visualEdges.contains(edgeAlt) ) {
	        		
	        		visualEdges.add(edge);
	        	}
	        	
	        	//g.drawLine(node1.x, node1.y, node2.x, node2.y);
	        }
	        
		}
		heightAndWidth = 2 * (int)maxHeuristic;
		resizeComponents();
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
	    
		double offsetX = heightAndWidth/2 + newPosX + draggedOffsetX;
		double offsetY = heightAndWidth/2 + newPosY + draggedOffsetX;
		/*
		System.out.println("W:"+this.getWidth());
		System.out.println("H:"+this.getHeight());
		System.out.println("Xoff:"+draggedOffsetX);
		System.out.println("Yoff:"+draggedOffsetY);
		System.out.println("PXoff:"+newPosX);
		System.out.println("PYoff:"+newPosY);
		*/
		
		int circleDiameter = 10;
		double wideX, wideY;
		
		g.setColor(Color.black);
		for (visualEdge e: visualEdges){
			g.setColor(e.edgeColor);
			wideX = wideY = visualNodes.get(e.node1).actualNode.h;
			visualNodes.get(e.node1).calcX(wideX, offsetX);
			visualNodes.get(e.node1).calcY(wideY, offsetY);
			
			wideX = wideY = visualNodes.get(e.node2).actualNode.h;
			visualNodes.get(e.node2).calcX(wideX, offsetX);
			visualNodes.get(e.node2).calcY(wideY, offsetY);
			
			g.drawLine(visualNodes.get(e.node1).getCenterX(circleDiameter),
					visualNodes.get(e.node1).getCenterY(circleDiameter),
					visualNodes.get(e.node2).getCenterX(circleDiameter),
					visualNodes.get(e.node2).getCenterY(circleDiameter));

		}
		for (Node n : visualNodes.keySet()) {
			visualNode s = visualNodes.get(n);
			g.setColor(s.nodeColor);
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
