public class ComputerPlayer extends Player{
	public MCBoard board;
	char type;
	public String name = "Computer Player";
	public boolean log = false;
	public int[] weights;

	public ComputerPlayer(MCBoard board, String name, char type) {
		this.board = board;
		this.name = name;
		this.type = type;
	}

	public ComputerPlayer(MCBoard board, String name, int[] weights) {
		this.board = board;
		this.name = name;
		this.type = 'w';
		this.weights = weights;
	}

	public synchronized void getMove(){
		if (type == 'w') {
			WeightedAlphaBetaThread engine = new WeightedAlphaBetaThread(board.clone(), weights);
			engine.start();
			try {
				Thread.sleep(2950L);
			} catch (InterruptedException e) {System.out.println(e);}
			engine.stop();
			Integer best = infHolder.MIN;
			Tuple nextMove = board.legalMoves.get(0);
			for (Tuple t : board.legalMoves) {
				if (engine.moveEvals.get(t) > best) {
					best = engine.moveEvals.get(t);
					nextMove = t;
				}
			}
			System.out.println(nextMove + " is the choice of " + name + " given evals:");
			System.out.println(engine);
			if (nextMove == null) {
				System.out.println("Error. null move chosen.");
				System.out.println(engine);
				System.out.println(board.dump());
			}
			board.move(nextMove);
		}
	}
	public String toString(){
		return this.name;
	}
}