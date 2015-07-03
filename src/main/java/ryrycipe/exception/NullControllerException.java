package ryrycipe.exception;


/**
 * Thrown if searched controller is not found in {@link ryrycipe.model.Plan#components}
 */
public class NullControllerException extends Exception {

    /**
     * Exception raise when no controller has been selected.
     */
    public NullControllerException() {
        super("No ComponentView has been selected or doesn't exists.");
    }
}
