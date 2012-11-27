// An iterative deepening minimax algorithm that uses an MCBoard with alpha-beta pruning
import java.util.HashMap;
import java.util.LinkedList;
import java.lang.Math;

public class WeightedAlphaBetaThread extends Thread {
	public MCBoard board;
	public int depth;
	public HashMap<Tuple, Integer> moveEvals;
	boolean verbose = false;
	public int evals = 0;
	public long startTime = -1L;
	public int[] weights;

	public WeightedAlphaBetaThread(MCBoard board, int[] weights) {
		this.board = board;
		depth = 2;
		moveEvals = new HashMap<Tuple, Integer>();
		for (Tuple t : board.legalMoves) {
			moveEvals.put(t, 0);
		}
		this.weights = weights;
	}

	public int alphabeta_min(int currentDepth, int alpha, int beta, Tuple lastMove) {
	    if (currentDepth >= depth || board.legalMoves.isEmpty()) {
	    	if (board.winStatus != 2) {
	    		return Integer.MAX_VALUE;
	    	}
	    	int eval = -1*board.eval(weights);
	    	evals++;
	    	return eval;
	    }
	    int b = infHolder.MAX;
	    LinkedList<Tuple> list = (LinkedList<Tuple>) board.legalMoves.clone();
	    for (Tuple chosenMove : list) {
	        board.move(chosenMove);
	        int s = alphabeta_max(currentDepth+1, alpha, beta, chosenMove.clone());
	        b = min(b, s);
	        if (b <= alpha) {
	        	board.takeBack(1);
	        	return b;
	        }
	        if (b < beta) {
	        	beta = b;
	        }
	        board.takeBack(1);
	    }
	    return beta;
	}

	public int alphabeta_max(int currentDepth, int alpha, int beta, Tuple lastMove) {
	    if (currentDepth >= depth || board.legalMoves.isEmpty()) {
	    	int eval = -1*board.eval(weights);
	    	evals++;
	    	return eval;
	    }
	    int a = infHolder.MIN;
	    LinkedList<Tuple> list = (LinkedList<Tuple>) board.legalMoves.clone();
	    for (Tuple chosenMove : list) {
	        board.move(chosenMove);
	        int s = alphabeta_min(currentDepth+1, alpha, beta, chosenMove.clone());
	        a = max(a, s);
	        if (a >= beta) {
	        	board.takeBack(1);
	        	return beta;
	        }
	        if (a > alpha) {
	        	alpha = a;
	        }
	        board.takeBack(1);
	    }
	    return alpha;
	}

	/* NOTES
		- Weird thing going on with ignoring wins
		- No need for double -1*
	*/

	public void run() {
		startTime = System.currentTimeMillis();
		while(System.currentTimeMillis() - startTime < 30000) {
			int alpha = infHolder.MIN;
			for (Tuple t : moveEvals.keySet()) {
				board.move(t);
				int s = alphabeta_min(0, alpha, infHolder.MAX, t.clone());
				moveEvals.put(t, s);
				// if (lS.score > alpha) {
				// 	System.out.println("Move " + t + " set alpha to " + lS.score + " from " + alpha + " at run.");
				// 	alpha = lS.score;
				// }
				MoveRecord tb = board.takeBack(1);
			}
			System.out.println(toString());
			depth += 1;
		}
	}

	public int max(int x, int y) {
		if (x > y)
			return x;
		if (x==y) {
			if (Math.random() > 0.5)
				return x;
		}
		return y;
	}
	public int min(int x, int y) {
		if (x < y)
			return x;
		if (x==y) {
			if (Math.random() > 0.5)
				return x;
		}
		return y;
	}
	public String toString(){
		String out = "";
		if (startTime != -1L) {
			long evalTime = System.currentTimeMillis() - startTime;
			out = "Depth " + depth + ", " + evals + " positions evaluated in " + evalTime + " millisenconds:\n";
			for (Tuple t : moveEvals.keySet()) {
				out += t.toString() + "..." + moveEvals.get(t) + "\n";
			}
		} else {
			out = "Minimax thread not yet running";
		}
		return out;
	}
}


