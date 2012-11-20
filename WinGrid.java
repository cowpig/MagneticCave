public class WinGrid {
	// Hold a table of coordinate values equal to number of available winning lines
   	public static int winGrid[][] = 
    {{3, 4, 5, 6, 6, 5, 4, 3},
    {4, 6, 7, 9, 9, 7, 6, 4}, 
    {5, 7, 10, 12, 12, 10, 7, 5}, 
    {6, 9, 12, 15, 15, 12, 9, 6}, 
    {6, 9, 12, 15, 15, 12, 9, 6}, 
    {5, 7, 10, 12, 12, 10, 7, 5}, 
    {4, 6, 7, 9, 9, 7, 6, 4}, 
    {3, 4, 5, 6, 6, 5, 4, 3}};
    
    public static int score(Tuple t) {
    	return winGrid[t.x][t.y];
    }
    public static int score(int x, int y){
    	return winGrid[x][y];
    }
}