// An iterative deepening minimax algorithm that uses an MCBoard
import java.util.HashMap;
import java.lang.Math;

public class MinimaxThread implements Runnable {
	public MCBoard board;
	public int depth;
	public HashMap<Tuple, Integer> moveEvals;
	// public HashMap<Tuple, ArrayList<moveRecord>> moves;
	boolean cont = true;
	boolean verbose = false;
	public int evals = 0;
	public long startTime = -1L;

	public MinimaxThread(MCBoard board) {
		this.board = board;
		depth = 1;
		moveEvals = new HashMap<Tuple, Integer>();
		for (Tuple t : board.legalMoves) {
			moveEvals.put(t, new Integer(0));
			// moves.put(t, new ArrayList<Tuple>());
		}
	}

	public MinimaxThread(MCBoard board, int initialDepth) {
		this.board = board;
		depth = initialDepth;
		moveEvals = new HashMap<Tuple, Integer>();
		for (Tuple t : board.legalMoves) {
			moveEvals.put(t, new Integer(0));
			// moves.put(t, new ArrayList<Tuple>());
		}
	}

	public MinimaxThread(MCBoard board, int initialDepth, boolean verbose) {
		this.board = board;
		depth = initialDepth;
		moveEvals = new HashMap<Tuple, Integer>();
		for (Tuple t : board.legalMoves) {
			moveEvals.put(t, new Integer(0));
			// moves.put(t, new ArrayList<Tuple>());
		}
		this.verbose = verbose;
	}

	public int minimax(int currentDepth) {
    	// System.out.println("Minimax called: depth " + currentDepth + " on position");
    	// System.out.println(board);
	    if (currentDepth == depth || board.legalMoves.isEmpty()) {
	    	int eval = board.eval();
	    	evals++;
	    	// System.out.println("Returning eval: " + eval);
	    	// if (eval < -1000 || eval > 1000)
	    		// System.out.println("WIN DETECTED");
	    	return eval;
	    }
	    int x = infHolder.MIN;
	    for (int i=0;i<board.legalMoves.size();i++) {
	    	// System.out.println("Now searching move " + board.legalMoves.get(i));
	        board.move(board.legalMoves.get(i));
	       
	        //VERBOSE VERSION
	        // next = -1*minimax(currentDepth+1);
	        // if (next > x) {
	        // 	moves.get(board.lastMove(2)).set(currentDepth, board.lastMove(1));
	        // }
	        // OPTIAML VERSION
	        x = max(x, -1*minimax(currentDepth+1));


	        MoveRecord oldMove = board.takeBack(1);
	        // System.out.println("Took back " + oldMove.move + " at depth " + currentDepth);
	    }
	    return x;
	}

	public void run() {
		startTime = System.currentTimeMillis();
		while(cont) {
			for (Tuple t : moveEvals.keySet()) {
				moveEvals.put(t, minimax(0));
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


