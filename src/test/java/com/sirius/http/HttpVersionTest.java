package com.sirius.http;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HttpVersionTest {
    // Test a perfect match for the supported HTTP version 1.1.
    @Test
    void getBestCompatibleVersionExactMatch() {
        HttpVersion version = null;

        try {
            version = HttpVersion.getBestCompatibleVersion("HTTP/1.1");
        } catch (BadHttpVersionException e) {
            fail();
        }

        assertNotNull(version);
        assertEquals(HttpVersion.HTTP_1_1, version);
    }


    // Test a badly formatted version provided to the server (lower case).
    @Test
    void getBestCompatibleVersionBadFormat() {
        HttpVersion version = null;

        // we expect the method to fail due the lower case "http".
        try {
            version = HttpVersion.getBestCompatibleVersion("http/1.1");
            fail();
        } catch (BadHttpVersionException e) {}
    }


    // It is expected that when a higher version is passed than the ones supported
    // an exception is thrown.
    @Test
    void getBestCompatibleVersionHigherVersion() {
        HttpVersion version = null;

        try {
            version = HttpVersion.getBestCompatibleVersion("HTTP/1.3");
            assertNotNull(version);
            assertEquals(HttpVersion.HTTP_1_1, version);
        } catch (BadHttpVersionException e) {
            fail();
        }
    }
}
