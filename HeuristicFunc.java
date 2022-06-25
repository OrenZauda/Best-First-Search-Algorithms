import java.util.Collections;
import java.util.Vector;

public class HeuristicFunc {
	Vector<Node> Goals = new Vector<Node>();
	int count = 0;
	public HeuristicFunc(Vector<Node> goals) {
		Goals = goals;
	}
	
	public int CalcValue(String[][] board) {
		count++;
//		result storage
		Vector<Integer> results = new Vector<Integer>();
		// variable for calculations
		int costOfMoves = 0;
		// to divide the cost to heavy and light
		int emptyCells = 0;
		int occupiedCells = 0;
		int stepsFromIdenticalCell = 0;
		int s = 0; // complete variable t, ahead, to the number of the steps
		int numOfSteps = 1;
		boolean found = false;
		//iterating any goal 
		for (int g = 0; g < Goals.size(); g++)
        {
			String[][] Goal = Goals.get(g).State.Board;
			// any unequal cell should add cost to the sum
			for (int i = 0; i < Goal.length; i++) {
				for (int j = 0; j < Goal[0].length; j++) {
					found = false;
					numOfSteps = 1;
				
					if (!Goal[i][j].equals(board[i][j]) && !Goal[i][j].equals("_") &&!Goal[i][j].equals("X")) {
						// searching from step 1 to step 9 (because 9 is the maximum in the big game) 
						// for identical node to the cost calculation
						// with all possible directions
						while(numOfSteps < 9 && !found) {
							for (int t = 0; t < numOfSteps; t++) {
								s = numOfSteps - t;
							
								if((i + t) < board.length && (j+s) < board[0].length && board[i+t][j+s].equals(Goal[i][j])
										&& !board[i+t][j+s].equals(Goal[i+t][j+s])) {
									
									emptyCells = countEmotyCellsPP(i,j, t ,s, board);
									occupiedCells = numOfSteps - emptyCells;
									stepsFromIdenticalCell = numOfSteps;
									found = true;
									break;

								}
								if((i + s) < board.length && (j+t) < board[0].length && board[i+s][j+t].equals(Goal[i][j])
										&& !board[i+s][j+t].equals(Goal[i+s][j+t])) {
									
									emptyCells = countEmotyCellsPP(i,j, s ,t, board);
									occupiedCells = numOfSteps - emptyCells;
									stepsFromIdenticalCell = numOfSteps;
									found = true;
									break;

								}
							
								if((i - t) >= 0 && (j+s)<board[0].length && board[i-t][j+s].equals(Goal[i][j])
										&& !board[i-t][j+s].equals(Goal[i-t][j+s])) {
									emptyCells = countEmotyCellsMP(i,j, t ,s, board);
									occupiedCells = numOfSteps - emptyCells;
									stepsFromIdenticalCell = numOfSteps;
									found = true;
									break;

								}
								if((i - s) >= 0 && (j+t)<board[0].length && board[i-s][j+t].equals(Goal[i][j])
										&& !board[i-s][j+t].equals(Goal[i-s][j+t])) {
									emptyCells = countEmotyCellsMP(i,j, s ,t, board);
									occupiedCells = numOfSteps - emptyCells;
									stepsFromIdenticalCell = numOfSteps;
									found = true;
									break;

								}

								if((i + t) < board.length && (j-s) >= 0 && board[i+t][j-s].equals(Goal[i][j])
										&& !board[i+t][j-s].equals(Goal[i+t][j-s])) {
									emptyCells = countEmotyCellsPM(i,j, t ,s, board);
									occupiedCells = numOfSteps - emptyCells;
									stepsFromIdenticalCell = numOfSteps;
									found = true;
									break;

								}

								if((i + s) < board.length && (j-t) >= 0 && board[i+s][j-t].equals(Goal[i][j])
										&& !board[i+s][j-t].equals(Goal[i+s][j-t])) {
									emptyCells = countEmotyCellsPM(i,j, s ,t, board);
									occupiedCells = numOfSteps - emptyCells;
									stepsFromIdenticalCell = numOfSteps;
									found = true;
									break;

								}
							

								if((i - t) >= 0 && (j-s) >= 0  && board[i-t][j-s].equals(Goal[i][j])
										&& !board[i-t][j-s].equals(Goal[i-t][j-s])) {
									emptyCells = countEmotyCellsMM(i,j, t ,s, board);
									occupiedCells = numOfSteps - emptyCells;
									stepsFromIdenticalCell = numOfSteps;
									found = true;
									break;
								}

								if(((i - s) >= 0) && ((j-t) >= 0)  && board[i-s][j-t].equals(Goal[i][j])
										&& !board[i-s][j-t].equals(Goal[i-s][j-t])) {
									emptyCells = countEmotyCellsMM(i,j, s ,t, board);
									occupiedCells = numOfSteps - emptyCells;
									stepsFromIdenticalCell = numOfSteps;
									found = true;
									break;
								}
									
									
							}
							numOfSteps++;
		
						}
					
						if(Goal[i][j].equals("R") || Goal[i][j].equals("Y")) {
							costOfMoves = costOfMoves + emptyCells + occupiedCells*2;

						}
					
						if(Goal[i][j].equals("B")) {
							costOfMoves = costOfMoves +   emptyCells*2 + occupiedCells*4;

						}
					
						if(Goal[i][j].equals("G")) {
							costOfMoves = costOfMoves +   emptyCells*10 + occupiedCells*20 ;

						}
					
					}
					}

				}
				results.add(costOfMoves);

			}
			
		return Collections.min(results);
	}
	// checking how many empty cells where the direction is plus plus
	private int countEmotyCellsPP(int i, int j, int t, int s, String[][] board) {
		int sumOfEmpty = 0;
		for (int z = i; z<=(i+t); z++) {
			for(int w =j ; w<=(j+s); w++ ) {
				if(board[z][w].equals("_")) {
					sumOfEmpty++;
				}
			}
		}
		return sumOfEmpty;
	}
	// checking how many empty cells where the direction is plus minus

	private int countEmotyCellsPM(int i, int j, int t, int s, String[][] board) {
		int sumOfEmpty = 0;
		for (int z = i; z<=(i+t); z++) {
			for(int w =j ; w>=(j-s); w-- ) {
				if(board[z][w].equals("_")) {
					sumOfEmpty++;
				}
			}
		}
		return sumOfEmpty;
	}
	// checking how many empty cells where the direction is minus plus

	private int countEmotyCellsMP(int i, int j, int t, int s, String[][] board) {
		int sumOfEmpty = 0;
		for (int z = i; z>=(i-t); z--) {
			for(int w =j ; w<=(j+s); w++ ) {
				if(board[z][w].equals("_")) {
					sumOfEmpty++;
				}
			}
		}
		return sumOfEmpty;
	}
	// checking how many empty cells where the direction is minus minus

	private int countEmotyCellsMM(int i, int j, int t, int s, String[][] board) {
		int sumOfEmpty = 0;
		for (int z = i; z>=(i-t); z--) {
			for(int w =j ; w>=(j-s); w-- ) {
				if(board[z][w].equals("_")) {
					sumOfEmpty++;
				}
			}
		}
		return sumOfEmpty;
	}
}
