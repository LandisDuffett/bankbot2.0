package exceptions;

public class EmptyListException extends Exception {

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "No accounts found for those credentials. Try again.";
	}
}
