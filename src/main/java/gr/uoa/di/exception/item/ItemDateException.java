package gr.uoa.di.exception.item;

@SuppressWarnings("serial")
public class ItemDateException extends RuntimeException {

    private static final String message = "The auction must end after it starts!";

    public ItemDateException() {
        super(message);
    }
}
