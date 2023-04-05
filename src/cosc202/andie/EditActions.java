package cosc202.andie;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;

 /**
 * <p>
 * Actions provided by the Edit menu.
 * </p>
 * 
 * <p>
 * The Edit menu is very common across a wide range of applications.
 * There are a lot of operations that a user might expect to see here.
 * In the sample code there are Undo and Redo actions, but more may need to be added.
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Steven Mills
 * @version 1.0
 */
public class EditActions {
    
    /** A list of actions for the Edit menu. */
    protected ArrayList<Action> actions;

    /**
     * <p>
     * Create a set of Edit menu actions.
     * </p>
     */
    public EditActions() {
        actions = new ArrayList<Action>();
        actions.add(new UndoAction(Language.getWord("Undo"), null, Language.getWord("Undo_desc"), Integer.valueOf(KeyEvent.VK_Z)));
        actions.add(new UndoAllAction(Language.getWord("Undo_all"), null, Language.getWord("Undo_all_desc"), Integer.valueOf(KeyEvent.VK_Z)));
        actions.add(new RedoAction(Language.getWord("Redo"), null, Language.getWord("Redo_desc"), Integer.valueOf(KeyEvent.VK_Y)));
        actions.add(new RedoAllAction(Language.getWord("Redo_all"), null, Language.getWord("Redo_all_desc"), Integer.valueOf(KeyEvent.VK_Y)));
    }

    /**
     * <p>
     * Create a menu contianing the list of Edit actions.
     * </p>
     * 
     * @return The edit menu UI element.
     */
    public JMenu createMenu() {
        JMenu editMenu = new JMenu(Language.getWord("Edit"));

        for (Action action: actions) {
            editMenu.add(new JMenuItem(action));
        }
        
        return editMenu;
    }

    /**
     * <p>
     * Action to undo an {@link ImageOperation}.
     * </p>
     * 
     * @see EditableImage#undo()
     */
    public class UndoAction extends ImageAction {

        /**
         * <p>
         * Create a new undo action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        UndoAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the undo action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the UndoAction is triggered.
         * It undoes the most recently applied operation.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            target.getImage().undo();
            target.repaint();
            target.getParent().revalidate();
        }
    }

    /**
     * <p>
     * Action to undo all {@link ImageOperation} operations applied to image.
     * </p>
     * 
     * @see EditableImage#undoAll()
     */
    public class UndoAllAction extends ImageAction {

        /**
         * <p>
         * Create a new undoAll action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        UndoAllAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the undoAll action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the UndoAllAction is triggered.
         * It undoes all of the applied operations.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            target.getImage().undoAll();
            target.repaint();
            target.getParent().revalidate();
        }
    }

     /**
     * <p>
     * Action to redo an {@link ImageOperation}.
     * </p>
     * 
     * @see EditableImage#redo()
     */   
    public class RedoAction extends ImageAction {

        /**
         * <p>
         * Create a new redo action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        RedoAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        
        /**
         * <p>
         * Callback for when the redo action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the RedoAction is triggered.
         * It redoes the most recently undone operation.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            target.getImage().redo();
            target.repaint();
            target.getParent().revalidate();
        }
    }

    /**
     * <p>
     * Action to redo all {@link ImageOperation} operations applied to the image.
     * </p>
     * 
     * @see EditableImage#redoAll()
     */   
    public class RedoAllAction extends ImageAction {

        /**
         * <p>
         * Create a new redoAll action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        RedoAllAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        
        /**
         * <p>
         * Callback for when the redoAll action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the RedoAllAction is triggered.
         * It redoes all undone operations.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            target.getImage().redoAll();
            target.repaint();
            target.getParent().revalidate();
        }
    }

}
