package main.java.config;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import org.json.simple.JSONValue;
import org.json.simple.JSONObject;

public class ConfigParser {
    private static final String CONFIG_FILE_PATH = Path.of("main", "resources", "config.json").toString();
    private long numberOfFloatingPointRegisters;

    @SuppressWarnings({"unchecked, deprecated"})
    public ConfigParser() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader(CONFIG_FILE_PATH))) {
            var obj = (JSONObject) JSONValue.parse(reader);
            var value = getValueFromJsonObjectOrThrow(obj, "numberOfFloatingPointRegisters");
            numberOfFloatingPointRegisters = castObjectToClassOrThrow(value, Long.class, "numberOfFloatingPointRegisters");
            System.out.println(numberOfFloatingPointRegisters);
        } catch (FileNotFoundException e) {
            throw new Exception("The path '" + CONFIG_FILE_PATH + "' could not be resolved. Please, create the file.");
        } catch (IOException e) {
            throw new Exception("A problem occurred while trying to open the config file. Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static Object getValueFromJsonObjectOrThrow(JSONObject obj, String jsonKey) throws Exception {
        var value = obj.get(jsonKey);

        if (value == null) {
            throw new Exception("The json key '" + jsonKey + "' was not found in config file. Please add it.");
        }

        return value;
    }

    private static <T> T castObjectToClassOrThrow(Object o, Class<T> c, String objName) throws Exception {
        try {
            return c.cast(o);
        } catch (ClassCastException e) {
            throw new Exception("Could not cast '" + objName + "' to " + c + ". Please put a valid value with this key.");
        }
    }
}
