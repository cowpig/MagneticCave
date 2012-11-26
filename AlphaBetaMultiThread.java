// An iterative deepening minimax algorithm that uses an MCBoard with alpha-beta pruning
import java.util.HashMap;
import java.lang.Math;

public class AlphaBetaMultiThread extends Thread {
	public MCBoard board;
	public int depth;
	public Integer maxDepth;
	boolean verbose = false;
	public Tuple move;
	public int evals = 0;
	public long startTime = -1L;
	Integer eval;
	GameThread manager;

	public AlphaBetaMultiThread(MCBoard board, Integer maxDepth, GameThread manager) {
		this.board = board;
		depth = 1;
		this.maxDepth = maxDepth;
		move = board.lastMove(1);
		eval = board.eval();
		this.manager = manager;
	}

	public AlphaBetaMultiThread(MCBoard board, Integer maxDepth, int initialDepth, GameThread manager) {
		this.board = board;
		depth = initialDepth;
		this.maxDepth = maxDepth;
		move = board.lastMove(1);
		eval = board.eval();
		this.manager = manager;
	}

	// public AlphaBetaMultiThread(MCBoard board, int initialDepth, boolean verbose) {
	// 	this.board = board;
	// 	depth = initialDepth;
	// 	move = board.lastMove(1);
	// 	this.verbose = verbose;
	// }

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
		while(true){
			if (depth >= maxDepth.intValue()) {
				this.setPriority(4);
				this.yield();
				break;
			} else {
				this.setPriority(5);
			}
			System.out.println("Evaluation running for " + move);
			eval = -1*alphabeta(0, infHolder.MIN, infHolder.MAX);
			manager.signalDepth();
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
		String out;
		if (startTime != -1L) {
			long evalTime = System.currentTimeMillis() - startTime;
			out = move.toString() + ": " + eval + "\t" + evals + " evals in " + evalTime + " ms at depth " + depth;
		} else {
			out = "Minimax thread not yet running";
		}
		return out;
	}
}
