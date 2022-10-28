package exceptions;

public class TypeMismatchException extends Exception {

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "Please enter the correct data type.";
	}
}
