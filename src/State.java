import java.lang.Character;

public class State {
	private char[][] board;
	int score;
	boolean kingChecked;
	String color;

	public State(char[][] board, int score, boolean kingChecked, String colorOfStateMover) {
		this.board = board;
		this.score = score;
		this.kingChecked = kingChecked;
		this.color = colorOfStateMover;
	}

	public void setBoard(char[][] board) {
		this.board = board;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void setKingChecked(boolean kingChecked) {
		this.kingChecked = kingChecked;
	}

	public char[][] getBoard() {
		return board;
	}

	public int getScore() {
		return score;
	}

	public boolean getKingChecked() {
		return kingChecked;
	}

	public String getColorOfStateMover() {
		return color;
	}

	public int computeScore() {
		for (int i = 0; i < ChessAI.BOARD_SIZE; i++) {
			for (int j = 0; j < ChessAI.BOARD_SIZE; j++) {
				if (color.equals("white") && Character.isLowerCase(board[i][j])) {
					score += BoardStateManager.pieceVal.get(board[i][j]);
				} else if (color.equals("black") && Character.isUpperCase(board[i][j])) {
					score += BoardStateManager.pieceVal.get(Character.toLowerCase(board[i][j]));
				}
			}
		}
	}
}