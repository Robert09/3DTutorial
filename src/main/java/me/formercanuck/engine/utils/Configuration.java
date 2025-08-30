package me.formercanuck.engine.utils;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Configuration {

    private final File file;

    private Map<String, Object> data;

    private final Yaml yaml;

    public Configuration(String fileName) {
        // Use your AppData directory for configs
        String home = System.getProperty("user.home");
        String configDir;
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            configDir = System.getenv("APPDATA");
            if (configDir == null) configDir = home + "\\AppData\\Roaming";
        } else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            configDir = home + "/Library/Application Support";
        } else {
            configDir = home + "/.config";
        }
        configDir += "/FormerFactory"; // your app subfolder

        File dir = new File(configDir);
        if (!dir.exists()) dir.mkdirs();

        this.file = new File(dir, fileName + ".yml");

        // YAML options
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setIndent(2);
        LoaderOptions loaderOptions = new LoaderOptions();
        this.yaml = new Yaml(options);

        load(); // Load or create file
    }

    public void load() {
        if (!file.exists()) {
            try {
                file.createNewFile();
                // Default config
                data = new HashMap<>();
                save(); // write defaults
            } catch (IOException e) {
                throw new RuntimeException("Failed to create config file", e);
            }
        } else {
            try (FileReader reader = new FileReader(file)) {
                data = yaml.load(reader);
            } catch (IOException e) {
                throw new RuntimeException("Failed to read config", e);
            }
        }
    }

    public void save() {
        try (FileWriter writer = new FileWriter(file)) {
            yaml.dump(data, writer);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save config", e);
        }
    }

    // Accessors like Minecraft's configuration API
    public Object get(String path) {
        String[] parts = path.split("\\.");
        Map<String, Object> section = data;
        for (int i = 0; i < parts.length - 1; i++) {
            Object sub = section.get(parts[i]);
            if (!(sub instanceof Map)) return null;
            section = (Map<String, Object>) sub;
        }
        return section.get(parts[parts.length - 1]);
    }

    public int getInt(String path, int def) {
        Object obj = get(path);
        return (obj instanceof Number) ? ((Number) obj).intValue() : def;
    }

    public int getInt(String path) {
        return getInt(path, 0);
    }

    public float getFloat(String path, float def) {
        Object obj = get(path);
        if (obj == null) return def;
        if (obj instanceof Number) return ((Number) obj).floatValue();
        try {
            return Float.parseFloat(obj.toString());
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public float getFloat(String path) {
        return getFloat(path, 0.0f);
    }

    public double getDouble(String path, double def) {
        Object obj = get(path);
        return (obj instanceof Number) ? ((Number) obj).doubleValue() : def;
    }

    public double getDouble(String path) {
        return getDouble(path, 0.0d);
    }

    public boolean getBoolean(String path, boolean def) {
        Object obj = get(path);
        if (obj instanceof Boolean) return (Boolean) obj;
        if (obj instanceof String) return Boolean.parseBoolean((String) obj);
        return def;
    }

    public boolean getBoolean(String path) {
        return getBoolean(path, false);
    }

    public String getString(String path) {
        Object obj = get(path);
        return (obj != null) ? obj.toString() : null;
    }

    // Optional: get a nested section as a Map
    public Map<String, Object> getSection(String path) {
        Object obj = get(path);
        if (obj instanceof Map) return (Map<String, Object>) obj;
        return new HashMap<>();
    }

    public void set(String path, Object value) {
        String[] parts = path.split("\\.");
        Map<String, Object> section = data;
        for (int i = 0; i < parts.length - 1; i++) {
            Object sub = section.get(parts[i]);
            if (!(sub instanceof Map)) {
                sub = new java.util.LinkedHashMap<String, Object>();
                section.put(parts[i], sub);
            }
            section = (Map<String, Object>) sub;
        }
        section.put(parts[parts.length - 1], value);
    }
}
