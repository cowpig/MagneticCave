public class XYWins {
    public HashMap<Tuple, ArrayList<Tuple>> winMap;
	public XYWins {
        winMap = new HashMap<Tuple, ArrayList>();
        for (int i=0; i<8; i++) {
            for (int j=0; j<8; j++){
                grid[i][j] = 2;
                ArrayList<Tuple> wins = new ArrayList<Tuple>();
                for (Tuple win : diagWins(i,j))
                    wins.add(win);
                for (Tuple win : horiWins(i,j))
                    wins.add(win);
                for (Tuple win : vertWins(i,j))
                    wins.add(win);
                xyWins.put(new Tuple(i,j), wins);
            }
        }
	}
    public ArrayList<Tuple> diagWins(int r, int c) {
        //TODO
        out = new ArrayList<Tuple>();
        return out;
    }
    public ArrayList<Tuple> horiWins(int r, int c) {
        //TODO
        out = new ArrayList<Tuple>();
        return out;
    }
    public ArrayList<Tuple> vertWins(int r, int c) {
        //TODO
        out = new ArrayList<Tuple>();
        return out;
    }
}