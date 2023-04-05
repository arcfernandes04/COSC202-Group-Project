package cosc202.andie;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import javax.swing.UIManager;


/**
 * 
 * A static class that manages translations inside of ANDIE.
 * 
 * @author Christopher Jones
 */
public abstract class Language
{
    /** The current language. */
    private static String lang = "en";
    /** The properties holding the current language's information. */
    private static Properties prop;
    /** The directory that is being searched for the language files. */
    private static String directory = "src/resources/";
    
    public static void setup()
    {
        try{//try get the current language, and the file associated with it.
            Properties getLang = getProperties(directory + "config.properties");
            lang = getLang.getProperty("language");
            Language.prop = getProperties(directory + "lang_" + lang + ".properties");
            // Make sure JOptionPane is in the right language
            UIManager.put("OptionPane.cancelButtonText", Language.getWord("OptionPane.cancelButtonText"));
            UIManager.put("OptionPane.okButtonText", Language.getWord("OptionPane.okButtonText"));
        } catch (IOException ex) {
            recover(); //if it fails, attempt to recover, or find a more systemic problem.
        }
    }

    /**
     * A helper method that opens a properties file
     * 
     * @param filePath The file path to be opened
     * @return The properties file that was loaded in
     * @throws IOException If the file in the provided path cannot be found
     */
    private static Properties getProperties(String filePath) throws IOException {
        FileInputStream inputStream = new FileInputStream(filePath);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8); //need to specify UTF_8 otherwise it won't display correctly
        Properties getProperties = new Properties();
        getProperties.load(inputStreamReader); //open the properties file
        inputStreamReader.close();
        inputStream.close();
        return getProperties;
    }

    /**
     * Retrieve the value associated with a key. If the corresponding value cannot be found, then the
     * inputted key is returned as is.
     * @param word The key
     * @return The value corresponding to the key, or the key if there is no value.
     */
    public static String getWord(String word)
    {
        try{
            String output = prop.getProperty(word);
            if (output == null) return word; //if it can't find the value, then return the key.
            return output;
        }catch(Exception e){ //if the 'prop' is null, then the language file can't be found
            recover();
            return word;
        }
    }

    /**
     * Changes the program's language, refreshing all of the toolbar elements to reflect this change.
     * @param lang The new language to be using
     */
    public static void setLang(String lang)
    {
        try{
            //change the current language preference
            storeLang(lang);

            //get the language file for the new language
            Language.prop = getProperties(directory + "lang_" + lang + ".properties");
            // Make sure JOptionPane is in the right language
            UIManager.put("OptionPane.cancelButtonText", Language.getWord("OptionPane.cancelButtonText"));
            UIManager.put("OptionPane.okButtonText", Language.getWord("OptionPane.okButtonText"));
            //redraw everything to be in the right language.
            Andie.redrawMenuBar();
        }catch(IOException ex){
            recover(); //if it fails, attempt to recover, or find a more systemic problem.
        }
    }

    /**
     * A helper method to easily change which language is being used.
     * 
     * @param lang The new language to switch to
     * @throws IOException Unable to change to the desired language
     */
    private static void storeLang(String lang) throws IOException {
        //store the new language in the config files
        FileOutputStream output = new FileOutputStream(directory + "config.properties");
        Properties setLang = new Properties();
        setLang.setProperty("language", lang);
        setLang.store(output, null);
        output.close();
        //store this change locally
        Language.lang = lang;
    }

    /**
     * A helper method that attempts to recover from a missing or incorrect language file by defaulting to english.
     * If that doesn't work then close the whole program
     */
    private static void recover() {
        try { //try default to english
            lang = "en";
            storeLang(lang);
            Language.prop = getProperties(directory + "lang_en.properties");
            UserMessage.showWarning(UserMessage.MISSING_LANG_WARN);
        } catch (Exception e) { //if english isn't there, the languages are screwed and we need to just exit.
            UserMessage.showWarning(UserMessage.FATAL_LANG_WARN);
            System.exit(1);
        }
    }
}