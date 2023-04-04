package cosc202.andie;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;


/**
 * 
 * @author Christopher Jones
 */
public abstract class Language
{
    private static String lang = "en";
    private static Properties prop;
    
    public static void setup()
    {
        try{//try get the current language, and the file associated with it.
            Properties getLang = getProperties("src/resources/config.properties");
            lang = getLang.getProperty("language");
            Language.prop = getProperties("src/resources/lang_" + lang + ".properties");
        } catch (IOException ex) {
            recover(); //if it fails, attempt to recover, or find a more systemic problem.
        }
    }

    /**
     * A helper method that opens a properties file
     * 
     * @param filePath
     * @return The properties file that was loaded in
     * @throws IOException
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

    public static String getWord(String word)
    {
        try{
            String output = prop.getProperty(word);
            if (output == null) return "?"; //ff it can't find the value, then return a question mark
            return output;
        }catch(Exception e){ //if the 'prop' is null, then the language file can't be found
            recover();
            return "?";
        }
    }

    public static void setLang(String lang)
    {
        try{
            //change the current language preference
            storeLang(lang);

            //get the language file for the new language
            Language.prop = getProperties("src/resources/lang_" + lang + ".properties");
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
        FileOutputStream output = new FileOutputStream("src/resources/config.properties");
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
            Language.prop = getProperties("src/resources/lang_en.properties");
            UserMessage.showWarning(UserMessage.MISSING_LANG_WARN);
        } catch (Exception e) { //if english isn't there, the languages are screwed and we need to just exit.
            UserMessage.showWarning(UserMessage.FATAL_LANG_WARN);
            System.exit(1);
        }
    }
}