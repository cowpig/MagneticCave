import java.lang.Math;

public class RandomPlayer extends Player{
	public MCBoard board;
	char type;
	public String name = "Random Player";
	public boolean log = false;
	public int[] weights;

	public RandomPlayer(MCBoard board, String name) {
		this.board = board;
		this.name = name;
	}

	public synchronized void getMove(){
		int i = (int)(Math.random() * board.legalMoves.size());
		System.out.println("Randomly choosing move " + board.legalMoves.get(i));
		board.move(board.legalMoves.get(i));
	}

	public String toString(){
		return this.name;
	}
}