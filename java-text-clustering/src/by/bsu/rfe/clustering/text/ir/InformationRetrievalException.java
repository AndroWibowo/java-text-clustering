package by.bsu.rfe.clustering.text.ir;

/**
 * Exception, occurred during information retrieval process
 * 
 * @author Siarhei Yarashevich
 * 
 */
public class InformationRetrievalException extends Exception {

    private static final long serialVersionUID = 7273395875805549020L;

    public InformationRetrievalException() {
        super();
    }

    public InformationRetrievalException(String message) {
        super(message);
    }

    public InformationRetrievalException(String message, Throwable cause) {
        super(message, cause);
    }

    public InformationRetrievalException(Throwable cause) {
        super(cause);
    }

}
