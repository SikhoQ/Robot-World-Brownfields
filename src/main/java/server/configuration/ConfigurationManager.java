package server.configuration;

import com.fasterxml.jackson.databind.JsonNode;
import server.json.JsonHandler;

import java.io.File;
import java.io.IOException;

public class ConfigurationManager {

    public ConfigurationManager() {}

    public static  int getPort() {
        return Config.PORT;
    }

    public static String getVisibility() {
        return String.valueOf(Config.VISIBILITY);
    }

    public static  String getReload() {
        return String.valueOf(Config.RELOAD);
    }

    public static  String getRepair() {
        return String.valueOf(Config.RELOAD);
    }

    public static  int getXConstraint() {
        return Config.XCONSTRAINT;
    }

    public static  int getYConstraint() {
        return Config.YCONSTRAINT;
    }

    public static int getMaxSheilds() {
        return Config.MAX_SHIELDS;
    }

    public static int getTileSize() {
        return Config.TILE_SIZE;
    }

    public static int getMaxRobots() {
        return Config.MAX_ROBOTS;
    }
    
}
