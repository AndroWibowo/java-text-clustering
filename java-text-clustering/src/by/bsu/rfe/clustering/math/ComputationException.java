package by.bsu.rfe.clustering.math;

public class ComputationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ComputationException() {
        super();
    }

    public ComputationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ComputationException(String message) {
        super(message);
    }

    public ComputationException(Throwable cause) {
        super(cause);
    }

}
