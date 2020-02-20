import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.lang.Math;
import java.lang.Character;

public class BoardStateManager
{	// HashMap storing value of pieces
	public static HashMap<Character, Float> pieceVal;
	// HashMap for a1 - h8 coords to numerical coordinates
	//private HashMap<String, int[]> strToCoord;

	public HashMap<String, ArrayList<int[]>> unitDirections;

	// HashMap for numerical to a1-h8 coords
	//private HashMap<int[], String> coordToStr;
	// HashMap for Storing valid moves of current team
	private HashMap<Character, ArrayList<int[]>> validMoves;

	// Hashmap to store current chess piece positions 
	private HashMap<String, int[]> allPiecePos;

	private Integer blackKing[];
	private Integer whiteKing[];

	public BoardStateManager() 
	{
		this.pieceVal = new HashMap<Character, Float>() {{
			put('q', 9.f);
			put('r', 5.f);
			put('b', 3.f);
			put('n', 3.f);
			put('p', 1.f);
			put('_', 0.f);
		}};

		unitDirections = new HashMap<>();

		ArrayList<int[]> diagonal = new ArrayList<>();
		diagonal.add(new int[]{1, 1});
		diagonal.add(new int[]{-1, -1});
		diagonal.add(new int[]{1, -1});
		diagonal.add(new int[]{-1, 1});
		unitDirections.put("diagonal", diagonal);

		ArrayList<int[]> grid = new ArrayList<>();
		grid.add(new int[]{1, 0});
		grid.add(new int[]{-1, 0});
		grid.add(new int[]{0, 1});
		grid.add(new int[]{0, -1});
		unitDirections.put("grid", grid);

		ArrayList<int[]> knight = new ArrayList<>();
		knight.add(new int[]{2, 1});
		knight.add(new int[]{2, -1});
		knight.add(new int[]{1, 2});
		knight.add(new int[]{1, -2});
		knight.add(new int[]{-2, 1});
		knight.add(new int[]{-2, -1});
		knight.add(new int[]{-1, 2});
		knight.add(new int[]{-1, -2});
		unitDirections.put("knight", knight);


		//this.strToCoord = computeStrToNumCoord();
	}

