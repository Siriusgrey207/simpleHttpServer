package com.sirius.httpserver.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.sirius.httpserver.util.Json;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ConfigurationManager {
    private static ConfigurationManager myConfigurationManager;
    private static Configuration myCurrentConfiguration;

    private ConfigurationManager() {}

    /*
    * Returns the already created instance of the configuration manager or creates one if one has not already been
    * instantiated.
    * */
    public static ConfigurationManager getInstance() {
        if (myConfigurationManager == null) {
            myConfigurationManager = new ConfigurationManager();
        }

        return myConfigurationManager;
    }


    /*
    * Loads a configuration from the provided path.
    * */
    public void loadConfigurationFile(String filePath) {
        FileReader fileReader = null;

        // Try to initialise the reader with the config.
        try {
            fileReader = new FileReader(filePath);
        } catch (FileNotFoundException e) {
            throw new HttpConfigurationException(e);
        }

        // Try to read from the file and append chars to the string buffer.
        StringBuffer sb = new StringBuffer();
        int i;
        try {
            while ((i = fileReader.read()) != -1) {
                sb.append((char) i);
            }
        } catch (IOException e) {
            throw new HttpConfigurationException(e);
        }

        // Try to parse the buffer string to a JSON Node
        JsonNode config = null;
        try {
            config = Json.parse(sb.toString());
        } catch (IOException e) {
            throw new HttpConfigurationException("Error parsing the configuration file", e);
        }

        // Try to convert the JSON node to a Configuration class
        try {
            myCurrentConfiguration = Json.fromJson(config, Configuration.class);
        } catch (JsonProcessingException e) {
            throw new HttpConfigurationException("Error parsing the configuration file (internal). Failed to convert the JSON node into a Configuration object.");
        }
    }


    /*
    * Returns the current loaded configuration.
    * */
    public Configuration getCurrentConfiguration() {
        if (myCurrentConfiguration == null) {
            throw new HttpConfigurationException("No Current Configuration Set.");
        }
        return myCurrentConfiguration;
    }
}

/*
* Notes:
* - The configuration manager is a singleton class because only one will be needed for this project. Its contents can be shared across
* wherever necessary.
* */
