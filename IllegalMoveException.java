public class IllegalMoveException extends RuntimeException {
	private static final long serialVersionUID = -8111357882221787L;
    public IllegalMoveException() {
        super("Illegal move exception found.");
    }
    public IllegalMoveException(String message) {
        super(message);
    }
}