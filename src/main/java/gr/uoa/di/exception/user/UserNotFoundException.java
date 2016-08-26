package gr.uoa.di.exception.user;


@SuppressWarnings("serial")
public class UserNotFoundException extends RuntimeException {

	private static final String message = "User does not exist!";
	
	public UserNotFoundException() {
		super(message);
	}
}