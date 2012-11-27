import java.util.Scanner;

public class GamePlayer {
	public static void main(String[] args) {
		for (String arg : args) {
			System.out.println(arg);
		}
		System.out.println(args.length);
		Player x=null;
		Player o=null;
		MCBoard board = new MCBoard();
		Scanner key = new Scanner(System.in);
		if (args.length == 1) {
			if (args[0].equals("c1")){
				System.out.println("Beginning Magnetic Cave: Computer vs Human.");
				int[] weights = {0,2,3,4,5};
				x = new ComputerPlayer(board, "Computer Player", weights);
				o = new HumanPlayer(board, key);
			} else if (args[0] == "c2") {
				System.out.println("Beginning Magnetic Cave: Human vs Computer.");
				int[] weights = {0,2,3,4,5};
				o = new ComputerPlayer(board, "Computer Player", weights);
				x = new HumanPlayer(board, key);
			}
		}
		if (x == null || o == null) {
			System.out.println("Beginning Magnetic Cave with two human players.");
			x = new HumanPlayer(board, key);
			o = new HumanPlayer(board, key);
		}
		while (board.winStatus == 2 && board.moveList.size() < 64) {
			println(board);
			if (board.turn)
				x.getMove();
			else
				o.getMove();
		}
		println(board);
		if (board.moveList.size() < 64)
			println("Game over. " + (board.winStatus==0? "X" : "O") + " wins.");
		else
			println("The game ended in a tie.");
		key.close();
	}
    public static void println(Object s) {
        System.out.println(s);
    }
}
