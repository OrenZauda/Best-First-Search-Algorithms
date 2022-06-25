import java.time.Duration;
import java.time.Instant;
import java.util.Vector;

public class DFID {
	int counter = 0;
	boolean issmall;
	
	public DFID() {
	}
	
	public String DepthFirstIterativeDeepening(Node start, Vector<Node> Goals, boolean isSmall, boolean open) {
    	// to measure the execution time
		Instant startTime = Instant.now();
		int depth = 1;
		
		issmall = isSmall;
		while(true) {
			
			HashTable hash = new HashTable(isSmall);
			if(open) {
				hash.print();
			}		
			String result = limitedDFS(start,Goals,hash, depth, isSmall);
			if (!result.equals("CUTOFF") &&!result.equals("no path")) {
				Instant finish = Instant.now();
		        double timeElapsed = Duration.between(startTime, finish).toMillis();  //in millis
		        timeElapsed = timeElapsed/1000;
				return result + '\n' + "Num: "+ counter + '\n'+ "Cost: "+ Cost(result) 
				+ '\n'+ timeElapsed;
				
			}
			if(result.equals("no path"))
			{
				 Instant finish = Instant.now();
			        double timeElapsed = Duration.between(startTime, finish).toMillis();  //in millis
			        timeElapsed = timeElapsed/1000;
					return "no path" +'\n'+ "Num: " + counter +'\n' + "Cost: inf" + '\n' +timeElapsed;		        
			}
			depth++;
			
		}

	}

	private String limitedDFS(Node n, Vector<Node> Goals,HashTable hash, int depth, boolean isSmall) {
		// to store the results
		String result = "";
		// if this is the goal, return the path and time

		for (Node Goal: Goals) {
			if(Goal.Equals(n.State.Board)) {
				return n.path();
			}
		}
		// tell the algo' that we end the tree 'til the limit
		if(depth == 0) {
			return "CUTOFF";
		}
		boolean isCutoff = false;
		// expand the node
		Vector<Node> Children = ExpandNode(n, hash,isSmall);
		
		for(int i = 0; i< Children.size(); i++) {
			Node Child = Children.get(i);
			if(!isUniqe(Child.State.Board, hash)) {
				continue;
			}
			result = limitedDFS(Child, Goals, hash, depth-1,isSmall);
			if(result.equals("CUTOFF")) {
				isCutoff = true;
			}
			if(!result.equals("CUTOFF") &&!result.equals("no path")) {
				return result;
			}
			
		}
		int index = hash.HashFunc(n.State.Board);
		hash.remove(n, index);
		if(isCutoff) {
			return "CUTOFF";
		}
		
		return "no path";
	}
	
	// find Indexes Of Blank , help expand node function
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
	// generate the children nodes of a state
	public Vector<Node> ExpandNode(Node state, HashTable hash, boolean isSmall){
		int maxI = 4;
		int maxJ = 4;
		if (issmall) {
			maxI = 2;
			maxJ = 3;
		}
		int[][] IndexesOfBlank = findIndexesOfBlank(state.State);
		
		// Vector to store all the children nodes
		Vector<Node> nextNodes = new Vector<Node>();
		
		for (int s = 0; s < IndexesOfBlank.length; s++) 
		{
			// extract location of "_"
			int i = IndexesOfBlank[s][0];
			int j = IndexesOfBlank[s][1];
			
			// if the line isn't 0 we can go down with the "_"
			if (i > 0 && !state.State.Board[i-1][j].equals("_") && !state.State.Board[i-1][j].equals("X")) { 
				
				Game nextState =  new Game(state.State.Board, isSmall);
				nextState.Board[i][j] = nextState.Board[i-1][j];
				nextState.Board[i-1][j] = "_";
				int[][] source = new int[1][2];
				source[0][0] = i-1;
				source[0][1] = j;
				int[][] destination = new int[1][2];
				destination[0][0] = i;
				destination[0][1] = j;
				counter++;				
				Node newNode = new Node(nextState,state,state.State.Board[i-1][j],source,destination,0);
				nextNodes.add(newNode);
	
			}
			
			// if the line isn't 2 we can go up with the "_"
			if (i < maxI && !state.State.Board[i+1][j].equals("_") && !state.State.Board[i+1][j].equals("X")) { 
				
				Game nextState =  new Game(state.State.Board, isSmall);
				nextState.Board[i][j] = nextState.Board[i+1][j];
				nextState.Board[i+1][j] = "_";
				int[][] source = new int[1][2];
				source[0][0] = i+1;
				source[0][1] = j;
				int[][] destination = new int[1][2];
				destination[0][0] = i;
				destination[0][1] = j;
				counter++;
				Node newNode = new Node(nextState,state,state.State.Board[i+1][j],source,destination,0);
				nextNodes.add(newNode);
					
				
			}
			// if the column isn't 2 we can go right with the "_"
			if (j < maxJ && !state.State.Board[i][j+1].equals("_") && !state.State.Board[i][j+1].equals("X")) { 
				
				Game nextState =  new Game(state.State.Board, isSmall);
				nextState.Board[i][j] = nextState.Board[i][j+1];
				nextState.Board[i][j+1] = "_";
				int[][] source = new int[1][2];
				source[0][0] = i;
				source[0][1] = j + 1;
				int[][] destination = new int[1][2];
				destination[0][0] = i;
				destination[0][1] = j;
				counter++;		
				Node newNode = new Node(nextState,state,state.State.Board[i][j+1],source,destination,0);
				nextNodes.add(newNode);
				
			}
			// if the column isn't 0 we can go left with the "_"
						
			if (j > 0 && !state.State.Board[i][j-1].equals("_") && !state.State.Board[i][j-1].equals("X")) { 
							
				Game nextState =  new Game(state.State.Board , isSmall);
				nextState.Board[i][j] = nextState.Board[i][j-1];
				nextState.Board[i][j-1] = "_";
				int[][] source = new int[1][2];
				source[0][0] = i;
				source[0][1] = j-1;
				int[][] destination = new int[1][2];
				destination[0][0] = i;
				destination[0][1] = j;
				counter++;
				Node newNode = new Node(nextState,state,state.State.Board[i][j-1],source,destination,0);
				nextNodes.add(newNode);				
				}
		}	
		return nextNodes;
	}
	// calculate the cost of the solution
	  private static int Cost(String path) {
			int cost = 0;
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
	  // checking that there is no identical node in the hash table
	public boolean isUniqe(String[][] someBoard,HashTable hash) {	
		
		int index = hash.HashFunc(someBoard);					
		for (int i = 0; i < hash.Array.get(index).size(); i++) {		
			if(hash.Array.get(index).get(i).Equals(someBoard)) 	{
				return false;
			}
		}
		return true;
	}
}
