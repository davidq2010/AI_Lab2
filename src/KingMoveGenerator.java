import java.util.List;
import java.util.ArrayList;

public class KingMoveGenerator extends PieceMoveGenerator {

	public KingMoveGenerator(BoardStateManager _bm) {
		List<int[]> dirs = new ArrayList<>() {{
			add(new int[]{1, 1});
			add(new int[]{-1, -1});
			add(new int[]{0, 1});
			add(new int[]{0, -1});
			add(new int[]{1, 0});
			add(new int[]{-1, 0});
			add(new int[]{-1, 1});
			add(new int[]{1, -1});
		}}
		super(dirs, false, _bm);
	}

	private State genCastleState(State _state, CastleDir _dir) {
		int[] currPlayerKingPos = _state.getKingPos(_state.getCurrentPlayer());
		int row = currPlayerKingPos[0];
		int[] newKingPos, newRookPos;
		if (_dir.equals(CastleDir.KING)) {
			newKingPos = new int[]{row, currPlayerKingPos[1]+2}; 
			newRookPos = new int[]{row, currPlayerRookPos[1]-2};
		} 
		else {  // Queenside castle
			newKingPos = new int[]{row, currPlayerKingPos[1]-2}; 
			newRookPos = new int[]{row, currPlayerRookPos[1]+3};
		}
		return m_bm.genNewState(_state, currPlayerKingPos, newKingPos, 
			currPlayerRookPos, newRookPos);
	}

	@Override
	boolean canAddNewPos(State _state, int[] _pos) {
		List<int[]> controllers = _state.getOpponentControllers(_pos, _state.getCurrentPlayer(), 
			ControlMode.CAPTURE);
		return controllers.isEmpty();
	}

	List<State> individualComputation(State _state, int[] _pos) {
		List<State> newStates = new ArrayList<>();
		if (_state.canCastle(CastleDir.KING)) {
			newStates.add(genCastleState(_state, CastleDir.KING));
		}
		if (_state.canCastle(CastleDir.QUEEN)) {
			newStates.add(genCastleState(_state, CastleDir.QUEEN));
		}
		return newStates;
	}

}