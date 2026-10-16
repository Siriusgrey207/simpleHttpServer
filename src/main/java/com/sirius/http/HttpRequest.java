package com.sirius.http;

public class HttpRequest extends HttpMessage{

    private HttpMethod method;
    private String requestTarget;
    private String httpVersion;

    // Only classes of this package can instantiate classes of this type directly.
    HttpRequest() {}

    public HttpMethod getMethod() { return method; }


    void setMethod(String methodName) throws HttpParsingException{
        // If the passed method name is a valid value that can be converted to an enum, set the new method.
        for (HttpMethod method: HttpMethod.values()) {
            if (methodName.equals(method.name())) {
                this.method = method;
                return;
            }
        }

        // Otherwise assume such a method is not supported and throw an error.
        throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
    }


    public String getRequestTarget() {
        return requestTarget;
    }


    void setRequestTarget(String requestTarget) throws HttpParsingException {
        if (requestTarget == null || requestTarget.length() == 0) {
            throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
        this.requestTarget = requestTarget;
    }
}

/*
* Notes:
* - The method setter is package-private because there is no reason to provider more access for the setter.
* */
