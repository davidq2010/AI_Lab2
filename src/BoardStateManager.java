import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.lang.Math;

public class BoardStateManager
{	// HashMap storing value of pieces
	private HashMap<String, Float> pieceVal;
	// HashMap for a1 - h8 coords to numerical coordinates
	//private HashMap<String, Integer[]> strToCoord;

	// HashMap for numerical to a1-h8 coords
	//private HashMap<Integer[], String> coordToStr;
	// HashMap for Storing valid moves of current team
	private HashMap<String, ArrayList<Integer[]>> validMoves;

	// Hashmap to store current chess piece positions 
	private HashMap<String, String[]> currPos;

	private Integer blackKing[];
	private Integer whiteKing[];

	public BoardStateManager() 
	{
		this.pieceVal = new HashMap<>() {{
			put("Q", 9.0),
			put("q", 9.0),
			put("R", 5.0),
			put("r", 5.0),
			put("B", 3.0),
			put("b", 3.0),
			put("N", 3.0),
			put("n", 3.0),
			put("P", 1.0),
			put("p", 1.0),
			put("_", 0.0)
		}};

		this.strToCoord = computeStrToNumCoord()

	}

	public float computeScore(ArrayList<ArrayList<String>> board) 
	{
		float score = 0;

		for(int i = 0; i < board.size(); i++)
		{
			for(int j = 0; j < board.get(0).size(); j++)
			{
				score += pieceVal.get(board.get(i).get(j));
			}
		}

		return score;
	}

	public void printBoard(ArrayList<ArrayList<String>> board) {
        for (int i = 0; i < board.size(); i++) {
            for (int j = 0; j < board.get(0).size(); j++) {
                System.out.print(board.get(i).get(j) + " ");
            }
            System.out.println();
        }
    }


    public HashMap<String, ArrayList<Integer[]>> computeValidMoves(ArrayList<ArrayList<String>> board, String currTeam)
	{
		Hashmap<String, ArrayList<Integer[]>> viableMoves = new Hashmap<>();
		piece =  new Piece(); // need to import piece class
		Integer currKing[] = new Integer[2];

		// List to temporarily hold king moves before placing in hashmap 
		ArrayList<Integer[]> kingMovesTemp;
		// Array holding type and str position of piece putting king in check
		String checkOne[] = new String[2];
		String checkTwo[] = new Sting[2];

		//determine team
		if(currTeam == "white")
    	{
    		currKing[0] = whiteKing[0];
    		currKing[1] = whiteKing[1];

    	}
    	else
    	{
    		currKing[0] = blackKing[0];
    		currKing[1] = blackKing[1];
    	}

    	// Check if King is in Check 
    	// check if any sliding piece has it in check
    	ArrayList<String[]> checkPieces = checkChecker(currKing[0], currKing[1]);
    	checkOne = checkPieces.get(0);
    	checkTwo = checkPieces.get(1); // might not need this 
		// check how many checks the king is in
		// if none, generate viable moves 
    		
    	Integer dir[] = new Integer[2]; // gets dir from AL
    	int newX;	// x coord of possible move
    	int newY;	// y coord of possible move
    	Integer newMove[]; // holds viable move 
    	for(int i = 0; i < piece.qkDir.size(); i++)
    	{
    		dir = piece.qkDir.get(i);
    		newX = currKing[0] + dir[0];
    		newY = currKing[1] + dir[1];
    		if(isControlled(board, newX, newY) == false) // if coord not controlled, add move as viable move for king
    		{
    			newMove[0] = newX;
    			newMove[1] = newY;
    			kingMovesTemp.add(newMove);

    		} 

    	}


        if(checkPieces.size() == 1 && checkOne != "none")// 1 check on king 
    	{
    		if(checkOne[0] == "N")
    		{ 
    			//HANDLE FOR KNIGHT
    		}
    		else if(checkOne[0] == "Q")
    		{
    			// HANDLE FOR QUEEN
    		}
    		else if(checkOne[0] == "R")
    		{
    			// HANDLE FOR ROOK
    		}
    		else if(checkOne[0] == "B")
    		{
    			// HANDLE FOR BISHOP
    		}
    		else if(checkOne[0] == "P")
    		{
    			// HANDLE FOR PAWN 
    		}

    	}else if(checkPieces.size() == 2)  // 2 checks on king so only valid moved are viable king moves
    	{
    		viableMoves.put("K", kingMovesTemp);
    		return viableMoves;
    	}


    }

    // Helper method to see if current king is in check
    // returns an array list of types abd positions of pieces putting king in check 
    // returns arraylist of size 1, with "none" in arraylist.get(0)[0]
    public ArrayList<String[]> checkChecker(int x, int y)
    {
    	ArrayList<String[]> checkPieces = new ArrayList<String[]>();

    	// Check sliding pieces

    	// Check Knights


    	return checkPieces;
    }

