import java.util.ArrayList;
import java.util.HashMap;
import java.lang.Math;

public class Tester {
	public static void main(String[] args){
		// testXYWins();
		// testMCBoardClone();
		// testMinimax();
		// testAlphaBeta();
		// testWeightedThread();
		// testComputerPlayer();
		testMultiThread();
		// testVerboseThread();
	}

	public static void testVerboseThread() {
		MCBoard board = new MCBoard();
		int[] weights = {0,2,3,4,5};
		board.move(2,0);
		board.move(7,0);
		board.move(3,0);
		board.move(7,7);
		board.move(4,0);
		board.move(7,6);
		ComputerPlayer cp = new ComputerPlayer(board, "Player C", weights, 'v');
		print(board);
		long start = System.currentTimeMillis();
		cp.getMove();
		long time = System.currentTimeMillis() - start;
		System.out.println("That took " + time + "ms to run, total.");
		System.out.println("-\t-\t-\t-\t-\t-\t-");
		print(board);
		while (board.winStatus == 2 && board.moveList.size() < 64) {
			start = System.currentTimeMillis();
			cp.getMove();
			time = System.currentTimeMillis() - start;
			System.out.println("That took " + time + "ms to run, total.");
		System.out.println("-\t-\t-\t-\t-\t-\t-");
			print(board);
		}
		System.out.println("Game over. Game history:\n");
		for (MoveRecord m : board.moveList){
			System.out.println(m.move);
		}
	}

	public static void testMultiThread() {
		MCBoard board = new MCBoard();
		int[] weights = {0,2,3,4,5};
		board.move(2,0);
		board.move(7,0);
		board.move(3,0);
		board.move(7,7);
		board.move(4,0);
		board.move(7,6);
		MultiThreadedComputerPlayer cp = new MultiThreadedComputerPlayer(board, "Quicky McCores", weights);
		print(board);
		long start = System.currentTimeMillis();
		cp.getMove();
		long time = System.currentTimeMillis() - start;
		System.out.println("That took " + time + "ms to run, total.");
		System.out.println("-\t-\t-\t-\t-\t-\t-");
		print(board);
		while (board.winStatus == 2 && board.moveList.size() < 64) {
			start = System.currentTimeMillis();
			cp.getMove();
			time = System.currentTimeMillis() - start;
			System.out.println("That took " + time + "ms to run, total.");
		System.out.println("-\t-\t-\t-\t-\t-\t-");
			print(board);
		}
		System.out.println("Game over. Game history:\n");
		for (MoveRecord m : board.moveList){
			System.out.println(m.move);
		}
		testMultiThread2();
	}
	public static void testMultiThread2(){
		System.out.println("===================== =========== =====================");
		System.out.println("===================== TEST PART 2 =====================");
		System.out.println("===================== =========== =====================");
		MCBoard board = new MCBoard();
		int[] weights = {0,2,3,4,5};
		MultiThreadedComputerPlayer cp = new MultiThreadedComputerPlayer(board, "Quicky McCores", weights);
		print(board);
		long start = System.currentTimeMillis();
		cp.getMove();
		long time = System.currentTimeMillis() - start;
		System.out.println("That took " + time + "ms to run, total.");
		print(board);
		while (board.winStatus == 2 && board.moveList.size() < 64) {
			start = System.currentTimeMillis();
			cp.getMove();
			time = System.currentTimeMillis() - start;
			System.out.println("That took " + time + "ms to run, total.");
		System.out.println("-\t-\t-\t-\t-\t-\t-");
			print(board);
		}
		System.out.println("Game over. Game history:\n");
		for (MoveRecord m : board.moveList){
			System.out.println(m.move);
		}
	}

	public static void testComputerPlayer(){
		MCBoard board = new MCBoard();
		int[] weights = {0,2,3,4,5};
		ComputerPlayer cp = new ComputerPlayer(board, "Player C", weights);
		print(board);
		long start = System.currentTimeMillis();
		cp.getMove();
		long time = System.currentTimeMillis() - start;
		System.out.println("That took " + time + "ms to run, total.");
		print(board);
		while (board.winStatus == 2 && board.moveList.size() < 64) {
			start = System.currentTimeMillis();
			cp.getMove();
			time = System.currentTimeMillis() - start;
			System.out.println("That took " + time + "ms to run, total.");
		System.out.println("-\t-\t-\t-\t-\t-\t-");
			print(board);
		}
		System.out.println("Game over. Game history:\n");
		for (MoveRecord m : board.moveList){
			System.out.println(m.move);
		}
	}

