package org.romelgomez;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

import java.io.InputStream;

public class ConfigLoader {
    public static GameConfig loadConfig(String resourcePath) throws IOException {


        
        
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = ConfigLoader.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new IOException("Cannot find configuration file: " + resourcePath);
            }
            return mapper.readValue(is, GameConfig.class);
        }
    }
}
