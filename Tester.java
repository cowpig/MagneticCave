public class Tester {
	public static void main(String[] args){
		XYWins w = new XYWins();
		// for (Tuple k: w.winMap.keySet()) {
		// 	System.out.println(k.toString() + '\t' + w.getWins(k).toString());
		// }
		for (int i=0; i<8; i++) {
			System.out.println("");
			for (int j=0; j<8; j++) {
				wins = w.getWins(i,j);
				if (wins != null) {
					System.out.print(w.getWins(i,j).size() + ' ');
				} else {
					System.out.println("0 ")
				}
			}
		}
	}
}