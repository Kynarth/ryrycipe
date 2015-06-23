package ryrycipe.exception;

/**
 * Thrown if an unsupported language is apply to the application.
 */
public class UnsupportedLanguageException extends Exception {

    /**
     * Exception raise when an unsupported language is apply to the application.
     *
     * @param language String - Unsupported language.
     */
    public UnsupportedLanguageException(String language) {
        super("The language: " + language + " is not supported by the application.");
    }
}
