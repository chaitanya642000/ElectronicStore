package com.lcwd.electronic.store.exceptions;

import com.lcwd.electronic.store.validate.UniqueTitle;

import javax.persistence.NonUniqueResultException;

public class UniqueTitleException extends NonUniqueResultException {
    UniqueTitleException()
    {
        super();
    }
    public UniqueTitleException(String message)
    {
        super(message);
    }
}
