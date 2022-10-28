package exceptions;

public class RecordAlreadyPresentException extends Exception {
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "You are already linked to this account.";
	}
}
