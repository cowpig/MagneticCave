import java.util.Scanner;

public class GamePlayer {
	public static void main(String[] args) {
		Scanner key = new Scanner(System.in);
		MCBoard board = new MCBoard();
		while (board.winner() == 2 && board.moveList.size() < 64) {
			println(board);
			println("Enter the next move");
			String input = key.nextLine();
			if (input.length() > 2) {
				try {
					if (input.substring(0,4).equalsIgnoreCase("back")) {
						int n = Integer.parseInt(input.substring(5));
						board.takeBack(n);
					} 
				} catch (NumberFormatException e) {
						println("Invalid input. The correct format is 'back N' where N is the number of moves to go back.");
				} catch (IllegalMoveException e) {
						println(e);
				} catch (StringIndexOutOfBoundsException e) {}

			} else {
				try {
					int r = Integer.parseInt(input.substring(0, 1));
					int c = Integer.parseInt(input.substring(1));
					board.move(new Tuple(r,c));
				} catch (NumberFormatException e) {
					println("Invalid input. The correct format is 'XY' where x is the row digit and Y is the column digit.");
				} catch (IllegalMoveException e) {
					println(e);
				}
			}
		}
		println(board);
		if (board.moveList.size() < 64)
			println("Game over. " + (board.winner()==0? "O" : "X") + " wins.");
		else
			println("The game ended in a tie.");
		key.close();
	}
    public static void println(Object s) {
        System.out.println(s);
    }
}
