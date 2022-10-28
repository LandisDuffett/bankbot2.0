package exceptions;

public class BalanceBelowZeroException extends Exception {

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "Transaction failed: balance cannot fall below 0.00.";
	}
}
