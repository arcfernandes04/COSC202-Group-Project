package cosc202.andie.draw;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

import cosc202.andie.*;

/**
 * <p>
 * Provides options for choosing the fill options in a shape (i.e. fill, border, fill and border). 
 * This class extends JMenu and can thus be added to JMenuBar instances.
 * </p>
 * 
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Joshua Carter
 * @see DrawPanel
 */
public class FillActions extends JMenuBar {

    private JMenu menu;

    /**
     * <p>
     * Default constructor which initialises the possible
     * fill options for the program.
     * </p>
     * 
     * The possible fill options are outlined in {@code DrawPanel}
     * @see DrawPanel
     * @param background The background colour to use for the JMenuBar;
     * if set to transparent, it will blend in nicely with the rest of the GUI.
     */
    public FillActions(Color background) {
        setBackground(background);
        setBorderPainted(false);
        menu = new JMenu();
        this.add(menu);
        menu.add(new FillOption(DrawPanel.BORDER_ONLY));
        menu.add(new FillOption(DrawPanel.FILL_ONLY));
        menu.add(new FillOption(DrawPanel.FILL_AND_BORDER));
    }

    /**
     * Updates the text on this JMenu.
     * Called once the user's drawing tools choices have changed.
     */
    public void update(){
        menu.setText(Language.getWord("Current_Fill") + " " + Language.getWord(DrawPanel.getFillType()));
    }

    /**
     * An inner class which represents each action of choosing a different fill
     * option.
     * 
     * If selected, it sets the program's fill option to equal the one represented
     * by this class.
     */
    private class FillOption extends ImageAction {
        /**
         * The tool this object represents
         */
        private String option;

        /**
         * <p>
         * Constructor which initialises the option and its text elements.
         * </p>
         * 
         * @param option The option to apply when the object is chosen.
         */
        private FillOption(String option) {
            super(Language.getWord(option), null, Language.getWord(option + "_desc"), null);
            this.option = option;
        }

        /**
         * A callback method which is triggered when the option is chosen.
         * The program's fill option is then set based on this choice.
         * 
         * @param e The {@code ActionEvent} which triggered this callback.
         */
        public void actionPerformed(ActionEvent e) {
            DrawPanel.setFillType(option);
        }
    }
}