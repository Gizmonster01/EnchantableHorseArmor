package monster.giz.Overhorsed.config;

import monster.giz.Overhorsed.util.OHLogger;
import net.fabricmc.loader.api.FabricLoader;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class OverhorsedConfig {
    private static OverhorsedConfig INSTANCE;
    public static final String FILE_NAME = "overhorsed-config.yaml";
    public static final Path CONFIG_PATH = Paths.get(FabricLoader.getInstance().getConfigDir() + File.separator + FILE_NAME);

    private Map<String, Object> yamlMap;
    private Map<String, Supplier<?>> supplierCache = new ConcurrentHashMap<>();

    private OverhorsedConfig() {
        reloadConfig();
    }

    public static OverhorsedConfig getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new OverhorsedConfig();
        }
        return INSTANCE;
    }

    private void reloadConfig() {
        if (!Files.exists(CONFIG_PATH)) {
            OHLogger.log("Configuration does not exist, attempting to paste it in at " + CONFIG_PATH);
            copyDefaultConfig();
        }

        try {
            yamlMap = loadConfig(CONFIG_PATH);
            OHLogger.log("Configuration loaded successfully.");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration: " + e.getMessage(), e);
        }
    }

    private Map<String, Object> loadConfig(Path configFile) throws IOException {
        try (InputStream inputStream = Files.newInputStream(configFile)) {
            Yaml yaml = new Yaml();
            return yaml.load(inputStream);
        }
    }

    private void copyDefaultConfig() {
        try (InputStream defaultConfigStream = getClass().getResourceAsStream("/" + FILE_NAME)) {
            Files.copy(defaultConfigStream, CONFIG_PATH);
        } catch (IOException e) {
            throw new RuntimeException("Failed to copy default configuration: " + e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getConfigValue(String key, Class<T> type, T defaultValue) {
        return (T) getInstance().supplierCache.computeIfAbsent(key, k -> () -> {
            T value = getInstance().getValue(key, type);
            return value != null ? value : defaultValue;
        }).get();
    }

    public static float getFloat(String key, float defaultValue) {
        return getConfigValue(key, Float.class, defaultValue);
    }

    public static int getInt(String key, int defaultValue) {
        return getConfigValue(key, Integer.class, defaultValue);
    }

    public static String getString(String key, String defaultValue) {
        return getConfigValue(key, String.class, defaultValue);
    }

    public static boolean getBoolean(String key) {
        return getConfigValue(key, Boolean.class, false);
    }

    @SuppressWarnings("unchecked")
    private <T> T getValue(String key, Class<T> type) {
        Object value = getValueFromMap(yamlMap, key);
        if (type.isInstance(value)) {
            return type.cast(value);
        } else if (type == Float.class && value instanceof String) {
            try {
                return type.cast(Float.parseFloat((String) value));
            } catch (NumberFormatException e) {
                // Handle parsing error
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private Object getValueFromMap(Map<String, Object> map, String key) {
        String[] keys = key.split("\\.");
        Map<String, Object> currentMap = map;

        for (String k : keys) {
            Object obj = currentMap.get(k);
            if (obj instanceof Map) {
                currentMap = (Map<String, Object>) obj;
            } else {
                return obj;
            }
        }
        return null;
    }

    public static List<String> getStringList(String key) {
        Object value = getInstance().getValueFromMap(getInstance().yamlMap, key);
        List<String> stringList = new ArrayList<>();

        if (value instanceof List<?>) {
            for (Object item : (List<?>) value) {
                if (item instanceof String) {
                    stringList.add((String) item);
                }
            }
        }
        return stringList;
    }

}