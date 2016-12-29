import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.swing.JTabbedPane;


public class visualGraph extends Component implements MouseListener, MouseMotionListener{ 
	private static final long serialVersionUID = 8533078139841015261L;
	
	public HashMap<Node, visualNode> visualNodes = new HashMap<Node, visualNode>();
	public ArrayList<visualEdge> visualEdges = new ArrayList<visualEdge>();
	public graphParser graph;
	public graphPlacement graphPlacement;
	public String currentFile;
	double clickedX, clickedY, draggedOffsetX=0, draggedOffsetY=0;
	double newPosX=0, newPosY=0;
	boolean createImage=false;
	int heightAndWidth = 800;
	int circleDiameter = 10;
	int imageHeight, imageWidth;
	
	BufferedImage image;
	JTabbedPane tabbedPane;
	configParser cfg;
	
	
	public visualGraph(JTabbedPane tabbedPane){ 
		cfg = new configParser();
		circleDiameter = Integer.valueOf(cfg.get("circle-diameter"));
		this.tabbedPane = tabbedPane;
		addMouseListener(this);
		addMouseMotionListener(this);
		this.resizeComponents();
	} 
	

	public void doSearch(String searchAlg, String start, String end) {
		try {
			Class<?> getC = Class.forName(searchAlg);
			Object getO = (Object)getC.newInstance();
			// TODO: Not the best way to do this.
			Method[] methods = getC.getMethods();
	
			for (Method method_ : methods) {
				if (method_.getName() == "Invoke") {
					method_.invoke(getO, graphPlacement.graph.graph, graphPlacement.graph.heuristic,
							start, end);
					break;
				}
			}
			softRepaint();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

	}
	
	public void useBranching() {
		graphPlacement = new branching();
	}
	
	public void useOrbiting() {
		graphPlacement = new orbiting();
	}
	
	
	public void resizeComponents() {
		this.tabbedPane.setSize(heightAndWidth, heightAndWidth);
		this.setSize(heightAndWidth, heightAndWidth);
		
	}
	
	public void readGraph(String FileName){
		currentFile = FileName;
		graphPlacement.graph = new graphParser();
		graphPlacement.graph.readGraph(FileName);
	}

	public void constructGraph(){
		graphPlacement.graphCreate();
		imageHeight = graphPlacement.imageHeight;
		imageWidth = graphPlacement.imageWidth;
		visualNodes = graphPlacement.visualNodes;
		visualEdges = graphPlacement.visualEdges;
		resizeComponents();
		softRepaint();
		
	}
	
	public void softRepaint() {
		image = new BufferedImage(Math.abs(imageHeight), Math.abs(imageWidth), BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		setSize(imageHeight, imageWidth);

		createImage=true;
		this.paint(g);
		try {
			ImageIO.write(image, "png", new File("images/"+graphPlacement.graph.sha1String+".png"));
			//System.out.println("Image created: "+graphPlacement.graph.sha1String+".png");
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
		//System.out.println("IX:"+imageHeight+"IY:"+imageWidth);
		for (Node n : visualNodes.keySet()) {
			visualNode s = visualNodes.get(n);
			g.setColor(s.actualNode.nodeColor);
			g.fillOval(s.x, s.y, circleDiameter, circleDiameter); 
			//System.out.println("X:"+s.x+"Y:"+s.y);
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
