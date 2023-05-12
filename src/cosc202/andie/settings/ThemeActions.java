package cosc202.andie.settings;

import cosc202.andie.Language;
import cosc202.andie.Preferences;
import cosc202.andie.Andie;
import cosc202.andie.ImageAction;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import com.formdev.flatlaf.intellijthemes.*;

/**
 * <p>
 * Gives the user options for selecting themes.
 * Uses the {@code FlatLaf} library's themes.
 * </p>
 * 
 * FlatLaf is open source licensed under the Apache 2.0 License:
 * https://github.com/JFormDesigner/FlatLaf/blob/main/LICENSE
 * 
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA
 * 4.0</a>
 * </p>
 * 
 * @author Joshua Carter
 */
public class ThemeActions {

    /** A list of actions for the File menu. */
    private static ArrayList<Action> actions;
    /** The theme currently being used in ANDIE. */
    private static LookAndFeel globalTheme;

    /**
     * <p>
     * Before we can start using a theme, we need to get the initial one
     * from the {@code Preferences} class. The preference may or may not exist,
     * in which case we default to {@code FlatMacLightLaf}.
     * </p>
     */
    public static void setup(){
        String themeFromFile = Preferences.getPreference("theme");

        //Go through each available theme and check if it matches
        createActions();
        for (Action option : actions){
            ThemeOption themeOption = (ThemeOption) option;
            if(themeOption.getThemeName().equals(themeFromFile)){
                ThemeActions.globalTheme = themeOption.theme;
                break;
            }
        }
        //This is the default value for themes.
        if (ThemeActions.globalTheme == null) ThemeActions.globalTheme = new FlatMacLightLaf();
        
        FlatLaf.setup(ThemeActions.globalTheme);
    }

    /**
     * <p>
     * Create a set of Language menu actions.
     * </p>
     * 
     * <p>
     * This default constructor calls {@code createActions} even though
     * the internal {@code actions} field will have already been initialised,
     * because we need to refresh the actions' names if the language changes.
     * </p>
     */
    public ThemeActions() {
        createActions();
    }

    /**
     * <p>
     * Create actions for all of the supported themes,
     * and store them on an array list.
     * </p>
     */
    private static void createActions(){
        actions = new ArrayList<Action>();
        actions.add(new ThemeOption(Language.getWord("theme_light_rounded"), null, Language.getWord("theme_light_rounded_desc"), 0, new FlatMacLightLaf()));
        actions.add(new ThemeOption(Language.getWord("theme_light_basic"), null, Language.getWord("theme_light_basic_desc"), 0, new FlatLightLaf()));
        actions.add(new ThemeOption(Language.getWord("theme_light_orange"), null, Language.getWord("theme_light_orange_desc"), 0, new FlatArcOrangeIJTheme()));
        actions.add(new ThemeOption(Language.getWord("theme_light_cyan"), null, Language.getWord("theme_light_cyan_desc"), 0, new FlatCyanLightIJTheme()));

        actions.add(new ThemeOption(Language.getWord("theme_dark_rounded"), null, Language.getWord("theme_dark_rounded_desc"), 0, new FlatMacDarkLaf()));
        actions.add(new ThemeOption(Language.getWord("theme_dark_basic"), null, Language.getWord("theme_dark_basic_desc"), 0, new FlatDarkLaf()));
        actions.add(new ThemeOption(Language.getWord("theme_dark_purple"), null, Language.getWord("theme_dark_purple_desc"), 0, new FlatDarkPurpleIJTheme()));
        actions.add(new ThemeOption(Language.getWord("theme_dark_contrast"), null, Language.getWord("theme_dark_contrast_desc"), 0, new FlatHighContrastIJTheme()));
    }

    /**
     * <p>
     * Create a menu containing the list of Language actions.
     * </p>
     * 
     * @return The Language menu UI element.
     */
    public JMenu createMenu() { // Creates a new JMenu with the name Language
        JMenu fileMenu = new JMenu(Language.getWord("Theme"));

        for (Action action : actions) { // for each loop to add all languages provided to arraylist
            fileMenu.add(new JMenuItem(action));
        }

        return fileMenu;
    }

    /**
     * <p>
     * Inner class representing a theme option.
     * </p>
     * 
     */
    public static class ThemeOption extends ImageAction {

        /** The theme this instance represents */
        private FlatLaf theme;
        /**
         * 
         * @param name The name of the theme, adjusted to the correct language
         * @param icon The icon to be displayed in GUI
         * @param desc The description to be displayed while hovering over the option
         * @param mnemonic The key binding to invoke this option
         * @param theme The theme this instance holds
         */
        ThemeOption(String name, ImageIcon icon, String desc, Integer mnemonic, FlatLaf theme) {
            super(name, icon, desc, mnemonic);
            this.theme = theme;
        }

        /**
         * Get the name of this instance's theme
         * 
         * @return The class name of this instance's theme, as a string.
         */
        protected String getThemeName(){
            return this.theme.getClass().toString();
        }

        /**
         * <p>
         * A callback triggered when this option is chosen.
         * </p>
         *  
         * Sets the global theme in ANDIE to the theme stored in this instance,
         * and revalidates the whole component hierarchy so that they are displayed correctly.
         */
        public void actionPerformed(ActionEvent e) {
            ThemeActions.globalTheme = this.theme;
            FlatLaf.setup(ThemeActions.globalTheme);
            SwingUtilities.updateComponentTreeUI(Andie.getFrame());
            Preferences.setPreference("theme", ThemeActions.globalTheme.getClass().toString());
        }
    }
}
