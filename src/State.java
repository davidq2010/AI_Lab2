import java.lang.Character;
import java.lang.Comparable;

public class State implements Comparable<State> {

	private int[] m_blackKingPos;
	private int[] m_whiteKingPos;
	private List<int[]> m_checkedPositions;
	private List<int[]> m_pinnedPositions;
	private boolean[] m_hasMoved;
	private Color m_currentPlayer;
	private double m_score;
	private char[][] m_board;
	private int[] m_enPassant;

	public State(char[][] _board, double _score, Color _currentPlayer, 
		int[] _blackKingPos, int[] _whiteKingPos, boolean[] _hasMoved) {
		m_board = _board;
		m_score = _score;
		m_currentPlayer = _currentPlayer;
		m_blackKingPos = _blackKingPos;
		m_whiteKingPos = _whiteKingPos;
		m_hasMoved = _hasMoved;

		// Sets m_checkedPositions and m_pinnedPositions
		m_checkedPositions = findControllersAndPins(
			getKingPos(m_currentPlayer),
			m_currentPlayer, ControlMode.CAPTURE, true);
	}

	public int compareTo(State other) {
		return this.m_score - other.m_score;
	}

	public int[] getKingPos(Color _color) {
		return _color.equals(Color.BLACK) ? m_blackKingPos : m_whiteKingPos;	
	}

	public Color colorAt(int[] _pos) {
		return Character.isUpperCase(m_board[_pos[0]][_pos[1]]) ? Color.BLACK : Color.WHITE;
	}

	public boolean canCastle(CastleDir _dir) {
		int[] currPlayerKingPos = getKingPos(m_currentPlayer);
		int row = currPlayerKingPos[0];
		int currPlayerRookCol;
		int[] newKingPos, newRookPos;
		// First, king can't have moved
		if (m_hasMoved[1]) return false;
		if (_dir.equals(CastleDir.KING)) {
			// Next, rook in _dir can't have moved
			if (m_hasMoved[2]) return false;
			currPlayerRookCol = BoardStateManager.BOARD_SIZE-1;
			// All spaces in between must be empty
			for (int col = m_whiteKingPos[1]+1; col < currPlayerRookCol; col++) {
				if (m_board[row][col] != '-') return false;
			}
			newKingPos = new int[]{row, currPlayerKingPos[1]+2}; 
			newRookPos = new int[]{row, currPlayerRookCol-2};
		} else {  // Queenside castle
			// Rook in _dir can't have moved
			if (m_hasMoved[0]) return false;
			currPlayerRookCol = 0;
			// All spaces in between must be empty
			for (int col = m_whiteKingPos[1]-1; col > currPlayerRookCol; col--) {
				if (m_board[row][col] != '-') return false;
			}
			newKingPos = new int[]{row, currPlayerKingPos[1]-2}; 
			newRookPos = new int[]{row, currPlayerRookCol+3};
		}
		// New king and rook pos must not be controlled by opponent
		List<int[]> newKingPosControllers = getOpponentControllers(newKingPos,
			m_currentPlayer, ControlMode.CAPTURE);
		if (!newKingPosControllers.isEmpty()) return false;
		List<int[]> newRookPosControllers = getOpponentControllers(newRookPos,
			m_currentPlayer, ControlMode.CAPTURE);
		if (!newRookPosControllers.isEmpty()) return false;

		return true;
	}

	public Color getCurrentPlayer() {
		return m_currentPlayer;
	}

	public void setEnPassant(int[] _pos) {
		m_enPassant = _pos;
	}

	public int[] getEnPassant() {
		return m_enPassant;
	}

	public boolean posIsEmpty(int[] _pos) {
		return m_board[_pos[0]][_pos[1]] == '-';
	}

	public List<int[]> getPinnedPositions() {
		return m_pinnedPositions;
	}

	public List<int[]> getCheckingPositions() {
		return m_checkedPositions;
	}

	public List<int[]> getOpponentControllers(int[] _pos, Color _color, ControlMode _mode) {
		return findControllerssAndPins(_pos, _color, _mode, false);
	}

	private List<int[]> findControllersAndPins(int[] _pos, Color _color, ControlMode _mode,
		boolean findPins) {
		// If mode is blocking, skip king
		// Will set m_pinnedPositions 
	}

}