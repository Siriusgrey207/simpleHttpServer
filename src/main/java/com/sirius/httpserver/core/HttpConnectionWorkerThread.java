package com.sirius.httpserver.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class HttpConnectionWorkerThread extends Thread{

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpConnectionWorkerThread.class);
    private final Socket socket;


    public HttpConnectionWorkerThread(Socket socket) {
        this.socket = socket;
    }


    @Override
    public void run() {
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            // We write the response in accordance to the HTTP 1.1 standard.
            String html = "<html><head><title>Simple Java Http Server</title></head><body><h1>This page was served using my simple Java HTTP server</h1></body></html>";

            final String CRLF = "\n\r"; // 13, 10 ASCII

            String response =
                    "HTTP/1.1 200 OK" + CRLF +                          // Status Line  :   HTTP_VERSION RESPONSE_CODE RESPONSE_MESSAGE
                            "Content-Length: " + html.getBytes().length + CRLF +// HEADERS
                            CRLF +                                              // *Finished with headers*
                            html +
                            CRLF + CRLF;
            outputStream.write(response.getBytes());

            LOGGER.info("Connection Processing Finished");

        } catch (IOException e) {
            LOGGER.error("Problem with communication", e);

        } finally {
            // Close streams and connections on cleanup.
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {}
            }

            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {}
            }

            if (socket != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {}
            }
        }
    }
}
