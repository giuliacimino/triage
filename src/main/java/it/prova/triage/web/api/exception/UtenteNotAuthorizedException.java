package it.prova.triage.web.api.exception;

public class UtenteNotAuthorizedException extends RuntimeException{
	public UtenteNotAuthorizedException(String message) {
		super(message);
	}

}
