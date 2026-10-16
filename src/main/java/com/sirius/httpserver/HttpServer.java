package com.sirius.httpserver;

/*
* Driver class for the http server.
* */

import com.sirius.httpserver.config.Configuration;
import com.sirius.httpserver.config.ConfigurationManager;
import com.sirius.httpserver.core.ServerListenerThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HttpServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpServer.class);

    public static void main(String[] args) {
        LOGGER.info("Server starting...");

        // Load the pre-defined configuration in http.json.
        ConfigurationManager.getInstance().loadConfigurationFile("src/main/resources/http.json");
        Configuration conf = ConfigurationManager.getInstance().getCurrentConfiguration();

        LOGGER.info("Using Port: " + conf.getPort());
        LOGGER.info("Using WebRoot: " + conf.getWebroot());

        // Create and start the server listener thread.
        try {
            ServerListenerThread serverListenerThread = new ServerListenerThread( conf.getPort(), conf.getWebroot());
            serverListenerThread.start();
        } catch (IOException e) {
            e.printStackTrace();
            // TODO handle later
        }
    }
}
