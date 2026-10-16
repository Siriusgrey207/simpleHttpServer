package com.sirius.httpserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;


public class ServerListenerThread extends Thread{

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerListenerThread.class);
    private int port;
    private String webroot;
    private ServerSocket serverSocket;


    public ServerListenerThread(int port, String webroot) throws IOException {
        this.port = port;
        this.webroot = webroot;
        this.serverSocket = new ServerSocket(this.port);
    }


    @Override
    public void run() {
        try {
            // Keep accepting new connections until the server socket is in a good condition.
            // Initialize the connection worker thread with the socket.
            while (serverSocket.isBound() && !serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                LOGGER.info(" * Connection accepted: " + socket.getInetAddress());
                HttpConnectionWorkerThread httpConnectionWorkerThread = new HttpConnectionWorkerThread(socket);
                httpConnectionWorkerThread.start();
            }

        } catch (IOException e) {
            LOGGER.error("Problem with setting socket", e);

        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {}
            }
        }
    }
}

/*
* Notes:
* - An exception may be thrown by the constructor from the initialisation of the server socket. This may happen if the
* port we are trying to initialise the socket with is already in use or we don't have permission to use that socket.
* - The "ServerListenerThread" class is responsible for accepting connection, but the actual processing is done in a
* different thread.
* */
