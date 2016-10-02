package gr.uoa.di.exception.category;

@SuppressWarnings("serial")
public class CategoryNotFoundException extends RuntimeException {

    private static final String message = "Category does not exist!";

    public CategoryNotFoundException() {
        super(message);
    }
}
