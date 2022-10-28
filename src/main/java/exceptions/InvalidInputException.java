package exceptions;

public class InvalidInputException extends Exception {

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "Invalid input for one or more fields. No account was found with input data.";
	}
}
