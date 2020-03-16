
public class BoardStateManager {
	public static final int BOARD_SIZE = 8;
	private Color m_userColor;

	public BoardStateManager(Color _userColor) {
		m_userColor = _userColor;
	}

	public boolean inBounds(int[] _pos) {
        if (_pos[0] < 0 || _pos[0] >= ChessAI.BOARD_SIZE || 
        	_pos[1] < 0 || _pos[1] >= ChessAI.BOARD_SIZE)
            return false;
        return true;
	}

	public int[] getInitKingPos(Color _color) {
		return m_userColor.equals(_color) ? new int[]{7, 4} :
											new int[]{0, 4}; 
	}

	public int[] getInitRookPos(Color _color, CastleDir _dir) {
		if (m_userColor.equals(_color)) {
			return _dir.equals(CastleDir.KING) ? new int[]{7, 7} :
												 // Queenside
												 new int[]{7, 0};
		} else {  // Opponent rook
			return _dir.equals(CastleDir.KING) ? new int[]{0, 7} :
												 // Queenside
												 new int[]{0, 0};
		}
	}

	public Color oppositeColor(Color _color) {
		if (_color == null) return null;
		return _color.equals(Color.BLACK) ? Color.WHITE : Color.BLACK;
	}

	public State genNewState(State _origState, int[] _origPos, int[] _newPos, 
		boolean _switch) {
		/*
        // Initializes new state 
        char[][] newState = new char[ChessAI.BOARD_SIZE][ChessAI.BOARD_SIZE];
        char[][] oldBoard = originalState.getBoard();
        // Temp structures to hold king positions 
        int[] blackKingPos = new int[2];
        int[] whiteKingPos = new int[2];

        // Iterate, and generate new board with piece at updated location
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
         // Ensure old location is empty
        newState[newPos[0]][newPos[1]] = piece;
        newState[oldPos[0]][oldPos[1]] = '_';

        // Remember, child states we imagine are of same team/color
        int updatedScore;
        // Generate score of board
        updatedScore = originalState.getScore() + pieceVal.get(Character.toLowerCase(oldBoard[newPos[0]][newPos[1]]));
        String color = Character.isLowerCase(piece) ? "white" : "black";
        State actualState = new State(newState, updatedScore, color, blackKingPos, whiteKingPos);
        // Return generated state
        return actualState;
		*/
	}
}