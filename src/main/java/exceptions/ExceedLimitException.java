package exceptions;

public class ExceedLimitException extends Exception {
	
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "Invalid input for one or more fields: Restrict input to 20 characters.";
	}
}
