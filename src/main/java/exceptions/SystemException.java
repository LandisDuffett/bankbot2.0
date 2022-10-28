package exceptions;

public class SystemException extends Exception {

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "There was an internal error! Please try again later!";
	}
}
