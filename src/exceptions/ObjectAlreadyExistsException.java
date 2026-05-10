package exceptions;

public class ObjectAlreadyExistsException extends Exception {
    public ObjectAlreadyExistsException(String objectName) {
        super(objectName + " already exists in the data structure.");
    }
}
