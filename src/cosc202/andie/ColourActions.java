package cosc202.andie;

import java.util.*;
import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.*;

/**
 * <p>
 * Actions provided by the Colour menu.
 * </p>
 * 
 * <p>
 * The Colour menu contains actions that affect the colour of each pixel directly 
 * without reference to the rest of the image.
 * This includes conversion to greyscale in the sample code, but more operations will need to be added.
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Steven Mills
 * @version 1.0
 */
public class ColourActions {
    
    /** A list of actions for the Colour menu. */
    protected ArrayList<Action> actions;

    /**
     * <p>
     * Create a set of Colour menu actions.
     * </p>
     */
    public ColourActions() {
        actions = new ArrayList<Action>();
        actions.add(new ConvertToGreyAction("Greyscale", null, "Convert to greyscale", Integer.valueOf(KeyEvent.VK_G)));
        actions.add(new BrightnessContrastAction("Brightness/Contrast", null, "Adjust brightness and contrast levels", Integer.valueOf(KeyEvent.VK_B)));
    }

    /**
     * <p>
     * Create a menu contianing the list of Colour actions.
     * </p>
     * 
     * @return The colour menu UI element.
     */
    public JMenu createMenu() {
        JMenu fileMenu = new JMenu("Colour");

        for(Action action: actions) {
            fileMenu.add(new JMenuItem(action));
        }

        return fileMenu;
    }

    /**
     * <p>
     * Action to convert an image to greyscale.
     * </p>
     * 
     * @see ConvertToGrey
     */
    public class ConvertToGreyAction extends ImageAction {

        /**
         * <p>
         * Create a new convert-to-grey action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        ConvertToGreyAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the convert-to-grey action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the ConvertToGreyAction is triggered.
         * It changes the image to greyscale.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            try{
                target.getImage().apply(new ConvertToGrey());
                target.repaint();
                target.getParent().revalidate();
            }catch(Exception ex){
                new UserMessage(ex);
            }
        }

    }

        /**
     * <p>
     * Action to adjust the brightness/contrast of image.
     * </p>
     * 
     * @see BrightnessContrastAdjustment
     */
    public class BrightnessContrastAction extends ImageAction {

        /**
         * Create a new brightness-contrast action
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        BrightnessContrastAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

         /**
         * <p>
         * Callback for when the brightness-contrast action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the BrightnessContrastAction is triggered.
         * It prompts the user for a brightness and a contrast percentage change, then applies the appropriate {@link BrightnessContrastAdjustment}.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            // Determine brigtness/contrast - ask the user
            int brightness = 0;
            int contrast = 0;

            //Pop-up dialog box to ask for the brightness/contrast values
            SpinnerNumberModel brightnessModel = new SpinnerNumberModel(0, -100, 100, 1);
            SpinnerNumberModel contrastModel = new SpinnerNumberModel(0, -100, 100, 1);
            JSpinner brightnessSpinner = new JSpinner(brightnessModel);
            JSpinner contrastSpinner = new JSpinner(contrastModel);
            
            JPanel spinnerPanel = new JPanel(new GridLayout(0, 1));
            spinnerPanel.add(new JLabel("Brightness:"));
            spinnerPanel.add(brightnessSpinner);
            spinnerPanel.add(new JLabel("Contrast:"));
            spinnerPanel.add(contrastSpinner);

            int option = JOptionPane.showOptionDialog(null, spinnerPanel, "Adjust brightness/contrast", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

            // Check return value from the dialog box
            if(option == JOptionPane.CANCEL_OPTION) {
                return;
            }else {
                brightness = brightnessModel.getNumber().intValue();
                contrast = contrastModel.getNumber().intValue();
            }

            // Create and apply filter
            target.getImage().apply(new BrightnessContrastAdjustment(brightness, contrast));
            target.repaint();
            target.getParent().revalidate();

        }
    }

}
