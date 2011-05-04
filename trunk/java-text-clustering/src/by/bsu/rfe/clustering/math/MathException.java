package by.bsu.rfe.clustering.math;

public class MathException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public MathException() {
        super();
    }

    public MathException(String message, Throwable cause) {
        super(message, cause);
    }

    public MathException(String message) {
        super(message);
    }

    public MathException(Throwable cause) {
        super(cause);
    }

}
