// An iterative deepening minimax algorithm that uses an MCBoard with alpha-beta pruning
import java.util.HashMap;
import java.lang.Math;

public class WeightedAlphaBetaThread extends Thread {
	public MCBoard board;
	public int depth;
	public HashMap<Tuple, Integer> moveEvals;
	boolean cont = true;
	boolean verbose = false;
	public int evals = 0;
	public long startTime = -1L;
	public int[] weights;

	public WeightedAlphaBetaThread(MCBoard board, int[] weights) {
		this.board = board;
		depth = 3;
		moveEvals = new HashMap<Tuple, Integer>();
		for (Tuple t : board.legalMoves) {
			moveEvals.put(t, new Integer(0));
		}
		this.weights = weights;
	}

	public int alphabeta(int currentDepth, int alpha, int beta) {
    	// System.out.println("\n============\nAlpha-Beta called: depth " + currentDepth + " on position");
    	// System.out.println("Alpha at " + alpha + ", beta at " + beta);
    	// System.out.println(board);
	    if (currentDepth >= depth || board.legalMoves.isEmpty()) {
	    	int eval = board.eval(weights);
	    	evals++;
	    	// System.out.println("Returning eval: " + eval);
	    	// if (eval < -1000000 || eval > 1000000)
	    		// System.out.println("WIN DETECTED");
	    	return eval;
	    }
	    int x = infHolder.MIN;
	    for (int i=0;i<board.legalMoves.size();i++) {
	    		Tuple move = board.legalMoves.get(i).clone();
	    	// System.out.println("Now searching move " + move);
	        board.move(board.legalMoves.get(i));
	        int x0 = -1*alphabeta(currentDepth+1, beta*-1, alpha*-1);
	        if (x0 >= beta) {
	        	// System.out.println("Move " + move + " better than beta at eval " + x0);
	        	board.takeBack(1);
	        	return beta;
	        }
	        if (x0 > alpha) {
	        	// System.out.println("Move " + move + " sets alpha to " + x0);
	        	alpha = x;
	        }
	        x = max(x,x0);
	        board.takeBack(1);
	        // MoveRecord oldMove = board.takeBack(1);
	        // System.out.println("Took back " + oldMove.move + " at depth " + currentDepth);
	    }
	    return x;
	}

	public void run() {
		startTime = System.currentTimeMillis();
		while(System.currentTimeMillis() - startTime < 30000) {
			for (Tuple t : moveEvals.keySet()) {
				board.move(t);
				moveEvals.put(t, -1*alphabeta(0, infHolder.MIN, infHolder.MAX));
				board.takeBack(1);
			}
			if (verbose)
				System.out.println(toString());
			depth += 1;
			// System.out.println("CRASHING ON PURPOSE LOL" + 0/0);
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
	public String toString(){
		String out = "";
		if (startTime != -1L) {
			long evalTime = System.currentTimeMillis() - startTime;
			out = "Depth " + depth + ", " + evals + " positions evaluated in " + evalTime + " millisenconds:\n";
			for (Tuple t : moveEvals.keySet()) {
				out += t.toString() + "\t" + moveEvals.get(t) + "\n";
			}
		} else {
			out = "Minimax thread not yet running";
		}
		return out;
	}
}


