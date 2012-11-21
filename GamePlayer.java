import java.util.Scanner;

public class GamePlayer {
	public static void main(String[] args) {
		Player x;
		Player o;
		MCBoard board = new MCBoard();
		if (args.length == 2) {
			x = new HumanPlayer(board);
			o = new HumanPlayer(board);
		} else {
			System.out.println("Beginning Magnetic Cave with two human players.");
			x = new HumanPlayer(board);
			o = new HumanPlayer(board);
		}
		while (board.winner() == 2 && board.moveList.size() < 64) {
			println(board);
			if (board.turn)
				x.getMove();
			else
				o.getMove();
		}
		println(board);
		if (board.moveList.size() < 64)
			println("Game over. " + (board.winStatus==0? "O" : "X") + " wins.");
		else
			println("The game ended in a tie.");
	}
    public static void println(Object s) {
        System.out.println(s);
    }
}
