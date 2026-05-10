package exceptions;

public class NegativeSalaryException extends Exception {
    public NegativeSalaryException(double salary) {
        super("Salary cannot be negative: " + salary);
    }
}
