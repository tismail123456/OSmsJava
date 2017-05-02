package org.akanza.osms.core.exception;

import org.akanza.osms.model.response.error.OAuthError;

/**
 * Created by Christian Amani on 01/04/2017.
 */
public class HttpApiOAuthOrangeException extends RuntimeException
{
    private OAuthError error;

    public HttpApiOAuthOrangeException(OAuthError error)
    {
        this.error = error;
    }

    @Override
    public String getMessage()
    {
        return error.getError();
    }

    public String getDescription()
    {
        return error.getErrorDescription();
    }

}
