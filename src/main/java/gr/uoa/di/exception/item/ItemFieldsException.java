package gr.uoa.di.exception.item;

@SuppressWarnings("serial")
public class ItemFieldsException extends RuntimeException {

    private static final String message = "Not all necessary fields are filled in!";

    public ItemFieldsException() {
        super(message);
    }
}
