package io.github.clivelewis.cvrclient.exception;

public class CvrApiException extends RuntimeException {
	private String message;

	public CvrApiException(Exception e){
		super(e);
	}
	public CvrApiException(String message) {
		super(message);
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
