package org.comtel.fritz.connect.exception;

public class ServiceNotSupportedException extends Exception {

	private static final long serialVersionUID = -5246799467710890846L;

	public ServiceNotSupportedException(String m) {
		super(m);
	}

	public ServiceNotSupportedException(String m, Throwable t) {
		super(m, t);
	}
}
