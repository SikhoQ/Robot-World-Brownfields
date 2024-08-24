package server.WorldApi;


import io.javalin.http.Context;
import java.util.List;
import java.util.Map;
import database.SQLiteWorldRepository;
import server.world.WorldObject;

public class ApiHandler {
    private final SQLiteWorldRepository worldRepository;

    public ApiHandler() {
        this.worldRepository = new SQLiteWorldRepository();
    }

    // Handler for loading a world
    public void loadWorld(Context context) {
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
    }

    // Handler for saving a world
    public void saveWorld(Context context) {
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
    }

    // Handler for removing a world
    public void removeWorld(Context context) {
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
    }
}

