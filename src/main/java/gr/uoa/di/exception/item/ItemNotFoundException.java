package gr.uoa.di.exception.item;

@SuppressWarnings("serial")
public class ItemNotFoundException extends RuntimeException {
    private static final String message = "Item does not exist!";

    public ItemNotFoundException() {
        super(message);
    }
}
