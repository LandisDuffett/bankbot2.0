package exceptions;

public class NameNotUniqueException extends Exception{
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "User name is taken. Please choose a new user name.";
	}
}
