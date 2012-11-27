import java.util.concurrent.CyclicBarrier;
import java.util.ArrayList;
import java.util.HashMap;

public class MultiThreadedComputerPlayer extends Player{
	public MCBoard board;
	public String name = "Computer Player";
	public boolean log = false;
	public int[] weights;
	public long timePerMove;
	public ArrayList<WeightedMultiThread> threadList;
	public HashMap<Tuple, Integer> moveEvals;

	public MultiThreadedComputerPlayer(MCBoard board, String name, int[] weights) {
		this.board = board;
		this.name = name;
		this.weights = weights;
		this.timePerMove = 2950L;
	}

	public void getMove(){
		int n = board.legalMoves.size();
		threadList = new ArrayList<WeightedMultiThread>();
		moveEvals = new HashMap<Tuple, Integer>();

		// Whenever a thread finishes its work at a given depth, it awaits() the other threads
		// When all threads are finished, the move evaluations are updated and the threads continue their work.
		CyclicBarrier barrier = new CyclicBarrier(n, new Runnable(){
			public void run() {
				System.out.println("Updating evals...");
				for(WeightedMultiThread t : threadList){
					moveEvals.put(t.move, t.eval);
				}
				System.out.println("...done. Threads may now continue.");
			}
		});

		// Prepare and start the threads
		for (Tuple move : board.legalMoves) {
			MCBoard nextBoard = board.clone();
			nextBoard.move(move);
			threadList.add(new WeightedMultiThread(nextBoard, weights, barrier));
			moveEvals.put(move, 0);
		}
		for (WeightedMultiThread t : threadList) {t.start();}

		// Let the threads run for the maximum amount of time per move
		try {
			Thread.sleep(timePerMove);
		} catch (InterruptedException e) {System.out.println(e);}
		for (WeightedMultiThread t : threadList) {
			t.kill();
		}
		for (WeightedMultiThread t : threadList) {
			System.out.println(t.getName() + " stopped.");
			try {
				t.join();
			} catch (InterruptedException e) {System.out.println(e);}
		}

		// Play the best move
		Integer best = infHolder.MIN;
		Tuple nextMove = board.legalMoves.get(0);
		for (Tuple m : board.legalMoves) {
			if (moveEvals.get(m) > best) {
				best = moveEvals.get(m);
				nextMove = m;
			}
		}
		System.out.println(nextMove + " is the choice of " + name + " given evals:");
		for (WeightedMultiThread t : threadList) {
			System.out.println(t);
		}
		board.move(nextMove);
	}
	public String toString(){
		return this.name;
	}
}