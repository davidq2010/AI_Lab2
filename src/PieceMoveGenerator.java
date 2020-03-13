import java.util.List;
import java.util.ArrayList;

public abstract class PieceMoveGenerator {

	private List<int[]> m_unitDirs;
	private boolean m_isSliding;
	private BoardStateManager m_bm;
	private Color m_color;
	private boolean m_canDoIndividualComputation = true;

	public PieceMoveGenerator(List<int[]> _dirs, boolean _isSliding, 
		BoardStateManager _bm, Color _color) {
		m_unitDirs = _dirs;
		m_isSliding = _isSliding;
		m_bm = _bm;
		m_color = _color;
	}

	// Template method design
	public final List<State> computeValidPositions(State _state, int[] _pos) {
		List<State> newStates = new ArrayList<>();
		// Ok to use out_param since commonComputation is private
		commonComputation(_state, _pos, newStates);
		if (m_canDoIndividualComputation) {
			newStates.addAll(individualComputation(_state, _pos));
		}
		return result;
	}

	private final void commonComputation(State _state, int[] _pos, 
		List<State> o_newStates) {
		for (int[] dir : m_unitDirs) {
			int[] newPos = new int[]{pos[0], pos[1]};
			while (true) {
				newPos[0] += dir[0];
				newPos[1] += dir[1];

				if (!m_bm.inBounds(newPos)) break;

				// Check for any sort of collision	
				Color newPosPieceColor = _state.colorAt(newPos);
				// Teammate collision
				if (newPosPieceColor == m_color) break;
				else {
					if (canAddNewPos(newPos)) {
						o_newStates.add(m_bm.genNewState(_state, _pos, newPos, false));
					}
					// Opposing piece collision
					if (newPosPieceColor != null) break;
				}

				// Non-sliding pieces move once in unit direction
				if (!m_isSliding) break;
			}
		}
	}

	void setCanDoIndividualComputation(boolean _canDo) {
		m_canDoIndividualComputation = _canDo;
	}

	// Package scope only
	abstract boolean canAddNewPos(int[] _pos);

	abstract List<State> individualComputation(State _state, int[] _pos);
}