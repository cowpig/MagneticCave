// An iterative deepening minimax algorithm that uses an MCBoard with alpha-beta pruning
import java.util.HashMap;
import java.util.LinkedList;
import java.lang.Math;

public class VerboseWeightedAlphaBetaThread extends Thread {
	public MCBoard board;
	public int depth;
	public HashMap<Tuple, LineScore> moveEvals;
	boolean verbose = false;
	public int evals = 0;
	public long startTime = -1L;
	public int[] weights;

	public VerboseWeightedAlphaBetaThread(MCBoard board, int[] weights) {
		this.board = board;
		depth = 2;
		moveEvals = new HashMap<Tuple, LineScore>();
		for (Tuple t : board.legalMoves) {
			moveEvals.put(t, new LineScore());
		}
		this.weights = weights;
	}

	public LineScore alphabeta_min(int currentDepth, int alpha, int beta, Tuple lastMove) {
	    if (currentDepth >= depth || board.legalMoves.isEmpty()) {
	    	if (currentDepth==0 && board.winStatus != 2) {
	    		System.out.println("Board state with immediate win:\n" + board);
	    		System.out.println("Returning highest possible value for immediate win at " + lastMove);
	    		return new LineScore("", Integer.MAX_VALUE);
	    	}
	    	System.out.println("Board state before eval return:\n" + board);
	    	int eval = -1*board.eval(weights);
	    	evals++;
	    	System.out.println("Returning eval: " + eval + " at min.");
	    	if (eval < -1000000 || eval > 1000000)
	    		System.out.println("WIN DETECTED FOR MAX!");
	    	return new LineScore("", eval);
	    }
	    Tuple bestMove=null;
	    String lineTrace=null;
	    LineScore lS=null;
	    int b = infHolder.MAX + 1;
	    LinkedList<Tuple> list = (LinkedList<Tuple>) board.legalMoves.clone();
	    for (Tuple chosenMove : list) {
	        board.move(chosenMove);
	    	System.out.println("Now searching move " + chosenMove + ", a=" + alpha + ", b=" + beta + " min");

	        lS = alphabeta_max(currentDepth+1, alpha, beta, chosenMove.clone());
	        if (b > lS.score) {
	    		bestMove = chosenMove;
	    		lineTrace = lS.line;
	        }
	        b = min(b, lS.score);
	        if (b < alpha) {
	        	System.out.println("Move " + chosenMove + " worse than alpha of " + alpha + ", (" + b + ")");
		        MoveRecord oldMove = board.takeBack(1);
		        System.out.println("Took back " + oldMove.move + " at depth " + currentDepth);
	        	return new LineScore(chosenMove + ", " + lS.line, b);
	        }
	        if (b < beta) {
	        	System.out.println("Move " + chosenMove + " sets beta to " + b + " (beta was " + beta + ")");
	        	beta = b;
	        }
	        MoveRecord oldMove = board.takeBack(1);
	        System.out.println("Took back " + oldMove.move + " at depth " + currentDepth);
	    }
	    return new LineScore(bestMove + ", " + lineTrace, b);
	}

	public LineScore alphabeta_max(int currentDepth, int alpha, int beta, Tuple lastMove) {
	    if (currentDepth >= depth || board.legalMoves.isEmpty()) {
	    	System.out.println("Board state before eval return:\n" + board);
	    	int eval = board.eval(weights);
	    	evals++;
	    	System.out.println("Returning eval: " + eval + " at max.");
	    	if (eval < -1000000 || eval > 1000000)
	    		System.out.println("WIN DETECTED FOR MIN!");
	    	return new LineScore("", eval);
	    }
	    Tuple bestMove=null;
	    String lineTrace=null;
	    LineScore lS=null;
	    int a = infHolder.MIN - 1;
	    LinkedList<Tuple> list = (LinkedList<Tuple>) board.legalMoves.clone();
	    for (Tuple chosenMove : list) {
	        board.move(chosenMove);
	    	System.out.println("Now searching move " + chosenMove + ", a=" + alpha + ", b=" + beta + " max");
	        if (lastMove.equals(new Tuple(0,0))){
	        	for (MoveRecord m : board.moveList){
	        		System.out.print(m.move + "[+" + m.newSpot + "],");
	        	}
	        	System.out.print("\n");
	        	for (Tuple m : board.legalMoves){
	        		System.out.print(m);
	        	}
	        	System.out.print("\n");
	        }
	        lS = alphabeta_min(currentDepth+1, alpha, beta, chosenMove.clone());
	        if (a < lS.score) {
	    		bestMove = chosenMove;
	    		lineTrace = lS.line;
	        }
	        a = max(a, lS.score);
	        if (a >= beta) {
	        	System.out.println("Move " + chosenMove + " better than beta of " + beta + " (" + a + ")");
		        MoveRecord oldMove = board.takeBack(1);
		        System.out.println("Took back " + oldMove.move + " at depth " + currentDepth);
	        	return new LineScore(chosenMove + ", " + lS.line, beta);
	        }
	        if (a > alpha) {
	        	System.out.println("Move " + chosenMove + " sets alpha to " + a + " (alpha was " + alpha + ")");
	        	alpha = a;
	        }
	        MoveRecord oldMove = board.takeBack(1);
	        System.out.println("Took back " + oldMove.move + " at depth " + currentDepth);
	    }
	    return new LineScore(bestMove + ", " + lineTrace, a);
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
				System.out.println("From run: moving " + t);
				board.move(t);
				LineScore lS = alphabeta_min(0, alpha, infHolder.MAX, t.clone());
				moveEvals.put(t, lS);
				if (lS.score > alpha) {
					System.out.println("Move " + t + " set alpha to " + lS.score + " from " + alpha + " at run.");
					alpha = lS.score;
				}
				MoveRecord tb = board.takeBack(1);
				System.out.println("From run: took back " + tb.move);
			}
			System.out.println(toString());
			depth += 1;
			break;
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


