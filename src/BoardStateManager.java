import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.lang.Math;
import java.lang.Character;
import java.util.Collections;

public class BoardStateManager
{  
    public int numStates = 0;

     // HashMap storing value of pieces
    public static HashMap<Character, Integer> pieceVal = new HashMap<Character, Integer>() {{
        put('q', 9);
        put('r', 5);
        put('b', 3);
        put('n', 3);
        put('p', 1);
        put('_', 0);
    }};;

    public HashMap<String, ArrayList<int[]>> unitDirections;

    public BoardStateManager() 
    {
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
    }

    public HashMap<String, ArrayList<int[]>> getUnitDirections() {
        return unitDirections;
    }

    public double computeScore(ArrayList<ArrayList<String>> board) 
    {
        double score = 0;

        for(int i = 0; i < board.size(); i++)
        {
            for(int j = 0; j < board.get(0).size(); j++)
            {
                score += pieceVal.get(board.get(i).get(j));
            }
        }

        return score;
    }

    public ImTiredClass negamax(State state, int depth, int alpha, int beta, int color) {
        String currColor = (color == 1) ? "white" : "black";

        ArrayList<int[]> checkList = new ArrayList<>();

        ArrayList<State> successors = computeAllStates(state, currColor, checkList);

        if(depth == 0)
        {
            ImTiredClass toReturn = new ImTiredClass(state, state.getScore());
            return toReturn;
        }
        if (successors.isEmpty() && !checkList.isEmpty()) {// Change this
            ImTiredClass toReturn = new ImTiredClass(state, -10000000);
            return toReturn;
        } else if(successors.isEmpty() && checkList.isEmpty()) {
            ImTiredClass toReturn = new ImTiredClass(state, 0);
        }
        
        
      // GenerateStates accesses computeAllStates

        // Visit the successors that are worse for opponent first
        //Collections.sort(successors);
        Collections.sort(successors, Collections.reverseOrder());
        //Collections.shuffle(successors);
        int value = Integer.MAX_VALUE * -1; 
        int idxOfBestState = 0;
        ImTiredClass weTired;
        for (int i = 0; i < successors.size(); i++) {
            weTired = negamax(successors.get(i), depth-1, -beta, -alpha, -color);
            int value2 = -weTired.getValue();
            if (value2 > value) {
                value = value2;
                idxOfBestState = i;
            }
            alpha = (int) Math.max(alpha, value);
            if(alpha >= beta) {
                break; //prune
            }
        }

        ImTiredClass toReturn = new ImTiredClass(successors.get(idxOfBestState), value);

        return toReturn;
    }

    public ArrayList<State> computeAllStates(State state, String color, ArrayList<int[]> checkList) {
        this.numStates++;

        ArrayList<ArrayList<int[]>> checkAndPinPositions = computeCheckAndPinPositions(state.getBoard(), 
            color.equals("black") ? state.blackKingPos : state.whiteKingPos, color);

        ArrayList<int[]> pinList = checkAndPinPositions.get(1);
        checkList.addAll(checkAndPinPositions.get(0));

        ArrayList<State> allState = computeKingStates(state,
            color.equals("black") ? state.blackKingPos : state.whiteKingPos, color, pinList, checkList);

        // If checked, return now;
        if (!checkList.isEmpty()) {
            return allState;
        }

        for (int i = 0; i < ChessAI.BOARD_SIZE; i++) {
            searchLoop:
            for (int j = 0; j < ChessAI.BOARD_SIZE; j++) {
                int[] piecePos = new int[]{i, j};
                char piece = state.getBoard()[i][j];
                // We only generate moves for the current color's pieces!
                if ((color.equals("black") && Character.isLowerCase(piece)) || 
                    color.equals("white") && Character.isUpperCase(piece)) { continue; }

                    for (int[] pinner : pinList) {
                        if (Arrays.equals(piecePos, pinner)) {
                            continue searchLoop; 
                        }
                    }

                    ArrayList<State> states = new ArrayList<>();
                    if (Character.toLowerCase(piece) == 'p') {
                        states = computePawnStates(state, piecePos,
                            Character.isLowerCase(piece) ? "white" : "black");
                    }
                    else if (Character.toLowerCase(piece) == 'n') {
                        states = computeKnightStates(state, piece, piecePos, 
                            Character.isLowerCase(piece) ? "white" : "black");
                    }
                    else if (Character.toLowerCase(piece) == 'b' || Character.toLowerCase(piece) == 'r' ||
                        Character.toLowerCase(piece) == 'q') {
                        states = computeSlidingStates(state, piecePos, piece,
                            Character.isLowerCase(piece) ? "white" : "black");
                }
                allState.addAll(states);
            }
        }

        return allState;
    }

