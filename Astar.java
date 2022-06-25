import java.time.Duration;
import java.time.Instant;
import java.util.PriorityQueue;
import java.util.Vector;

public class Astar {
	int counter = 0;
	boolean issmall;
	HeuristicFunc H;
	public Astar() {
		
	}
	public String A (Node start, Vector<Node> Goals, boolean isSmall, boolean open){
		//measure elapsed time for execution
		Instant startTime = Instant.now();
		issmall = isSmall;
		// HeuristicFunc to calculate the cost
		H = new HeuristicFunc(Goals);
		// open list and close lists to avoid repeating nodes
		HashTable OpenList = new HashTable(isSmall);
        HashTable closedList = new HashTable(isSmall);
        // priority queue for the children
        PriorityQueue<Node> openlist = new PriorityQueue<>();

        OpenList.add(start);
        openlist.add(start);
        

        while (!openlist.isEmpty()) {
        	//print open list content
        	if(open) {
        		OpenList.print();
        	}
        	Node n = openlist.poll();
        	
        	for (Node Goal : Goals) 
        	{ 
        		// return the path when the goal found
    			if (n.Equals(Goal.State.Board)){
    				Instant finish = Instant.now();
    				int costOFsolution = Cost(n.path());
    				String path = n.path();
    				path = path.substring(0, path.length()-2);
    				double timeElapsed = Duration.between(startTime, finish).toMillis();  //in millis
   		         	timeElapsed = timeElapsed/1000;
   		         	return path + '\n' + "Num: " + counter+ '\n' + "Cost: " +costOFsolution + '\n' + timeElapsed;
    				}	
        	}
        	closedList.add(n);
        	//generate children and inserting to open list if needed
        	Vector<Node> Children = ExpandNode(n, closedList,OpenList, openlist, isSmall);

        	
        }
        
        Instant finish = Instant.now();
        double timeElapsed = Duration.between(startTime, finish).toMillis();  //in millis
        timeElapsed = timeElapsed/1000;
		return "no path" +'\n'+ "Num: " + counter +'\n' + "Cost: inf" + '\n' +timeElapsed;
	}
	
