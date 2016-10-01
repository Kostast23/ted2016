package gr.uoa.di.exception.item;

@SuppressWarnings("serial")
public class ItemRatingException extends RuntimeException {

    private static final String message = "Cannot rate item!";

    public ItemRatingException() {
        super(message);
    }
}
