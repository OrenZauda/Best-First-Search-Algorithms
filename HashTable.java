import java.util.Vector;

public class HashTable{
	boolean issmall;
	// buckets for the hash
	public Vector<Node> array0 = new Vector<Node>();
	public Vector<Node> array1 = new Vector<Node>();
	public Vector<Node> array2 = new Vector<Node>();
	public Vector<Node> array3 = new Vector<Node>();
	public Vector<Node> array4 = new Vector<Node>();
	public Vector<Node> array5 = new Vector<Node>();
	public Vector<Node> array6 = new Vector<Node>();
	public Vector<Node> array7 = new Vector<Node>();
	public Vector<Node> array8 = new Vector<Node>();

	public Vector<Vector<Node>> Array = new Vector <Vector<Node>>();;
	
	public HashTable(boolean isSmall){
		issmall = isSmall;
		Array.add(array0);
		Array.add(array1);
		Array.add(array2);
		Array.add(array3);
		Array.add(array4);
		Array.add(array5);
		Array.add(array6);
		Array.add(array7);
		Array.add(array8);
	}
	// 9 blanks
	
	
	
	//lower bound
	
	// 1   2   3   4   5   6   7    8     9
	//00   01  10  11  21  12  22  32     23
	// 0 + 1 + 1+ 2 + 3 +  3   + 4 + 5 +5 
	//       2 +  2 +    6 +   4 +    10 = 24
	
	
	// function based on the location of the blanks.
	public int HashFunc(String [][] Board) {
	
		int index = 0;
		
		for(int i = 0; i<Board.length; i ++) {
			for (int j = 0; j<Board[0].length; j++) {
				if(Board[i][j].equals("_")) {
					index = index + i + j;	
					}
				}
			}
		
		if (issmall) {
			return (index - 2);
		}
		
		else {
			index = index - 17;
			
			double axuliary = 38/8;
			return (int)Math.floor(index/axuliary);
		}
	}
	// to add node to the hash
	public void add(Node n) {
		int index = HashFunc(n.State.Board);
		
		Array.get(index).add(n);
	}
	// check if it is empty
	public boolean isEmpty() {
		
		for(int i = 0; i<9; i++) {
			if(!Array.get(i).isEmpty()) {
				return false;
			} 
		}

		return true;
	}
	// remove the first from hash table (not needed)
	public Node poll() {
		
		for(int i = 0; i<9; i++) {
			if(!Array.get(i).isEmpty()) {
				 Node n = Array.get(i).get(Array.get(i).size()-1);
				 Array.get(i).remove(Array.get(i).size()-1);
				 return n;
			} 
		}
		return null;
		
	}
	// remove node from the hash
	public void remove(Node n, int index) {
			
	Vector<Node> array = Array.get(index);
		for (int i =0; i<array.size(); i++) {
			Node a = array.get(i);
			if (a.equals(n)) {
				array.remove(i);
			}
			
		}
		
	}
	// print the content of the hash.
	public void print() {
		// TODO Auto-generated method stub
		for (int i =0; i <9; i++) {
			
			for(int j=0; j< Array.get(i).size();j++) {
				Array.get(i).get(j).print();
				System.out.println("");
			}
		}
	}
	
}