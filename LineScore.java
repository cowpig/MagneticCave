public class LineScore {
	public String line;
	public int score;
	public LineScore(){
		this.line = "none";
		this.score = 0;
	}
	public LineScore(String line, int score){
		this.line = line;
		this.score = score;
	}
	public String toString(){
		return line + " : " + score;
	}
}