// An iterative deepening minimax algorithm that uses an MCBoard with alpha-beta pruning
// This version is designed to be run in parallel

// TODO: big time overhaul

import java.util.HashMap;
import java.lang.Math;

public class AlphaBetaMultiThread implements Thread {
	public MCBoard board;
	public int depth;
	public HashMap<Tuple, Integer> moveEvals;
	boolean cont = true;
	boolean verbose = false;
	public int evals = 0;
	public long startTime = -1L;

	public AlphaBetaMultiThread(MCBoard board) {
		this.board = board;
		depth = 1;
		moveEvals = new HashMap<Tuple, Integer>();
		for (Tuple t : board.legalMoves) {
			moveEvals.put(t, new Integer(0));
		}
	}

	public AlphaBetaMultiThread(MCBoard board, int initialDepth) {
		this.board = board;
		depth = initialDepth;
		moveEvals = new HashMap<Tuple, Integer>();
		for (Tuple t : board.legalMoves) {
			moveEvals.put(t, new Integer(0));
		}
	}

	public AlphaBetaMultiThread(MCBoard board, int initialDepth, boolean verbose) {
		this.board = board;
		depth = initialDepth;
		moveEvals = new HashMap<Tuple, Integer>();
		for (Tuple t : board.legalMoves) {
			moveEvals.put(t, new Integer(0));
		}
		this.verbose = verbose;
	}

	public int alphabeta(int currentDepth, int alpha, int beta) {
    	// System.out.println("Alpha-Beta called: depth " + currentDepth + " on position");
    	// System.out.println(board);
	    if (currentDepth >= depth || board.legalMoves.isEmpty()) {
	    	int eval = board.eval();
	    	evals++;
	    	// System.out.println("Returning eval: " + eval);
	    	// if (eval < -1000 || eval > 1000)
	    	// 	System.out.println("WIN DETECTED");
	    	return eval;
	    }
	    int x = infHolder.MIN;
	    for (int i=0;i<board.legalMoves.size();i++) {
	    	// System.out.println("Now searching move " + board.legalMoves.get(i));
	        board.move(board.legalMoves.get(i));
	        int x0 = -1*alphabeta(currentDepth+1, beta*-1, alpha*-1);
	        if (x0 >= beta) {
	        	board.takeBack(1);
	        	return beta;
	        }
	        if (x0 > alpha) {
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
		while(cont) {
			for (Tuple t : moveEvals.keySet()) {
				moveEvals.put(t, alphabeta(0, infHolder.MIN, infHolder.MAX));
			}
			if (verbose)
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


