package com.graduationajajat.artiste.exception;

public class PasswordErrorException extends IllegalArgumentException {
    public PasswordErrorException() {
        super();
    }

    public PasswordErrorException(String msg) {
        super(msg);
    }
}
