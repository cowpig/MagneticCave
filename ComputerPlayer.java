public class ComputerPlayer extends Player{
	public MCBoard board;
	char type;
	public String name = "Computer Player";
	public boolean log = false;
	public int[] weights;
	public long timePerMove;

	public ComputerPlayer(MCBoard board, String name, int[] weights) {
		this.board = board;
		this.name = name;
		this.type = 'w';
		this.weights = weights;
		this.timePerMove = 3000L;
	}

	public ComputerPlayer(MCBoard board, String name, int[] weights, char type) {
		this.board = board;
		this.name = name;
		this.type = type;
		this.weights = weights;
		this.timePerMove = 3000L;
	}

	public synchronized void getMove(){
		if (type == 'w') {
			WeightedAlphaBetaThread engine = new WeightedAlphaBetaThread(board.clone(), weights);
			engine.start();
			try {
				Thread.sleep(timePerMove);
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
		if (type == 'v') {
			VerboseWeightedAlphaBetaThread engine = new VerboseWeightedAlphaBetaThread(board.clone(), weights);
			engine.start();
			try {
				Thread.sleep(200L);
			} catch (InterruptedException e) {System.out.println(e);}
			engine.stop();
			int best = infHolder.MIN;
			Tuple nextMove = board.legalMoves.get(0);
			for (Tuple t : board.legalMoves) {
				if (engine.moveEvals.get(t).score > best) {
					best = engine.moveEvals.get(t).score;
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