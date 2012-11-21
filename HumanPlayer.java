import java.util.Scanner;

public class HumanPlayer extends Player {
	public HumanPlayer(MCBoard board){
		this.board = board;
	}
	public void getMove(){
		Scanner key = new Scanner(System.in);
		println("Enter the next move");
		String input = key.nextLine();
		if (input.length() > 3) {
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
		key.close();
	}
	public String toString(){
		return ("Human");
	}
	public void println(Object o){
		System.out.println(o);
	}
}