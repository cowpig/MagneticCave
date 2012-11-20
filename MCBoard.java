import java.util.ArrayList;
import java.util.HashMap;

public class MCBoard implements Cloneable{
    public int grid[][] = new int[8][8];
    public boolean turn = true;

    // Keep a list of legal moves
    public ArrayList<Tuple> legalMoves;

    // Maps grid spots to an ArrayList of ArrayList 
    public static XYWins winMap = new XYWins();

    // Order prioritizes middle
    public static int[] orderedMoves = new int[] {3,4,2,5,1,6,0,7};

    // Keep a list of moves so that we can backtrack
    public ArrayList<MoveRecord> moveList;

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
        moveList = new ArrayList<MoveRecord>();
    }

    public MCBoard
        (int[][] grid, boolean turn, ArrayList<Tuple> legalMoves, ArrayList<MoveRecord> moveList) {
            this.grid = grid;
            this.turn = turn;
            this.legalMoves = legalMoves;
            this.moveList = moveList;
        }
    
    public MCBoard clone() {
        int[][] gridClone = new int[8][8];
        for (int i=0;i!=8;i++){
            for (int j=0;j!=8;j++){
                gridClone[i][j] = grid[i][j];
            }
        }
        ArrayList<Tuple> legalMovesClone = new ArrayList<Tuple>();
        for (Tuple t: legalMoves) {
            legalMovesClone.add(t.clone());
        }
        ArrayList<MoveRecord> moveListClone = new ArrayList<MoveRecord>();
        for (MoveRecord mr: moveList) {
            moveListClone.add(mr.clone());
        }
        return new MCBoard(gridClone, turn, legalMovesClone, moveListClone);
    }

    public synchronized void move(Tuple move) throws IllegalMoveException{
        int r=move.x;
        int c=move.y;
        if (!legalMoves.contains(move)) {
            String msg = "Illegal move. Here is a list of available moves:\n";
            for(Tuple m : legalMoves) {
                msg += m.toString() + "\n";
            }
            throw new IllegalMoveException(msg);
        } else {
            grid[r][c] = turn? 1:0;
            turn = !turn;
            legalMoves.remove(move);
            Tuple newSpot = null;
            // check left
            if (c!=0){
                if (grid[r][c-1]==2) {
                    newSpot = new Tuple(r, c-1);
                    legalMoves.add(newSpot);
                }
            }
            // check right
            if (c!=7){
                if (grid[r][c+1]==2) {
                    newSpot = new Tuple(r, c+1);
                    legalMoves.add(newSpot);
                }
            }
            moveList.add(new MoveRecord(move, newSpot));
        }
    }

    public synchronized void move(int x, int y) {
        move(new Tuple(x,y));
    }

    public synchronized void takeBack(int n) throws IllegalMoveException {
        int moves = moveList.size();
        if (moves < n) 
            throw new IllegalMoveException("You cannot take back "
             + n + " moves when only " + moves + " moves have been played!");
        else {
            for(int i=0; i<n; i++) {
                MoveRecord oldMove = moveList.remove(moveList.size()-1);
                turn = !turn;
                legalMoves.remove(oldMove.newSpot);
                grid[oldMove.move.x][oldMove.move.y] = 2;
                legalMoves.add(oldMove.move);
            }
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
    
    public int eval() {
        int w = winner();
        if (w==1){
            return Integer.MAX_VALUE;
        } else if (w==0) {
            return Integer.MIN_VALUE;
        }
        int e = 0;
        for (int i=0;i!=8;i++){
            for (int j=0;j!=8;j++){
                if (grid[i][j] == 0) {
                    e -= WinGrid.score(i,j);
                } else if (grid[i][j] == 1)
                    e += WinGrid.score(i,j);
            }
        }
        return e;
    }


    public static int nega(boolean turn) {
        return (turn==false ? -1 : 1);
    }

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
