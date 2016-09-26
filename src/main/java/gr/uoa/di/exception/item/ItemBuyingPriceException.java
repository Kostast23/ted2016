package gr.uoa.di.exception.item;

@SuppressWarnings("serial")
public class ItemBuyingPriceException extends RuntimeException {

    private static final String message = "Buying price has to be equal to or greater than the starting price!";

    public ItemBuyingPriceException() {
        super(message);
    }
}