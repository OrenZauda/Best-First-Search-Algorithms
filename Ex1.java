import java.io.BufferedReader;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.Vector;

import javax.print.DocFlavor.URL;

public class Ex1 {

    public static void main(String[] args) throws Exception
    {
    	
    	// reading the file
        FileReader fr = new FileReader("input.txt");
        BufferedReader bufferedReader = new BufferedReader(fr);
        
        // Extracting data from the input
        String Algorithm = bufferedReader.readLine();
        String openList = bufferedReader.readLine();
        String SizeOfBoard = bufferedReader.readLine();       
        String StartBoard = "";
        String GoalState = "";
        boolean open = false;
        if(openList.equals("with open")) {
        	open = true;
        }
        	
       // extracting the goal and the start
        if(SizeOfBoard.equals("small")){
        	StartBoard = bufferedReader.readLine() 
        			   + bufferedReader.readLine()
        			   + bufferedReader.readLine();
        	bufferedReader.readLine();
        	
        	GoalState =   bufferedReader.readLine() 
     			        + bufferedReader.readLine()
     			        + bufferedReader.readLine();
        	
        }
        else {
        	StartBoard =   bufferedReader.readLine() 
     			         + bufferedReader.readLine()
     			         + bufferedReader.readLine()
        	             + bufferedReader.readLine()
		                 + bufferedReader.readLine();
        	
        	bufferedReader.readLine();

        	GoalState =   bufferedReader.readLine() 
  			            + bufferedReader.readLine()
  			            + bufferedReader.readLine()
     	                + bufferedReader.readLine()
		                + bufferedReader.readLine();
        }
        bufferedReader.close();
        // insert the starting state into 2d String array 
        String[][] boardStartingPoint = new String[5][5];
        String[][] boardGoal = new String[5][5];

       
        if(SizeOfBoard.equals("small")) {
        	 for(int i = 0 ; i < 5; i ++) {
             	for (int j = 0; j< 5; j++) {
             		boardStartingPoint[i][j] = "X";
             		boardGoal[i][j] = "X";
             		}
             	}
        }	
        boolean isSmall = false;
        if(SizeOfBoard.equals("small")) {
        	isSmall = true;
        }
        // integers to iterate the input
        int c1 = 0;
        int c2 = 0;
        for(int i = 0; i<5; i++) {
        	for(int j = 0; j<5; j++) {
        		if(isSmall&& j ==3) {
        			j = 5;
        			continue;
        		}
        		if(c1>=StartBoard.length()){
        			break;
        		}	
        		// extract start state
        		if(StartBoard.charAt(c1)!=','){
        			boardStartingPoint[i][j] = Character.toString(StartBoard.charAt(c1++));
  			 	}
        		else {
        			c1++;
        			boardStartingPoint[i][j] = Character.toString(StartBoard.charAt(c1++));
        		}
        		// extract Goal state
        		if(GoalState.charAt(c2)!=','){
        			boardGoal[i][j] = Character.toString(GoalState.charAt(c2++));
  			 	}
        		else {
        			c2++;
        			boardGoal[i][j] = Character.toString(GoalState.charAt(c2++));
        		}
        	}
		
        }


        // prepare the parameters for the algo'
        Game start =  new Game(boardStartingPoint, isSmall);
    	Node startNode = new Node(start);
    	Game goal =  new Game(boardGoal, isSmall);
    	Node goalNode = new Node(goal);
    	Vector<Node> VectorGoals = new Vector<Node>();
    	VectorGoals.add(goalNode);
    	String result = "";


        if(Algorithm.equals("BFS")) {
           
        	BFS bfs = new BFS();
        	System.out.println( result = bfs.BreadthFirstSearch(startNode, VectorGoals, isSmall, open));
        }
        if(Algorithm.equals("DFID")) {
        	DFID dfid = new DFID();
        	result = dfid.DepthFirstIterativeDeepening(startNode, VectorGoals, isSmall, open);
        }
        if(Algorithm.equals("DFBnB")) {
        	Instant startTime = Instant.now();
        	System.out.println("");
        	startNode.print();
        	System.out.println("");
        	goalNode.print();
        	System.out.println("");
        	DFBnB dfbnb = new DFBnB();
        	String result2[] = dfbnb.DepthFirstBranchandBound(startNode, VectorGoals, isSmall, open);
        	Instant finish = Instant.now();
	        double timeElapsed = Duration.between(startTime, finish).toMillis();  //in millis
	        timeElapsed = timeElapsed/1000;
	        int cost = Cost(result);
	        
	        if(result2[0].equals("")) {
	        	result =  "no path" +'\n'+ "Num: " + result2[1] +'\n' + "Cost: inf" + '\n' +timeElapsed;
	    	}
	        result2[0] = result2[0].substring(2);
	        result = result2[0] + '\n' + "Num: " + result2[1] + '\n' + "Cost: "+ cost +'\n' + 
	        		timeElapsed;
	        System.out.println(result);
	        
        }
        if(Algorithm.equals("A*")) {
        	Astar astar = new Astar();
        	result = astar.A(startNode, VectorGoals, isSmall, open);
        	System.out.println(result);
        }
        if(Algorithm.equals("IDA*")) {
        	IDA ida = new IDA();
        	result = ida.IterativeDeepeningA(startNode, VectorGoals, isSmall, open);
        	System.out.println(result);
        	
        }
        //write to file
        try {
            FileWriter myWriter = new FileWriter("output.txt");
            myWriter.write(result + '\n');
            
            myWriter.close();
          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
        }
        // calculate the cost (for dfbnb)
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
    

}