		// to calculate the cost of the path
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
		// help expand node
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
	// generate nodes children
	public Vector<Node> ExpandNode(Node state, HashTable closedList, HashTable OpenList, PriorityQueue<Node> openlist, boolean isSmall){
		
		
		int[][] IndexesOfBlank = findIndexesOfBlank(state.State);
		int maxI = 4;
		int maxJ = 4;
		if (issmall) {
			maxI = 2;
			maxJ = 3;
		}
		// Vector to store all the children nodes
		Vector<Node> nextNodes = new Vector<Node>();
		
		for (int s = 0; s < IndexesOfBlank.length; s++) 
		{
			// extract location of "_"
			int i = IndexesOfBlank[s][0];
			int j = IndexesOfBlank[s][1];
			
			// if the line isn't 0 we can go down with the "_"
			if (i > 0 && !state.State.Board[i-1][j].equals("_")&& !state.State.Board[i-1][j].equals("X")) { 
				
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
				int cost= H.CalcValue(nextState.Board); 
				if (isUniqeinOpenList(nextState.Board, OpenList) && isUniqeinClosedList(nextState.Board, closedList)) {
					Node newNode = new Node(nextState,state,state.State.Board[i-1][j],source,destination,cost);
					nextNodes.add(newNode);
					OpenList.add(newNode);
					openlist.add(newNode);

				}
				
				else if (!isUniqeinOpenList(nextState.Board, OpenList)&&isUniqeinClosedList(nextState.Board, closedList) ) {
					
					int index = OpenList.HashFunc(nextState.Board);
					int costOFPreviousNode = 0;
					Node previous = new Node (nextState);
					int insideIndex = 0;
					for (int v = 0; v < OpenList.Array.get(index).size(); v++) {
						
						previous = OpenList.Array.get(index).get(v);
						if (previous.Equals(nextState.Board)){
							costOFPreviousNode = previous.totalCost;
							insideIndex = v;
						}
					
					}
					
					if(cost < costOFPreviousNode ) {
						openlist.remove(previous);
						OpenList.Array.get(index).remove(insideIndex);
						Node newNode = new Node(nextState,state,state.State.Board[i-1][j],source,destination,cost);
						OpenList.add(newNode);
						openlist.add(newNode);
						nextNodes.add(newNode);


					}
				}
				
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
				int cost= H.CalcValue(nextState.Board);
				if (isUniqeinOpenList(nextState.Board, OpenList) && isUniqeinClosedList(nextState.Board, closedList)) {
					Node newNode = new Node(nextState,state,state.State.Board[i+1][j],source,destination,cost);
					OpenList.add(newNode);
					openlist.add(newNode);
				}
				else if (!isUniqeinOpenList(nextState.Board, OpenList)&&isUniqeinClosedList(nextState.Board, closedList)) {
					
					int index = OpenList.HashFunc(nextState.Board);
					int costOFPreviousNode = 0;
					Node previous = new Node(nextState);
					int insideIndex = 0;
					for (int v = 0; v < OpenList.Array.get(index).size(); v++) {
						
						previous = OpenList.Array.get(index).get(v);
						if (previous.Equals(nextState.Board)){
							costOFPreviousNode = previous.totalCost;
							insideIndex = v;
						}
					
					}
					if(cost < costOFPreviousNode ) {
						openlist.remove(previous);
						OpenList.Array.get(index).remove(insideIndex);
						Node newNode = new Node(nextState,state,state.State.Board[i+1][j],source,destination,cost);
						OpenList.add(newNode);
						openlist.add(newNode);
						nextNodes.add(newNode);

					}
				}
			}
			// if the column isn't 2 we can go right with the "_"
			if (j < maxJ && !state.State.Board[i][j+1].equals("_")&& !state.State.Board[i][j+1].equals("X")) { 
				
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
				int cost= H.CalcValue(nextState.Board);
				if (isUniqeinOpenList(nextState.Board, OpenList) && isUniqeinClosedList(nextState.Board, closedList)) {
					Node newNode = new Node(nextState,state,state.State.Board[i][j+1],source,destination,cost);
					nextNodes.add(newNode);
					OpenList.add(newNode);
					openlist.add(newNode);

				}
				else if (!isUniqeinOpenList(nextState.Board, OpenList) && isUniqeinClosedList(nextState.Board, closedList)) {
					
					int index = OpenList.HashFunc(nextState.Board);
					int costOFPreviousNode = 0;
					Node previous = new Node(nextState);
					int insideIndex = 0;
					for (int v = 0; v < OpenList.Array.get(index).size(); v++) {
						
						previous = OpenList.Array.get(index).get(v);
						if (previous.Equals(nextState.Board)){
							costOFPreviousNode = previous.totalCost;
							insideIndex = v;
						}
					
					}
					if(cost < costOFPreviousNode ) {
						openlist.remove(previous);
						OpenList.Array.get(index).remove(insideIndex);
						Node newNode = new Node(nextState,state,state.State.Board[i][j+1],source,destination,cost);
						OpenList.add(newNode);
						openlist.add(newNode);
						nextNodes.add(newNode);

					}
				}
			}
			// if the column isn't 0 we can go left with the "_"
						
			if (j > 0 && !state.State.Board[i][j-1].equals("_")&& !state.State.Board[i][j-1].equals("X")) { 
							
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
				int cost= H.CalcValue(nextState.Board);
				if (isUniqeinOpenList(nextState.Board, OpenList) && isUniqeinClosedList(nextState.Board, closedList)) {
					Node newNode = new Node(nextState,state,state.State.Board[i][j-1],source,destination,cost);
					nextNodes.add(newNode);
					OpenList.add(newNode);
					openlist.add(newNode);

				}
				else if (!isUniqeinOpenList(nextState.Board, OpenList) && isUniqeinClosedList(nextState.Board, closedList)) {
					
					int index = OpenList.HashFunc(nextState.Board);
					int costOFPreviousNode = 0;
					Node previous = new Node(nextState);
					int insideIndex = 0;
					for (int v = 0; v < OpenList.Array.get(index).size(); v++) {
						
						previous = OpenList.Array.get(index).get(v);
						if (previous.Equals(nextState.Board)){
							costOFPreviousNode = previous.totalCost;
							insideIndex = v;
						}
					
					}
					
					if(cost < costOFPreviousNode ) {
						openlist.remove(previous);
						OpenList.Array.get(index).remove(insideIndex);
						Node newNode = new Node(nextState,state,state.State.Board[i][j-1],source,destination,cost);
						OpenList.add(newNode);
						openlist.add(newNode);
						nextNodes.add(newNode);

					}
				}
				}

			
		}
		
		return nextNodes;
	}
	
	// Uniqueness check in the closed list
	public boolean isUniqeinClosedList(String[][] someBoard,HashTable closedList) {
		
		int index = closedList.HashFunc(someBoard);

		if (closedList.Array.get(index).isEmpty()) {
//			System.out.println("EMPTY");
			return true;
		}
		
	
		
		for (int i = 0; i < closedList.Array.get(index).size(); i++) {
			
			String[][] state = closedList.Array.get(index).get(i).State.Board;
			
			for (int j = 0; j < state.length; j++) {
				for(int t = 0; t<state[0].length;t++) {
					if (!state[j][t].equals(someBoard[j][t])) {
					return true;
					}
				}
			}
		}
		
		
	
		return false;
	}
	// Uniqueness check in the open list

	public boolean isUniqeinOpenList(String[][] someBoard,HashTable OpenList) {
		
		int index = OpenList.HashFunc(someBoard);

		if (OpenList.Array.get(index).isEmpty()) {
			return true;
		}		
		
		for (int i = 0; i < OpenList.Array.get(index).size(); i++) {
			
			 if(OpenList.Array.get(index).get(i).Equals(someBoard)) {
				 return false;
			 }
		}
			
	
		return true;
	}

}
