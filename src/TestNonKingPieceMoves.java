import java.util.ArrayList;
import java.io.IOException;



public class TestNonKingPieceMoves {
	
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
	public static void testSlidingPiece(BoardStateManager bm, char[][] board) {
		String currColor = "white";
		char piece = 'q';
		int[] pos = findPiece(board, piece);
		ArrayList<char[][]> states = bm.computeSlidingStates(board, pos, piece, currColor);
		for (char[][] state : states) {
			ChessAI.printBoard(state);
			System.out.println();
		}
	}
/*
	public static void testPawn(BoardStateManager bm, char[][] board) {



	}

	public static void testKnight(BoardStateManager bm, char[][] board) {



	}
*/
public static void main(String[] args) throws IOException {
		if (args.length != 1) {
            System.out.println("Usage: java Chess <inputFile>");
            System.exit(0);
        }
        String filename = args[0];
        char[][] board = ChessAI.GetBoard(filename);
        ChessAI.printBoard(board);

		BoardStateManager bm = new BoardStateManager();

		//testKingIsChecked(bm, board);
		//testBlockingCheck(bm, board);
		//testKingMoveGen(bm, board);
		//testKingStateHelper(bm, board);
		//testTheWholeShebang(bm, board);
		testSlidingPiece(bm, board);
	}

}