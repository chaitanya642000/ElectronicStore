package com.lcwd.electronic.store.exceptions;

/**
 * Eception to handle invalid filename extensions
 *
 * */


public class BadApiRequest extends RuntimeException{

    BadApiRequest()
    {
        super();
    }
    public BadApiRequest(String message)
    {
        super(message);
    }

}
