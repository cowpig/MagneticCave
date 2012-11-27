// An iterative deepening minimax algorithm that uses an MCBoard with alpha-beta pruning
import java.util.HashMap;
import java.lang.Math;
import java.util.concurrent.CyclicBarrier;
import java.util.LinkedList;

public class WeightedMultiThread extends Thread {
	public MCBoard board;
	public int depth;
	public Integer eval;
	public int evals = 0;
	public int[] weights;
	CyclicBarrier barrier;
	public long startTime = -1L;
	public Tuple move;
	boolean cont = true;

	public WeightedMultiThread(MCBoard board, int[] weights, CyclicBarrier barrier) {
		this.board = board;
		depth = 4;
		this.eval = 0;
		this.weights = weights;
		this.barrier = barrier;
		this.move = board.lastMove(0);
	}

	public int alphabeta_min(int currentDepth, int alpha, int beta) {
		if (!cont)
			return 0;
	    if (currentDepth >= depth || board.legalMoves.isEmpty()) {
	    	if (currentDepth==0 && board.winStatus != 2) {
	    		return Integer.MAX_VALUE;
	    	}
	    	int eval = -1*board.eval(weights);
	    	evals++;
	    	return eval;
	    }
	    int b = infHolder.MAX + 1;
	    LinkedList<Tuple> list = (LinkedList<Tuple>) board.legalMoves.clone();
	    for (Tuple chosenMove : list) {
	        board.move(chosenMove);
	        int s = alphabeta_max(currentDepth+1, alpha, beta);
	        b = min(b, s);
	        if (b < alpha) {
		        	board.takeBack(1);
	        	return b;
	        }
	        if (b < beta) {
	        	beta = b;
	        }
	        	board.takeBack(1);
	    }
	    return b;
	}

	public int alphabeta_max(int currentDepth, int alpha, int beta) {
		if (!cont)
			return 0;
	    if (currentDepth >= depth || board.legalMoves.isEmpty()) {
	    	int eval = board.eval(weights);
	    	evals++;
	    	return eval;
	    }
	    int a = infHolder.MIN - 1;
	    LinkedList<Tuple> list = (LinkedList<Tuple>) board.legalMoves.clone();
	    for (Tuple chosenMove : list) {
	        board.move(chosenMove);
	        int s = alphabeta_min(currentDepth+1, alpha, beta);
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
	    return a;
	}

	public void run() {
		startTime = System.currentTimeMillis();
		while(cont) {
			int nextEval = alphabeta_min(0, infHolder.MIN, infHolder.MAX);
			try{barrier.await();} catch (Exception e) {}
			if (cont) {
				eval = nextEval;
				depth += 1;
				// System.out.println(this.getName() + " now at depth " + depth);
			}
		}
	}

	public void kill(){
		this.cont = false;
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
			out = move + " : " + eval + "\td " + depth + ", e " + evals;
		} else {
			out = move + " not evaluated, thread not started.";
		}
		return out;
	}
}



