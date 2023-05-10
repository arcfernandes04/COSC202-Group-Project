package cosc202.andie;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;


/**
 * <p>
 * A static class that manages access to the config.properties file,
 * handling both retrieval and storing of data.
 * <p>
 * 
 * <p>
 * If the config file cannot be located, it will be re-generated.
 * Classes accessing the config file should expect this possibility,
 * and use a default value if the inputted key cannot be resolved.
 * </p>
 * 
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Joshua Carter
 */
public abstract class Preferences{

    /**
     * The current properties map (config settings).
     */
    private static Properties prop;

    /**
     * The directory to look for resources
     */
    private static String directory = "src/resources/";

    /**
     * The name of the config file.
     */
    private static String filename = "config.properties";

    static {
        setup();
    }
    
    /**
     * Attempt to open the config file.
     * If it is not possible, simply create a blank version.
     */
    private static void setup(){
        try{
            if(prop != null) return;
            prop = getProperties(filename);
        } catch (IOException ex) {
            //There is an issue with the config file, so we need to just make a blank version.
            prop = new Properties();
        }
    }

    /**
     * A helper method that opens a properties file
     * 
     * @param filePath The file path to be opened, inside of the src/resources/ directory.
     * @return The properties file that was loaded in
     * @throws IOException If the file in the provided path cannot be found
     */
    public static Properties getProperties(String filePath) throws IOException {
        FileInputStream inputStream = new FileInputStream(directory + filePath);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8); //need to specify UTF_8 otherwise it won't display correctly
        Properties getProperties = new Properties();
        getProperties.load(inputStreamReader); //open the properties file
        inputStreamReader.close();
        inputStream.close();
        return getProperties;
    }

    /**
     * Retrieve the value associated with a key, or null if the key cannot be found in the file.
     * @param preference The key
     * @return The value corresponding to the key, or null if there is no corresponding value.
     */
    public static String getPreference(String key){
        if (prop == null) setup();
        return prop.getProperty(key);
    }

    /**
     * <p>
     * Store the {@code key} and its {@code value} in the config file.
     * </p>
     * 
     * @param key The name identifying this attribute.
     * @param value The value to store under this name, as a string.
     * @return Whether or not this operation was successful.
     */
    public static boolean setPreference(String key, String value){
        try{
            if(prop == null) setup();
            //Otherwise, store the property in the file and return true
            FileOutputStream output = new FileOutputStream(directory + filename);
            prop.put(key, value);
            prop.store(output, null);
            return true;
        }catch(IOException ex){
            return false;
        }
    }
}