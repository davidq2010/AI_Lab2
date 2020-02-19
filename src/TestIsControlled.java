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

	public static void testKingIsChecked(BoardStateManager bm, char[][] board) {
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

	public static void testBlockingCheck(BoardStateManager bm, char[][] board) {
		// Imagine we are black king; since blocking, pose as white
		String currColor = "white";
		boolean findPins = false;
		String mode = "blocking";
		int[] blockPos = new int[]{4, 2};
		ArrayList<ArrayList<int[]>> controlInfo = bm.isControlled(board, blockPos, currColor, findPins, mode);
		ArrayList<int[]> controlPos = controlInfo.get(0);
		if (controlInfo.get(1) == null) {
			System.out.println("Null pinlist!");
		}
		System.out.println("Possible Blocker Positions: ");
		for (int[] pos : controlPos) {
			System.out.println("\t(" + pos[0] + ", " + pos[1] + ")");
		}
	}

	public static void testKingMoveGen(BoardStateManager bm, char[][] board) {
		// Imagine we are black king; since blocking, pose as white
		String currColor = "black";
		boolean findPins = false;
		String mode = "kingMove";
		int[] kingPos = findPiece(board, 'K');
		System.out.println("KingPos: " + "(" + kingPos[0] + ", " + kingPos[1] + ")");
		ArrayList<int[]> dirs = new ArrayList<>();
		dirs.addAll(bm.getUnitDirections().get("diagonal"));
		dirs.addAll(bm.getUnitDirections().get("grid"));
		for (int[] dir : dirs) {
			int[] pos = new int[]{kingPos[0] + dir[0], kingPos[1] + dir[1]};
			ArrayList<ArrayList<int[]>> controlInfo = bm.isControlled(board, pos, currColor, findPins, mode);
			if (controlInfo.get(0).isEmpty()) {
				System.out.println("Valid KingMove Pos: " + "(" + pos[0] + ", " + pos[1] + ")");
			} else {
				System.out.println("Pos " + "(" + pos[0] + ", " + pos[1] + ") is attacked by:" );
				for (int[] p : controlInfo.get(0)) {
					System.out.println("\t(" + p[0] + ", " + p[1] + ")");
				}
			}
		}
	}

	public static void testKingStateHelper(BoardStateManager bm, char[][] board) {
		// Imagine we are black king; since blocking, pose as white
		String currColor = "black";
		boolean findPins = false;
		String mode = "kingMove";
		int[] kingPos = findPiece(board, 'K');
		System.out.println("KingPos: " + "(" + kingPos[0] + ", " + kingPos[1] + ")");
		ArrayList<char[][]> states = new ArrayList<>();
		bm.nextKingStateHelper(board, states, kingPos, currColor);
		for (char[][] state : states) {
			ChessAI.printBoard(state);
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

		BoardStateManager bm = new BoardStateManager();

		//testKingIsChecked(bm, board);
		//testBlockingCheck(bm, board);
		//testKingMoveGen(bm, board);
		testKingStateHelper(bm, board);
	}
}