package cosc202.andie;

import java.util.*;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.*;
import javax.swing.*;

/**
 * <p>
 * Actions provided by the Filter menu.
 * </p>
 * 
 * <p>
 * The Filter menu contains actions that update each pixel in an image based on
 * some small local neighbourhood. 
 * This includes a mean filter (a simple blur) in the sample code, but more operations will need to be added.
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Steven Mills
 * @version 1.0
 */
public class FilterActions {
    
    /** A list of actions for the Filter menu. */
    protected ArrayList<Action> actions;

    /** The program's main icon, which will be displayed inside JOptionPane dialog messages. */
    private static Icon icon = Andie.getIcon();

    /** The JFrame to create pop up windows inside of. */
    private static JFrame parent = Andie.getFrame();

    /**
     * <p>
     * Create a set of Filter menu actions.
     * </p>
     */
    public FilterActions() {
        actions = new ArrayList<Action>();
        actions.add(new MeanFilterAction(Language.getWord("Mean"), null, Language.getWord("Mean_desc"), Integer.valueOf(KeyEvent.VK_M), true, 1, 10, 1, 0));
        actions.add(new SharpenFilterAction(Language.getWord("Sharpen"), null, Language.getWord("Sharpen_desc"), Integer.valueOf(KeyEvent.VK_N)));
        actions.add(new GaussianBlurFilterAction(Language.getWord("Gaussian"), null, Language.getWord("Gaussian_desc"), Integer.valueOf(KeyEvent.VK_I), true, 1, 10, 1, 0));
        actions.add(new MedianFilterAction(Language.getWord("Median"), null, Language.getWord("Median_desc"), Integer.valueOf(KeyEvent.VK_L)));
        actions.add(new EmbossFilterAction(Language.getWord("Emboss"), null, Language.getWord("Emboss_desc"), Integer.valueOf(KeyEvent.VK_O)));
        actions.add(new SobelFilterAction(Language.getWord("Sobel"), null, Language.getWord("Sobel_desc"), Integer.valueOf(KeyEvent.VK_S)));
    }

    /**
     * <p>
     * Create a menu containing the list of Filter actions.
     * </p>
     * 
     * @return The filter menu UI element.
     */
    public JMenu createMenu() {
        JMenu fileMenu = new JMenu(Language.getWord("Filter"));

        for(Action action: actions) {
            fileMenu.add(new JMenuItem(action)).setAccelerator(KeyStroke.getKeyStroke((Integer) action.getValue("MnemonicKey"), InputEvent.CTRL_DOWN_MASK));
        }

        return fileMenu;
    }

    /**
     * <p>
     * Action to blur an image with a mean filter.
     * </p>
     * 
     * @see MeanFilter
     */
    public class MeanFilterAction extends UserInput {

        /**
         * <p>
         * Create a new mean-filter action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        MeanFilterAction(String name, ImageIcon icon, String desc, Integer mnemonic,
                            boolean slider, int min, int max, int val, int zeroVal) {
            super(name, icon, desc, mnemonic, slider, min, max, val, zeroVal);
        }

        @Override
        Object mutateImage(int input) {
            if(target.getSelection().isEmpty()) return new MeanFilter(input);
            else {
                Point[] corners = target.getSelection().getCorners();
                return new MeanFilter(input, corners[0], corners[1]);
            }
        }

        

    }

    /**
     * <p>
     * Action to sharpen an image with a sharpen filter.
     * </p>
     * 
     * @see SharpenFilter
     */
    public class SharpenFilterAction extends ImageAction {
        
        /**
         * <p>
         * Create a new sharpen-filter action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
        SharpenFilterAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }
        
        /**
         * <p>
         * Callback for when the sharpen-filter action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the SharpenFilterAction is triggered.
         * Applys a {@link SharpenFilter} to the whole image.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            if(target.getSelection().isEmpty()) target.getImage().apply(new SharpenFilter());
            else{
                Point[] corners = target.getSelection().getCorners();
                target.getImage().apply(new SharpenFilter(corners[0], corners[1]));
            }
            target.repaint();
            target.getParent().revalidate();
        }
    }

    /**
     * <p>
     * Action to blur an image with a gaussian filter.
     * </p>
     * 
     * @see GaussianBlurFilter
     */
    public class GaussianBlurFilterAction extends UserInput {

