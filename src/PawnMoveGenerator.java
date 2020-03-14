import java.util.List;
import java.util.ArrayList;

class OpponentPawnMoveGenerator extends PieceMoveGenerator 
	implements PawnMoveGenerator {
	
	public OpponentPawnMoveGenerator(BoardStateManager _bm) {
		List<int[]> dirs = new ArrayList<>() {{
			add(1, 0);
		}}	
	}

	public State computeTwoStep(State _state, int[] _pos) {
		if (_pos[0] != 1) {
			return null;
		}
		int[] newPos = new int[]{_pos[0]+2, _pos[1]};
		if (_state.posIsEmpty(new int[]{_pos[0]+1, _pos[1]}) &&
			_state.posIsEmpty(newPos)) {
			State newState = m_bm.genNewState(_state, _pos, newPos, false);
			newState.setEnPassant(newPos);
			return newState;
		}
		return null;
	}

	public Lis<State> computeBlockStates(State _state, int[] _pos) {
		List<State> newStates = new ArrayList<>();
		commonComputation(_state, _pos, newStates);
		State twoStep = computeTwoStep(_state, _pos);
		if (twoStep != null) {
			newStates.add(twoStep);
		}

		return newStates;
	}

	public List<State> computeCaptureStates(State _state, int[] _pos) {
		List<State> captureStates = new ArrayList<>();
		// Diagonal (left/right are relative to the opponent's POV downward)
		int[] leftDiag = new int[]{_pos[0]+1, _pos[1]+1};
		int[] rightDiag = new int[]{_pos[0]+1, _pos[1]-1};
		// TODO: Should the info of whether the pos is an enemyPos be from bm or state?
		if (_state.colorAt(leftDiag) == m_bm.oppositeColor(_state.getCurrentPlayer())) {
			captureStates.add(m_bm.genNewState(_state, _pos, leftDiag, false));
		}
		if (_state.colorAt(rightDiag) == m_bm.oppositeColor(_state.getCurrentPlayer())) {
			captureStates.add(m_bm.genNewState(_state, _pos, rightDiag, false));
		}

		// En passant
		int[] enPassant = _state.getEnPassant();
		if (_pos[0] == 4 && enPassant != null) {
			// Again, from the opponent's POV
			int[] left = new int[]{_pos[0], _pos[0]+1};
			int[] right = new int[]{_pos[0], _pos[0]-1};
			if (left.equals(enPassant)) {
				captureStates.add(m_bm.genNewState(_state, _pos, leftDiag, false));
			}
			if (right.equals(enPassant)) {
				captureStates.add(m_bm.genNewState(_state, _pos, rightDiag, false));
			}
		} 

		return captureStates;
	}

	@Override
	List<State> individualComputation(State _state, int[] _pos) {
		List<State> newStates = new ArrayList<>();

		State twoStep = computeTwoStep(_state, _pos);
		if (twoStep != null) {
			newStates.add(twoStep);
		}

		newStates.addAll(computeCaptureStates);

		return newStates;
	}

	@Override
	boolean canAddNewPos(State _state, int[] _pos) {
		if (_state.isEmptyPos(_pos)) {
			return true;
		} 
		// Pawn cannot capture by moving in its unit direction
		return false;
	}
}
