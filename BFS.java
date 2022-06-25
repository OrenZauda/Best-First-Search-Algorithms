import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

public class BFS {
	boolean issmall;
	int counter = 0;
	int cost = 0;
	public BFS() {
		
	}
	
	public String BreadthFirstSearch(Node start, Vector<Node> Goals, boolean isSmall, boolean open){
		// check the time
		Instant startTime = Instant.now();
		issmall=isSmall;

        // hashtable for loop avoidance
		HashTable OpenList = new HashTable(isSmall);
        HashTable closedList = new HashTable(isSmall);
        // vector as the stack
        Vector<Node> openlist = new Vector<Node>();
        OpenList.add(start);
        openlist.add(start);
        while (!openlist.isEmpty()) 
        {
        	// printing open list content
        	if(open) {
        		for (Node n: openlist) {
        			OpenList.print();
        		}
        	}
        	Node n = openlist.firstElement();
        	openlist.remove(0);
        	//generate children of a node
         	Vector<Node> Children = ExpandNode(n, closedList,OpenList,isSmall);
         	
        	
         	for (Node Child : Children) 
        	{ 
        		
        		for (Node Goal : Goals) 
            	{ 
        			// if the goal is find save the path
        			if (Child.Equals(Goal.State.Board)){
        				int costOFsolution = Cost(Child.path());
        				String path = Child.path();
        				path = path.substring(0, path.length()-2);
        				Instant finish = Instant.now();

        		         double timeElapsed = Duration.between(startTime, finish).toMillis();  //in millis
        		         timeElapsed = timeElapsed/1000;
        				return path + '\n' + "Num: " + counter+ '\n' + "Cost: " +costOFsolution + '\n' + timeElapsed;
        				}
        			else {
        				openlist.add(Child);
        			}
            	}
            } 
        	closedList.add(n);
        }
        // measure the time and return it
        Instant finish = Instant.now();
        double timeElapsed = Duration.between(startTime, finish).toMillis();  //in millis
        timeElapsed = timeElapsed/1000;
        return "no path" +'\n'+ "Num: " + counter +'\n' + "Cost: inf" + '\n' +timeElapsed;

	}
	
	//for calculate the cost from the path
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
	
	// helps expandNode
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
	// generating children of node
	public Vector<Node> ExpandNode(Node state, HashTable closedList, HashTable OpenList, boolean isSmall){
		
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
			if (i > 0 && !state.State.Board[i-1][j].equals("_")&&!state.State.Board[i-1][j].equals("X")) { 
				
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
			
				if (isUniqe(nextState.Board, closedList, OpenList)) {
					Node newNode = new Node(nextState,state,state.State.Board[i-1][j],source,destination,0);
					nextNodes.add(newNode);
					
				}
				
			}
			
			// if the line isn't 2 we can go up with the "_"
			if (i < maxI && !state.State.Board[i+1][j].equals("_")&&!state.State.Board[i+1][j].equals("X")) { 
				
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
				
				if (isUniqe(nextState.Board, closedList, OpenList)) {
					Node newNode = new Node(nextState,state,state.State.Board[i+1][j],source,destination,0);
					nextNodes.add(newNode);
					
				}
			}
			// if the column isn't 2 we can go right with the "_"
			if (j < maxJ && !state.State.Board[i][j+1].equals("_")&&!state.State.Board[i][j+1].equals("X")) { 
				
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
				if (isUniqe(nextState.Board,closedList, OpenList)) {
					Node newNode = new Node(nextState,state,state.State.Board[i][j+1],source,destination,0);
					nextNodes.add(newNode);
				}
			}
			// if the column isn't 0 we can go left with the "_"
						
			if (j > 0 && !state.State.Board[i][j-1].equals("_")&&!state.State.Board[i][j-1].equals("X")) { 
							
				Game nextState =  new Game(state.State.Board, isSmall);
				nextState.Board[i][j] = nextState.Board[i][j-1];
				nextState.Board[i][j-1] = "_";
				int[][] source = new int[1][2];
				source[0][0] = i;
				source[0][1] = j-1;
				int[][] destination = new int[1][2];
				destination[0][0] = i;
				destination[0][1] = j;
				counter++;

				if (isUniqe(nextState.Board,closedList,OpenList)) {
					Node newNode = new Node(nextState,state,state.State.Board[i][j-1],source,destination,0);
	
					nextNodes.add(newNode);
				}
				}

			
		}
		
		return nextNodes;
	}
	
	// to avoid loops and nodes we allready expanded
	public boolean isUniqe(String[][] someBoard,HashTable closedList, HashTable OpenList) {
		
		int index = closedList.HashFunc(someBoard);
					
		for (int i = 0; i < closedList.Array.get(index).size(); i++) {
			
			if(closedList.Array.get(index).get(i).Equals(someBoard)) {
				return false;
			}
		}
		
		for (int i = 0; i < OpenList.Array.get(index).size(); i++) {
			
			if(OpenList.Array.get(index).get(i).Equals(someBoard)) {
				return false;
			}
		}

		return true;
	}
}
