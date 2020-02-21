import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ChessAI {

    public static final int BOARD_SIZE = 8;

    public static char[][] GetBoard(String fileName)
        throws IOException {
        // Try with resource
        try (BufferedReader in =
                new BufferedReader(new FileReader(fileName))) {
            char[][] board = new char[BOARD_SIZE][BOARD_SIZE];
            String line;
            for (int j = 0; j < BOARD_SIZE; j++) {
                line = in.readLine();
                if (line == null) break;
                char[] row = new char[BOARD_SIZE];
                String[] line_arr = line.split(", ");
                for (int i = 0; i < BOARD_SIZE; i++) {
                    row[i] = line_arr[i].charAt(0);
                }
                board[j] = row;
           }
            return board;
        }
    }

    public static void printBoard(char[][] board) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static int[] findPiece(char[][] board, char piece) {
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

        String color = "black";

        BoardStateManager bm = new BoardStateManager();
        State startState = new State(board, 0, color, blackKingPos, whiteKingPos);
        startState.computeScore();
        int maxDepth = 4;
        int alpha = Integer.MAX_VALUE*-1;
        int beta = Integer.MAX_VALUE;
        int color1 = -1;


        ImTiredClass bestMove = bm.negamax(startState, maxDepth, alpha, beta, color1);
        State nextState = bestMove.getState();

        System.out.println("BEST MOOOOOOOOVEEEEEEEEEEEEEEEE");
        printBoard(nextState.getBoard());
        System.out.println(bm.numStates);
    }
}
