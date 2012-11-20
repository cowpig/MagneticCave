import java.util.ArrayList;
import java.util.HashMap;

public class XYWins {
    public HashMap<Tuple, ArrayList<Tuple[]>> winMap;
    public String log = "";

	public XYWins() {
        winMap = new HashMap<Tuple, ArrayList<Tuple[]>>();
        for (int i=0; i<8; i++) {
            for (int j=0; j<8; j++){
                ArrayList<Tuple[]> wins = new ArrayList<Tuple[]>();
                for (Tuple[] win : diagWins(i,j))
                    wins.add(win);
                for (Tuple[] win : horiWins(i,j))
                    wins.add(win);
                for (Tuple[] win : vertWins(i,j))
                    wins.add(win);
                winMap.put(new Tuple(i,j), wins);
            }
        }
	}

    //This is the only method that matters
    public ArrayList<Tuple[]> getWins(Tuple rc){
        return winMap.get(rc);
    }
    public ArrayList<Tuple[]> getWins(int r, int c){
        return winMap.get(new Tuple(r,c));
    }

    public ArrayList<Tuple[]> diagWins(int r, int c) {
        verbose("\nAdding diagonals for (" + r + ", " + c + ")...");
        ArrayList<Tuple[]> out = new ArrayList<Tuple[]>();
        for (int i=4;i>=0;i--){
            // check NW-SE diagonal
            int n = r-i;
            int w = c-i;
            int s = r-i+4;
            int e = c-i+4;
            if (n>=0 && w >=0 && s<=7 && e<=7){
                verbose("\n\tDiagonal found from (" + n + ", "  + w + ") to (" + s + ", " + e + ")\n\t\t");
                Tuple[] tuplist = new Tuple[5];
                for (int j=0;j!=5;j++) {
                    tuplist[j] = new Tuple(n+j, w+j);
                    verbose(tuplist[j].toString() + ", ");
                }
                out.add(tuplist);
            }
            // check SW-NE diagonal
            s = r+i;
            w = c-i;
            n = r+i-4;
            e = c-i+4;
            if (n>=0 && w >=0 && s<=7 && e<=7){
                verbose("\n\tDiagonal found from (" + s + ", "  + w + ") to (" + n + ", " + e + ")\n\t\t");
                Tuple[] tuplist = new Tuple[5];
                for (int j=0;j!=5;j++) {
                    tuplist[j] = new Tuple(s-j, w+j);
                    verbose(tuplist[j].toString() + ", ");
                }
                out.add(tuplist);
            }
        }
        return out;
    }
    public ArrayList<Tuple[]> vertWins(int r, int c) {
        verbose("\nAdding verticals for (" + r + ", " + c + ")...");
        ArrayList<Tuple[]> out = new ArrayList<Tuple[]>();
        for (int i=4;i>=0;i--){
            int n=c-i;
            int s=c-i+4;
            if(n>=0 && s<=7){
                verbose("\n\tVertical found from (" + r + ", "  + n + ") to (" + r + ", " + s + ")\n\t\t");
                Tuple[] tuplist = new Tuple[5];
                for (int j=0;j!=5;j++) {
                    tuplist[j] = new Tuple(r, n+j);
                    verbose(tuplist[j].toString() + ", ");
                }
                out.add(tuplist);
            }
        }
        return out;
    }
    public ArrayList<Tuple[]> horiWins(int r, int c) {
        verbose("\nAdding horizontals for (" + r + ", " + c + ")...");
        ArrayList<Tuple[]> out = new ArrayList<Tuple[]>();
        for (int i=4;i>=0;i--){
            int w=r-i;
            int e=r-i+4;
            if(w>=0 && e<=7){
                verbose("\n\thorizontal found from (" + w + ", "  + c + ") to (" + e + ", " + c + ")\n\t\t");
                Tuple[] tuplist = new Tuple[5];
                for (int j=0;j!=5;j++) {
                    tuplist[j] = new Tuple(w+j, c);
                    verbose(tuplist[j].toString() + ", ");
                }
                out.add(tuplist);
            }
        }
        return out;
    }
    public void verbose (Object s) {
        log += s;
    }
}