    // If a piece is eaten during a move, how do we account for it? if we had a board of strings it would be easier!!
    // in the hashmap of pieces to coords we need support for multiple Pawns, Knights, etc...can't support this w char!
    public ArrayList<State> computeKnightStates(State originalState, char piece, int[] knightPos, String currColor) {
        ArrayList<State> knightStates = new ArrayList<>();
        int[] newKnightPos = new int[]{knightPos[0], knightPos[1]};
        char updatingPiece = piece;

        char[][] board = originalState.getBoard();

        ArrayList<int[]> knightDir = unitDirections.get("knight");
        for(int i = 0; i < knightDir.size(); i++) {
            newKnightPos[0] = knightPos[0] + knightDir.get(i)[0];
            newKnightPos[1] = knightPos[1] + knightDir.get(i)[1];

            if (inBounds(newKnightPos)) {
                if((currColor.equals("white") && Character.isLowerCase(board[newKnightPos[0]][newKnightPos[1]])) || (currColor.equals("black") && Character.isUpperCase(board[newKnightPos[0]][newKnightPos[1]])))
                {
                    continue;
                }else{
                    knightStates.add(newStateGenerator(originalState, knightPos, newKnightPos, updatingPiece));
                }
            }
        }
        return knightStates;
    }

    public ArrayList<State> computePawnStates(State originalState, int[] pawnPos, String currColor) {
        char[][] board = originalState.getBoard();
        ArrayList<State> pawnStates = new ArrayList<>();
        int[] newPawnPos = new int[]{pawnPos[0], pawnPos[1]};
        ArrayList<int[]> pawnMoves = new ArrayList<>();
        char piece;

        if(currColor.equals("white")) {
            piece = 'p';
            //down
            newPawnPos[0] = pawnPos[0] + 1;
            newPawnPos[1] = pawnPos[1];
            if (inBounds(newPawnPos)) {
                if(board[newPawnPos[0]][newPawnPos[1]] == '_') {
                //System.out.println(newPawnPos[0]);
                    pawnMoves.add(new int[]{newPawnPos[0], newPawnPos[1]});

                }
            }
            //down left
            newPawnPos[0] = pawnPos[0] + 1;
            newPawnPos[1] = pawnPos[1] - 1;
            if (inBounds(newPawnPos)) {
                if(Character.isUpperCase(board[newPawnPos[0]][newPawnPos[1]]))
                {
                    pawnMoves.add(new int[]{newPawnPos[0], newPawnPos[1]});
                }
            }
            //down right
            newPawnPos[0] = pawnPos[0] + 1;
            newPawnPos[1] = pawnPos[1] + 1;
            if (inBounds(newPawnPos)) {
                if(Character.isUpperCase(board[newPawnPos[0]][newPawnPos[1]]))
                {
                    pawnMoves.add(new int[]{newPawnPos[0], newPawnPos[1]});
                }
            }
            if(pawnPos[0] == 1) {
                newPawnPos[0] = pawnPos[0] + 1;
                newPawnPos[1] = pawnPos[1];
                if(board[newPawnPos[0]][newPawnPos[1]] == '_'){
                    newPawnPos[0] += 1;


                    if(board[newPawnPos[0]][newPawnPos[1]] == '_') {
                    //System.out.println(newPawnPos[0]);
                        pawnMoves.add(new int[]{newPawnPos[0], newPawnPos[1]});

                    }

                }
            }


        }else {
            piece = 'P';
            //up
            newPawnPos[0] = pawnPos[0] - 1;
            newPawnPos[1] = pawnPos[1];
            if (inBounds(newPawnPos)) {
                if(board[newPawnPos[0]][newPawnPos[1]] == '_') {
                    pawnMoves.add(new int[]{newPawnPos[0], newPawnPos[1]});

                }
            }
            //up left
            newPawnPos[0] = pawnPos[0] - 1;
            newPawnPos[1] = pawnPos[1] - 1;
            if (inBounds(newPawnPos)) {
                if(Character.isLowerCase(board[newPawnPos[0]][newPawnPos[1]])) {
                    pawnMoves.add(new int[]{newPawnPos[0], newPawnPos[1]});
                }
            }
            //up right
            newPawnPos[0] = pawnPos[0] - 1;
            newPawnPos[1] = pawnPos[1] + 1;
            if (inBounds(newPawnPos)) {
                if(Character.isLowerCase(board[newPawnPos[0]][newPawnPos[1]])) {
                    pawnMoves.add(new int[]{newPawnPos[0], newPawnPos[1]});
                }
            }
            if(pawnPos[0] == 6) {
                newPawnPos[0] = pawnPos[0] - 1;
                newPawnPos[1] = pawnPos[1];
                if(board[newPawnPos[0]][newPawnPos[1]] == '_'){
                    newPawnPos[0] -= 1;

                    if(board[newPawnPos[0]][newPawnPos[1]] == '_') {
                        pawnMoves.add(new int[]{newPawnPos[0], newPawnPos[1]});

                    }
                }
            }
        }
        int[] move;
        for(int i = 0; i < pawnMoves.size(); i++) {
            move = pawnMoves.get(i);
        //System.out.println("====================");
        //System.out.println(move[0]);
        //System.out.println(move[1]);

            pawnStates.add(newStateGenerator(originalState, pawnPos, move, piece));

        }
        return pawnStates;


    }

