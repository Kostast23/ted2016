package gr.uoa.di.exception.message;

@SuppressWarnings("serial")
public class MessageFieldsException extends RuntimeException {

    private static final String message = "Not all necessary fields are filled in!";

    public MessageFieldsException() {
        super(message);
    }
}
