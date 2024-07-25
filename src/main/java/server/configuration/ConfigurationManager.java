package server.configuration;

import com.fasterxml.jackson.databind.JsonNode;
import server.json.JsonHandler;

import java.io.File;
import java.io.IOException;

public class ConfigurationManager {

    public ConfigurationManager() {}

    public static int getPort() {
        return Config.PORT;
    }

    public static void setPort(int port) {
        Config.PORT = port;
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

    public static void setXConstraint(int x) {
        Config.XCONSTRAINT = x;
    }

    public static  int getYConstraint() {
        return Config.YCONSTRAINT;
    }

    public static void setYConstraint(int y) {
        Config.YCONSTRAINT = y;
    }

    public static int getMaxShields() {
        return Config.MAX_SHIELDS;
    }

    public static int getTileSize() {
        return Config.TILE_SIZE;
    }

    public static int getMaxRobots() {
        return Config.MAX_ROBOTS;
    }
    
    public static void setObstacles(String obstacle) {
        Config.OBSTACLE = obstacle;
    }
    
    public static int getWorldSize() {
        return Config.WORLD_SIZE;
    }

    public static String getObstacles() {
        return Config.OBSTACLE;
    }

    public static void setWorldSize(int worldSize) {
        Config.WORLD_SIZE = worldSize;
    }
}
