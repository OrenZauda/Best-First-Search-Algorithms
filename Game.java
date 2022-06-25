import java.util.Vector;

public class Game {
	
	boolean isSmall = false;
	public String[][] Board =  new String[5][5];
	
	public Game(String[][] input, boolean issmall) {
		isSmall = issmall;
		if(isSmall) {
		
			for (int i = 0; i < 3; i++) 
			{
				for (int j = 0; j < 3; j++) 
					{
						Board[i][j] = input[i][j];
					}
			}
			for (int i = 0; i < 5; i++) 
			{
				for (int j = 0; j < 5; j++) 
					{
						if(Board[i][j]==null) {
							Board[i][j] = "X";
							}
					}
			}
			

		}
		else {
			
			for (int i = 0; i < 5; i++) 
			{
				for (int j = 0; j < 5; j++) 
					{
						Board[i][j] = input[i][j];
					}
			}
		}
		
				
	}
	

	

}
