package exception;

public class LoginRequiredException extends RuntimeException { //runtimeExceptio�� ����ó�� ��������.
	public LoginRequiredException(String msg){
		super(msg);
	}
}
