//import java.util.Comparator;

public class Tuple {
	
    public int x;
    public int y;

	public Tuple(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public String toString(){
        return "(" + x + ", " + y + ")";
    }
    
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}
	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tuple other = (Tuple) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
	/*
	public int compare(Object o1, Object o2) {
		
		return (t1.x + t1.y - t2.x - t2.y);
	}*/
	
} 