    import java.util.ArrayList;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ChessAI {

    public static ArrayList<ArrayList<String>> GetBoard(String fileName)
        throws IOException {
        // Try with resource
            try (BufferedReader in =
                    new BufferedReader(new FileReader(fileName))) {
                ArrayList<ArrayList<String>> board = new ArrayList<>();
                String line;
                do {
                    line = in.readLine();
                    if (line == null) break;
                    ArrayList<String> row =
                        new ArrayList<>(Arrays.asList(line.split(", ")));
                    board.add(row);
                } while (true);

                return board;
            }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Chess <inputFile>");
            System.exit(0);
        }
        String filename = args[0];
        try {
            ArrayList<ArrayList<String>> board = GetBoard(filename);
            PrintBoard(board);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
