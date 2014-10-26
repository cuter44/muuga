package com.github.cuter44.muuga.user.exception;

public class LoginBlockedException extends RuntimeException
{
    public LoginBlockedException()
    {
        super();
    }

    public LoginBlockedException(String message)
    {
        super(message);
    }

    public LoginBlockedException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public LoginBlockedException(Throwable cause)
    {
        super(cause);
    }
}
