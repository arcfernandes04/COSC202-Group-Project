package cosc202.andie;

import java.awt.AWTEvent;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import cosc202.andie.settings.ThemeActions;

/**
 * <p>
 * Creates a non-modal dialog that contains all of the macro-related actions. 
 * </p>
 * 
 * <p>
 * Class is abstract to allow for a single line call to create the menu.
 * </p>
 * 
 * @author Abby Fernandes
 */
public abstract class MacroDialog extends JDialog{
    private static String[] opsFileNames;
    private static ArrayList<String> opsFilePaths;

    /** Whether there is currently a MacroDialog being displayed in the program */
    private static boolean isDisplayed;

    // Components   
    private static JDialog dialog; 
    private static ImageIcon btnIcon;
    private static ImageIcon dialogIcon;
    private static JLabel applyLabel, recordLabel;
    private static JButton openBtn, recordBtn, closeBtn, applyBtn;
    private static JList<String> macroList;
    private static JScrollPane macroScrollPane;

    /**
     * <p>
     * Whether the MacroDialog is currently being displayed. 
     * </p>
     * 
     * @return True if the dialog is currently being displayed.
     */
    public static boolean isDisplayed(){
        return isDisplayed;
    }

    /**
     * <p>
     * Set whether the MacroDialog is currently being displayed.
     * </p>
     * 
     * <p>
     * Should only really be called upon displaying and closing the dialog window.
     * </p>
     * 
     * @param displayed Whether the dialog is displayed.
     */
    private static void setDisplayed(boolean displayed){
        isDisplayed = displayed;
    }

    /**
     * <p>
     * Update the visual components of the dialog.
     * </p>
     */
    public static void update(){
        if(isDisplayed()){
            getBtnIcon();

            Image img = btnIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            openBtn.setIcon(new ImageIcon(img));
            
            SwingUtilities.updateComponentTreeUI(dialog);

            getDialogIcon();
            dialog.setIconImage(dialogIcon.getImage());

            dialog.repaint();
            dialog.revalidate();
        }
    }

    /**
     * <p>
     * Figure out the correct icon to use for the dialog window (based on the current theme).
     * </p>
     */
    private static void getDialogIcon(){
        dialogIcon = Andie.getIcon();
    }

    /**
     * <p>
     * Figure out the  correct icon to use for the openBtn (based on whether the current theme is light or dark).
     * </p>
     */
    private static void getBtnIcon(){
        String directory = "/resources/icons/";
        if(ThemeActions.isDark()) directory += "light/";
        else directory += "dark/";

        try{
            // Load in the icons
             btnIcon = new ImageIcon(MacroDialog.class.getResource(directory + "open_icon.png"));
        }catch(Exception e){
            //If an error occurs, maybe have text filled button instead
        }
    }

    /**
     * <p>
     * Generate the list of the ops files in the macro directory.
     * </p>
     */
    private static void getOpsList(){
        // Get all the files in the macro directory
        File[] allFiles = new File(Preferences.getPreference("macroDirectory")).listFiles();

        opsFilePaths = new ArrayList<String>();
        
        // Filter to only ops files
        for(int i = 0; i < allFiles.length; i++){
            try{
                String filePath = allFiles[i].getCanonicalPath();
                String extension = filePath.substring(filePath.lastIndexOf(".") + 1).toLowerCase();

                if(extension.equals(EditableImage.getOpsExtension())){
                    opsFilePaths.add(filePath);
                }
            }catch(Exception ex){}                    
        }

        opsFileNames = new String[opsFilePaths.size()];

        // Trimming down the paths to get user-friendly file names
        for(int i = 0; i < opsFileNames.length; i++){
            String fileName = opsFilePaths.get(i);

            if(fileName.contains("\\")) fileName = fileName.substring(fileName.lastIndexOf("\\") + 1, fileName.lastIndexOf("."));
            else fileName = fileName.substring(fileName.lastIndexOf("/") + 1, fileName.lastIndexOf("."));

            opsFileNames[i] = fileName;
        }
    }

    /**
     * <p>
     * The actions to occur when the openBtn is triggered.
     * </p>
     * 
     * <p>
     * A file chooser is displayed, allowing the user select an ops file to apply to the current image.
     * </p>
     * 
     * <p>
     * Note: if currently recording, the ops from this file are also recorded. 
     * </p>
     * 
     * @param e The triggering event.
     */
    private static void openBtnAction(ActionEvent e){
        if(ImageAction.getTarget().getImage().isRecording()){
            int result = UserMessage.showDialog(UserMessage.RECORDING_CONTINUE_DIALOG);

            if(result != UserMessage.YES_OPTION){
                return;
            }
        }

        AndieFileChooser fileChooser = new AndieFileChooser(true);
        int result = fileChooser.showOpenDialog(ImageAction.getTarget());

        if(result == JFileChooser.APPROVE_OPTION){
                // get path and apply the ops from the file
                String opsFilepath = fileChooser.getPath();
                ImageAction.getTarget().getImage().applyOpsFile(opsFilepath);
        }
    }