	public static void testWeightedThread(){
		MCBoard board = new MCBoard();
		board.move(5,0);
		board.move(4,0);
		board.move(3,0);
		board.move(3,1);
		board.move(3,2);
		board.move(3,3);
		board.move(4,1);
		board.move(4,2);
		board.move(4,3);
		board.move(5,1);
		board.move(1,0);
		board.move(2,0);
		board.move(2,1);
		board.move(6,0);
		board.move(5,2);
		print(board);
		print("Starting eval: " + board.eval() + "\n");
		int[] weights = {0,100,300,900,2700};
		WeightedAlphaBetaThread wabt = new WeightedAlphaBetaThread((MCBoard)board.clone(), weights);
		wabt.start();
	}

/*	public static void OLDtestMultiThread(){
		MCBoard board = new MCBoard();
		board.move(5,0);
		board.move(4,0);
		board.move(3,0);
		board.move(3,1);
		board.move(3,2);
		board.move(3,3);
		board.move(4,1);
		board.move(4,2);
		board.move(4,3);
		board.move(5,1);
		board.move(1,0);
		board.move(2,0);
		board.move(2,1);
		board.move(6,0);
		board.move(5,2);
		print(board);
		print("Starting eval: " + board.eval() + "\n");
		GameThread gt = new GameThread((MCBoard)board.clone(), 2, true);
		gt.start();
	}*/

	public static void testXYWins(){
		XYWins w = new XYWins();
		// for (Tuple k: w.winMap.keySet()) {
		// 	System.out.println(k.toString() + '\t' + w.getWins(k).toString());
		// }
		System.out.println(w.log);
		System.out.println(w.winMap.containsKey(new Tuple(0,0)));
		System.out.println(w.winMap.keySet());
		for (Tuple[] tl: w.winMap.get(new Tuple(0,0))){
			System.out.println();
			for (Tuple t : tl)
				System.out.println(t);
		}
		for (int i=0; i<8; i++) {
			System.out.println("");
			for (int j=0; j<8; j++) {
				ArrayList<Tuple[]> wins = w.getWins(i,j);
				if (wins != null) {
					System.out.print(w.getWins(i,j).size() + ", ");
				} else {
					System.out.print("0 ");
				}
			}
		}
	}
	public static void testMCBoardClone(){
		MCBoard board = new MCBoard();
		board.move(5,0);
		board.move(4,0);
		board.move(3,0);
		board.move(3,1);
		print(board);
		print("New board...");
		MCBoard board2 = board.clone();
		board2.move(5,1);
		print(board2);
		board2.takeBack(4);
		print(board2);
		board2.move(3,0);
		board2.move(3,1);
		board2.move(3,2);
		print(board2);
		print("Old board:");
		print(board);
	}

	public static void testAlphaBeta(){
		MCBoard board = new MCBoard();
		board.move(5,0);
		board.move(4,0);
		board.move(3,0);
		board.move(3,1);
		board.move(3,2);
		board.move(3,3);
		board.move(4,1);
		board.move(4,2);
		board.move(4,3);
		board.move(5,1);
		board.move(1,0);
		board.move(2,0);
		board.move(2,1);
		board.move(6,0);
		board.move(5,2);
		print(board);
		print("Starting eval: " + board.eval() + "\n");
		AlphaBetaThread abt = new AlphaBetaThread((MCBoard)board.clone(), 0, true);
		abt.run();
	}

	public static void testMinimax(){
		MCBoard board = new MCBoard();
		board.move(5,0);
		board.move(4,0);
		board.move(3,0);
		board.move(3,1);
		board.move(3,2);
		board.move(3,3);
		board.move(4,1);
		board.move(4,2);
		board.move(4,3);
		board.move(5,1);
		board.move(1,0);
		board.move(2,0);
		board.move(2,1);
		board.move(6,0);
		board.move(5,2);
		print(board);
		MinimaxThread mmt = new MinimaxThread((MCBoard)board.clone(), 2, true);
		mmt.run();
	}
	public static void print(Object o){
		System.out.print(o);
	}
}