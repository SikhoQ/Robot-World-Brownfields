package server.WorldApi;

import io.javalin.Javalin;
import java.util.List;
import java.util.Map;
import database.SQLiteWorldRepository;
import server.world.WorldObject;


public class RobotWorldsAPI {
    private final Javalin apiServer;
    private final SQLiteWorldRepository worldRepository;

    public RobotWorldsAPI() {
        this.apiServer = Javalin.create();
        this.worldRepository = new SQLiteWorldRepository();

        // Endpoint to load a world
        this.apiServer.get("/loadWorld", context -> {
            String worldName = context.queryParam("worldName");
            if (worldName != null && !worldName.isEmpty()) {
                Map<Integer, List<Map<String, List<Integer>>>> worldData = worldRepository.loadWorld(worldName);
                if (worldData != null && !worldData.isEmpty()) {
                    context.json(worldData);
                } else {
                    context.status(404).result("World not found");
                }
            } else {
                context.status(400).result("World name is required");
            }
        });

        // Endpoint to save a world
        this.apiServer.post("/saveWorld", context -> {
            String worldName = context.formParam("worldName");
            int worldSize = Integer.parseInt(context.formParam("worldSize"));
            List<WorldObject> worldObjects = context.bodyStreamAsClass(List.class);

            if (worldName != null && !worldName.isEmpty()) {
                boolean success = worldRepository.save(worldName, worldSize, worldObjects);
                if (success) {
                    context.status(200).result("World saved successfully");
                } else {
                    context.status(500).result("Failed to save world");
                }
            } else {
                context.status(400).result("World name and size are required");
            }
        });

        // Endpoint to delete a world
        this.apiServer.delete("/removeWorld", context -> {
            String worldName = context.queryParam("worldName");
            if (worldName != null && !worldName.isEmpty()) {
                String result = worldRepository.removeWorld(worldName);
                if (result.equals("removed")) {
                    context.status(200).result("World removed successfully");
                } else {
                    context.status(404).result("World not found");
                }
            } else {
                context.status(400).result("World name is required");
            }
        });
    }

    public Javalin start() {
        return this.apiServer.start(7000);
    }

    public Javalin stop() {
        return this.apiServer.stop();
    }

    public static void main(String[] args) {
        RobotWorldsAPI api = new RobotWorldsAPI();
        api.start();
    }
}