    // compute all sliding piece states
    public ArrayList<State> computeSlidingStates(State originalState, int[] currPos, char piece, String currColor) {
        ArrayList<State> newStates = new ArrayList<>();
        String[] dir = new String[2];
        int[] newPos = new int[]{currPos[0], currPos[1]};
        if(Character.toLowerCase(piece) == 'r') {
            dir[0] = "grid";
        }else if(Character.toLowerCase(piece) == 'b') {
            dir[0] = "diagonal";
        }else if(Character.toLowerCase(piece) == 'q') {
            dir[0] = "grid";
            dir[1] = "diagonal";
        }

        for(int i = 0; i < dir.length; i++) {
            if(dir[i] == null) break;
        //System.out.println("HERE");
        //System.out.println(dir[0]);
        //System.out.println(piece);
            computeSlidingStatesHelper(newStates, originalState, currPos, newPos, currColor, dir[i], piece);

        }
        return newStates;   

    }

    public void computeSlidingStatesHelper(ArrayList<State> newStates, State originalState, int[] oldPos, int[] newPos, 
        String currColor, String dir, char piece)
    {
        char[][] board = originalState.getBoard();
    //System.out.println("Curr position: ("+oldPos[0]+" ,"+oldPos[1]+")");
    //System.out.println("New position: ("+newPos[0]+" ,"+newPos[1]+")");
    //System.out.println(currColor);
    //System.out.println(dir);
    //System.out.println(piece);

        ArrayList<int[]> dirList = unitDirections.get(dir);
        for(int i = 0; i < dirList.size(); i++) {
            newPos[0] = oldPos[0];
            newPos[1] = oldPos[1];
            while(true){
                newPos[0] += dirList.get(i)[0];
                newPos[1] += dirList.get(i)[1];

                if (inBounds(newPos)) {
                    // checks if piece from same team is at new pos
                    if((currColor.equals("white") && Character.isLowerCase(board[newPos[0]][newPos[1]])) || (currColor.equals("black") && Character.isUpperCase(board[newPos[0]][newPos[1]])))
                    {
                        break;
                    // checks if piece from enemy color is at new pos 
                    }else if((currColor.equals("white") && Character.isUpperCase(board[newPos[0]][newPos[1]])) || currColor.equals("black") && Character.isLowerCase(board[newPos[0]][newPos[1]])) {
                        newStates.add(newStateGenerator(originalState, oldPos, newPos, piece));
                        break;
                    }
                    else{
                        newStates.add(newStateGenerator(originalState, oldPos, newPos, piece));
                    }
                } else{
                    break;
                }
            }

        }
    }

