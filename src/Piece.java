
public class Piece {
	private ArrayList<Integer[]> knightDir;
	private ArrayList<Integer[]> qkDir;
	private ArrayList<Integer[]> bishopDir;
	private ArrayList<Integer[]> rookDir;
	private ArrayList<Integer[]> pawnDir;
	public Piece()
	{
		Integer[] dir = {1,0}
		//Queen and King 
		qkDir.add(dir)
		dir = new Integer[]{0,1}
		qkDir.add(dir)
		dir = new Integer[]{1,1}
		qkDir.add(dir)
		dir = new Integer[]{-1,1}
		qkDir.add(dir)
		dir = new Integer[]{1,-1}
		qkDir.add(dir)
		dir = new Integer[]{-1,-1}
		qkDir.add(dir)

		//Knight
		dir = new Integer[]{2,1}
		knightDir.add(dir)
		dir = new Integer[]{2,-1}
		knightDir.add(dir)
		dir = new Integer[]{-2,1}
		knightDir.add(dir)
		dir = new Integer[]{-2,-1}
		knightDir.add(dir)
		dir = new Integer[]{1,2}
		knightDir.add(dir)
		dir = new Integer[]{1,-2}
		knightDir.add(dir)
		dir = new Integer[]{-1,2}
		knightDir.add(dir)
		dir = new Integer[]{-1,-2}
		knightDir.add(dir)




	}

    
    // Helper method to determine squares that are controlled by specifed piece 
    public ArrayList<Integer[]> controlSquares(ArrayList<ArrayList<String>> board, int x, int y, String type)
    {

    }

}
