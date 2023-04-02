package cosc202.andie;

import java.awt.event.*;
import java.util.*;
import java.util.prefs.Preferences;
import javax.swing.*;

import javax.swing.Action;

public abstract class Language
{
    private static ResourceBundle bund;
    protected static ArrayList<Action> actions;
    private static String lang = "en";
    
    static Preferences prefs = Preferences.userNodeForPackage(Language.class);

    public static void setup(/*String lang*/) //after button for language is pressed and premade lang code is given it creates a default locale to pull words
    {

        Locale.setDefault(new Locale(prefs.get("language", "mi"/*lang*/), prefs.get("country", "NZ")));
        
        bund = ResourceBundle.getBundle("MessageBundle");
    }

    public static String getWord(String word)
    {
        return bund.getString(word);
    }

    public JMenu createMenu() {
        JMenu fileMenu = new JMenu(Language.getWord("Language"));

        for(Action action: actions) {
            fileMenu.add(new JMenuItem(action));
        }

        return fileMenu;
    }

    public static String getLang()
    {
        return lang;
    }

    public static void setLang(String lang)
    {
        Language.lang = lang;

        prefs.put("language", lang);
        prefs.put("country", "NZ");
    }
}