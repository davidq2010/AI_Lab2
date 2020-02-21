import java.lang.Character;
import java.lang.Comparable;

public class State implements Comparable<State> {
	private char[][] board;
	private int score;
	private String color;
	public int[] blackKingPos;
	public int[] whiteKingPos;

	public State(char[][] board, int score, String colorOfStateMover,
		int[] blackKingPos, int[] whiteKingPos) {
		this.board = board;
		this.score = score;
		this.color = colorOfStateMover;
		this.blackKingPos = blackKingPos;
		this.whiteKingPos = whiteKingPos;
	}

	public int compareTo(State other) {
		return this.score - other.score;
	}

	public void setBoard(char[][] board) {
		this.board = board;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int[] getBlackKingPos() { return blackKingPos; }
	public int[] getWhiteKingPos() { return whiteKingPos; }

	public char[][] getBoard() {
		return board;
	}

	public int getScore() {
		return score;
	}

	public String getColorOfStateMover() {
		return color;
	}

	public void computeScore() {
		for (int i = 0; i < ChessAI.BOARD_SIZE; i++) {
			for (int j = 0; j < ChessAI.BOARD_SIZE; j++) {
				if (Character.toLowerCase(board[i][j]) == 'k') {
					continue;
				}
				if (color.equals("white") && Character.isLowerCase(board[i][j])) {
					score += BoardStateManager.pieceVal.get(board[i][j]);
				} else if (color.equals("black") && Character.isUpperCase(board[i][j])) {
					score += BoardStateManager.pieceVal.get(Character.toLowerCase(board[i][j]));
				}
				else {
					score -= BoardStateManager.pieceVal.get(Character.toLowerCase(board[i][j]));
				}
			}
		}
	}
}