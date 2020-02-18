import java.util.ArrayList;
import java.io.IOException;

public class TestIsControlled {
	
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

	public static void kingIsChecked(BoardStateManager bm, char[][] board) {
		// Imagine we are black king
		String currColor = "black";
		boolean findPins = true;
		String mode = "eating";
		int[] kingPos = findPiece(board, 'K');
		ArrayList<ArrayList<int[]>> controlInfo = bm.isControlled(board, kingPos, currColor, findPins, mode);
		ArrayList<int[]> controlPos = controlInfo.get(0);
		ArrayList<int[]> pinList = controlInfo.get(1);
		System.out.println("Control Positions: ");
		for (int[] pos : controlPos) {
			System.out.println("\t(" + pos[0] + ", " + pos[1] + ")");
		}
		System.out.println("Pinned Positions: ");
		for (int[] pos : pinList) {
			System.out.println("\t(" + pos[0] + ", " + pos[1] + ")");
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

		BoardStateManager boardManager = new BoardStateManager();

		kingIsChecked(boardManager, board);

	}
}