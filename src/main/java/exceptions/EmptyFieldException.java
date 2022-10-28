package exceptions;

public class EmptyFieldException extends Exception {

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "You must enter a valid value for each field.";
	}
}
