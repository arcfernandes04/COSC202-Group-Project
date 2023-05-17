package cosc202.andie;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
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
     * The name of the config file.
     */
    private static String filename = "config.properties";

    static {
        setup();
    }
    
    /**
     * Attempt to open the config file.
     * If it is not possible, simply create a blank version,
     * although specify that the language is English to avoid
     * an unnecessary warning about the language not being found.
     */
    private static void setup(){
        try{
            if(prop != null) return;
            //Preferences are not stored in the class path - they are in AppData.
            FileInputStream inputStream = new FileInputStream(getAppDataPath() + filename);
            prop = new Properties();
            prop.load(inputStream); //open the properties file
            inputStream.close();
            if(prop.isEmpty()) prop.setProperty("language", "en");
        } catch (NullPointerException | IOException ex) {
            //There is an issue with the config file, so we need to just make a blank version.
            prop = new Properties();
            prop.setProperty("language", "en");
        }
    }

    /**
     * A helper method that opens a properties file
     * 
     * @param filePath The file path to be opened, inside of the src/resources/ directory.
     * @return The properties file that was loaded in
     * @throws IOException If the file in the provided path cannot be found
     */
    public static Properties getProperties(String filePath) throws NullPointerException, IOException {
        InputStream inputStream = Preferences.class.getResourceAsStream("/resources/" + filePath);
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
            FileOutputStream output = new FileOutputStream(new File(getAppDataPath() + filename));
            prop.put(key, value);
            prop.store(output, null);
            return true;
        }catch(IOException e){
            return false;
        }
    }

    /**
     * <p>
     * Returns the directory which holds ANDIE's local application data.
     * This will vary depending on the operating system;
     * Windows stores this data in "AppData/Local", while Unix systems
     * tend to store it in the "user.home" property.
     * </p>
     * 
     * @return The path to the local application data.
     */
    private static String getAppDataPath(){
        try{
            //The config is in a different place for different operating systems.
            String OS = (System.getProperty("os.name")).toUpperCase();

            String targetDir = "";
            if(OS.contains("WIN")){ //Windows
                String appdata = System.getenv("LOCALAPPDATA");
                targetDir = appdata + "/ANDIE/";
            }else if(OS.contains("MAC")){ //Mac
                String userhome = System.getProperty("user.home");
                targetDir = userhome + "/Library/Preferences/ANDIE/";
            }else{ //Linux / other OS
                String userhome = System.getProperty("user.home");
                targetDir = userhome + "/.ANDIE/"; // Has a dot in front of the name to hide it
            }
            //Make sure that the directory exists.
            Files.createDirectories(Paths.get(targetDir));

            return targetDir;
        }catch(IOException e){
            return "";
        }
    }
}