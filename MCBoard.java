import java.util.ArrayList;
import java.util.HashMap;

public class MCBoard {
    public int grid[][] = new int[8][8];
    public boolean turn = false;
    public ArrayList<Tuple> legalMoves;
    // Maps grid spots to an ArrayList of ArrayList 
    public static XYWins winMap = new XYWins();
    // Order prioritizes middle
    public static int[] orderedMoves = new int[] {3,4,2,5,1,6,0,7};
    public ArrayList<Move> moveList;
    public int movesPlayed;
    
    public MCBoard() {
        legalMoves = new ArrayList<Tuple>();
        for (int i=0; i < grid.length; i++) {
            for (int j=0; j<grid[i].length; j++){
                grid[i][j] = 2;
            }
        }
        for (int row: orderedMoves) {
            legalMoves.add(new Tuple(row, 0));
            legalMoves.add(new Tuple(row, 7));
        }
        moveList = new ArrayList<Move>();
        movesPlayed = 0;
    }
    
    public void move(int r, int c) {
        Tuple move = new Tuple(r,c);
        if (!legalMoves.contains(move)) {
            print("Illegal move. Here is a list of available moves:\n");
            for(Tuple m : legalMoves) {
                print(m + "\n");
            }
        } else {
            grid[r][c] = turn? 1:0;
            turn = !turn;
            movesPlayed++;
            legalMoves.remove(move);
            // check left
            if (c!=0){
                if (grid[r][c-1]==2) {
                    legalMoves.add(new Tuple(r, c-1));
                }
            }
            // check right
            if (c!=7){
                if (grid[r][c+1]==2) {
                    legalMoves.add(new Tuple(r, c+1));
                }
            }
            movesPlayed++;
        }
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
    
    //TODO:
        //Put all the lines into a set and create a function to check them
    // returns 2 for nobody, 0 for O and 1 for X
    public int winner() {
        int w;
        // check rows
        for (int c=2; c<6; c++) {
            for (int r: orderedMoves){
                w = grid[r][c-2];
                if (w != 2) {
                    for(int c2 = c-1; c2 <= c+2; c2++){
                        if (w != grid[r][c2]) {
                            w = 2;
                            break;
                        }
                    }
                if (w != 2)
                    return w;
                }
            }
        }
        //check columns
        for (int r=2; r<6; r++) {
            for (int c: orderedMoves){
                w = grid[r-2][c];
                if (w != 2) {
                    for(int r2 = r-1; r2 <= r+2; r2++){
                        if (w != grid[r2][c]) {
                            w = 2;
                            break;
                        }
                    }
                if (w != 2)
                    return w;
                }
            }
        }
        // check diagonals
        for (int r=2;r<6;r++){
            for (int c=2;c<6;c++){
                // NW - SE
                w = grid[r-2][c-2];
                if (w != 2) {
                    for(int i=-1; i<=2; i++){
                        if (w != grid[r+i][c+i]) {
                            w = 2;
                            break;
                        }
                    }
                if (w != 2)
                    return w;
                }
                // NE - SW
                w = grid[r+2][c-2];
                if (w != 2) {
                    for(int i=1; i>=-2; i--){
                        if (w != grid[r+i][c-i]) {
                            w = 2;
                            break;
                        }
                    }
                if (w != 2)
                    return w;
                }
            }
        }
        return 2;
    }

    public static void print (Object s) {
        System.out.print(s);
    }
    
/*
    public static void main(String args[]) {
        MCBoard test = new MCBoard();
        test.grid[2][0] = 0;
        test.grid[2][1] = 1;
        test.grid[3][0] = 0;
        test.grid[3][1] = 0;
        test.grid[3][2] = 1;
        test.grid[3][3] = 0;
        test.grid[4][0] = 1;
        test.grid[4][1] = 1;
        test.grid[4][2] = 0;
        test.grid[5][0] = 1;
        
        print(test);
        
        test.move(5,1);
        test.move(6,0);
        
        print(test);
    }*/
}