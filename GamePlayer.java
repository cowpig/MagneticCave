import java.util.Scanner;

public class GamePlayer {
	public static void main(String[] args) {
		Player x;
		Player o;
		MCBoard board = new MCBoard();
		Scanner key = new Scanner(System.in);
		if (args.length == 2) {
			x = new HumanPlayer(board, key);
			o = new HumanPlayer(board, key);
		} else {
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
