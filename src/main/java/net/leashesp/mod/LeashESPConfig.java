package net.leashesp.mod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class LeashESPConfig {

    // GSON object that makes easily to write/read files saved by JSON
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    // Path to the config file
    private static final File CONFIG_FILE = new File("config/leashesp/config.json");
    private static Map<String, Object> config = new HashMap<>();

    // Static block to initialize default values
    static {
        config.put("leashESPEnabled", false);
    }

    public static void init() {
        createDirectories();
        load();
        save();
    }

    // Save config file
    public static void save() {
        createDirectories();
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(config, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load config info
    public static void load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                Type type = new TypeToken<Map<String, Object>>() {}.getType();
                config = GSON.fromJson(reader, type);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        config.putIfAbsent("leashESPEnabled", false);
    }

    // Checks isLeashESPEnabled() is enabled
    public static boolean isLeashESPEnabled() {
        Object value = config.get("leashESPEnabled");
        return value instanceof Boolean ? (Boolean) value : false;
    }

    // Sets default value of leashESPenabled
    public static void setLeashESPEnabled(boolean enabled) {
        config.put("leashESPEnabled", enabled);
        save();
    }

    // Creates configuration folder if it don't exists
    private static void createDirectories() {
        File parentDir = CONFIG_FILE.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        Object value = config.get(key);
        return value instanceof Boolean ? (Boolean) value : defaultValue;
    }

    public static void setBoolean(String key, boolean value) {
        config.put(key, value);
        save();
    }

    public static float getFloat(String key, float defaultValue) {
        Object value = config.get(key);
        return value instanceof Number ? ((Number) value).floatValue() : defaultValue;
    }

    public static void setFloat(String key, float value) {
        config.put(key, value);
        save();
    }

    public static int getInt(String key, int defaultValue) {
        Object value = config.get(key);
        return value instanceof Number ? ((Number) value).intValue() : defaultValue;
    }

    public static void setInt(String key, int value) {
        config.put(key, value);
        save();
    }
}