    // 1. Is King in check? (Do this by imagining King is all of the pieces and seeing if any opponent pieces are the first thing we hit.)
    // 2. First, compute valid moves for King (aka moves in which it's not checked)
    // 3. Are we double checked? If so, return list of king moves.
    // 4. If single checked, add options of eat, block to the original list of moves.
    // 5. For eating and blocking, make sure the piece isn't pinned. (Make sure that the piece we're considering moving is not the only piece
    // between the king and sliding piece.)
    public ArrayList<State> computeKingStates(State originalState, int[] kingPos, String color, ArrayList<int[]> pinList,
        ArrayList<int[]> checkList) {
        ArrayList<State> allStates = new ArrayList<>();

        char[][] board = originalState.getBoard();

        // Compute valid king states
        // For each king move, make sure it won't be checked
    //System.out.println("!!!!!!!Computing King States!!!!!");
        nextKingStateHelper(originalState, allStates, kingPos, color);

    // Is king checked?
        // If so, is it double checked?
        if (checkList.size() > 1) {
            return allStates;
        }

        if (checkList.isEmpty()) {
            return allStates;
        }

        int[] checkingPos = checkList.get(0);
    //System.out.println("\tChecking King from (" + checkingPos[0] + ", " + checkingPos[1] + ")");

    //System.out.println("!!!!!!Tryna Eat!!!!!!!!!");
        // Generate eating states
        // Radiate from the checking enemy piece; for each piece that isn't on the pin list, using that piece to eat results in a valid state
        eatEnemyCheckHelper(originalState, allStates, checkingPos, pinList, color);

        // Is it a knight?
        if (Character.toLowerCase(board[checkingPos[0]][checkingPos[1]]) == 'n') {
            return allStates;
        }

    //System.out.println("!!!!!!Tryna Block!!!!!!!!!");
        // Generate blocking states
        // Consider all squares between our king and the checking piece. For each of those squares, call IsControlled()
    //System.out.println("StartPos: (" + kingPos[0] + ", " + kingPos[1] + ")");
    //System.out.println("EndPos: (" + checkingPos[0] + ", " + checkingPos[1] + ")");
        computeBlockingStates(originalState, allStates, kingPos, checkingPos, pinList, color);

        return allStates;
    }

