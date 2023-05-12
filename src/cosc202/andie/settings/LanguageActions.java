package cosc202.andie.settings;

import cosc202.andie.Language;
import cosc202.andie.ImageAction;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * The class which manages changing languages through the menu item in the GUI.
 * 
 * @author Christopher Jones
 */
public class LanguageActions {
    
    /** A list of actions for the File menu. */
    protected ArrayList<Action> actions;
    /**
     * Create a set of Language menu actions.
     */
    public LanguageActions() {
        actions = new ArrayList<Action>();
        actions.add(new engLang(Language.getWord("eng"), null, Language.getWord("eng_desc"), Integer.valueOf(KeyEvent.VK_F7))); // button that changes language to english
        actions.add(new maoLang(Language.getWord("mao"), null, Language.getWord("mao_desc"), Integer.valueOf(KeyEvent.VK_F8))); // button that changes language to maori
        actions.add(new afrLang(Language.getWord("afr"), null, Language.getWord("afr_desc"), Integer.valueOf(KeyEvent.VK_F9))); // button that changes language to afrikaans
    }
    /**
     * <p>
     * Create a menu containing the list of Language actions.
     * </p>
     * 
     * @return The Language menu UI element.
     */
    public JMenu createMenu() { // Creates a new JMenu with the name Language
        JMenu fileMenu = new JMenu(Language.getWord("Language"));

        for(Action action: actions) { // for each loop to add all languages provided to arraylist
            fileMenu.add(new JMenuItem(action));
        }

        return fileMenu;
    }

    /**
     * class to set language to english and create buttons under the fileMenu
     */
    public class engLang extends ImageAction
    {
        engLang(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        public void actionPerformed(ActionEvent e)
        {
            Language.setLanguage("en");
        }
    }

    /**
     * class to set language to maori and create buttons under the fileMenu
     */
    public class maoLang extends ImageAction
    {
        maoLang(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        public void actionPerformed(ActionEvent e)
        {
            Language.setLanguage("mi");
        }
    }

    /**
     * class to set language to afrikaans and create buttons under the fileMenu
     */
    public class afrLang extends ImageAction
    {
        afrLang(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        public void actionPerformed(ActionEvent e)
        {
            Language.setLanguage("af");
        }
    }
}
