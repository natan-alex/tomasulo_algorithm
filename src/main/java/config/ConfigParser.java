package main.java.config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;

public class ConfigParser {
    private static final String CONFIG_FILE_PATH = Path.of("main", "resources", "config.json").toString();
    public ConfigParser() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CONFIG_FILE_PATH))) {
            System.out.println(reader.readLine());
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
