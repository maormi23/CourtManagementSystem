package exceptions;

public class FutureDateException extends Exception {
    public FutureDateException() {
        super("Future dates are not allowed.");
    }
}
