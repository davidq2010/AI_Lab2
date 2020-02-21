
//Initial call to negamax should be
//negamax(initialState, depth, ((-1)*Double.INFINITY), Double.INFINITY, 1)


public static double negamax(ArrayList<char[][]> state, int depth, double alpha, double beta, int color) {
  if(depth == 0 || numSuccessor() == 0)
  {
    return color * state.getScore();
  }
  // TODO write a method to encapsulate all state generation
  ArrayList<char[][]> successors = state.generateStates();
  // GenerateStates accesses computeAllStates

  // TODO write method to reorder states based on some policy
  ArrayList<char[][]> orderedSuccessors = reorderStates(successors);
  double value = Double.INFINITY*(-1);
  for(node : orderedSuccessors){
    value = Math.max(value, -negamax(node, depth -1, -beta, -alpha, -color));
    alpha = Math.max(alpha, value)
    if(alpha >= beta) {
      break; //prune
    }
  }
  return value;
}
