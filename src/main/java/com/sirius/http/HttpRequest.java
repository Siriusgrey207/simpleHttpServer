package com.sirius.http;

public class HttpRequest extends HttpMessage{

    private HttpMethod method;
    private String requestTarget;
    private String originalHttpVersion; // the version literal coming from the request
    private HttpVersion bestCompatibleHttpVersion;

    // Only classes of this package can instantiate classes of this type directly.
    HttpRequest() {
    }

    public HttpMethod getMethod() { return method; }

    public String getRequestTarget() {
        return requestTarget;
    }

    public HttpVersion getBestCompatibleHttpVersion() { return bestCompatibleHttpVersion; }

    public String getOriginalHttpVersion() { return originalHttpVersion; }

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

    void setRequestTarget(String requestTarget) throws HttpParsingException {
        if (requestTarget == null || requestTarget.isEmpty()){
            throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_500_INTERNAL_SERVER_ERROR);
        }
        this.requestTarget = requestTarget;
    }

    void setOriginalHttpVersion(String originalHttpVersion) throws BadHttpVersionException, HttpParsingException {
        this.originalHttpVersion = originalHttpVersion;
        this.bestCompatibleHttpVersion = HttpVersion.getBestCompatibleVersion(originalHttpVersion);

        // If the best compatible version is null, there are no compatible version for the request.
        if (this.bestCompatibleHttpVersion == null) {
            throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED);
        }
    }
}

/*
* Notes:
* - The method setter is package-private because there is no reason to provider more access for the setter.
* */
