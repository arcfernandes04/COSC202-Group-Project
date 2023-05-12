package cosc202.andie.settings;

import cosc202.andie.Language;

import javax.swing.*;

/**
 * <p>
 * Holds the options for changing settings in ANDIE;
 * most notably the language and theme options.
 * </p>
 * 
 * 
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Joshua Carter
 */
public class SettingsActions { 
    
    /** The menu for choosing languages. */
    private LanguageActions languageActions;
    /** The menu for choosing themes. */
    private ThemeActions themeActions;

    /**
     * <p>
     * Default construct which instantiates a new {@code SettingsActions} menu,
     * to hold {@code LanguageActions} and {@code ThemeActions}.
     * </p>
     * 
     * <p>
     * Use {@code createMenu()} to create the corresponding {@code JMenu}.
     * </p>
     */
    public SettingsActions() {
        languageActions = new LanguageActions();
        themeActions = new ThemeActions();
    }

    /**
     * <p>
     * Create the menu of all the options contained in this class.
     * </p>
     * 
     * @return A {@code JMenu} which holds the language and theme actions
     */
    public JMenu createMenu() {
        JMenu editMenu = new JMenu(Language.getWord("Settings"));
        editMenu.add(languageActions.createMenu());
        editMenu.add(themeActions.createMenu());        
        return editMenu;
    }    
}
