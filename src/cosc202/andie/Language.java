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
        
    public static void setup() //after button for language is pressed and premade lang code is given it creates a default locale to pull words
    {
        try{//try get the current language, and the file associated with it.
            Properties getLang = getProperties("src/resources/config.properties");
            lang = getLang.getProperty("language");
            Language.prop = getProperties("src/resources/lang_" + lang + ".properties");
        } catch (IOException ex) {
            UserMessage.showWarning(UserMessage.GENERIC_WARN);
        }
    }

    private static Properties getProperties(String filePath) throws IOException {
        FileInputStream inputStream = new FileInputStream(filePath);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8); //meed to specify UTF_8 otherwise it won't display correctly
        Properties getProperties = new Properties();
        getProperties.load(inputStreamReader);
        inputStreamReader.close();
        inputStream.close();
        return getProperties;
    }

    public static String getWord(String word)
    {
        return prop.getProperty(word);
    }

    public static void setLang(String lang)
    {
        try{
            // change the current language preference
            FileOutputStream output = new FileOutputStream("src/resources/config.properties");
            Properties setLang = new Properties();
            setLang.setProperty("language", lang);
            setLang.store(output, null);
            output.close();

            //get the language file for the new language
            Language.prop = getProperties("src/resources/lang_" + lang + ".properties");
            Language.lang = lang;
            //redraw everything to be in the right language.
            Andie.redrawMenuBar();
        }catch(IOException ex){
            UserMessage.showWarning(UserMessage.GENERIC_WARN);
        }
    }
}