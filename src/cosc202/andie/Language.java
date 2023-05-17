package cosc202.andie;

import java.io.*;
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
    private static String language;
    /** The properties holding the current language's information. */
    private static Properties prop;
    /** The default language to use when there is none specified, or the
     * one specified in the config file is not available. */
    private static final String DEFAULT_LANG = "en";

    static {
        setup();
    }
    
    /**
     * Check which language is to be used, and load in the properties file corresponding to it.
     */
    private static void setup()
    {
        try{//try get the current language, and the file associated with it.
            Language.language = Preferences.getPreference("language");
            Language.prop = Preferences.getProperties("lang_" + language + ".properties");
            // Make sure JOptionPane and AndieFileChooser is in the right language
            updateUIManager();
        } catch (IOException | NullPointerException ex) {
            recover(DEFAULT_LANG); //if it fails, attempt to recover, or find a more systemic problem.
        }
    }

    /**
     * Update the GUI elements managed by the UIManager; i.e. {@code OptionPane} and {@code JColorChooser}
     * (used by {@code UserMessage}) and {@code JFileChooser} (extended by {@code AndieFileChooser}).
     */
    private static void updateUIManager(){
        UIManager.put("OptionPane.cancelButtonText", Language.getWord("OptionPane.cancelButtonText"));
        UIManager.put("OptionPane.okButtonText", Language.getWord("OptionPane.okButtonText"));
        UIManager.put("ColorChooser.hsvHueText", Language.getWord("ColorChooser.hsvHueText"));
        UIManager.put("ColorChooser.hsvSaturationText", Language.getWord("ColorChooser.hsvSaturationText"));
        UIManager.put("ColorChooser.hsvValueText", Language.getWord("ColorChooser.hsvValueText"));
        UIManager.put("ColorChooser.hsvTransparencyText", Language.getWord("ColorChooser.hsvTransparencyText"));

        for (String option : AndieFileChooser.getElementsToRename()) {
            UIManager.put("FileChooser." + option, Language.getWord("FileChooser." + option));
        }
    }

    /**
     * Retrieve the value associated with a key. If the corresponding value cannot be found, then the
     * inputted key is returned as is.
     * @param word The key
     * @return The value corresponding to the key, or the key if there is no value.
     */
    public static String getWord(String word)
    {
        if(prop == null) setup();
        try{
            String output = prop.getProperty(word);
            if (output == null) return word; //if it can't find the value, then return the key.
            return output;
        }catch(Exception e){ //if the 'prop' is null, then the language file can't be found
            recover(DEFAULT_LANG);
            return word;
        }
    }

    /**
     * Changes the program's language, refreshing all of the toolbar elements to reflect this change.
     * @param lang The new language to be using
     */
    public static void setLanguage(String lang)
    {
        //Store the previous language so that we can return to it if an error occurs.
        String previous_lang = Language.language;
        try{
            //change the current language preference
            Preferences.setPreference("language", lang);
            //get the language file for the new language
            Language.language = lang;
            Language.prop = Preferences.getProperties("lang_" + language + ".properties");
            // Make sure JOptionPane and AndieFileChooser is in the right language
            updateUIManager();
            //redraw everything to be in the right language.
            Andie.redrawPanels();
        }catch(IOException | NullPointerException ex){
            recover(previous_lang); //if it fails, attempt to recover, or find a more systemic problem.
        }
    }

    /**
     * A helper method that attempts to recover from a missing or incorrect language file by defaulting to english.
     * If that doesn't work then close the whole program
     */
    private static void recover(String previous_lang) {
        try { //try default to english
            Language.language = previous_lang;
            Language.prop = Preferences.getProperties("lang_" + language + ".properties");
            Preferences.setPreference("language", language);
            UserMessage.showWarning(UserMessage.MISSING_LANG_WARN);
        } catch (Exception e) { //if english isn't there, the languages are screwed and we need to just exit.
            UserMessage.showWarning(UserMessage.FATAL_LANG_WARN);
            System.exit(1);
        }
    }
}