package main.java.config;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Map;

import org.json.simple.JSONValue;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class ConfigParser {
    private static final String CONFIG_FILE_PATH = Path.of("main", "resources", "config.json").toString();

    private static final String INSTRUCTION_QUEUE_LENGTH_JSON_KEY = "instructionQueueLength";
    private static final String NUMBER_OF_FLOATING_POINT_REGISTERS_JSON_KEY = "numberOfFloatingPointRegisters";

    private static final String BUFFERS_SECTION_JSON_KEY = "buffers";
    private static final String NUMBER_OF_LOAD_BUFFERS_JSON_KEY = "numberOfLoadBuffers";
    private static final String NUMBER_OF_STORE_BUFFERS_JSON_KEY = "numberOfStoreBuffers";

    private static final String RESERVATIONS_STATIONS_SECTION_JSON_KEY = "reservationStations";
    private static final String NUMBER_OF_MUL_STATIONS_KEY = "numberOfMulStations";
    private static final String NUMBER_OF_ADD_STATIONS_JSON_KEY = "numberOfAddStations";


    public static Config parse() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader(CONFIG_FILE_PATH))) {
            var configInfos = (JSONObject) JSONValue.parseWithException(reader);
            return createConfigFromJsonObject(configInfos);
        } catch (FileNotFoundException e) {
            throw new Exception("The path '" + CONFIG_FILE_PATH + "' could not be resolved. Please, create the file.");
        } catch (ParseException e) {
            throw new Exception("The config file must be contains valid JSON. Error: " + e.getMessage());
        } catch (IOException e) {
            throw new Exception("A problem occurred while trying to open the config file. Error: " + e.getMessage());
        } catch (Exception e) {
            throw e;
        }
    }

    private static Config createConfigFromJsonObject(JSONObject obj) throws Exception {
        var instructionQueueLength = getValueFromJsonObjectOrThrow(obj, INSTRUCTION_QUEUE_LENGTH_JSON_KEY, Long.class);
        var numberOfFloatingPointRegisters = getValueFromJsonObjectOrThrow(obj, NUMBER_OF_FLOATING_POINT_REGISTERS_JSON_KEY, Long.class);
        var buffersSection = getValueFromJsonObjectOrThrow(obj, BUFFERS_SECTION_JSON_KEY, JSONObject.class);
        var numberOfLoadBuffers = getValueFromJsonObjectOrThrow(buffersSection, NUMBER_OF_LOAD_BUFFERS_JSON_KEY, Long.class);
        var numberOfStoreBuffers = getValueFromJsonObjectOrThrow(buffersSection, NUMBER_OF_STORE_BUFFERS_JSON_KEY, Long.class);
        var stationsSection = getValueFromJsonObjectOrThrow(obj, RESERVATIONS_STATIONS_SECTION_JSON_KEY, JSONObject.class);
        var numberOfMulStations = getValueFromJsonObjectOrThrow(stationsSection, NUMBER_OF_MUL_STATIONS_KEY, Long.class);
        var numberOfAddStations = getValueFromJsonObjectOrThrow(stationsSection, NUMBER_OF_ADD_STATIONS_JSON_KEY, Long.class);

        return new Config(instructionQueueLength,
            numberOfFloatingPointRegisters,
            numberOfLoadBuffers, numberOfStoreBuffers,
            numberOfAddStations, numberOfMulStations);
    }

    private static <T> T getValueFromJsonObjectOrThrow(JSONObject obj, String jsonKey, Class<T> castTo) throws Exception {
        var value = obj.get(jsonKey);

        if (value == null) {
            throw new Exception("The json key '" + jsonKey + "' was not found in config file. Please add it.");
        }

        try {
            return castTo.cast(value);
        } catch (ClassCastException e) {
            throw new Exception("Could not cast '" + jsonKey + "' to " + castTo + ". Please put a valid value with this key.");
        }
    }
}
