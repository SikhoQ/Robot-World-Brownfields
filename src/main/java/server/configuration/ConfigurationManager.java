package server.configuration;

import com.fasterxml.jackson.databind.JsonNode;
import server.json.JsonHandler;

import java.io.File;
import java.io.IOException;

public class ConfigurationManager {

    private JsonNode configurationFile;

    public ConfigurationManager() {
        try {
            this.configurationFile = JsonHandler.deserializeJsonFile(new File("src/main/java/server/configuration/Configuration.json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getPort() {
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

    public int getXConstraint() {
        return configurationFile.get("world").get("x_constraint").asInt();
    }

    public int getYConstraint() {
        return configurationFile.get("world").get("y_constraint").asInt();
    }

    public int getMaxSheilds() {
        return configurationFile.get("world").get("max_sheilds").asInt();
    }

    public int getTileSize() {
        return configurationFile.get("world").get("tileSize").asInt();
    }

    public int getMaxRobots() {
        return configurationFile.get("world").get("maxRobots").asInt();
    }
    
}
