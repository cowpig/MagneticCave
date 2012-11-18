public class MCBoard {
    public int grid[][] = new int[8][8];
    public boolean turn = false;
    public ArrayList<Tuple> legalMoves;
    // Order prioritizes middle
    public static int[] orderedMoves = new int[] {3,4,2,5,1,6,0,7};
    public ArrayList<Move> moveList;
    
    public MCBoard() {
        legalMoves = new ArrayList<Tuple>();
        for (int row: orderedMoves) {
            legalMoves.add(new Tuple(row, 0);
            legalMoves.add(new Tuple(row, 7);
        }
    }

    public static void main(String args[]) {
        MCBoard test = new MCBoad);
        test.grid[2][0]=0;
        test.grid[2][1]=1;
        test.grid[3][0]=0;
        test.grid[3][1]=0;
        test.grid[3][2]=1;
        test.grid[3][3]=0;
        test.grid[4][0]=1;
        test.grid[4][1]=1;
        test.grid[4][2]=0;
        test.grid[5][0]=1;
        
        print(test);
        
        test.move(5,1);
        test.move(6,0);
        
        print(test);

    }
    
    public void move(Tuple rc) {
        if (!legalMoves.contains(rc)) {
            print("Illegal move.");
        }
        grid[x][y] = turn? 1:0;
        turn = !turn;
    }

    public String toString() {
        String out = "";
        out += "   0 1 2 3 4 5 6 7\n\n";
        for (int i=0; i < grid.length; i++) {
            out += i + " [";
            for (int j=0; j<grid[i].length; j++){
                out += convert(grid[i][j]) + " ";
            }
            out += "] " + i + "\n";
        }
        out += "\n   0 1 2 3 4 5 6 7\n\n";
        return out;
    }
    
    private String convert(int x) {
        if (x == 2)
            return "-";
        else if (x == 0)
            return "O";
        else if (x == 1)
            return "X";
        else
            return "e";
    }

    public static void print (Object s) {
        System.out.print(s);
    }
}

