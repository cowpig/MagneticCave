// An iterative deepening minimax algorithm that uses an MCBoard with alpha-beta pruning
import java.util.HashMap;
import java.lang.Math;
import java.util.concurrent.CyclicBarrier;

public class WeightedMultiThread extends Thread {
	public MCBoard board;
	public int depth;
	public Integer eval;
	public int evals = 0;
	public int[] weights;
	CyclicBarrier barrier;
	public long startTime = -1L;
	public Tuple move;

	public WeightedMultiThread(MCBoard board, int[] weights, CyclicBarrier barrier) {
		this.board = board;
		depth = 5;
		this.eval = 0;
		this.weights = weights;
		this.barrier = barrier;
		this.move = board.lastMove(0);
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
	    	//	Tuple move = board.legalMoves.get(i).clone();
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
		while(true) {
			int nextEval = alphabeta(0, infHolder.MIN, infHolder.MAX);
			try{barrier.await();} catch (Exception e) {}
			eval = nextEval;
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
			out = move + " : " + eval + "\td " + depth + ", e " + evals;
		} else {
			out = move + " not evaluated, thread not started.";
		}
		return out;
	}
}