    // Helper Method determins if square is controlled by something 
    public boolean isControlled(ArrayList<ArrayList<String>> board, int x, int y, String adversaryTeam)
    {
    	//TO-DO00
    	//boolean  controlled = false;
    	if(board.get(x).get(y) != "_")
    	{
    		return true;
    	}
    	
    	// Sliding pieces
    	Integer dir[] = new Integer[2];
    	int offsetX;
    	int offsetY;
    	int tempX;
    	int tempY;
    	String grid;
    	for(int i = 0; i < board.size() - x; i++)
    	{
    		for(k = 0; k < board.get(0).size - y; k++)
    		{
	    		for(int j = 0; j < piece.qkDir.size(); j++)
	    		{
	    			dir = piece.qkDir.get(j);
	    			offsetX = dir[0] * (i+1);
	    			offsetY = dir[1] * (j+1);
	    			tempX = x + offsetX;
	    			tempY = y + offsetY;
	    			grid = board.get(tempX).get(tempY);

	    			if(grid != "_")
	    			{
	    				if((grid == "P" && adversaryTeam == "white") || (grid == "p" && adversaryTeam = "black"))
	    				{
	    					if(Math.abs(offsetY) - Math.abs(offsetX) == 0)
	    					{
	    						return true;
	    					}
	    				}

	    				if((grid == "N" && adversaryTeam == "white") || (grid == "n" && adversaryTeam = "black"))
	    				{
	    					if(Math.abs(offsetY) - Math.abs(offsetX) != 0)
	    					{
	    						return true;
	    					}
	    				}

	    				if((grid == "Q" && adversaryTeam == "white") || (grid == "q" && adversaryTeam = "black"))
	    				{
	    						return true;
	    					
	    				}


	    			}
	    		}
	    	}
    		
    	}
    }

/* No more mapping 

    public HashMap<String,Integer[]> computeStrToNumCoord()
    {
    		HashMap<String, Integer[]> mapping = new HashMap<>() {{
			put("a1", new Integer[] = {1,1}),
			put("a2", new Integer[] = {1,2}),
			put("a3", new Integer[] = {1,3}),
			put("a4", new Integer[] = {1,4}),
			put("a5", new Integer[] = {1,5}),
			put("a6", new Integer[] = {1,6}),
			put("a7", new Integer[] = {1,7}),
			put("a8", new Integer[] = {1,8}),
			put("b1", new Integer[] = {2,1}),
			put("b2", new Integer[] = {2,2}),
			put("b3", new Integer[] = {2,3}),
			put("b4", new Integer[] = {2,4}),
			put("b5", new Integer[] = {2,5}),
			put("b6", new Integer[] = {2,6}),
			put("b7", new Integer[] = {2,7}),
			put("b8", new Integer[] = {2,8}),
			put("c1", new Integer[] = {3,1}),
			put("c2", new Integer[] = {3,2}),
			put("c3", new Integer[] = {3,3}),
			put("c4", new Integer[] = {3,4}),
			put("c5", new Integer[] = {3,5}),
			put("c6", new Integer[] = {3,6}),
			put("c7", new Integer[] = {3,7}),
			put("c8", new Integer[] = {3,8}),
			put("d1", new Integer[] = {4,1}),
			put("d2", new Integer[] = {4,2}),
			put("d3", new Integer[] = {4,3}),
			put("d4", new Integer[] = {4,4}),
			put("d5", new Integer[] = {4,5}),
			put("d6", new Integer[] = {4,6}),
			put("d7", new Integer[] = {4,7}),
			put("d8", new Integer[] = {4,8}),
			put("e1", new Integer[] = {5,1}),
			put("e2", new Integer[] = {5,2}),
			put("e3", new Integer[] = {5,3}),
			put("e4", new Integer[] = {5,4}),
			put("e5", new Integer[] = {5,5}),
			put("e6", new Integer[] = {5,6}),
			put("e7", new Integer[] = {5,7}),
			put("e8", new Integer[] = {5,8}),
			put("f1", new Integer[] = {6,1}),
			put("f2", new Integer[] = {6,2}),
			put("f3", new Integer[] = {6,3}),
			put("f4", new Integer[] = {6,4}),
			put("f5", new Integer[] = {6,5}),
			put("f6", new Integer[] = {6,6}),
			put("f7", new Integer[] = {6,7}),
			put("f8", new Integer[] = {6,8}),
			put("g1", new Integer[] = {7,1}),
			put("g2", new Integer[] = {7,2}),
			put("g3", new Integer[] = {7,3}),
			put("g4", new Integer[] = {7,4}),
			put("g5", new Integer[] = {7,5}),
			put("g6", new Integer[] = {7,6}),
			put("g7", new Integer[] = {7,7}),
			put("g8", new Integer[] = {7,8}),
			put("h1", new Integer[] = {8,1}),
			put("h2", new Integer[] = {8,2}),
			put("h3", new Integer[] = {8,3}),
			put("h4", new Integer[] = {8,4}),
			put("h5", new Integer[] = {8,5}),
			put("h6", new Integer[] = {8,6}),
			put("h7", new Integer[] = {8,7}),
			put("h8", new Integer[] = {8,8}),

		}};
		return mapping;
		
    }
    */
}