public class Tuple implements Comparable<Tuple>, Cloneable{ 
    public int x;
    public int y;
    
    public Tuple(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public String toString(){
        return "(" + x + ", " + y + ")";
    }

    public int hashCode(){
    	return this.x * 19 - this.y * 3;
    }
    public boolean equals(Object o) {
    	if (o instanceof Tuple) {
    		Tuple t = (Tuple) o;
    		return (t.x == this.x && t.y == this.y);
    	}
    	return false;
    }

    public int compareTo(Tuple t) {
    	int myScore = WinGrid.score(x,y);
    	int tScore = WinGrid.score(t);
    	if (myScore > tScore)
    		return -1;
    	else if (myScore < tScore)
    		return 1;
    	else
    		return 0;
    }

    public Tuple clone(){
    	return new Tuple(x, y);
    }
} 
