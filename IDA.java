import java.time.Duration;
import java.time.Instant;
import java.util.Stack;
import java.util.Vector;

public class IDA {
	
	public int counter = 0;
	boolean issmall;
	public IDA() {		
	}
	
	public String IterativeDeepeningA(Node start, Vector<Node> Goals, boolean isSmall,  boolean open) {
		// to measure time
		Instant startTime = Instant.now();
		issmall = isSmall;
		// hash and stack to contain the nodes
		HashTable hash = new HashTable(issmall);
		Stack<Node> openList = new Stack<Node>();
		// to calculate the cost
		HeuristicFunc func = new HeuristicFunc(Goals);
		int treshHold = func.CalcValue(start.State.Board);
		System.out.println("treshHold = "+ treshHold);
		
		while (treshHold!=(int)Double.POSITIVE_INFINITY) {
			// restate the node start as not out, for re-use
			start.out = false;
			hash.add(start);
			openList.add(start);
			double MinF = Double.POSITIVE_INFINITY;
			while (!openList.isEmpty()) {

				//print open list content
				if(open) {
					hash.print();
				}
				Node n = openList.pop();
				
				
				if (n.out) {
					int index = hash.HashFunc(n.State.Board);
					hash.remove(n,index);
				}
				else {
					n.out = true; 
					openList.add(n);
					// expand the node that pop from the stack 
					Vector<Node> Children = ExpandNode(n, func);
					
					for (int i = 0; i<Children.size(); i++) {
						
						Node Child = Children.get(i);
						
						
						
						double valueOFchild = Child.totalCost;
						
						if (valueOFchild > treshHold) {
							
						MinF = Math.min(MinF,valueOFchild);	
						continue;
						}
						
						Node g = isUniqe(Child.State.Board, hash,func);
						
						if (g!= null && !g.out) {
							
							if(func.CalcValue(g.State.Board) > func.CalcValue(n.State.Board) ) {
								int index = hash.HashFunc(g.State.Board);
								hash.remove(g,index);
								openList.remove(g);
								
							}
							else {
								continue;
							}
						}
						if (g!= null && g.out) {
							
							
							continue;
						}
						// if this is the goal, return the path and time
						for(Node Goal: Goals) {
							if (Goal.Equals(Child.State.Board)) {
								Child.out = true;
								openList.add(Child);
								Instant finish = Instant.now();
			    				int costOFsolution = Cost(n.path());
			    				String path = n.path();
			    				path = path.substring(0, path.length()-2);
			    				double timeElapsed = Duration.between(startTime, finish).toMillis();  //in millis
			   		         	timeElapsed = timeElapsed/1000;
			   		           
			   		         	return path + '\n' + "Num: " + counter+ '\n' + "Cost: " +costOFsolution + '\n' + timeElapsed;
								
							}
							
						}
						hash.add(Child);
						openList.add(Child);

						

					} 
					
				}
			}
			
			treshHold = (int) MinF;
			
			
		}
		 Instant finish = Instant.now();
		 double timeElapsed = Duration.between(startTime, finish).toMillis();
         timeElapsed = timeElapsed/1000;
		 return "no path" +'\n'+ "Num: " + counter +'\n' + "Cost: inf" + '\n' +timeElapsed; 
	}
	private int Cost(String path) {
		int cost = 0;
		// TODO Auto-generated method stub
		for (int i = 0; i< path.length();i++) {
			if(path.charAt(i)=='B') {
				cost = cost + 2;
			}
			if(path.charAt(i)=='R' ||path.charAt(i)=='Y') {
				cost = cost + 1;
			}
			if(path.charAt(i)=='G') {
				cost = cost + 10;
			}
			
		}
		return cost;
	}
	private String path( Stack<Node> openList) {
		String path = "";
		while(!openList.isEmpty())
		{
		Node n = openList.pop();
		if(n.out) {
				
				if(n.Source[0][0] != -1) {
				path = path + "--("+ Integer.toString(n.Source[0][0]+ 1) + "," + Integer.toString(n.Source[0][1]+ 1) + "):" + n.Color 
					+ ":(" + Integer.toString(n.Destination[0][0] + 1) + "," + Integer.toString(n.Destination[0][1] + 1) + ")";
				}
				//(2,2):B:(2,3)--(2,3):B:(1,3)--(3,2):G:(2,2)
		}
		}
		return path;
	}

