package server.configuration;

import com.fasterxml.jackson.databind.JsonNode;
import server.json.JsonHandler;

import java.io.File;
import java.io.IOException;

public class ConfigurationManager {

//    public ConfigurationManager() {}
    private JsonNode configurationFile;

    public ConfigurationManager() {
        try {
            this.configurationFile = JsonHandler.deserializeJsonFile(new File("src/main/java/server/configuration/Configuration.json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getPort() {
//        System.out.println(configurationFile.get("server").get("port"));
        return configurationFile.get("server").get("port").asInt();
    }

    public String getHost() {
        return configurationFile.get("server").get("host").asText();
    }

    public String getVisibility() {
        return configurationFile.get("world").get("visibility").asText();
    }

    public String getReload() {
        return configurationFile.get("world").get("reload").asText();
    }

    public String getRepair() {
        return configurationFile.get("world").get("repair").asText();
    }

}
