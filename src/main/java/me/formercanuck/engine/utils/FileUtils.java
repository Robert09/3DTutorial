package me.formercanuck.engine.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class FileUtils {

    public static String loadShader(String path) {
        // Using getResourceAsStream means the file must be on the classpath (e.g., inside resources/).
        InputStream in = FileUtils.class.getResourceAsStream(path);
        if (in == null) {
            // If the file isn't found on the classpath, you'll get null here.
            throw new RuntimeException("File not found: " + path);
        }
        try (in) {
            return new String(in.readAllBytes());
        } catch (Exception e) {
            throw new RuntimeException("Failed to read file: " + path, e);
        }
    }

    public static File loadFile(String path) {
        // getResource returns a URL if the file is bundled in resources.
        // This is not the same as a normal file path on disk.
        URL url = FileUtils.class.getClassLoader().getResource(path);

        // If the resource doesn't exist, url will be null -> leads to NullPointerException here.
        File file = new File(url.getFile());

        if (!file.exists()) {
            // You're calling mkdirs() on the *file itself*, not its parent directory.
            // This will try to create a directory with the same name as your file.
            file.mkdir();

            try {
                file.createNewFile(); // This might fail if "file" is already a directory.
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return file;
    }

    public static void writeToFile(String content, String path) throws IOException {
        String home = System.getProperty("user.home");
        String configDir;
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            configDir = System.getenv("APPDATA"); // Roaming
            if (configDir == null) configDir = home + "\\AppData\\Roaming";
        } else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            configDir = home + "/Library/Application Support";
        } else {
            configDir = home + "/.config";
        }
        configDir += "/FormerFactory"; // your app subfolder

        File dir = new File(configDir);
        File file = new File(configDir + "/" + path);

        if (!dir.exists()) dir.mkdirs();

        if (!file.exists()) {
            file.createNewFile();
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
