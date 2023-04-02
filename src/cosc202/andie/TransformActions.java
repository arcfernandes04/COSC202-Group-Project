package cosc202.andie;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * <p>
 * Actions provided by the Transform menu.
 * </p>
 *  
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA
 * 4.0</a>
 * </p>
 * 
 * @version 1.0
 */
public class TransformActions {

    /** A list of actions for the Transform menu. */
    protected ArrayList<Action> actions;

    /**
     * <p>
     * Create a set of Transform menu actions.
     * </p>
     */
    public TransformActions() {
        actions = new ArrayList<Action>();
        actions.add(new FlipImageAction("Flip Horizontally", null, "Flip Image Horizontally",
                Integer.valueOf(KeyEvent.VK_H), "horizontal"));
        actions.add(new FlipImageAction("Flip Vertically", null, "Flip Image Vertically",
                Integer.valueOf(KeyEvent.VK_H), "vertical"));
        actions.add(new RotateImageAction("Rotate 180 Degrees", null, "Rotate Image 180 Degrees",
                Integer.valueOf(KeyEvent.VK_H), "180"));
        actions.add(new RotateImageAction("Rotate 90 Degrees Right", null, "Rotate Image 90 Degrees Right",
                Integer.valueOf(KeyEvent.VK_H), "90 Right"));
        actions.add(new RotateImageAction("Rotate 90 Degrees Left", null, "Rotate Image 90 Degrees Left",
                Integer.valueOf(KeyEvent.VK_H), "90 Left"));
        actions.add(new ResizeImageAction("Resize Image", null, "Resize Image with a Spinner",
                Integer.valueOf(KeyEvent.VK_H)));
    }

    /**
     * <p>
     * Create a menu contianing the list of Transform actions.
     * </p>
     * 
     * @return The Transform menu UI element.
     */
    public JMenu createMenu() {
        JMenu transformMenu = new JMenu("Transform");

        for (Action action : actions) {
            transformMenu.add(new JMenuItem(action));
        }

        return transformMenu;
    }

    /**
     * <p>
     * Action to flip an image.
     * </p>
     * 
     * @see FlipImage
     */
    public class FlipImageAction extends ImageAction {

        private String direction;

        /**
         * <p>
         * Create a new flip-image action.
         * </p>
         * 
         * @param name      The name of the action (ignored if null).
         * @param icon      An icon to use to represent the action (ignored if null).
         * @param desc      A brief description of the action (ignored if null).
         * @param mnemonic  A mnemonic key to use as a shortcut (ignored if null).
         * @param direction The line the flip will occur on.
         */
        FlipImageAction(String name, ImageIcon icon, String desc, Integer mnemonic, String direction) {
            super(name, icon, desc, mnemonic);
            this.direction = direction;
        }

        /**
         * <p>
         * Callback for when the flip-image action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the FlipImageAction is triggered.
         * It Flips the image over a line deciphered from the direction data-field.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            target.getImage().apply(new FlipImage(this.direction));
            target.repaint();
            target.getParent().revalidate();
        }
    }

    public class RotateImageAction extends ImageAction {

        private String rotation;

        /**
         * <p>
         * Create a new rotate-image action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         * @param rotation The direction and degree of rotation.
         */
        RotateImageAction(String name, ImageIcon icon, String desc, Integer mnemonic, String rotation) {
            super(name, icon, desc, mnemonic);
            this.rotation = rotation;
        }

        /**
         * <p>
         * Callback for when the rotate-image action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the RotateImageAction is triggered.
         * It Rotates the image based on the degree and direction given by the
         * rotation datafield.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            target.getImage().apply(new RotateImage(rotation));
            target.repaint();
            target.getParent().revalidate();
        }
    }

    public class ResizeImageAction extends ImageAction {
        private boolean slider = true;

        /**
         * <p>
         * Create a new rotate-image action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         * @param rotation The direction and degree of rotation.
         */
        ResizeImageAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the rotate-image action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the RotateImageAction is triggered.
         * It Rotates the image based on the degree and direction given by the
         * rotation datafield.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            try {

                // set default sizePercentageIncrease to 100%
                int sizePercentageIncrease = 100;
                    // Determine the size percentage multiplier - ask the user.
                    // Pop-up dialog box to ask for the sizePercentageIncrease value.
                    SpinnerNumberModel percentageModel = new SpinnerNumberModel(100, 10, 1000, 1);
                    JSpinner percentageSpinner = new JSpinner(percentageModel);
                    int option = JOptionPane.showOptionDialog(null, percentageSpinner, "Enter Resize Percentage", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

                    // Check the return value from the dialog box.
                    if (option == JOptionPane.CANCEL_OPTION) {
                        return;
                    } else if (option == JOptionPane.OK_OPTION) {
                        sizePercentageIncrease = percentageModel.getNumber().intValue();
                    }
                target.getImage().apply(new ResizeImage(sizePercentageIncrease));
                target.repaint();
                target.getParent().revalidate();

            } catch (Exception ex){
                UserMessage.showWarning(UserMessage.GENERIC_WARN);
            }
        }
    }

}
