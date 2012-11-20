import java.util.ArrayList;
import java.util.HashMap;
import java.lang.Math;

public class Tester {
	public static void main(String[] args){
		print(-1 * Integer.MAX_VALUE);
		print(-1 * Integer.MIN_VALUE);
		// testXYWins();
		// testMCBoardClone();
		testMinimax();
	}
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