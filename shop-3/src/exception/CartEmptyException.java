package exception;

public class CartEmptyException extends RuntimeException {
	
	public CartEmptyException(String msg){
		super(msg);
	}
}
