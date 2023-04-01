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
    
    public static void setup(/*String lang*/) //after button for language is pressed and premade lang code is given it creates a default locale to pull words
    {
        actions = new ArrayList<Action>();
        actions.add(new engLang(getWord("eng"), null, "language change to english", Integer.valueOf(0)));
        actions.add(new maoLang(getWord("mao"), null, "language change to maori", Integer.valueOf(0)));

        Preferences prefs = Preferences.userNodeForPackage(Language.class);

        Locale.setDefault(new Locale(prefs.get("language", "en"/*lang*/), prefs.get("country", "NZ")));
        
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

    public class engLang extends ImageAction
    {
        engLang(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        public void actionPerformed(ActionEvent e)
        {
            lang = "en";
        }
    }

    public class maoLang extends ImageAction
    {
        maoLang(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        public void actionPerformed(ActionEvent e)
        {
            lang = "mao";
        }
    }
}