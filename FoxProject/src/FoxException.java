public class FoxException extends Exception {
    public FoxException(String errorNo, Throwable cause) {
        super(errorNo, cause);
    }
}