    // start and end pos here are exclusive
    void computeBlockingStates(State originalState, ArrayList<State> states, int[] startPos, int[] endPos, 
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

    //System.out.println("Iterating til EndPos: (" + endPos[0] + ", " + endPos[1] + ")");

        char[][] board = originalState.getBoard();

        while (!Arrays.equals(pos, endPos)) {
        //System.out.println("Blocking at pos (" + pos[0] + ", " + pos[1] + ")");
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
                states.add(newStateGenerator(originalState, blocker, pos, board[blocker[0]][blocker[1]]));
            }

            pos[0] += dRow;
            pos[1] += dCol;
        }
    }

    void eatEnemyCheckHelper(State originalState, ArrayList<State> states, int[] pos, ArrayList<int[]> pinList, String color) {
        String mode = "eating";
        boolean findPins = false;
        char[][] board = originalState.getBoard();
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
            states.add(newStateGenerator(originalState, eater, pos, board[eater[0]][eater[1]]));
        }
    }

    public ArrayList<ArrayList<int[]>> computeCheckAndPinPositions(char[][] board, int[] kingPos, String color) {
        boolean findPins = true;
        String mode = "eating";
        ArrayList<ArrayList<int[]>> controlInfo = isControlled(board, kingPos, color, findPins, mode);
        return controlInfo;
    }

    public State newStateGenerator(State originalState, int[] oldPos, int[] newPos, char piece) {
        //System.out.println("PEAAACCEEEEE " + piece);
        char[][] newState = new char[ChessAI.BOARD_SIZE][ChessAI.BOARD_SIZE];
        char[][] oldBoard = originalState.getBoard();
        int[] blackKingPos = new int[2];
        int[] whiteKingPos = new int[2];
        for (int i = 0; i < ChessAI.BOARD_SIZE; i++) {
            for (int j = 0; j < ChessAI.BOARD_SIZE; j++) {
                newState[i][j] = oldBoard[i][j]; 
                if (newState[i][j] == 'k') {
                    whiteKingPos[0] = i;
                    whiteKingPos[1] = j;
                }
                else if (newState[i][j] == 'K') {
                    blackKingPos[0] = i;
                    blackKingPos[1] = j;
                }
            }
        }
        newState[newPos[0]][newPos[1]] = piece;
        newState[oldPos[0]][oldPos[1]] = '_';

        // Remember, child states we imagine are of same team/color
        int updatedScore;
        if (originalState.getScore() > 0) {
            updatedScore = originalState.getScore() + pieceVal.get(Character.toLowerCase(oldBoard[newPos[0]][newPos[1]]));
        } else {
            updatedScore = originalState.getScore() + pieceVal.get(Character.toLowerCase(oldBoard[newPos[0]][newPos[1]]));
        }
        String color = Character.isLowerCase(piece) ? "white" : "black";
        State actualState = new State(newState, updatedScore, color, blackKingPos, whiteKingPos);

        return actualState;
    }

    public void nextKingStateHelper(State originalState, ArrayList<State> states, int[] kingPos, String color) {
        char[][] board = originalState.getBoard();
        // Check all grid directions and diagonal directions
        ArrayList<int[]> dirs = new ArrayList<>();
        dirs.addAll(unitDirections.get("grid"));
        dirs.addAll(unitDirections.get("diagonal"));
        for (int[] dir : dirs) {
            int[] pos = new int[]{kingPos[0] + dir[0], kingPos[1] + dir[1]};
            if (inBounds(pos) && noSameTeamPiece(board, pos, color)) {
                ArrayList<int[]> controlPos = isControlled(board, pos, color, false, "kingMove").get(0);
                if (controlPos.isEmpty()) {
                    states.add(newStateGenerator(originalState, kingPos, pos, color.equals("black") ? 'K' : 'k'));
                    //System.out.println("Free Pos For King at (" + pos[0] + ", " + pos[1] + ")");
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

            //System.out.println();
            //System.out.println("PieceType: " + pieceType);

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

            //System.out.print("Possible Pieces:");
            for (char piece : possiblePieces) {
                //System.out.println(piece);
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

            ////System.out.println("PawnMoveMax: " + pawnMoveMax);

            // Directions that correspond to the current PieceType
            for (int[] dir : directions) 
            {
                //System.out.println("Considering direction " + "(" + dir[0] + ", " + dir[1] + ") from (" + currPos[0] + ", " + currPos[1] + ")");

                // This can be done using a ternary
                boolean findPins = goFurther;
                if (pieceType.equals("knight")) // Knights can't result in pin
                findPins = false;

                boolean pinnedOne = false;

                int moveCount = 0;          // King and knight become invalid after 1 move
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

                    //System.out.println("\tChecking out board[" + pos[0] + "][" + pos[1] + "], which has char " + board[pos[0]][pos[1]]);

                    // If we hit our own piece first it can't be enemy desired piece so we either go further or stop trying this direction
                    if (findPins && ((currColor.equals("white") && Character.isLowerCase(board[pos[0]][pos[1]])) ||
                     (currColor.equals("black") && Character.isUpperCase(board[pos[0]][pos[1]])))) {
                        pinList.add(new int[]{pos[0], pos[1]});
                        pinnedOne = true;
                        //System.out.println("Pinned one!");
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
                            //System.out.println("Found a controlling piece: " + piece);
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
}
