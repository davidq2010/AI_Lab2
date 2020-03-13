import java.util.List;
import java.util.ArrayList;

public class KingMoveGenerator extends PieceMoveGenerator {

	public KingMoveGenerator(BoardStateManager _bm, Color _color) {
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
		super(dirs, false, _bm, _color);
	}

	private State genCastleState(State _state, CastleDir _dir) {
		int[] currPlayerKingPos = m_bm.getInitKingPos(_state.getCurrentPlayer());
		if (_dir.equals(CastleDir.KING)) {
			int[] currPlayerKingRookPos = m_bm.getInitRookPos(_state.getCurrentPlayer(), CastleDir.KING);
			return m_bm.genNewState(_state, currPlayerKingPos,
				currPlayerKingRookPos, true);
		} 
		// Queenside castle; no option for null CastleDir
		else {
			int[] currPlayerQueenRookPos = m_bm.getInitRookPos(_state.getCurrentPlayer(), CastleDir.QUEEN);
			return m_bm.genNewState(_state, currPlayerKingPos,
				currPlayerQueenRookPos, true);
		}
	}

	@Override
	boolean canAddNewPos(int[] _pos) {
		// TODO: Return whether/not _pos is in check
		return true;
	}

	List<State> individualComputation(State _state, int[] _pos) {
		List<State> newStates = new ArrayList<>();
		if (m_bm.canCastle(_state, CastleDir.KING)) {
			newStates.add(genCastleState(_state, CastleDir.KING));
		}
		if (m_bm.canCastle(_state, CastleDir.QUEEN)) {
			newStates.add(genCastleState(_state, CastleDir.QUEEN));
		}
		return newStates;
	}

}