package gr.uoa.di.exception.item;

@SuppressWarnings("serial")
public class ItemCannotBeEditedException extends RuntimeException {

    private static final String message = "You cannot edit this item!";

    public ItemCannotBeEditedException() {
        super(message);
    }
}
