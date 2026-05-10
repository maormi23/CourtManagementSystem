package exceptions;

public class InvalidBirthDateException extends Exception {
    public InvalidBirthDateException() {
        super("The provided birth date is invalid.");
    }
}
