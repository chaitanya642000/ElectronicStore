package com.lcwd.electronic.store.exceptions;

import java.io.IOException;

public class ExceptionDuringFolderCreation extends IOException {

    public ExceptionDuringFolderCreation()
    {
        super();
    }

    public ExceptionDuringFolderCreation(String message)
    {
        super(message);
    }
}
