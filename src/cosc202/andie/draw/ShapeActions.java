package cosc202.andie.draw;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

import cosc202.andie.*;

/**
 * <p>
 * Provides options for drawing different shapes. This class extends
 * JMenu and can thus be added to JMenuBar instances.
 * </p>
 * 
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Joshua Carter
 * @see DrawPanel
 */
public class ShapeActions extends JMenuBar {

    private JMenu menu;
    
    /**
     * <p>
     * Default constructor which initialises the possible
     * shape options for the program.
     * </p>
     * 
     * The possible shape options are outlined in {@code DrawPanel}
     * 
     * @see DrawPanel
     * @param background The background colour to use for the JMenuBar;
     * if set to transparent, it will blend in nicely with the rest of the GUI.
     */
    public ShapeActions(Color background) {
        setBackground(background);
        setBorderPainted(false);
        menu = new JMenu();
        this.add(menu);
        menu.add(new ShapeOption(DrawPanel.RECTANGLE));
        menu.add(new ShapeOption(DrawPanel.OVAL));
        menu.add(new ShapeOption(DrawPanel.LINE));
    }

    /**
     * Updates the text on this JMenu.
     * Called once the user's drawing tools choices have changed.
     */
    public void update() {
        menu.setText(Language.getWord("Current_Shape") + " " + Language.getWord(DrawPanel.getShapeType()));
    }

    /**
     * An inner class which represents each action of choosing a different shape to draw.
     * 
     * If selected, it changes program's shape option to equal the one stored in this instance.
     */
    private class ShapeOption extends ImageAction {
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
        private ShapeOption(String option) {
            super(Language.getWord(option), null, Language.getWord(option + "_desc"), null);
            this.option = option;
        }

        /**
         * A callback method which is triggered when the option is chosen.
         * The program's shape is then set based on this choice.
         * 
         * @param e The {@code ActionEvent} which triggered this callback.
         */
        public void actionPerformed(ActionEvent e) {
            DrawPanel.setShapeType(option);
        }
    }
}