	public HashMap<String, ArrayList<int[]>> getUnitDirections() {
		return unitDirections;
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

    public void printBoard(char[][] board) {
        for (int i = 0; i < ChessAI.BOARD_SIZE; i++) {
            for (int j = 0; j < ChessAI.BOARD_SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    // If a piece is eaten during a move, how do we account for it? if we had a board of strings it would be easier!!
    // in the hashmap of pieces to coords we need support for multiple Pawns, Knights, etc...can't support this w char!
    public ArrayList<char[][]> computeKnightStates(char[][] board, int[] knightPos, String color) {
    	int[] knight1;
    	int[] knight2; 
    	ArrayList<Character[][]> knightStates = new ArrayList<>();
    	char[][] succState1 = board;
    	char[][] succState2 = board;
    	char updatingPiece;

    	if(currColor.equals("white")) {
    		knight1 = allPiecePos.get("K1");
    		knight2 = allPiecePos.get("K2");
    		updatingPiece = 'K';
    	}
    	else{
    		knight1 = allPiecePos.get("k1");
    		knight2 = allPiecePos.get("k2");
    		updatingPiece = 'k';
    	}
    	ArrayList<int[]> knightDir = unitDirections.get("knight")
    	int knightOffsetX;
    	int knightOffsetY;
    
    	for(i = 0; i < knightDir.size(); i++) {
	    		//first knight
    		knightOffsetX = knightDir.get(i)[0];
    		knightOffsetY = knightDir.get(i)[1];
    	
    		if (knight1[0] * knightOffsetX < 0 || knight1[0] * knightOffsetX >= ChessAI.BOARD_SIZE || knight1[1] * knightOffsetY < 0 || knight1[1] * knightOffsetY >= ChessAI.BOARD_SIZE) {
	    		char[][] succState1 = board;
	    		succState1[knight1[0]][knight1[1]] = '_';
	    		succState1[knight1[0] * ][knight1[1] * ] = updatingPiece;
	    		knightStates.add(succState1);
	    		}
	    		// second knight
	    	if (knight2[0] * knightOffsetX < 0 || knight2[0] * knightOffsetX >= ChessAI.BOARD_SIZE || knight2[1] * knightOffsetY < 0 || knight2[1] * knightOffsetY >= ChessAI.BOARD_SIZE) {
	    		char[][] succState2 = board;
	    		succState2[knight2[0]][knight2[1]] = '_';
	    		succState2[knight2[0] * knightDir.get(i)[0]][knight2[1] * knightDir.get(i)[1]] = updatingPiece;
	    		knightStates.add(succState2);
	    	}
    	}
    	return knightStates;
    }

    // 1. Is King in check? (Do this by imagining King is all of the pieces and seeing if any opponent pieces are the first thing we hit.)
    // 2. First, compute valid moves for King (aka moves in which it's not checked)
    // 3. Are we double checked? If so, return list of king moves.
    // 4. If single checked, add options of eat, block to the original list of moves.
    // 5. For eating and blocking, make sure the piece isn't pinned. (Make sure that the piece we're considering moving is not the only piece
    // between the king and sliding piece.)
    public ArrayList<char[][]> computeKingStates(char[][] board, int[] kingPos, String color) {
    	ArrayList<char[][]> allStates = new ArrayList<>();

    	// Compute valid king states
    	// For each king move, make sure it won't be checked
    	System.out.println("!!!!!!!Computing King States!!!!!");
    	nextKingStateHelper(board, allStates, kingPos, color);

    	// Is king checked?
    	// Also compute all the pins in computeCheckPositions. goFurther has to be true here
    	System.out.println("!!!!!!!Positions Checking King And Pins!!!!!");
        ArrayList<ArrayList<int[]>>	checkAndPinPositions = computeCheckAndPinPositions(board, kingPos, color);

    	// If so, is it double checked?
    	if (checkAndPinPositions.get(0).size() > 1) {
    		return allStates;
    	}

    	int[] checkingPos = checkAndPinPositions.get(0).get(0);
    	System.out.println("\tChecking King from (" + checkingPos[0] + ", " + checkingPos[1] + ")");
    	ArrayList<int[]> pinList = checkAndPinPositions.get(1);

    	System.out.println("!!!!!!Tryna Eat!!!!!!!!!");
    	// Generate eating states
    	// Radiate from the checking enemy piece; for each piece that isn't on the pin list, using that piece to eat results in a valid state
    	eatEnemyCheckHelper(board, allStates, checkingPos, pinList, color);

    	// Is it a knight?
    	if (Character.toLowerCase(board[checkingPos[0]][checkingPos[1]]) == 'n') {
    		return allStates;
    	}

    	System.out.println("!!!!!!Tryna Block!!!!!!!!!");
    	// Generate blocking states
    	// Consider all squares between our king and the checking piece. For each of those squares, call IsControlled()
    	System.out.println("StartPos: (" + kingPos[0] + ", " + kingPos[1] + ")");
    	System.out.println("EndPos: (" + checkingPos[0] + ", " + checkingPos[1] + ")");
    	computeBlockingStates(board, allStates, kingPos, checkingPos, pinList, color);

    	return allStates;
    }

    // start and end pos here are exclusive
    void computeBlockingStates(char[][] board, ArrayList<char[][]> states, int[] startPos, int[] endPos, 
    	ArrayList<int[]> pinList, String color) {
    	// Compute "slope" between start and end; guaranteed to either be vert, horiz, diagonal
    	int dRow = endPos[0] - startPos[0];
    	int dCol = endPos[1] - startPos[1];

    	if (dRow != 0) {
    		dRow /= Math.abs(dRow);
    	}
    	if (dCol != 0) {
    		dCol /= Math.abs(dCol);
    	}

    	int[] pos = new int[]{startPos[0] + dRow, startPos[1] + dCol};

    	boolean findPins = false;
    	String mode = "blocking";

    	System.out.println("Iterating til EndPos: (" + endPos[0] + ", " + endPos[1] + ")");

    	while (!Arrays.equals(pos, endPos)) {
    		System.out.println("Blocking at pos (" + pos[0] + ", " + pos[1] + ")");
    		ArrayList<int[]> blockerOptions = isControlled(board, pos, color.equals("black") ? "white" : "black",
    			findPins, mode).get(0);
    		blockerLoop:
    		for (int[] blocker : blockerOptions) {
	    		for (int[] pinner : pinList) {
	    			if (Arrays.equals(blocker, pinner)) {
	    				continue blockerLoop; // Go to next blocker
	    			}
	    		}
	    		// Add blocker to states
	    		states.add(newStateGenerator(board, blocker, pos, board[blocker[0]][blocker[1]]));
    		}

    		pos[0] += dRow;
    		pos[1] += dCol;
    	}
    }

    void eatEnemyCheckHelper(char[][] board, ArrayList<char[][]> states, int[] pos, ArrayList<int[]> pinList, String color) {
    	String mode = "eating";
    	boolean findPins = false;
    	ArrayList<int[]> eaterOptions = isControlled(board, pos, color.equals("black") ? "white" : "black", 
    		findPins, mode).get(0);
    	eaterLoop:
    	for (int[] eater : eaterOptions) {
    		// TODO: pinList could be a HashSet
    		for (int[] pinner : pinList) {
    			if (Arrays.equals(eater, pinner)) {
    				continue eaterLoop; // Go to next eater
    			}
    		}
    		// Add eater to states
    		states.add(newStateGenerator(board, eater, pos, board[eater[0]][eater[1]]));
    	}
    }

    public ArrayList<ArrayList<int[]>> computeCheckAndPinPositions(char[][] board, int[] kingPos, String color) {
    	boolean findPins = true;
    	String mode = "eating";
    	ArrayList<ArrayList<int[]>> controlInfo = isControlled(board, kingPos, color, findPins, mode);
    	return controlInfo;
    }

    public char[][] newStateGenerator(char[][] board, int[] oldPos, int[] newPos, char piece) {
		char[][] newState = new char[ChessAI.BOARD_SIZE][];
		for (int i = 0; i < newState.length; i++) {
			newState[i] = board[i].clone();
		}
		newState[newPos[0]][newPos[1]] = piece;
		newState[oldPos[0]][oldPos[1]] = '_';
		return newState;
    }

    public void nextKingStateHelper(char[][] board, ArrayList<char[][]> states, int[] kingPos, String color) {
    	// Check all grid directions and diagonal directions
    	ArrayList<int[]> dirs = new ArrayList<>();
    	dirs.addAll(unitDirections.get("grid"));
    	dirs.addAll(unitDirections.get("diagonal"));
    	for (int[] dir : dirs) {
    		int[] pos = new int[]{kingPos[0] + dir[0], kingPos[1] + dir[1]};
    		if (inBounds(pos) && noSameTeamPiece(board, pos, color)) {
    			ArrayList<int[]> controlPos = isControlled(board, pos, color, false, "kingMove").get(0);
    			if (controlPos.isEmpty()) {
					states.add(newStateGenerator(board, kingPos, pos, color.equals("black") ? 'K' : 'k'));
					System.out.println("Free Pos For King at (" + pos[0] + ", " + pos[1] + ")");
    			}
    		}
    	}
    }

    public boolean noSameTeamPiece(char[][] board, int[] pos, String color) {
    	if (color.equals("black") && Character.isUpperCase(board[pos[0]][pos[1]])) {
    		return false;	
    	}
    	else if (color.equals("white") && Character.isLowerCase(board[pos[0]][pos[1]])) {
    		return false;
    	}
    	return true;
    }

   	public boolean inBounds(int[] pos) {
		if (pos[0] < 0 || pos[0] >= ChessAI.BOARD_SIZE || pos[1] < 0 || pos[1] >= ChessAI.BOARD_SIZE)
			return false;
		return true;
   	}

    // Helper Method that determines if square is controlled by something 
    // NOTE: We find all the possible pieces that can control currPos of the OPPOSING COLOR
    // SOOOO to find blocking pieces for CURR COLOR, we must pose as the OPPOSING COLOR
    // mode can be kingMove, blocking, eating
    public ArrayList<ArrayList<int[]>> isControlled(char[][] board, int[] currPos, String currColor, boolean goFurther, String mode)
    {
    	ArrayList<int[]> controlledFromPos = new ArrayList<>();
    	ArrayList<int[]> pinList = null;
    	if (goFurther)
    		pinList = new ArrayList<>();

    	for (HashMap.Entry<String, ArrayList<int[]>> entry : unitDirections.entrySet())
    	{
			// Iterate from pos in unitDirection until hit some piece. 
			// The piece can be our piece or opponent piece. If opponent piece, we go on to next unit direction.
			// If our piece, if we're not trying to fill the pin list, we stop. Otherwise, continue until we hit another piece.
			// If the second piece we hit is our piece or edge of board, remove from pin list and stop.
			// If second piece we hit is opponent piece, go on to next unit direction.
			ArrayList<int[]> directions = entry.getValue();
			String pieceType = entry.getKey();

			System.out.println();
    		System.out.println("PieceType: " + pieceType);

			ArrayList<Character> possiblePieces = new ArrayList<>();
			if (pieceType.equals("diagonal")) {
				possiblePieces.add('q');
				possiblePieces.add('b');
				if (mode.equals("kingMove")) {
					possiblePieces.add('p');
					possiblePieces.add('k');
				}
				else if (mode.equals("eating")) {
					possiblePieces.add('p');
					// Commented out b/c we could have this state appear as duplicate. If king can escape check by eating, that's the same as 
					// moving to that square, which would have already been computed as a move.
					//possiblePieces.add('k'); 
				}
			}
			else if (pieceType.equals("grid")) {
				possiblePieces.add('q');
				possiblePieces.add('r');
				if (mode.equals("blocking")) {
					possiblePieces.add('p');
				}
				// No eating mode for same reason as above
				else if (mode.equals("kingMove")) {
					possiblePieces.add('k');
				}
			}
			else if (pieceType.equals("knight")) {
				possiblePieces.add('n');
			}

			// Remember, possiblePieces are of the OPPOSITE team
			if (currColor.equals("white")) {
				for (int i = 0; i < possiblePieces.size(); i++)
					possiblePieces.set(i, Character.toUpperCase(possiblePieces.get(i)));
			}

			System.out.print("Possible Pieces:");
			for (char piece : possiblePieces) {
				System.out.println(piece);
			}

			// Pawns can move twice if we're two away from pawn starting row
			// Remember, when blocking we pose as the opponent. So it's really that white can be at row 3 and
			// black can be at row 4.
			int pawnMoveMax = 1;
			if (pieceType.equals("grid") && mode.equals("blocking")) {
				if (currColor.equals("black") && currPos[0] == 3) {
					pawnMoveMax++;
				}
				else if (currColor.equals("white") && currPos[0] == 4) {
					pawnMoveMax++;
				}
			}

			//System.out.println("PawnMoveMax: " + pawnMoveMax);

			// Directions that correspond to the current PieceType
			for (int[] dir : directions) 
			{
				System.out.println("Considering direction " + "(" + dir[0] + ", " + dir[1] + ") from (" + 
					currPos[0] + ", " + currPos[1] + ")");

				// This can be done using a ternary
				boolean findPins = goFurther;
				if (pieceType.equals("knight")) // Knights can't result in pin
					findPins = false;

				boolean pinnedOne = false;

				int moveCount = 0;			// King and knight become invalid after 1 move
				int[] pos = new int[]{currPos[0], currPos[1]};

				directionLoop:
				while (true) 
				{
					// I'm 90% sure this is important to do first to ensure that king taking a bishop in a reveal check from rook
					// will be a valid move
					pos[0] += dir[0];
					pos[1] += dir[1];

					// Board bounds
					if (!inBounds(pos)) {
						if (pinnedOne) 
							pinList.remove(pinList.size()-1);
						
						break;
					}

					// Skip over our king if doing kingMoves
					if (mode.equals("kingMove") && board[pos[0]][pos[1]] == (currColor.equals("white") ? 'k' : 'K')) {
						moveCount++;
						continue;
					}

					System.out.println("\tChecking out board[" + pos[0] + "][" + pos[1] + "], which has char " + board[pos[0]][pos[1]]);

					// If we hit our own piece first it can't be enemy desired piece so we either go further or stop trying this direction
					if (findPins && ((currColor.equals("white") && Character.isLowerCase(board[pos[0]][pos[1]])) ||
									 (currColor.equals("black") && Character.isUpperCase(board[pos[0]][pos[1]])))) {
						pinList.add(new int[]{pos[0], pos[1]});
						pinnedOne = true;
						System.out.println("Pinned one!");
						findPins = !findPins;
						moveCount++;
						continue;
					}
					else if (!findPins && ((currColor.equals("white") && Character.isLowerCase(board[pos[0]][pos[1]])) ||
										   (currColor.equals("black") && Character.isUpperCase(board[pos[0]][pos[1]])))) {
						if (pinnedOne) {
							pinList.remove(pinList.size()-1);
						}
						break
						;
					}

					// See if any of the desired pieces are found
					for (char piece : possiblePieces) {
						// Kings can only move once in a given direction
						if (moveCount > 0 && Character.toLowerCase(piece) == 'k') continue;

						// Pawns can only move once (or twice if grid) in a given direction
						if (moveCount >= pawnMoveMax && Character.toLowerCase(piece) == 'p') continue;

						// White pawn can only move down the board (row increases)
						// Black pawn can only move up the board (row decreases)
						// Which means...the direction of exploration has to be row decreases to find a possible
						// controlling white pawn and vice versa for black

						if ((piece == 'p' && dir[0] != -1) || (piece == 'P' && dir[0] != 1)) {
							continue;
						}

						// Stop going in this direction. Btw we now have a pinned piece (if we were searching for that).
						if (board[pos[0]][pos[1]] == piece) {
							System.out.println("Found a controlling piece: " + piece);
							if (!pinnedOne)
								controlledFromPos.add(pos);
							break directionLoop;
						}
					}

					// If we hit a piece of opposite color that wasn't a desired piece 
					// (if it was a desired piece we'd be out of the loop by now)
					if ((currColor.equals("white") && Character.isUpperCase(board[pos[0]][pos[1]])) ||
						(currColor.equals("black") && Character.isLowerCase(board[pos[0]][pos[1]]))) {
						if (pinnedOne) {
							pinList.remove(pinList.size()-1);
						}
						break;
					}

					// Knight moves once per direction. Don't need to worry about removing from pinlist b/c findPin is false for Knight
					if (pieceType.equals("knight")) break;

					moveCount++;
				}
			}
    	}
    	ArrayList<ArrayList<int[]>> controlInfo = new ArrayList<>();
    	controlInfo.add(controlledFromPos);
    	controlInfo.add(pinList);
    	return controlInfo;
    }

   	/*
   	// Lookup position in curPos hashmap based on team 
   	// use unit directions hashmap to generate all of the possible successor states
   	public ArrayList<Character[][]> computeKnightStates()
    {

    	if(currPos.get())
    }


    public HashMap<String, ArrayList<int[]>> computeValidMoves(ArrayList<ArrayList<String>> board, String currTeam)
	{
		Hashmap<String, ArrayList<int[]>> viableMoves = new Hashmap<>();
		piece =  new Piece(); // need to import piece class
		Integer currKing[] = new Integer[2];

		// List to temporarily hold king moves before placing in hashmap 
		ArrayList<int[]> kingMovesTemp;
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

    

/* No more mapping 
*/

  
}