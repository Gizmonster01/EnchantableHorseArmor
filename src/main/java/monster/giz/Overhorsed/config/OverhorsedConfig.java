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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OverhorsedConfig {
    private static OverhorsedConfig INSTANCE;
    public static final String FILE_NAME = "overhorsed-config.yaml";
    public static final Path CONFIG_PATH = Paths.get(FabricLoader.getInstance().getConfigDir() + File.separator + FILE_NAME);

    private Map<String, Object> yamlMap;

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

    public static String getString(String key) {
        Object value = getInstance().getValue(key);

        if (value instanceof String) {
            return (String) value;
        }

        return null;
    }

    public static int getInt(String key) {
        Object value = getInstance().getValue(key);
        if (value instanceof Integer) {
            return (Integer) value;
        }
        return 0;
    }

    public static int getInt(String key, int defaultValue) {
        Object value = getInstance().getValue(key);
        if (value instanceof Integer) {
            return (Integer) value;
        }
        return defaultValue;
    }

    public static float getFloat(String key) {
        Object value = getInstance().getValue(key);
        if (value instanceof Float) {
            return (Float) value;
        }
        return 0.0f;
    }

    public static float getFloat(String key, float defaultValue) {
        Object value = getInstance().getValue(key);
        if (value instanceof Float) {
            return (Float) value;
        }
        return defaultValue;
    }

    public static double getDouble(String key) {
        Object value = getInstance().getValue(key);
        if (value instanceof Double) {
            return (Double) value;
        }
        return 0.0f;
    }

    public static double getDouble(String key, double defaultValue) {
        Object value = getInstance().getValue(key);
        if (value instanceof Double) {
            return (Double) value;
        }
        return defaultValue;
    }

    public static boolean getBoolean(String key) {
        Object value = getInstance().getValue(key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return false;
    }

    public static List<String> getStringList(String key) {
        Object value = getInstance().getValue(key);
        if (value instanceof List<?>) {
            List<?> valueList = (List<?>) value;
            return valueList.stream()
                    .filter(String.class::isInstance)
                    .map(String.class::cast)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @SuppressWarnings("unchecked")
    private Object getValue(String key) {
        return getValueFromMap(yamlMap, key);
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
}
