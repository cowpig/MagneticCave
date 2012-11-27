public class MoveRecord implements Cloneable{
	public Tuple move;
	public Tuple newSpot;
	public MoveRecord(Tuple move, Tuple newSpot){
		this.move = move;
		this.newSpot = newSpot;
	}
	public MoveRecord clone() {
		if (newSpot == null)
			return new MoveRecord(move.clone(), null);
		return new MoveRecord(move.clone(), newSpot.clone());
	}
}