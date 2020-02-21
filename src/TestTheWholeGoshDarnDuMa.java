import java.util.ArrayList;
import java.io.IOException;

public class TestTheWholeGoshDarnDuMa {
	
	private static int[] findPiece(char[][] board, char piece) {
		int[] pos = new int[2];
		for (int i = 0; i < ChessAI.BOARD_SIZE; i++) {
			for (int j = 0; j < ChessAI.BOARD_SIZE; j++) {
				if (board[i][j] == piece) {
					pos[0] = i;
					pos[1] = j;
				}
			}
		}
		return pos;
	}

	public static void testEverything(BoardStateManager bm, State startState, String color) {
		ArrayList<State> allStates = bm.computeAllStates(startState, color);

		for (State state : allStates) {
			ChessAI.printBoard(state.getBoard());
			System.out.println();
		}
	}

	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.out.println("Usage: java Chess <inputFile>");
			System.exit(0);
		}
		String filename = args[0];
		char[][] board = ChessAI.GetBoard(filename);
		ChessAI.printBoard(board);

		int[] whiteKingPos = findPiece(board, 'k');	
		int[] blackKingPos = findPiece(board, 'K');

		System.out.println("BlackKingPos: " + blackKingPos[0] + ", " + blackKingPos[1]);

		String color = "white";

		BoardStateManager bm = new BoardStateManager();
		State startState = new State(board, 0, color, blackKingPos, whiteKingPos);
		startState.computeScore();

		testEverything(bm, startState, color);
	}

}