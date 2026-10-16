package com.sirius.http;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HttpParserTest {
    private HttpParser httpParser;

    @BeforeAll
    public void beforeClass() {
        httpParser = new HttpParser();
    }


    @Test
    void parseHttpRequest() {
        HttpRequest parsedHttpRequest = null;
        try {
            parsedHttpRequest = httpParser.parseHttpRequest(generateValidGETTestCase());
        } catch (HttpParsingException e) {
            fail(e);
        }

        assertNotNull(parsedHttpRequest);
        assertEquals(HttpMethod.GET, parsedHttpRequest.getMethod());
        assertEquals("/", parsedHttpRequest.getRequestTarget());
    }


    @Test
    void parseHttpRequestBadMethodName1() {
        try {
            HttpRequest parsedRequest = httpParser.parseHttpRequest(generateBadTestCaseInvalidMethodName1());
            // We expect the line above to result in an exception being thrown.
            fail();
        } catch (HttpParsingException e) {
            assertEquals(HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED, e.getErrorCode());
        }
    }


    // Tests a long method name in the http request. The method name is longer than any defined and that can be hanlded by
    // the server.
    @Test
    void parseHttpRequestBadMethodName2() {
        try {
            HttpRequest parsedRequest = httpParser.parseHttpRequest(generateBadTestCaseInvalidMethodName2());
            // We expect the line above to result in an exception being thrown.
            fail();
        } catch (HttpParsingException e) {
            assertEquals(HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED, e.getErrorCode());
        }
    }


    // Wrong number of items in the request line.
    @Test
    void parseHttpRequestInvalidNumItems1() {
        try {
            HttpRequest parsedRequest = httpParser.parseHttpRequest(generateBadTestCaseRequestLineInvalidNumItems1());
            fail();
        } catch (HttpParsingException e) {
            assertEquals(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST, e.getErrorCode());
        }
    }


    @Test
    void parseHttpEmptyRequestLine() {
        try {
            HttpRequest parsedRequest = httpParser.parseHttpRequest(generateBadTestCaseEmptyRequestLine());
            fail();
        } catch (HttpParsingException e) {
            assertEquals(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST, e.getErrorCode());
        }
    }


    @Test
    void parseHttpRequestLineCRnoLF() {
        try {
            HttpRequest parsedRequest = httpParser.parseHttpRequest(
                    generateBadTestCaseRequestLineOnlyCRnoLF()
            );
            fail();
        } catch (HttpParsingException e) {
            assertEquals(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST, e.getErrorCode());
        }
    }



    // ---
    // TEST CASE GENERATORS
    // ---

    // For the purposes of testing our parser, the following method defines a valid HTTP request, puts it in a stream
    // and returns the stream.
    private InputStream generateValidGETTestCase() {
        // Define a valid HTTP request.
        String rawData = "GET / HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Connection: keep-alive\r\n" +
                "Cache-Control: max-age=0\r\n" +
                "sec-ch-ua: \"Google Chrome\";v=\"155\", \"Chromium\";v=\"155\", \"Not(A:Brand\";v=\"24\"\r\n" +
                "sec-ch-ua-mo16:50:12.701 [Thread-0] INFO com.sirius.httpserver.core.ServerListenerThread -  * Connection accepted: /0:0:0:0:0:0:0:1\r\n" +
                "bile: ?0\r\n" +
                "sec-ch-ua-platform: \"Windows\"\r\n" +
                "Upgrade-Insecure-Requests: 1\r\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/155.0.0.0 Safari/537.36\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/jxl,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7\r\n" +
                "Sec-Fetch-Site: none\r\n" +
                "Sec-Fetch-Mode: navigate\r\n" +
                "Sec-Fetch-User: ?1\r\n" +
                "Sec-Fetch-Dest: document\r\n" +
                "Accept-Encoding: gzip, deflate, br, zstd\r\n" +
                "Accept-Language: en-GB,en-US;q=0.9,en;q=0.8\r\n" +
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );

        return inputStream;
    }


    // Test case with a bad method name.
    private InputStream generateBadTestCaseInvalidMethodName1() {
        // Define a valid HTTP request.
        String rawData = "GeT / HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Accept-Language: en-GB,en-US;q=0.9,en;q=0.8\r\n" +
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );

        return inputStream;
    }


    // Generates a test case with am HTTP request with a really longer method name.
    private InputStream generateBadTestCaseInvalidMethodName2() {
        // Define a valid HTTP request.
        String rawData = "GETTTTTTTTTTTTTTTTTTTT / HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Accept-Language: en-GB,en-US;q=0.9,en;q=0.8\r\n" +
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );

        return inputStream;
    }


    private InputStream generateBadTestCaseRequestLineInvalidNumItems1() {
        String rawData = "GET / AAAAAAAAA HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Accept-Language: en-GB,en-US;q=0.9,en;q=0.8\r\n" +
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );

        return inputStream;
    }


    private InputStream generateBadTestCaseEmptyRequestLine() {
        String rawData = "\r\n" +
                "Host: localhost:8080\r\n" +
                "Accept-Language: en-GB,en-US;q=0.9,en;q=0.8\r\n" +
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(rawData.getBytes(StandardCharsets.US_ASCII));
        return inputStream;
    }


    private InputStream generateBadTestCaseRequestLineOnlyCRnoLF() {
        String rawData = "GET / HTTP/1.1\r" + // <---- no LF on purpose (test case)
                "Host: localhost:8080\r\n" +
                "Accept-Language: en-GB,en-US;q=0.9,en;q=0.8\r\n" +
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );

        return inputStream;
    }
}