package exceptions;

public class ObjectDoesNotExistException extends Exception {
    public ObjectDoesNotExistException(String objectName) {
        super(objectName + " does not exist in the data structure.");
    }
}
