package gr.uoa.di.exception.item;

@SuppressWarnings("serial")
public class ItemDateException extends RuntimeException {

    private static final String message = "The auction must end at a later time!";

    public ItemDateException() {
        super(message);
    }
}
