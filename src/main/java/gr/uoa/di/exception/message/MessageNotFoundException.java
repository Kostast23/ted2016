package gr.uoa.di.exception.message;

@SuppressWarnings("serial")
public class MessageNotFoundException extends RuntimeException {

    private static final String message = "Message does not exist!";

    public MessageNotFoundException() {
        super(message);
    }

}
