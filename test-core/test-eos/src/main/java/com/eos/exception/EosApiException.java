package com.eos.exception;

public class EosApiException extends RuntimeException {

    public EosApiError getError() {
        return error;
    }
    private EosApiError error;

    public EosApiException(EosApiError apiError) {
        this.error = apiError;
    }


    public EosApiException(Throwable cause){
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
