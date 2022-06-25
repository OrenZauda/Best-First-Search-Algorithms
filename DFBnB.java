import java.util.Collection;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.Vector;

public class DFBnB {
	boolean issmall;
	int counter = 0;
		
	public DFBnB(){
	}
	
	public String[] DepthFirstBranchandBound(Node start, Vector<Node> Goals, boolean isSmall,  boolean open) {
		
		issmall = isSmall;
		// HeuristicFunc object to calculate the cost
		HeuristicFunc func = new HeuristicFunc(Goals);
		// 1 hash and 2 stacks for storing the nodes
		HashTable hash = new HashTable(isSmall);
		Stack<Node> stack = new Stack<Node>();
		Stack<Node> stackForReversing = new Stack<Node>();
		stack.add(start);
		hash.add(start);
		// variable to store the result and the cost
		String result[] = new String[2];
		result[0] = "";
		int treshold = (int)Double.POSITIVE_INFINITY;
		// two seperate priority queues, for looping and for inserting
		PriorityQueue<Node> OrderedChildrenforlOOPING = new PriorityQueue<>();
		PriorityQueue<Node> OrderedChildrenInsertingToStack = new PriorityQueue<>();

		while(!stack.isEmpty()) {
			if(open) {
				hash.print();
			}
			Node n = stack.pop();
			
			// remove the node if it allready have been expanded
			if (n.out) {
				int index = hash.HashFunc(n.State.Board);
				hash.remove(n,index);
				
			}
			else {
				n.out = true;
				stack.add(n);
				//expand the node
				Vector<Node> Children = ExpandNode(n , func, isSmall);
				
				for(Node Child : Children){
					OrderedChildrenforlOOPING.add(Child);
					OrderedChildrenInsertingToStack.add(Child);
					}
				
				
				for(int i = 0; i<Children.size(); i++) {
					
					Node g = OrderedChildrenforlOOPING.poll();
					int cost = g.totalCost;
						
					// in case of of exceeding the treshold, move forward
					if(cost >= treshold) {
						OrderedChildrenforlOOPING.clear();
						
						OrderedChildrenInsertingToStack.removeIf((Node) -> Node.totalCost>=cost);
						i = Children.size();
					}
					else {
						// retrieving identical node from the hash, if exists
						Node identicalNode = isUniqe(g.State.Board, hash,func);
						
						// in case that we get back to node on our path, remove it
						if(identicalNode!= null && g.out) {
							
							OrderedChildrenInsertingToStack.remove(g);
						}
						// in case we get a node in our stack
						if(identicalNode!= null && !g.out) {
							
							if (func.CalcValue(identicalNode.State.Board) <= cost) {
								
								OrderedChildrenInsertingToStack.remove(g);
							}
							else {
								int index = hash.HashFunc(identicalNode.State.Board);
								hash.remove(identicalNode,index);
								stack.remove(identicalNode);
							}
						}//return the path when the goal is found
						for(Node Goal: Goals) {
							
							if(Goal.Equals(g.State.Board)) {
								
								Stack<Node> forPath = (Stack<Node>) stack.clone();
								forPath.add(g);
								result[0] = path(forPath,g);
								treshold = g.totalCost; 
								
								
									System.out.println("goal found");
									System.out.println("new treshold = "+ treshold);
								
								OrderedChildrenInsertingToStack.removeIf((Node) -> Node.totalCost >= cost);
								OrderedChildrenforlOOPING.removeIf((Node) -> Node.totalCost >= cost);
								i = Children.size();
							}
						}
						
					}
				}
				
				// inserting the children in reverse order
				stackForReversing = reversestack(OrderedChildrenInsertingToStack);

				int size = stackForReversing.size();
		    	for(int i = 0; i < size; i++) {
		    		
		    		Node Child = stackForReversing.pop();

		    		stack.add(Child);
		    		hash.add(Child);
				}
				
			}
		}
		
		result[1] = Integer.toString(counter);
		
		return result;
		
	}
	
	//  PRIORITY Q   Stack1    STACK2
	//    1           3          1
	//	  2   -->     2    -->   2
	//    3           1          3
	// to reverse the stack
	private Stack<Node> reversestack(PriorityQueue<Node> s) {
 	 	

		 Stack<Node> A = new Stack<Node>();
	     
	     while (!s.isEmpty()) {
   	
	    	 A.push(s.poll());    	 
	     }   
		return A;
	}
	// to return the path from the begining to the end.
	private String path(Stack<Node> openList, Node g) {
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
		path = path +"--("+ Integer.toString(g.Source[0][0]+ 1) + "," + Integer.toString(g.Source[0][1]+ 1) + "):" + g.Color 
				+ ":(" + Integer.toString(g.Destination[0][0] + 1) + "," + Integer.toString(g.Destination[0][1] + 1) + ")";
		return path;
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
	// generate the children of the node
	public Vector<Node> ExpandNode(Node state, HeuristicFunc func, boolean isSmall){
		int maxI = 4;
		int maxJ = 4;
		if(issmall) {
			maxI = 2;
			maxJ = 2;
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
				int cost= func.CalcValue(nextState.Board);
				Node newNode = new Node(nextState,state,state.State.Board[i-1][j],source,destination,cost);
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
				int cost= func.CalcValue(nextState.Board);
				Node newNode = new Node(nextState,state,state.State.Board[i+1][j],source,destination,cost);
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
				int cost= func.CalcValue(nextState.Board);
				Node newNode = new Node(nextState,state,state.State.Board[i][j+1],source,destination,cost);
				nextNodes.add(newNode);
				
			}
			
			// if the column isn't 0 we can go left with the "_"
						
			if (j > 0 && !state.State.Board[i][j-1].equals("_") && !state.State.Board[i][j-1].equals("X")) { 
							
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
				int cost= func.CalcValue(nextState.Board);
				Node newNode = new Node(nextState,state,state.State.Board[i][j-1],source,destination,cost);
						
				nextNodes.add(newNode);
				
				}

			
		}
		
		return nextNodes;
	}
	
	//checking if the node is unique
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
