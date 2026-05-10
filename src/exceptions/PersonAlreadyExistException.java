package exceptions;

public class PersonAlreadyExistException extends Exception {
    public PersonAlreadyExistException(String firstName, String lastName) {
        super(firstName + " " + lastName + " already exists in the system, will not be added again.");
    }
}
