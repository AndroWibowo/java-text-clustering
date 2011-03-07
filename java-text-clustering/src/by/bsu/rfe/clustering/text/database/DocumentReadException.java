package by.bsu.rfe.clustering.text.database;

public class DocumentReadException extends Exception {

	private static final long serialVersionUID = 7273395875805549020L;

	public DocumentReadException() {
		super();
	}

	public DocumentReadException(String message) {
		super(message);
	}

	public DocumentReadException(String message, Throwable cause) {
		super(message, cause);
	}

	public DocumentReadException(Throwable cause) {
		super(cause);
	}

}
