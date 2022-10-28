package exceptions;

public class ScanInputException extends Exception {

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "You must enter a number corresponding to one of the options.";
	}
}
