import java.io.*;
import java.util.HashMap;
import java.util.ArrayList;

public class WeightsFinder {
	public static void main(String[] args){
		MCBoard board = new MCBoard();

		ArrayList<Player> players = new ArrayList<Player>();
		players.add(new RandomPlayer(board, "Player Random1"));
		// players.add(new RandomPlayer(board, "Player Random2"));
		// int[] weights = new int[] {0,2,3,4,5};
		// players.add(new MultiThreadedComputerPlayer(board, "MPlayer {0,2,3,4,5}", weights));
		// weights = new int[] {0,1,2,4,16};
		// players.add(new MultiThreadedComputerPlayer(board, "MPlayer {0,1,2,4,16}", weights));
		// weights = new int[] {0,1,1,1,1};
		// players.add(new MultiThreadedComputerPlayer(board, "MPlayer {0,1,1,1,1}", weights));
		// weights = new int[] {0,1,4,16,64};
		// players.add(new MultiThreadedComputerPlayer(board, "MPlayer {0,1,4,16,64}", weights));
		int[] weights = new int[] {0,2,3,4,5};
		players.add(new ComputerPlayer(board, "CPlayer {0,2,3,4,5}", weights));
		weights = new int[] {0,1,2,4,16};
		players.add(new ComputerPlayer(board, "CPlayer {0,1,2,4,16}", weights));
		weights = new int[] {0,1,1,1,1};
		players.add(new ComputerPlayer(board, "CPlayer {0,1,1,1,1}", weights));
		weights = new int[] {0,1,4,16,64};
		players.add(new ComputerPlayer(board, "CPlayer {0,1,4,16,64}", weights));
		weights = new int[] {1,2,3,4,5};
		players.add(new ComputerPlayer(board, "CPlayer {1,2,3,4,5}", weights));
		weights = new int[] {1,1,2,4,16};
		players.add(new ComputerPlayer(board, "CPlayer {1,1,2,4,16}", weights));
		weights = new int[] {1,1,1,1,1};
		players.add(new ComputerPlayer(board, "CPlayer {1,1,1,1,1}", weights));
		weights = new int[] {1,1,4,16,64};
		players.add(new ComputerPlayer(board, "CPlayer {1,1,4,16,64}", weights));


		HashMap<Player, Float> scores = new HashMap<Player, Float>();
		HashMap<Player, String> reports = new HashMap<Player, String>();
		for (Player p : players) {
			System.out.println(p);
			scores.put(p, new Float(0.));
			reports.put(p,"");
		}

		for (Player p : players)
			System.out.println(p + " has a starting score of " + scores.get(p));

		for (Player x : players){
			for (Player o : players) {
				if (x == o)
					break;
				board.reset();
				while (board.winStatus == 2 && board.moveList.size() < 64) {
					System.out.println(board);
					if (board.turn)
						x.getMove();
					else
						o.getMove();
				}
				if (board.winStatus == 1) {
					System.out.println("Recording win for player " + x);
					scores.put(x, new Float(scores.get(x).floatValue() + 1));
					reports.put(x, reports.get(x) + "win vs " + o + "\n");
					reports.put(o, reports.get(o) + "loss vs " + x + "\n");
				} else if (board.winStatus == 0) {
					System.out.println("Recording win for player " + o);
					scores.put(o, new Float(scores.get(o).floatValue() + 1));
					reports.put(x, reports.get(x) + "loss vs " + o + "\n");
					reports.put(o, reports.get(o) + "win vs " + x + "\n");
				} else {
					System.out.println("Recording tie for players " + x + " and " + o);
					scores.put(x, new Float(scores.get(x).floatValue() + 0.5));
					scores.put(o, new Float(scores.get(o).floatValue() + 0.5));
					reports.put(x, reports.get(x) + "draw vs " + o + "\n");
					reports.put(o, reports.get(o) + "draw vs " + x + "\n");
				}
			}
			System.out.println("STANDINGS AFTER THAT GAME:");
			for (Player p : players) {
		    	System.out.println(p + "\t\t" + scores.get(p).floatValue());
		    }
		}
		String outFile = "output" + (int)(Math.random()*10000);
		System.out.println("Recording final scores to file...");
		try {
	    	PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outFile, true)));
		    for (Player p : players) {
		    	System.out.println(p + "\t\t" + scores.get(p).floatValue());
		    	out.println(p + "\t\t" + scores.get(p).floatValue());
		    }
	    	System.out.println("===Player summaries===");
	    	out.println("===Player summaries===");
		    for (Player p : players) {
		    	System.out.println(p + "\n-----\n" + reports.get(p));
		    	out.println(p + "\n-----\n" + reports.get(p));
		    }
		    out.close();
		} catch (IOException e) {
		    //oh noes!
		}

	}
}