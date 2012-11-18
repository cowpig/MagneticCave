import java.util.Scanner;

public class GamePlayer {
	public static void main(String[] args) {
		Scanner key = new Scanner(System.in);
		MCBoard board = new MCBoard();
		while (board.winner() == 2 && board.movesPlayed < 64) {
			println(board);
			println("Enter the next move");
			try {
				String input = key.next();
				int r = Integer.parseInt(input.substring(0, 1));
				int c = Integer.parseInt(input.substring(1));
				board.move(r, c);
			} catch (NumberFormatException e) {
				println("Invalid input. The correct format is 'XY' where x is the row digit and Y is the column digit.");
			}
		}
		println(board);
		if (board.movesPlayed < 64)
			println("Game over. " + (board.winner()==0? "O" : "X") + " wins.");
		else
			println("The game ended in a tie.");
		key.close();
	}
    public static void println(Object s) {
        System.out.println(s);
    }
}