	public int[][] findIndexesOfBlank(Game board) {
		int numberOfBlanks = 9;
		if(issmall) {
			numberOfBlanks = 3;
		}
		int[][] IndexesOfBlank = new int[numberOfBlanks][2];
		// find where the "_" is:
		int s = 0;

			for (int i = 0; i < board.Board.length; i++) 
			{
				for (int j = 0; j < board.Board[0].length; j++)
					
					{
						if (board.Board[i][j].equals("_")){
							IndexesOfBlank[s][0] = i;
							IndexesOfBlank[s++][1] = j;
						}
					}
			
		}
		
		return IndexesOfBlank;
	}
	// generating the childdren of the node
	public Vector<Node> ExpandNode(Node state, HeuristicFunc func){
		
		
		int[][] IndexesOfBlank = findIndexesOfBlank(state.State);
		int maxI = 4;
		int maxJ = 4;
		if(issmall) {
			maxI = 2;
			maxJ = 2;
		}
		// Vector to store all the children nodes
		Vector<Node> nextNodes = new Vector<Node>();
		
		for (int s = 0; s < IndexesOfBlank.length; s++) 
		{
			// extract location of "_"
			int i = IndexesOfBlank[s][0];
			int j = IndexesOfBlank[s][1];
			
			// if the line isn't 0 we can go down with the "_"
			if (i > 0 && !state.State.Board[i-1][j].equals("_") && !state.State.Board[i-1][j].equals("X")) { 
				
				Game nextState =  new Game(state.State.Board,issmall);
				nextState.Board[i][j] = nextState.Board[i-1][j];
				nextState.Board[i-1][j] = "_";
				int[][] source = new int[1][2];
				source[0][0] = i-1;
				source[0][1] = j;
				int[][] destination = new int[1][2];
				destination[0][0] = i;
				destination[0][1] = j;
				counter++;
				int cost= func.CalcValue(nextState.Board);
				Node newNode = new Node(nextState,state,state.State.Board[i-1][j],source,destination,cost);
				nextNodes.add(newNode);
					
				
				
			}
			
			// if the line isn't 2 we can go up with the "_"
			if (i < maxI && !state.State.Board[i+1][j].equals("_")&& !state.State.Board[i+1][j].equals("X")) { 
				
				Game nextState =  new Game(state.State.Board,issmall);
				nextState.Board[i][j] = nextState.Board[i+1][j];
				nextState.Board[i+1][j] = "_";
				int[][] source = new int[1][2];
				source[0][0] = i+1;
				source[0][1] = j;
				int[][] destination = new int[1][2];
				destination[0][0] = i;
				destination[0][1] = j;
				counter++;
				int cost= func.CalcValue(nextState.Board);
				Node newNode = new Node(nextState,state,state.State.Board[i+1][j],source,destination,cost);
				nextNodes.add(newNode);
					
				
			}
			// if the column isn't 2 we can go right with the "_"
			if (j < maxJ && !state.State.Board[i][j+1].equals("_")&& !state.State.Board[i][j+1].equals("X")) { 
				
				Game nextState =  new Game(state.State.Board,issmall);
				nextState.Board[i][j] = nextState.Board[i][j+1];
				nextState.Board[i][j+1] = "_";
				int[][] source = new int[1][2];
				source[0][0] = i;
				source[0][1] = j + 1;
				int[][] destination = new int[1][2];
				destination[0][0] = i;
				destination[0][1] = j;
				counter++;
				int cost= func.CalcValue(nextState.Board);
				Node newNode = new Node(nextState,state,state.State.Board[i][j+1],source,destination,cost);
				nextNodes.add(newNode);
				
			}
			// if the column isn't 0 we can go left with the "_"
						
			if (j > 0 && !state.State.Board[i][j-1].equals("_")&& !state.State.Board[i][j-1].equals("X")) { 
							
				Game nextState =  new Game(state.State.Board,issmall);
				nextState.Board[i][j] = nextState.Board[i][j-1];
				nextState.Board[i][j-1] = "_";
				int[][] source = new int[1][2];
				source[0][0] = i;
				source[0][1] = j-1;
				int[][] destination = new int[1][2];
				destination[0][0] = i;
				destination[0][1] = j;
				counter++;
				int cost= func.CalcValue(nextState.Board);
				Node newNode = new Node(nextState,state,state.State.Board[i][j-1],source,destination,cost);
						
				nextNodes.add(newNode);
				
				}

			
		}
		
		return nextNodes;
	}
	
	// ensuring the node is uniqe and not in the open list
	public Node isUniqe(String[][] someBoard, HashTable OpenList, HeuristicFunc func) {
		
		int index = OpenList.HashFunc(someBoard);
		if (OpenList.Array.get(index).isEmpty()) {
			
			return null;
		}  
		
		for (int i = 0; i < OpenList.Array.get(index).size(); i++) {
						
					if (OpenList.Array.get(index).get(i).Equals(someBoard)) {
					
						return OpenList.Array.get(index).get(i);
						
					
				
			}
		}
		
		
		return null;
	}

}

