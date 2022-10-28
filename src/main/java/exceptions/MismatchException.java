package exceptions;

public class MismatchException extends Exception {
	
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "PIN must consist of 4 digits only.";
	}
}
