import java.util.HashMap;
import java.lang.Math;

public class GameThread extends Thread {
		public MCBoard board;
		public Integer depth;
		public HashMap<Tuple, Integer> moveEvals;
		boolean verbose = false;
		public int evals = 0;
		public long startTime = -1L;
		public AlphaBetaMultiThread[] threadList;
		public int signals = 0;
		
		public GameThread(MCBoard board) {
			this.board = board;
			this.depth = 2;
			this.moveEvals = new HashMap<Tuple, Integer>();
		}

		public GameThread(MCBoard board, int initialDepth) {
			this.board = board;
			this.depth = initialDepth;
			this.moveEvals = new HashMap<Tuple, Integer>();
		}

		public GameThread(MCBoard board, int initialDepth, boolean verbose) {
			this.board = board;
			this.depth = initialDepth;
			this.verbose = verbose;
			this.moveEvals = new HashMap<Tuple, Integer>();
		}
		
		public void run(){
			startTime = System.currentTimeMillis();
			int nMoves = board.legalMoves.size();
			threadList = new AlphaBetaMultiThread[nMoves];
			for (int i=0; i<nMoves; i++){
				MCBoard nextBoard = board.clone();
				Tuple nextMove = board.legalMoves.get(i);
				nextBoard.move(nextMove);
				threadList[i] = new AlphaBetaMultiThread(nextBoard, depth.intValue()-1, depth, this);
				threadList[i].setPriority(5);
				moveEvals.put(nextMove, threadList[i].eval);
			}
			System.out.println("Starting threads");
			for (AlphaBetaMultiThread t : threadList) {
				t.start();
			}
			try {
				evals = 0;
				for (AlphaBetaMultiThread t : threadList) {
					t.join();
					evals += t.evals;
				}
			} catch (Exception e) {
				System.out.println("Error joining threads: " + e);
			}
		}

		public void killAll(){
			for (Thread t : threadList)
				t.destroy();
			this.destroy();
		}

		public synchronized void signalDepth() {
			signals++;
			if (signals % threadList.length == 0){
				if (verbose)
					System.out.println(toString());
				depth++;
			}
		}
	public String toString(){
		String out = "";
		if (startTime != -1L) {
			long evalTime = System.currentTimeMillis() - startTime;
			out = "GameThread depth " + depth + ", " + evals + " positions evaluated in " + evalTime + " millisenconds:\n";
			for (Thread t : threadList) {
				out += t.toString() + "\n";
			}
		} else {
			out = "GameThread not yet running";
		}
		return out;
	}
}