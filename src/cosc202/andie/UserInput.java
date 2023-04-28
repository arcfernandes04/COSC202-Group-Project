package cosc202.andie;

import java.util.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * An abstract class that allows for consistent user input methods, using JSlider and JSpinner.
 * Image operations which have slider functionality extend this class.
 * 
 * @author Corban Surtees
 */
public abstract class UserInput extends ImageAction {

    /** True if the action uses a JSlider, false if it uses a JSpinner. */
    private boolean slider = true;
    /** The minimum value that can be inputted. */
    private int min;
    /** The maximum value that can be inputted. */
    private int max;
    /** The starting value */
    private int val;
    /** The value that represents zero, i.e. when the filter needs to be turned off in the previewApply() method. */
    private int zeroVal;

    /** The program's main Icon, which will be displayed inside JOptionPane message boxes. */
    private static Icon icon = Andie.getIcon();

    /** The JFrame to create pop up windows inside of. */
    private static JFrame parent = Andie.getFrame();

    /**
     * Set the parent component for all ColourActions instances.
     * 
     * @param parent The parent JFrame instance
     */
    public static void setParent(JFrame parent) {
        UserInput.parent = parent;
    }

    /**
     * <p>
     *      Asks user for input.
     * </p>
     * 
     * @param slider If this user input should have a slider.
     */
    UserInput(String name, ImageIcon icon, String desc, Integer mnemonic,
                boolean slider, int min, int max, int val, int zeroVal) {
        super(name, icon, desc, mnemonic);
        this.slider = slider;
        this.min = min;
        this.max = max;
        this.val = val;
        this.zeroVal = zeroVal;
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

            if (target.getImage().hasImage() == false) {
                UserMessage.showWarning(UserMessage.NULL_FILE_WARN);
                return;
            }

            // set default sizeIncrease to 100%
            int sizeIncrease = val;

            if (slider){

                // Determine the size percentage multiplier - ask the user.
                // Pop-up dialog box to ask for the sizeIncrease value.
                JSlider percentageSlider = new JSlider(JSlider.HORIZONTAL, min, max, val);

                SpinnerNumberModel percentageModel = new SpinnerNumberModel(val, min, max, 1);
                JSpinner percentageSpinner = new JSpinner(percentageModel);

                class JSliderListener implements ChangeListener{
                    public void stateChanged(ChangeEvent e){
                        try {
                            JSlider source = (JSlider)e.getSource();
                            percentageSpinner.setValue((int)source.getValue());
                            target.getImage().previewApply((ImageOperation)mutateImage((int)source.getValue()));
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        target.repaint();
                        target.getParent().revalidate();
                    }
                }

                class JSpinnerListener implements ChangeListener{
                    public void stateChanged(ChangeEvent e){
                        try {
                            JSpinner source = (JSpinner)e.getSource();
                            percentageSlider.setValue((int)source.getValue());
                            target.getImage().previewApply((ImageOperation)mutateImage((int)source.getValue()));
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        target.repaint();
                        target.getParent().revalidate();
                    }
                }

                // add listeners to slider and spinner
                percentageSlider.addChangeListener(new JSliderListener());
                percentageSpinner.addChangeListener(new JSpinnerListener());

                JPanel optionPanel = new JPanel();
                optionPanel.add(percentageSlider);
                optionPanel.add(percentageSpinner);

                int option = JOptionPane.showOptionDialog(UserInput.parent, optionPanel, Language.getWord("EnterValue"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, UserInput.icon, null, null);
                
                // Check the return value from the dialog box.
                if (option != JOptionPane.OK_OPTION) {
                    target.getImage().previewApply((ImageOperation)mutateImage(zeroVal));
                    target.repaint();
                    target.getParent().revalidate();
                    return;
                } else {
                    sizeIncrease = (int)percentageSpinner.getValue();
                }
            }
            else {
                // Determine the size percentage multiplier - ask the user.
                // Pop-up dialog box to ask for the sizeIncrease value.

                class JSpinnerListener implements ChangeListener{
                    public void stateChanged(ChangeEvent e){
                        try {
                            JSpinner source = (JSpinner)e.getSource();
                            target.getImage().previewApply((ImageOperation)mutateImage((int)source.getValue()));
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        target.repaint();
                        target.getParent().revalidate();
                    }
                }

                SpinnerNumberModel percentageModel = new SpinnerNumberModel(val, min, max, 1);
                JSpinner percentageSpinner = new JSpinner(percentageModel);
                percentageSpinner.addChangeListener(new JSpinnerListener());
                int option = JOptionPane.showOptionDialog(UserInput.parent, percentageSpinner, Language.getWord("EnterValue"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, UserInput.icon, null, null);

                // Check the return value from the dialog box.
                if (option != JOptionPane.OK_OPTION) {
                    sizeIncrease = val;
                    target.getImage().previewApply((ImageOperation)mutateImage(zeroVal));
                    return;
                } else {
                    sizeIncrease = percentageModel.getNumber().intValue();
                }
            }

            target.getImage().apply((ImageOperation)mutateImage(sizeIncrease));
            target.repaint();
            target.getParent().revalidate();

        } catch (Exception ex) {
            UserMessage.showWarning(UserMessage.GENERIC_WARN);
        }
    }

    abstract Object mutateImage(int input);

}

