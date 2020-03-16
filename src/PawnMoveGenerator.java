import java.util.List;
import java.util.ArrayList;

class PawnMoveGenerator extends PieceMoveGenerator {
	private int[] m_pawnUnitDir;
	
	public PawnMoveGenerator(BoardStateManager _bm, Color _color) {
		List<int[]> dirs = new ArrayList<>();
		if (_color.equals(_bm.getUserColor())) {
			m_pawnUnitDir = new int[]{-1, 0};
		} else {  // Opponent
			m_pawnUnitDir = new int[]{1, 0};
		}
		dirs.add(m_pawnUnitDir);
		super(dirs, false, _bm);
	}

	public State computeTwoStep(State _state, int[] _pos) {
		if (_pos[0] != 1) {
			return null;
		}
		int[] twoStepPos = new int[]{_pos[0]+2*m_pawnUnitDir[0], _pos[1]};
		if (_state.posIsEmpty(new int[]{_pos[0]+m_pawnUnitDir[0], _pos[1]}) &&
			_state.posIsEmpty(twoStepPos)) {
			State newState = m_bm.genNewState(_state, _pos, twoStepPos);
			newState.setEnPassant(twoStepPos);
			return newState;
		}
		return null;
	}

	public List<State> computeBlockStates(State _state, int[] _pos) {
		List<State> blockStates = new ArrayList<>();
		commonComputation(_state, _pos, blockStates);  // Unit dir position added
		State twoStep = computeTwoStep(_state, _pos);
		if (twoStep != null) {
			blockStates.add(twoStep);
		}

		return blockStates;
	}

	public List<State> computeCaptureStates(State _state, int[] _pos) {
		List<State> captureStates = new ArrayList<>();
		int[] diag1 = new int[]{_pos[0]+m_pawnUnitDir[0], _pos[1]+1};
		int[] diag2 = new int[]{_pos[0]+m_pawnUnitDir[0], _pos[1]-1};
		if (m_bm.inBounds(diag1) && 
			_state.colorAt(diag1) == m_bm.oppositeColor(_state.getCurrentPlayer())) {
			captureStates.add(m_bm.genNewState(_state, _pos, diag1));
		}
		if (m_bm.inBounds(diag2) &&
			_state.colorAt(diag2) == m_bm.oppositeColor(_state.getCurrentPlayer())) {
			captureStates.add(m_bm.genNewState(_state, _pos, diag2));
		}

		int[] enPassant = _state.getEnPassant();
		if (_pos[0] == 4 && enPassant != null) {
			int[] side1 = new int[]{_pos[0], _pos[0]+1};
			int[] side2 = new int[]{_pos[0], _pos[0]-1};
			if (side1.equals(enPassant)) {
				captureStates.add(m_bm.genNewState(_state, _pos, diag1));
			}
			if (side2.equals(enPassant)) {
				captureStates.add(m_bm.genNewState(_state, _pos, diag2));
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
		if (_state.posIsEmpty(_pos)) {
			return true;
		} 
		// Pawn cannot capture by moving in its unit direction
		return false;
	}
}