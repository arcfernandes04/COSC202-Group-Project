package cosc202.andie.draw;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

import cosc202.andie.*;

/**
 * <p>
 * Provides options for different tools. This class extends
 * JMenu and can thus be added to JMenuBar instances.
 * </p>
 * 
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Joshua Carter
 * @see DrawPanel
 * @see Selection
 */
public class ToolActions extends JMenuBar {

    private JMenu menu;

    /**
     * <p>
     * Default constructor which initialises the possible
     * tool options for the program.
     * </p>
     * 
     * The options are outlined in {@code DrawPanel}
     * @see DrawPanel
     * @param background The background colour to use for the JMenuBar;
     * if set to transparent, it will blend in nicely with the rest of the GUI.
     */
    public ToolActions(Color background) {
        setBackground(background);
        setBorderPainted(false);
        menu = new JMenu();
        this.add(menu);
        menu.add(new ToolOption(DrawPanel.SELECTION));
        menu.add(new ToolOption(DrawPanel.SHAPE));
        menu.add(new ToolOption(DrawPanel.BRUSH));
    }

    /**
     * Updates the text on this JMenu.
     * Called once the user's drawing tools choices have changed.
     */
    public void update(){
        menu.setText(Language.getWord("Current_Tool") + " " + Language.getWord(DrawPanel.getTool()));
    }

    /**
     * An inner class which represents each action of choosing a different tool.
     * 
     * If selected, it changes the program's tool to equal the tool represented by this instance.
     */
    private class ToolOption extends ImageAction {
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
        private ToolOption(String option) {
            super(Language.getWord(option), null, Language.getWord(option + "_desc"), null);
            this.option = option;
        }

        /**
         * A callback method which is triggered when the option is chosen.
         * The program's tool is then set based on this choice.
         * 
         * @param e The {@code ActionEvent} which triggered this callback.
         */
        public void actionPerformed(ActionEvent e) {
            DrawPanel.setTool(option);
        }
    }
}