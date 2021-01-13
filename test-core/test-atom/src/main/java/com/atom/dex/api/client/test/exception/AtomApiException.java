package com.atom.dex.api.client.test.exception;

public class AtomApiException extends RuntimeException {

    public AtomApiError getError() {
        return error;
    }
    private AtomApiError error;

    public AtomApiException(AtomApiError apiError) {
        this.error = apiError;
    }


    public AtomApiException(Throwable cause){
        super(cause);
    }

    @Override
    public String getMessage() {
        if (error != null) {
            return error.getMessage();
        }
        return super.getMessage();
    }
}
