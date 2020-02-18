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

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Chess <inputFile>");
            System.exit(0);
        }
        String filename = args[0];
        try {
            char[][] board = GetBoard(filename);
            printBoard(board);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