        /**
         * <p>
         * Create a new gaussian-filter action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        GaussianBlurFilterAction(String name, ImageIcon icon, String desc, Integer mnemonic,
                                    boolean slider, int min, int max, int val, int zeroVal) {
            super(name, icon, desc, mnemonic, slider, min, max, val, zeroVal);
        }

        @Override
        Object mutateImage(int input) {
            if(target.getSelection().isEmpty()) return new GaussianBlurFilter(input);
            else {
                Point[] corners = target.getSelection().getCorners();
                return new GaussianBlurFilter(input, corners[0], corners[1]);
            } 
        }
    }

    /**
     * <p>
     * Action to blur an image with a median filter
     * <p>
     * 
     * @see MedianFilter
     */
    public class MedianFilterAction extends ImageAction {
        
        /**
         * <p>
         * Create a new median-filter action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        MedianFilterAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name , icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the median filter action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the MedianFilterAction is triggered.
         * It prompts the user for a filter radius, then applies an appropriately sized {@link MedianFilter}.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {

            if (target.getImage().hasImage() == false) {
                UserMessage.showWarning(UserMessage.NULL_FILE_WARN);
                return;
            }

            // Determine the radius - ask the user.
            int radius = 1;

            // Pop-up dialog box to ask for the radius value.
            SpinnerNumberModel radiusModel = new SpinnerNumberModel(1, 1, 10, 1);
            JSpinner radiusSpinner = new JSpinner(radiusModel);
            int option = JOptionPane.showOptionDialog(Andie.getFrame(), radiusSpinner, Language.getWord("EnterValue"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, Andie.getIcon(), null, null);

            // Check the return value from the dialog box.
            if (option != JOptionPane.OK_OPTION) {
                Andie.getImagePanel().getSelection().reset();
                return;
            } else {
                radius = radiusModel.getNumber().intValue();
            }

            //Create and apply the filter
            target.getImage().apply(new MedianFilter(radius));
            target.repaint();
            target.getParent().revalidate();
        }
    
    }

    /**
     * <p>
     * Action to emboss an image.
     * </p>
     * 
     * @see EmbossFilter
     */
    public class EmbossFilterAction extends ImageAction{
        private int choice;

        /**
         * <p>
         * Creates a new emboss filter action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        EmbossFilterAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the emboss filter action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the EmbossFilterAction is triggered. 
         * It prompts the user for a direction to emboss, then applies an {@link EmbossFilter} with the appropriate direction. 
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e){
            if (target.getImage().hasImage() == false) {
                UserMessage.showWarning(UserMessage.NULL_FILE_WARN);
                return;
            }  

            // Creating the menu
            String[] embossOptions = {
                Language.getWord("None"), 
                Language.getWord("Emboss_E"), 
                Language.getWord("Emboss_NE"), 
                Language.getWord("Emboss_N"), 
                Language.getWord("Emboss_NW"),
                Language.getWord("Emboss_W"),
                Language.getWord("Emboss_SW"),
                Language.getWord("Emboss_S"),
                Language.getWord("Emboss_SE")
            };

            JComboBox cbEmboss = new JComboBox(embossOptions);
            JLabel labelEmboss = new JLabel(Language.getWord("Emboss_type"));

            JPanel embossPanel = new JPanel(new GridLayout(2, 1));
            embossPanel.add(labelEmboss);
            embossPanel.add(cbEmboss);

            /*
             * Listener for the emboss combo box. 
             * The direction of emboss depends on combo box selection.
             */
            class EmbossComboBoxListener implements ActionListener{
                public void actionPerformed(ActionEvent e){
                    JComboBox cb = (JComboBox)e.getSource();
                    String embossChoice = (String) cb.getSelectedItem();

                    try{
                        if(embossChoice.equals(embossOptions[0])) choice = EmbossFilter.NONE;
                        else if(embossChoice.equals(embossOptions[1])) choice = EmbossFilter.EAST;
                        else if(embossChoice.equals(embossOptions[2])) choice = EmbossFilter.NORTH_EAST;
                        else if(embossChoice.equals(embossOptions[3])) choice = EmbossFilter.NORTH;
                        else if(embossChoice.equals(embossOptions[4])) choice = EmbossFilter.NORTH_WEST;
                        else if(embossChoice.equals(embossOptions[5])) choice = EmbossFilter.WEST;
                        else if(embossChoice.equals(embossOptions[6])) choice = EmbossFilter.SOUTH_WEST;
                        else if(embossChoice.equals(embossOptions[7])) choice = EmbossFilter.SOUTH;
                        else if(embossChoice.equals(embossOptions[8])) choice = EmbossFilter.SOUTH_EAST;

                        if(target.getSelection().isEmpty()) target.getImage().previewApply(new EmbossFilter(choice));
                        else{
                            Point[] corners = target.getSelection().getCorners();
                            target.getImage().previewApply(new EmbossFilter(choice, corners[0], corners[1]));
                        }
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }

                    target.repaint();
                    target.getParent().revalidate();
                }
            }

