import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class MCBoard implements Cloneable{
    public int grid[][] = new int[8][8];
    public boolean turn = true;

    // Keep a list of legal moves
    public LinkedList<Tuple> legalMoves;
    public LinkedList<Tuple> legalMovesBackup;

    // Maps grid spots to an ArrayList of ArrayList 
    public static XYWins winMap = new XYWins();

    // Order prioritizes middle
    public static int[] orderedMoves = new int[] {3,4,2,5,1,6,0,7};

    // Keep a list of moves so that we can backtrack
    public ArrayList<MoveRecord> moveList;


    public int winStatus = 2;

    public MCBoard() {
        legalMoves = new LinkedList<Tuple>();
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
        (int[][] grid, boolean turn, LinkedList<Tuple> legalMoves, ArrayList<MoveRecord> moveList) {
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
        LinkedList<Tuple> legalMovesClone = new LinkedList<Tuple>();
        for (Tuple t: legalMoves) {
            legalMovesClone.add(t.clone());
        }
        ArrayList<MoveRecord> moveListClone = new ArrayList<MoveRecord>();
        for (MoveRecord mr: moveList) {
            moveListClone.add(mr.clone());
        }
        return new MCBoard(gridClone, turn, legalMovesClone, moveListClone);
    }

    public Tuple lastMove(int index){
        // if (winStatus != 2) 
        //     return moveListBackup.get(moveList.size()-(index+1)).move;
        return moveList.get(moveList.size()-(index+1)).move;
    }

    public synchronized void move(Tuple move) throws IllegalMoveException{
        // print("Moving " + move + "\n");
        int r = move.x;
        int c = move.y;
        if (!legalMoves.contains(move)) {
            String msg = "Illegal move. Here is a list of available moves:\n";
            for(Tuple m : legalMoves) {
                msg += m.toString() + "\n";
            }
            throw new IllegalMoveException(msg);
        } else {
            grid[r][c] = turn? 1:0;
            turn = !turn;
            updateWinner(r,c);
            if (winStatus != 2) {
                legalMovesBackup = (LinkedList<Tuple>)legalMoves.clone();
                legalMoves = new LinkedList<Tuple>();
                moveList.add(new MoveRecord(move, null));
            } else {
                legalMoves.remove(move);
                Tuple newSpot = null;
                // check left
                if (c!=0){
                    if (grid[r][c-1]==2) {
                        newSpot = new Tuple(r, c-1);
                        legalMoves.addFirst(newSpot);
                    }
                }
                // check right
                if (c!=7){
                    if (grid[r][c+1]==2) {
                        newSpot = new Tuple(r, c+1);
                        legalMoves.addFirst(newSpot);
                    }
                }
                moveList.add(new MoveRecord(move, newSpot));
            }
        }
    }

    public synchronized void move(int x, int y){
        move(new Tuple(x,y));
    }

    // returns the MoveRecord of the last move taken back
    public synchronized MoveRecord takeBack(int n) throws IllegalMoveException {
        int moves = moveList.size();
        if (moves < n) {
            throw new IllegalMoveException("You cannot take back "
             + n + " moves when only " + moves + " moves have been played!");
        } else if (n < 1) {
            throw new IllegalMoveException("You cannot take back zero moves!");
        }
        MoveRecord oldMove = null;
        if (winStatus != 2) {
            // print ("Taking back winner at " + lastMove(0) + "\n");
            winStatus = 2;
            legalMoves = (LinkedList<Tuple>) legalMovesBackup.clone();
            oldMove = moveList.remove(moveList.size()-1);
            grid[oldMove.move.x][oldMove.move.y] = 2;
            // print (oldMove.move + "removed.\n");
            n -= 1;
        }
        for(int i=0; i<n; i++) {
            oldMove = moveList.remove(moveList.size()-1);
            // print ("Taking back move " + oldMove.move + "\n");
            turn = !turn;
            legalMoves.remove(oldMove.newSpot);
            grid[oldMove.move.x][oldMove.move.y] = 2;
            legalMoves.add(oldMove.move);
        }
        return oldMove;
    
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
        out += "(Turn : " + (turn ? 1 : 0) + ")\n";
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
    
    // basic initial evaluation function, score simply based on the number of possible wins per spot on the grid
    // for example, (0,0) has a score of 3, for its contribution to three possible winning lines.
    public int eval() {
        int e = 0;
        int player = turn ? 1 : 0;
        int otherPlayer = turn ? 0 : 1;
        if (winStatus==player){
            return infHolder.MAX;
        } else if (winStatus==otherPlayer) {
            return infHolder.MIN;
        } else {
            for (int i=0;i!=8;i++){
                for (int j=0;j!=8;j++){
                    if (grid[i][j] == otherPlayer) {
                        e -= WinGrid.score(i,j);
                    } else if (grid[i][j] == player)
                        e += WinGrid.score(i,j);
                }
            }
        }
        return e;
    }


    // This is the eval function designed for genetic algorithm
    // Which will search for the best weights for position features
    public int eval(int[] weights) {
        int e = 0;
        int player = turn ? 1 : 0;
        int otherPlayer = turn ? 0 : 1;
        if (winStatus==player){
            return infHolder.MAX;
        } else if (winStatus==otherPlayer) {
            return infHolder.MIN;
        } else {
            for (int i=0;i!=8;i++){
                for (int j=0;j!=8;j++){
                    if (grid[i][j] == otherPlayer) {
                        e -= scoreSpot(i,j,weights);
                    } else if (grid[i][j] == player)
                        e += scoreSpot(i,j,weights);
                }
            }
        }
        return e;
    }


    public int scoreSpot(int r, int c, int[] weights) {
        int score = 0;
        int player = grid[r][c];
        int otherPlayer = (player==0) ? 1 : 0;
        // Iterates through every win line which contains a certain spot
        for (Tuple[] line : winMap.getWins(r, c)) {
            int lineScore = 0;
            for (Tuple t : line) {
                if (grid[t.x][t.y] == otherPlayer ){
                    lineScore = 0;
                    break;
                } else if (grid[t.x][t.y] == player) {
                    lineScore++;
                }
            }
            // For every line containing only the player's pieces, adds to score
            // weighted by the weights[] array
            try{score += weights[lineScore];} catch (ArrayIndexOutOfBoundsException e) {
                // print("Array index " + lineScore + " found when examining line:\n");
                for (Tuple t : line) {
                    // print ("\t"+t+", "+grid[t.x][t.y]+"\n");
                }
                // print("On board: " + toString());
                // print("Last move played: " + lastMove(0) + "\n");
                // print("Winstatus: " + winStatus + "\n\n");
                int crash = 99/0;
            }
        }
        return score;
    }

    public boolean isFull(){
        return (this.moveList.size() == 64);
    }

    public static int nega(boolean turn) {
        return (turn==false ? -1 : 1);
    }

    public void updateWinner(int x, int y) {
        updateWinner(new Tuple(x,y));
    }

    public void updateWinner(Tuple t) {
        ArrayList<Tuple[]> linesToCheck = winMap.getWins(t);
        int winner = grid[t.x][t.y];
        // print("Checking winner... player: " + winner + " spot " + t + "\n");
        for (Tuple[] ts : linesToCheck) {
            // print("\tChecking line " + ts[0] + " - " + ts[4] + " | ");
            boolean winnerFound = true;
            for (int i=0; i<5; i++){
                // print(grid[ts[i].x][ts[i].y] +"-"+ts[i]+".");
                if (grid[ts[i].x][ts[i].y] != winner) {
                    winnerFound = false;
                    // print("...No winner on this line" + "\n");
                    break;
                }
            }
            if (winnerFound) {
                // print("\t\tWinner found!\n\n");
                // print(toString());
                winStatus = winner;
                break;
                // int c = 90/0;
            }
        }

    }

    // returns 2 for nobody, 0 for O and 1 for X
    // replaced by much more efficient hash table lookup
    /*public int winner() {
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
    }*/

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
