import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;


class visualEdge {
	Node node1;
	Node node2;
	Color edgeColor = Color.black;
}


class visualNode {
	Node actualNode;
	int x, y;
	double radsX, radsY;
	
	
	public void moveX(double totalOffsetX){
		this.x += totalOffsetX;
	}
	
	public void moveY(double totalOffsetY){
		this.y += totalOffsetY;
	}
	
	public void calcX(double wideX, double offsetX, double deg){
		radsX = 2  * Math.PI * (deg);
		this.x = (int)(Math.cos(radsX) * wideX + offsetX);
	}
	
	public void calcY(double wideY, double offsetY, double deg){
		radsY = 2  * Math.PI * (deg);
		this.y = (int)(Math.sin(radsY) * wideY + offsetY);
	}
	
	public int getCenterX(int diameter){
		return this.x + diameter/2;
	}
	
	public int getCenterY(int diameter){
		return this.y + diameter/2;
	}
}


public abstract class graphPlacement {
	public HashMap<Node, visualNode> visualNodes = new HashMap<Node, visualNode>();
	public ArrayList<visualEdge> visualEdges = new ArrayList<visualEdge>();
	public graphParser graph;
	
	int heightAndWidth = 800;
	double offsetX=heightAndWidth/2;
	double offsetY=heightAndWidth/2;
	int imageHeight=heightAndWidth, imageWidth=heightAndWidth;
	configParser cfg;
		
	public graphPlacement() {
		cfg = new configParser();
	}
	
	
	abstract void graphCreate();

	

}