            cbEmboss.addActionListener(new EmbossComboBoxListener());

            int option = JOptionPane.showOptionDialog(FilterActions.parent, embossPanel, Language.getWord("Emboss"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, FilterActions.icon, null, null);

            // Check return value from the dialog box
            if(option != JOptionPane.OK_OPTION){
                target.getImage().previewApply(new EmbossFilter(EmbossFilter.NONE));
                Andie.getImagePanel().getSelection().reset();

            }else{
                // Determining which direction to emboss
                String embossChoice = (String) cbEmboss.getSelectedItem();

                if(embossChoice.equals(embossOptions[0])) return;
                else{
                    if(target.getSelection().isEmpty()) target.getImage().apply(new EmbossFilter(choice));
                    else{
                        Point[] corners = target.getSelection().getCorners();
                        target.getImage().apply(new EmbossFilter(choice, corners[0], corners[1]));
                    }
                }
            }
            
            target.repaint();
            target.getParent().revalidate();
        }
    }

    /**
     * <p>
     * Action to apply a Sobel filter to an image.
     * </p>
     * 
     * @see SobelFilter
     */
    public class SobelFilterAction extends ImageAction {
        int choice;
        
         /**
         * <p>
         * Creates a new Sobel filter action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        public SobelFilterAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

         /**
         * <p>
         * Callback for when the Sobel filter action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the SobelFilterAction is triggered. 
         * It prompts the user for a direction, then applies a {@link SobelFilter} with the appropriate direction. 
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            if (target.getImage().hasImage() == false) {
                UserMessage.showWarning(UserMessage.NULL_FILE_WARN);
                return;
            }  

            // Creating the menu
            String[] sobelOptions = {
                Language.getWord("None"), 
                Language.getWord("Sobel_H"), 
                Language.getWord("Sobel_V"), 
            };

            JComboBox cbSobel = new JComboBox(sobelOptions);
            JLabel labelSobel = new JLabel(Language.getWord("Sobel_type"));

            JPanel sobelPanel = new JPanel(new GridLayout(2, 1));
            sobelPanel.add(labelSobel);
            sobelPanel.add(cbSobel);

            /*
             * Listener for the sobel combo box. 
             * The direction of sobel depends on combo box selection.
             */
            class SobelComboBoxListener implements ActionListener{
                public void actionPerformed(ActionEvent e){
                    JComboBox cb = (JComboBox)e.getSource();
                    String sobelChoice = (String) cb.getSelectedItem();

                    try{
                        if(sobelChoice.equals(sobelOptions[0])) choice = SobelFilter.NONE;
                        else if(sobelChoice.equals(sobelOptions[1])) choice = SobelFilter.HORIZONTAL;
                        else if(sobelChoice.equals(sobelOptions[2])) choice = SobelFilter.VERTICAL;

                        if(target.getSelection().isEmpty()) target.getImage().previewApply(new SobelFilter(choice));
                        else{
                            Point[] corners = target.getSelection().getCorners();
                            target.getImage().previewApply(new SobelFilter(choice, corners[0], corners[1]));
                        }
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }

                    target.repaint();
                    target.getParent().revalidate();
                }
            }

            cbSobel.addActionListener(new SobelComboBoxListener());

            int option = JOptionPane.showOptionDialog(FilterActions.parent, sobelPanel, Language.getWord("Sobel"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, FilterActions.icon, null, null);

            // Check return value from the dialog box
            if(option != JOptionPane.OK_OPTION){
                target.getImage().previewApply(new SobelFilter(SobelFilter.NONE));
                Andie.getImagePanel().getSelection().reset();
            }else{
                // Determining which direction to emboss
                String sobelChoice = (String) cbSobel.getSelectedItem();

                if(sobelChoice.equals(sobelOptions[0])) return;
                else{
                    if(target.getSelection().isEmpty()) target.getImage().apply(new SobelFilter(choice));
                    else{
                        Point[] corners = target.getSelection().getCorners();
                        target.getImage().apply(new SobelFilter(choice, corners[0], corners[1]));
                    }
                }  
            }
            
            target.repaint();
            target.getParent().revalidate();
        }
    }
}
