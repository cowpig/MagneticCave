public abstract class Player {
	public MCBoard board;
	public String name;

	public abstract void getMove();
		// This should get a move from the board.

	public abstract String toString();
}