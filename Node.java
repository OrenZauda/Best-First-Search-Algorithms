import java.util.Vector;
import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors

public class Node implements Comparable<Node> {
	// parent for the path
	public Node Parent;
	// state to store the 2D String
	public Game State;
	// to accuire the path
	public int[][] Source = new int[1][2];
	public int[][] Destination;
	public String Color;
	public int costUpToNow;
	public int totalCost;
	// mostly for debug
	public int distanceFromRoot;
	public boolean out = false;
	
	public Node(Game state){
		State = state;	
		costUpToNow = 0;
		totalCost =0;
		Source[0][0] = -1;
		distanceFromRoot = 0;
	}
	
	//The constructor Node(BigGame, Node, String, int[][], int[][], int) is undefined
	public Node(Game state, Node parent, String color, int[][] source, int[][] destination, int cost) {
		
		State = state;	
		Parent = parent;
		Source = source;
		Destination = destination;
		Color = color;
		if (Color.equals("R")||Color.equals("Y")) {costUpToNow = parent.costUpToNow + 1;}
		if (Color.equals("B")) {costUpToNow = parent.costUpToNow + 2;}
		if (Color.equals("G")) {costUpToNow = parent.costUpToNow + 10;}
		totalCost = cost + costUpToNow;
		distanceFromRoot = parent.distanceFromRoot + 1;
	}

	
	// to check if two nodes are identical
	public boolean Equals(String[][] b) {
		
		for (int i = 0; i < b.length; i++) {
			
			for (int j = 0; j < b[0].length; j++) {
				
				if(!State.Board[i][j].equals(b[i][j])&&!State.Board[i][j].equals("X")&& !b[i][j].equals("X")) {
					return false;
				}
			}
		}
		return true;
	}

	// return the path, recursively
	public String path() {
		
		//(2,2):B:(2,3)--(2,3):B:(1,3)--(3,2):G:(2,2)
		if (Parent == null) 
		{
		return "";	
		}
		String Path = "("+ Integer.toString(Source[0][0]+ 1) + "," + Integer.toString(Source[0][1]+ 1) + "):" + Color 
				+ ":(" + Integer.toString(Destination[0][0] + 1) + "," + Integer.toString(Destination[0][1] + 1) + ")"; 
		
		return Parent.path() +  Path + "--";
	}

	// print the content. for debug
	public void print() {
		if(State.isSmall) {
			for(int i = 0; i < 3; i++) {
				for(int j = 0; j < 3; j++) {
					System.out.print(State.Board[i][j]);
					}
				System.out.println("");
			}
		}
		else {
			for(int i = 0; i < 5; i++) {
				for(int j = 0; j < 5; j++) {
					System.out.print(State.Board[i][j]);
					}
				System.out.println("");
			}
		}
		
	}


	@Override
	// ordering the nodes for the priority queue
	public int compareTo(Node other) {
				
		if(totalCost < other.totalCost) {
			return -1;
		}
		if(totalCost == other.totalCost) {
			if (distanceFromRoot > other.distanceFromRoot) {
				return -1;
			}
			else return 1;
		}
		
		else {
			return 1;
		}
		
	}


	
	
}

