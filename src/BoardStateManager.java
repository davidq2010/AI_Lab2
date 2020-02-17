import java.util.ArrayList;
import java.util.HashMap;

public class BoardStateManager
{	
	private HashMap<String, Float> pieceVal;

	public BoardStateManager() 
	{
		this.pieceVal = new HashMap<>() {{
			put("Q", 9.0),
			put("q", 9.0),
			put("R", 5.0),
			put("r", 5.0),
			put("B", 3.0),
			put("b", 3.0),
			put("N", 3.0),
			put("n", 3.0),
			put("P", 1.0),
			put("p", 1.0),
			put("_", 0.0)
		}};
	}

	public float computeScore(ArrayList<ArrayList<String>> board) 
	{
		float score = 0;

		for(int i = 0; i < board.size(); i++)
		{
			for(int j = 0; j < board.get(0).size(); j++)
			{
				score += pieceVal.get(board.get(i).get(j));
			}
		}

		return score;
	}

	public void printBoard(ArrayList<ArrayList<String>> board) {
        for (int i = 0; i < board.size(); i++) {
            for (int j = 0; j < board.get(0).size(); j++) {
                System.out.print(board.get(i).get(j) + " ");
            }
            System.out.println();
        }
    }
}