    /**
     * <p>
     * The actions to occur when the recordBtn is triggered.
     * </p>
     * 
     * <p>
     * If not currently recording, recording will begin, allowing the user to apply a series of operations to record. 
     * Otherwise, a file chooser is displayed, allowing the user to save the recorded operations to a file.
     * </p>
     * 
     * @param e The triggering event.
     */
    private static void recordBtnAction(ActionEvent e){
        if(ImageAction.getTarget().getImage().isRecording()){
            AndieFileChooser fileChooser = new AndieFileChooser(true);
            int result = fileChooser.showSaveDialog(ImageAction.getTarget());

            if(result == JFileChooser.APPROVE_OPTION){
                // get path and save the ops to this file
                String opsFilepath = fileChooser.getPath();

                if(fileChooser.isSuccessful() == false) return;

                ImageAction.getTarget().getImage().saveToOpsFile(opsFilepath);

                ImageAction.getTarget().getImage().setRecording(false);
                recordBtn.setText(Language.getWord("macro_record_start"));

                // Regenerate the list of ops
                getOpsList();
                macroList.setListData(opsFileNames);

            }

        }else{
            ImageAction.getTarget().getImage().setRecording(true);
            recordBtn.setText(Language.getWord("macro_record_stop"));
        }
    }

    /**
     * <p>
     * The actions to occur when the applyBtn is triggered.
     * </p>
     * 
     * <p>
     * The selected ops file (if any) is applied to the current image.
     * </p>
     * 
     * <p>
     * Note: if currently recording, the ops from this file are also recorded. 
     * </p>
     * 
     * @param e The triggering event.
     */
    private static void applyBtnAction(ActionEvent e){
        // If nothing is selected, do nothing

        // Otherwise, if something is selected then set result to the index selected
            if(!macroList.isSelectionEmpty()){
                if(ImageAction.getTarget().getImage().isRecording()){
                    int result = UserMessage.showDialog(UserMessage.RECORDING_CONTINUE_DIALOG);

                    if(result != UserMessage.YES_OPTION){
                        return;
                    }
                }

                String selected = macroList.getSelectedValue();

                for(int i = 0; i < opsFileNames.length; i++){
                    if(selected.equals(opsFileNames[i])) ImageAction.getTarget().getImage().applyOpsFile(opsFilePaths.get(i));
                }

            }

            macroList.clearSelection();
    }

    /**
     * <p>
     * The actions to occur when the user wishes to close the window.
     * </p>
     * 
     * <p>
     * The dialog is closed.
     * </p>
     * 
     * <p>
     * Note: if currently recording, the user is asked to confirm before continuing. 
     * </p>
     * 
     * @param e The triggering event.
     */
    private static void closeAction(AWTEvent e){
        if(ImageAction.getTarget().getImage().isRecording()){
            int result = UserMessage.showDialog(UserMessage.RECORDING_END_DIALOG);

            if(result != UserMessage.YES_OPTION){
                return;
            }
        }

        ImageAction.getTarget().getImage().setRecording(false);
        setDisplayed(false);
        dialog.dispose();
    }

    /**
     * <p>
     * Shows the macro dialog as a non-modal pop-up.
     * </p>
     * 
     * @param parent The parent component in which the dialog will be displayed.
     */
    public static void showDialog(Frame parent){
        setDisplayed(true);

        dialog = new JDialog(parent, Language.getWord("macro"), false);
        getBtnIcon();
        getOpsList();

        // Set the correct closing operation
        dialog.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e){
                closeAction(e);
            }
        });

        applyLabel = new JLabel(Language.getWord("macro_select_label"));

        // Set up the open button
        Image img = btnIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        openBtn = new JButton(new ImageIcon(img));

        openBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                openBtnAction(e);
            }
        });

        // Fill the list with the ops files (if any)
        macroList = new JList<>();
        if(opsFileNames.length != 0) macroList.setListData(opsFileNames);
        macroScrollPane = new JScrollPane(macroList);


        // Set up the record button
        recordLabel = new JLabel(Language.getWord("macro_record_label"));
        recordBtn = new JButton(Language.getWord("macro_record_start"));

        recordBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                recordBtnAction(e);
            }
        });

        // Set up the close button
        closeBtn = new JButton(Language.getWord("close"));

        closeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                closeAction(e);
            }
        });        

        // Set up the apply button
        applyBtn = new JButton(Language.getWord("macro_apply"));

        applyBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                applyBtnAction(e);
            }
        });        

        // The following layout code was automatically generated by the NetBeans GUI builder        
        GroupLayout layout = new GroupLayout(dialog.getContentPane());
        dialog.getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(macroScrollPane)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(applyLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(openBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(applyBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(closeBtn))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(recordLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(recordBtn)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(openBtn)
                    .addComponent(applyLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(macroScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(recordBtn)
                    .addComponent(recordLabel))
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(closeBtn)
                    .addComponent(applyBtn))
                .addContainerGap())
        );
        
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }
  
}
                

                
                


    



            






