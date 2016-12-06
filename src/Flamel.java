import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;

public class Flamel {
	public static Frame framePanel;
	
	public static void main(String args[]) {
		visualGraph canvasPanel = new visualGraph();
		framePanel = new Frame(); 
		framePanel.setSize(600, 600); 
		framePanel.add(canvasPanel); 
		framePanel.setVisible(true); 
		framePanel.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				System.exit(0);
			}
		});
		
		Scanner scan = new Scanner(System.in);
		System.out.print("Give the path to your graph file>");
		String graphFile = scan.next();
		System.out.print("Give the starting point>");
		String start = scan.next();
		System.out.print("Give the ending point>");
		String end = scan.next();
		graphFile = "graphs/graph.txt";
		start = "A";
		end = "E";
		graphParser graph = new graphParser();
		graph.readGraph(graphFile);
		
		Astar astar = new Astar();
		astar.setGraph(graph.graph);
		astar.setHeuristics(graph.heuristic);
		astar.setOptions(true, true);
		astar.lookForPath(start, end);

		scan.close();
		

	}
}
