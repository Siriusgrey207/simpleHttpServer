package com.sirius.http;

public enum HttpMethod {
    GET, HEAD;

    public static final int MAX_LENGTH;

    // Initialise the MAX_LENGTH to the longest http method name.
    static {
        int tempMaxLength = -1;
        for (HttpMethod httpMethod: values()) {
            if (tempMaxLength < httpMethod.name().length()) {
                tempMaxLength = httpMethod.name().length();
            }
        }
        MAX_LENGTH = tempMaxLength;
    }
}
