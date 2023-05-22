package cosc202.andie;

import java.util.*;
import java.io.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.*;
import javax.imageio.*;

import cosc202.andie.draw.DrawBrush;
import cosc202.andie.draw.DrawPanel;


/**
 * <p>
 * An image with a set of operations applied to it.
 * </p>
 * 
 * <p>
 * The EditableImage represents an image with a series of operations applied to it.
 * It is fairly core to the ANDIE program, being the central data structure.
 * The operations are applied to a copy of the original image so that they can be undone.
 * THis is what is meant by "A Non-Destructive Image Editor" - you can always undo back to the original image.
 * </p>
 * 
 * <p>
 * Internally the EditableImage has two {@link BufferedImage}s - the original image 
 * and the result of applying the current set of operations to it. 
 * The operations themselves are stored on a {@link Stack}, with a second {@link Stack} 
 * being used to allow undone operations to be redone.
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Steven Mills
 * @version 1.0
 */
public class EditableImage {

    /** The original image. This should never be altered by ANDIE. */
    private BufferedImage original;
    /** The current image, the result of applying {@link ops} to {@link original}. */
    private BufferedImage current;
    /** Stored image from when previewApply method is called */
    private BufferedImage tempOriginal;
    /** The sequence of operations currently applied to the image. */
    private Stack<ImageOperation> ops;
    /** A memory of 'undone' operations to support 'redo'. */
    private Stack<ImageOperation> redoOps;
    /** The file where the original image is stored */
    private String imageFilename;
    /** The file where the operation sequence is stored. */
    private String opsFilename;
    /** Whether or not there are unsaved changes. */
    private boolean unsavedChanges;
    /** The file extension of the most recently opened image. */
    private String extension;
    /** The scale that the image has been resized by */
    private double resizedScale = 1.0;
    private int rotation;
    private double resizeTesting = 1.0;
    /** The allowed extensions for files that ANDIE can open. */
    private static final String[] allowedExtensions = ImageIO.getWriterFileSuffixes();
    /** The extension used for ANDIE's operation files */
    private static final String opsExtension = "ops";
    /** The sequence of operations to be recorded to a file.  */
    private Stack<ImageOperation> macroOps;
    /** Whether macro recording is active. */
    private boolean recording = false;

    /**
     * <p>
     * Create a new EditableImage.
     * </p>
     * 
     * <p>
     * A new EditableImage has no image (it is a null reference), and an empty stack of operations.
     * </p>
     */
    public EditableImage() {
        original = null;
        current = null;
        ops = new Stack<ImageOperation>();
        redoOps = new Stack<ImageOperation>();
        imageFilename = null;
        opsFilename = null;
    }

    /**
     * <p>
     *  Returns the current image dimensions.
     * </p>
     * 
     * @author Corban Surtees
     */
    public Dimension getDimensions() {
        return new Dimension(current.getWidth(), current.getHeight());
    }
    
    /**
     * <p>
     *  Returns the current image dimensions.
     * </p>
     * 
     * @author Corban Surtees
     */
    public Dimension getOriginalDimensions() {
        return new Dimension(original.getWidth(), original.getHeight());
    }

    public double getResizeScale() {
        return this.resizedScale;
    }

    public double getResizeScaleTesting() {
        return this.resizeTesting;
    }

    /**
     * <p>
     * Check if there is an image loaded.
     * </p>
     * 
     * @return True if there is an image, false otherwise.
     */
    public boolean hasImage() {
        return current != null;
    }

    /**
     * Check if there is an image open that does not relate to an already saved file.
     * {@code true} if there is an image with no file path.
     * {@code false} if there is an image with a pre-existing save file, or there is no image currently open.
     */
    public boolean hasLocalImage() {
        return (imageFilename == null) && (current != null);
    }

    /**
     * <p>
     * Check if there are currently any unsaved changes to the open file.
     * </p>
     * 
     * @return True if there are unsaved changes, false otherwise.
     */
    public boolean hasUnsavedChanges(){
        return unsavedChanges;
    }

    /**
     * Lists the extensions currently available for writing image files, as a copy of the private data field in order to prevent outside classes mutating it.
     * @return An array containing the extensions that the current image writers can use.
     */
    public static String[] getAllowedExtensions(){
        return Arrays.copyOf(allowedExtensions, allowedExtensions.length);
    }

    /**
     * Gets the extension that ANDIE uses for operation files.
     * @return
     */
    public static String getOpsExtension(){
        return opsExtension;
    }

    /**
     * Resets the temp original image after an operation, even if it was not applied,
     * because otherwise the program will continue to apply the next operation to 
     * tempOriginal, even if the file has been changed or several operations were pushed/popped.
     */
    private void resetTempOriginal(){
        tempOriginal = null;
    }

    /**
     * <p>
     * Make a 'deep' copy of a BufferedImage. 
     * </p>
     * 
     * <p>
     * Object instances in Java are accessed via references, which means that assignment does
     * not copy an object, it merely makes another reference to the original.
     * In order to make an independent copy, the {@code clone()} method is generally used.
     * {@link BufferedImage} does not implement {@link Cloneable} interface, and so the 
     * {@code clone()} method is not accessible.
     * </p>
     * 
     * <p>
     * This method makes a cloned copy of a BufferedImage.
     * This requires knowledge of some details about the internals of the BufferedImage,
     * but essentially comes down to making a new BufferedImage made up of copies of
     * the internal parts of the input.
     * </p>
     * 
     * <p>
     * This code is taken from StackOverflow:
     * <a href="https://stackoverflow.com/a/3514297">https://stackoverflow.com/a/3514297</a>
     * in response to 
     * <a href="https://stackoverflow.com/questions/3514158/how-do-you-clone-a-bufferedimage">https://stackoverflow.com/questions/3514158/how-do-you-clone-a-bufferedimage</a>.
     * Code by Klark used under the CC BY-SA 2.5 license.
     * </p>
     * 
     * <p>
     * This method (only) is released under <a href="https://creativecommons.org/licenses/by-sa/2.5/">CC BY-SA 2.5</a>
     * </p>
     * 
     * @param bi The BufferedImage to copy.
     * @return A deep copy of the input.
     */
    private static BufferedImage deepCopy(BufferedImage bi) {
        BufferedImage result = null;

        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        result = new BufferedImage(cm, raster, isAlphaPremultiplied, null);
        
        return result;
    }

    /**
     * <p>
     * Change the data fields of this instance of {@code EditableImage}.
     * Before calling this method, the supplied values need to be checked,
     * i.e. {@code img} should not be {@code null}, {@code filename} should exist,
     * and {@code extension} should be a valid image format.
     * </p>
     * 
     * <p>
     * This method also sets {@code unsavedChanges} to false, and calls
     * {@code resetTempOriginal()} to ensure any previously opened images are forgotten.
     * A new instance of {@code StackImage<Operation>} is created for the list of redo operations,
     * and {@code deepCopy} is applied to {@code img} to make sure {@code current} and {@code original}
     * point to two different objects.
     * </p>
     * 
     * 
     * @param img The image to be copied onto datafields {@code current} and {@code original}
     * @param filename The name of the file being opened
     * @param extension The file time of the current image
     * @param ops The list of previously applied operations
     */
    private void setDatafields(BufferedImage img, String filename, String extension, Stack<ImageOperation> ops) {
        this.current = img;
        this.original = deepCopy(img);
        this.imageFilename = filename;
        if(filename != null) this.opsFilename = filename + "." + opsExtension;
        else this.opsFilename = null;
        this.extension = extension;
        this.ops = ops;
        this.redoOps = new Stack<ImageOperation>();
        this.refresh(); //Redraw
        resetTempOriginal(); //Need to reset this, otherwise the new image will think it is still the old image
        unsavedChanges = false; //Tell the program that there are no unsaved changes
        //Check that the currently selected colours account for the transparency of the current image.
        DrawPanel.setTransparencyEnabled(this.current.getColorModel().hasAlpha());
    }
    
    /**
     * <p>
     * Open an image from a file.
     * </p>
     * 
     * <p>
     * Opens an image from the specified file.
     * Also tries to open a set of operations from the file with <code>.ops</code> added.
     * So if you open <code>some/path/to/image.png</code>, this method will also try to
     * read the operations from <code>some/path/to/image.png.ops</code>.
     * </p>
     * 
     * @param filePath The file to open the image from.
     */
    public void open(String filePath) {

        //Variables that we want to ensure are not null before making the datafields point to them
        String imageFilenameCheck = filePath;
        String opsFilenameCheck = filePath + "." + opsExtension;
        String extensionCheck = null;
        BufferedImage currentCheck = null;

        Stack<ImageOperation> opsFromFile = null;
        FileInputStream fileIn = null;
        ObjectInputStream objIn = null;

        try {
            //Attempt to open the file and get the extension
            File imageFile = new File(imageFilenameCheck);
            currentCheck = ImageIO.read(imageFile);
            extensionCheck = imageFilenameCheck.substring(1 + imageFilenameCheck.lastIndexOf(".")).toLowerCase();
            
            //Attempt to load in the operations
            fileIn = new FileInputStream(opsFilenameCheck);
            objIn = new ObjectInputStream(fileIn);
            // Silence the Java compiler warning about type casting.
            // Understanding the cause of the warning is way beyond
            // the scope of COSC202, but if you're interested, it has
            // to do with "type erasure" in Java: the compiler cannot
            // produce code that fails at this point in all cases in
            // which there is actually a type mismatch for one of the
            // elements within the Stack, i.e., a non-ImageOperation.
            @SuppressWarnings("unchecked")
            Stack<ImageOperation> opsTemp = (Stack<ImageOperation>) objIn.readObject();
            opsFromFile = opsTemp;
            objIn.close();
            fileIn.close();
        }catch(FileNotFoundException e){// This Exception means that there is no associated operations file - so need to reset it in case there was a previous file open.
            opsFromFile = new Stack<ImageOperation>();
        }catch (javax.imageio.IIOException ex) { //File doesn't exist - don't load any of the local variables to the datafields.
            UserMessage.showWarning(UserMessage.FILE_NOT_FOUND_WARN);
            return;
        }catch(java.io.StreamCorruptedException | InvalidClassException ex) { //The operations file is incorrectly formatted. Attempt to resolve by deleting/overwriting it, otherwise return.
            int result = UserMessage.showDialog(UserMessage.DELETE_OPS_DIALOG);
            if(result == UserMessage.YES_OPTION){
                try{
                    if(objIn != null) objIn.close();
                    if(fileIn != null) fileIn.close();
                    File corruptedOps = new File(opsFilenameCheck);
                    corruptedOps.delete();
                    opsFromFile = new Stack<ImageOperation>(); //Make sure that there is an operation stack to use!
                }catch(Exception e){/*Pretend like nothing happened (if it can't delete the file, it'll just overwrite it anyway)*/}
            }else{
                return;
            }
        }catch(Exception ex){ //Something else goes wrong
            UserMessage.showWarning(UserMessage.GENERIC_WARN);
        }

        //Only load the files in if there aren't any big issues.
        if (currentCheck != null) setDatafields(currentCheck, imageFilenameCheck, extensionCheck, opsFromFile);
    }

    /**
     * <p>
     * Save an image to file.
     * </p>
     * 
     * <p>
     * Saves an image to the file it was opened from, or the most recent file saved as.
     * Also saves a set of operations from the file with <code>.ops</code> added.
     * So if you save to <code>some/path/to/image.png</code>, this method will also save
     * the current operations to <code>some/path/to/image.png.ops</code>.
     * </p>
     * 
     * @return Whether the operation was successful.
     */
    public boolean save() {
        if (this.opsFilename == null) {
            this.opsFilename = this.imageFilename + "." + opsExtension;
        }
        try{
            // Write image file based on file extension
            ImageIO.write(original, extension, new File(imageFilename));
            // Write operations file
            FileOutputStream fileOut = new FileOutputStream(this.opsFilename);
            ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
            objOut.writeObject(this.ops);
            objOut.close();
            fileOut.close();

            //Make sure the program knows that there are no unsaved changes.
            unsavedChanges = false;
        }catch (IllegalArgumentException | NullPointerException ex){ //There is no file currently open
            UserMessage.showWarning(UserMessage.NULL_FILE_WARN);
            return false;
        } catch (Exception ex) {
            UserMessage.showWarning(UserMessage.INVALID_PATH_WARN);
            this.opsFilename = null;
            this.imageFilename = null;
            return false;
        }
        return true;
    }


    /**
     * <p>
     * Save an image to a specified file.
     * </p>
     * 
     * <p>
     * Saves an image to the file provided as a parameter.
     * Also saves a set of operations from the file with <code>.ops</code> added.
     * So if you save to <code>some/path/to/image.png</code>, this method will also
     * save
     * the current operations to <code>some/path/to/image.png.ops</code>.
     * </p>
     * 
     * <p>
     * The {@code AndieFileChooser} method should have already checked the imageFilename to ensure that
     * it is sensible. This means that the filename passed to this method is not checked.
     * </p>
     * 
     * @param imageFilename The file location to save the image to.
     * @return Whether the operation was successful.
     */
    public boolean saveAs(String imageFilename) {
        this.extension = imageFilename.substring(imageFilename.lastIndexOf(".") + 1).toLowerCase();
        this.imageFilename = imageFilename;
        this.opsFilename = this.imageFilename + "." + opsExtension;
        return save();
    }

    /**
     * <p>
     * Export an image with the current operations to a specified file.
     * </p>
     * 
     * <p>
     * Exports an image to the file provided as a parameter.
     * The current operations applied to the image are saved onto the
     * new file, and no operations file is generated.
     * </p>
     * 
     * @param imageFilename The file location to export the image to.
     */
    public void export(String imageFilename) {
        try{
            String extensionCheck = imageFilename.substring(imageFilename.lastIndexOf(".") + 1).toLowerCase();
            BufferedImage img = current;

            if (extensionCheck.equals("jpg") || extensionCheck.equals("jpeg")) {
                img = new BufferedImage(current.getWidth(), current.getHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics2D g = img.createGraphics();
                g.drawImage(current, 0, 0, Color.WHITE, null);
                g.dispose();
            }
            ImageIO.write(img, extensionCheck, new File(imageFilename));
        } catch (IllegalArgumentException | NullPointerException ex) {
            UserMessage.showWarning(UserMessage.NULL_FILE_WARN);
        } catch (Exception ex){
            UserMessage.showWarning(UserMessage.GENERIC_WARN);
        }

    }

    /**
     * Ensures that the extension on the file name is appropriate.
     * 
     * @param imageFilename
     * @return The inputted imageFilename with the appropriate extension. If imageFilename did not
     * have any extension, or it had an illegal extension (one that ANDIE cannot process), then an
     * appropriate extension is added.
     */
    public String makeSensible(String imageFilename) {
        // If the filename has an allowed extension, return
        for(String ext : getAllowedExtensions()){
            if(imageFilename.toLowerCase().endsWith("." + ext.toLowerCase())) return imageFilename;
        }
        
        // If it ends ith a random full stop, add the extension
        if(imageFilename.endsWith(".")) return imageFilename + this.extension;
        
        // Otherwise, add both the . and the extension
        else return imageFilename + "." + this.extension;
  }

    /**
     * <p>
     * Ensures that the extension on the operation file is appropriate.
     * </p>
     * 
     * @param opsFileName
     * @return A version of the opsFileName that has the correct extension.
     */
    public static String makeOpsSensible(String opsFileName){
        // If the filename has an allowed extension, return
        if(opsFileName.toLowerCase().endsWith("." + opsExtension.toLowerCase())) return opsFileName;
        
        // If it ends ith a random full stop, add the extension
        if(opsFileName.endsWith(".")) return opsFileName + opsExtension;

        // Otherwise, add both the . and the extension
        else return opsFileName + "." + opsExtension;

    }

    /**
     * <p>
     * Apply an {@link ImageOperation} to this image.
     * </p>
     * 
     * @param op The operation to apply.
     */
    public void apply(ImageOperation op) {
        try{
            if(op == null) return;
            refresh();
            if (this.tempOriginal == null){
                this.tempOriginal = deepCopy(current);
            }

            BufferedImage result = op.apply(tempOriginal);
            resetTempOriginal(); //Need to reset this, otherwise the new image will think it is still the old image
            if(result != null){ //Only count this as a valid operation if it returns non-null value.
                current = result;
                ops.add(op);
                if(isRecording()) macroOps.add(op);
                unsavedChanges = true;
            }
            refresh();
            Andie.getImagePanel().getSelection().reset(); // Not in refresh because may we want to keep the selection through previewApply
        }catch(NullPointerException ex){ // If there is a NullPointerException, then we have a null image
            UserMessage.showWarning(UserMessage.NULL_FILE_WARN);
        }catch(java.awt.image.RasterFormatException ex){ // The image's data is in an incorrect format.
            UserMessage.showWarning(UserMessage.INVALID_IMG_FILE_WARN);
        } catch (Exception ex) { // Just in case!
            UserMessage.showWarning(UserMessage.GENERIC_WARN);
        }
    }

    /**
     * <p>
     * Show what image will look like when this.apply(ImageOperation op) is executed.
     * Does not save operation to ops.
     * </p>
     * 
     * @author Corban Surtees
     * @param op The operation to apply.
     */
    public void previewApply(ImageOperation op) {
        try {
            if(op == null) return;
            if (this.tempOriginal == null){
                this.tempOriginal = deepCopy(current);
            }
            BufferedImage result = op.apply(deepCopy(tempOriginal));
            if(result != null){ //Only count this as a valid operation if it returns non-null value.
                current = result;
            }
            
            // If op is an instance of a filter using convolution, refresh filters differently
            if (op instanceof EmbossFilter || op instanceof GaussianBlurFilter || op instanceof SobelFilter || op instanceof MeanFilter || op instanceof DrawBrush) {
                refresh(0, op);
            }
            


        }catch(Exception ex){ //Do not want to show the warning here, otherwise the user could be spammed.
            //UserMessage.showWarning(UserMessage.GENERIC_WARN);
        }
    }

    /**
     * <p>
     * Apply operations from an existing operation file to the current image.
     * </p>
     * 
     * @param opsFilePath The location of the ops file to apply
     */
    public void applyOpsFile(String opsFilePath) {
        
        String opsFilenameCheck = opsFilePath;

        Stack<ImageOperation> opsFromFile = null;
        FileInputStream fileIn = null;
        ObjectInputStream objIn = null;

        try{
            //Attempt to load in the operations
            fileIn = new FileInputStream(opsFilenameCheck);
            objIn = new ObjectInputStream(fileIn);

            @SuppressWarnings("unchecked")
            Stack<ImageOperation> opsTemp = (Stack<ImageOperation>) objIn.readObject();
            opsFromFile = opsTemp;
            objIn.close();
            fileIn.close();

            //Only load the files in if there aren't any big issues.
            if (!opsFromFile.isEmpty()) {
                for (ImageOperation op : opsFromFile) {
                    apply(op);
                }
            }
            refresh();
        }catch(java.io.StreamCorruptedException ex) { // Something is wrong with the ops file, it can't be read
            UserMessage.showWarning(UserMessage.UNREADABLE_OPS_FILE_WARN);
        }catch(InvalidClassException ex){
            UserMessage.showWarning(UserMessage.OUTDATED_OPS_FILE_WARN);
        }catch(Exception ex){
            UserMessage.showWarning(UserMessage.GENERIC_WARN);
        }
    }
    
    /**
     * Save the current {@code macroOps} to a file.
     * 
     * @param macroOpsFileName The file location to save the ops file to.
     * @return Whether the operation was successful
     */
    public boolean saveToOpsFile(String macroOpsFileName){
        try{
            FileOutputStream fileOut = new FileOutputStream(macroOpsFileName);
            ObjectOutputStream objOut = new ObjectOutputStream(fileOut);

            objOut.writeObject(this.macroOps);
            objOut.close();
            fileOut.close();
        }catch (NullPointerException ex){
            UserMessage.showWarning(UserMessage.NULL_FILE_WARN);
            return false;
        } catch (Exception ex) {
            UserMessage.showWarning(UserMessage.INVALID_PATH_WARN);
            this.opsFilename = null;
            this.imageFilename = null;
            return false;
        }
        return true;
    }

    /**
     * <p>
     * Undo the last {@link ImageOperation} applied to the image.
     * </p>
     */
    public void undo(){
        if (!hasImage()) {
            UserMessage.showWarning(UserMessage.NULL_FILE_WARN);
            return;
        }
        try{
            resetTempOriginal(); // make sure we aren't using an old version of the image
            redoOps.push(ops.pop());
            if(isRecording()) macroOps.pop();
            refresh();
            unsavedChanges = true;
        }catch(EmptyStackException ex){
            UserMessage.showWarning(UserMessage.EMPTY_UNDO_STACK_WARN);
        }
    }

    /**
     * <p>
     *  Undo all operations applied to the image.
     * </p>
     */
    public void undoAll(){
        if (!hasImage()) {
            UserMessage.showWarning(UserMessage.NULL_FILE_WARN);
            return;
        }
        if(ops.size() == 0) UserMessage.showWarning(UserMessage.EMPTY_UNDO_STACK_WARN);

        while(ops.size() > 0){ //Shouldn't just call undo() repeatedly here - it is very inefficient
            redoOps.push(ops.pop());
            if(isRecording()) macroOps.pop();
        }
        resetTempOriginal(); // make sure we aren't using an old version of the image
        refresh();
        unsavedChanges = true;
    }

    /**
     * <p>
     * Reapply the most recently {@link undo}ne {@link ImageOperation} to the image.
     * </p>
     */
    public void redo() {
        if (!hasImage()) {
            UserMessage.showWarning(UserMessage.NULL_FILE_WARN);
            return;
        }
        try{
            resetTempOriginal(); //make sure we aren't using an old version of the image
            apply(redoOps.pop());

            unsavedChanges = true;
        }catch(EmptyStackException ex){
            UserMessage.showWarning(UserMessage.EMPTY_REDO_STACK_WARN);
        }
    }

    /**
     * <p>
     *  Reapply all operations applied to the image
     * </p>
     */
    public void redoAll(){
        if (!hasImage()) {
            UserMessage.showWarning(UserMessage.NULL_FILE_WARN);
            return;
        }
        if(redoOps.size() == 0) UserMessage.showWarning(UserMessage.EMPTY_REDO_STACK_WARN);
        while(redoOps.size() > 0){
            redo();
        }
    }

    /**
     * <p>
     * Take the current clipboard, check its data type, and
     * apply to the current {@code EditableImage} if it matches.
     * (Image) files copied to the clipboard can also be pasted.
     * </p>
     * 
     * <p>
     * If a list of files is pasted at the same time, the image files
     * will be turned into a list and presented to the user to ask them
     * which one they would like to copy into the program. Non-image files
     * are ignored.
     * </p>
     */
    public void pasteFromClipboard(){
        try{
            Toolkit toolkit = Toolkit.getDefaultToolkit(); 
            Clipboard clipboard = toolkit.getSystemClipboard();

            //Not a list of files or an image
            if (!clipboard.isDataFlavorAvailable(DataFlavor.imageFlavor) && !clipboard.isDataFlavorAvailable(DataFlavor.javaFileListFlavor)) {
                UserMessage.showWarning(UserMessage.NON_IMG_FILE_WARN);
                return;
            }
            
            Transferable transferable = clipboard.getContents(null);
            BufferedImage img = null;
            String extensionCheck = null;

            //If it is simply an image in the clipboard (not a whole file), then copy it.
            if (clipboard.isDataFlavorAvailable(DataFlavor.imageFlavor)) img = (BufferedImage) transferable.getTransferData(DataFlavor.imageFlavor);

            //May not be an image file. There may be multiple files - should need to iterate through them to check.
            if (clipboard.isDataFlavorAvailable(DataFlavor.javaFileListFlavor)){
                @SuppressWarnings("unchecked") //There shouldn't be any issues here since this code can only execute if the clipboard is holding a file list
                List<File> files = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
                //Make a list of which provided files are actually images.
                ArrayList<File> imageFiles = new ArrayList<File>();
                for(File f : files) if (ImageIO.read(f) != null) imageFiles.add(f);
                //Don't continue if there aren't any image files on the clipboard
                if(imageFiles.size() == 0){
                    UserMessage.showWarning(UserMessage.NON_IMG_FILE_WARN);
                    return;
                }
                
                File toOpen;
                
                if(imageFiles.size() > 1){ //There are multiple images on the clipboard
                    //Make an array of strings
                    String[] scrollItems = new String[imageFiles.size()];
                    for(int f = 0; f < imageFiles.size(); f++) scrollItems[f] = imageFiles.get(f).toString();
                    
                    int result = UserMessage.showScroll(UserMessage.PASTE_FILES_SCROLL, scrollItems);
                    if(result == UserMessage.CANCEL_OPTION) return;
                    toOpen = imageFiles.get(result);
                }else{ //There is only one image on the clipboard
                    toOpen = imageFiles.get(0);
                }

                String filenameCheck = toOpen.getAbsolutePath();
                img = ImageIO.read(toOpen);
                extensionCheck = filenameCheck.substring(filenameCheck.lastIndexOf(".") + 1).toLowerCase();
            }

            if (img == null) return; //Just in case something went wrong, need to return before overwriting the data fields.

            if(hasUnsavedChanges() == true){ //If there are unsaved changes, ask to save before forgetting the changes.
                int result = UserMessage.showDialog(UserMessage.SAVE_AND_OPEN_DIALOG);
                if(result == UserMessage.YES_OPTION){
                    boolean success = FileActions.saveAction(); //Need to make sure they don't cancel! Otherwise we open the new file without saving the old
                    if(success == false) return;
                }
                else if(result != UserMessage.NO_OPTION) return;
            }
            if(extensionCheck == null) extensionCheck = img.getColorModel().hasAlpha() ? "png" : "jpg"; // Default to PNG or JPG if there is no file extension known.

            setDatafields(img, null, extensionCheck, new Stack<ImageOperation>());
            unsavedChanges = true;
        
        }catch(Exception e){
            UserMessage.showWarning(UserMessage.FILE_NOT_FOUND_WARN);
        }
    }

    /**
     * <p>
     * Copy the value of {@code current} onto the clipboard.
     * Fails if there is no image loaded into ANDIE, otherwise the clipboard would be overwritten.
     * </p>
     */
    public void copyToClipboard() {
        if(!hasImage()) return; //make sure the current clipboard is not overwritten if there is nothing loaded in ANDIE.

        try{
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Clipboard clipboard = toolkit.getSystemClipboard();
            clipboard.setContents(new TransferableImage(this.current), null);
        }catch(Exception e){} //An exception occurring here isn't a problem; it's just debugging info.
    }

    /**
     * <p>
     * A private inner class that allows data to be transferred between
     * applications. In this case, it is being used to transfer data to the clipboard.
     * </p>
     * 
     * 
     * Code adapted from here:
     * <p>https://stackoverflow.com/questions/4552045/copy-bufferedimage-to-clipboard</p>
     */
    private record TransferableImage(Image i) implements Transferable {
        /* Returns the image being stored within the class (i.e. the current image). */
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
            if (flavor.equals(DataFlavor.imageFlavor) && i != null) return i;
            throw new UnsupportedFlavorException(flavor); //If they aren't asking for an image, throw an exception.
        }
        /* Returns which data flavors are available for this instance of Transferable. */
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[]{DataFlavor.imageFlavor};
        }
        /* Returns whether the inputted data flavor is supported or not. */
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            if (flavor.equals(DataFlavor.imageFlavor)) return true;
            return false;
        }
    }

    /**
     * <p>
     * Get the current image after the operations have been applied.
     * </p>
     * 
     * @return The result of applying all of the current operations to the {@link original} image.
     */
    public BufferedImage getCurrentImage() {
        return current;
    }

    /**
     * <p>
     * Reapply the current list of operations to the original.
     * </p>
     * 
     * <p>
     * While the latest version of the image is stored in {@link current}, this
     * method makes a fresh copy of the original and applies the operations to it in sequence.
     * This is useful when undoing changes to the image, or in any other case where {@link current}
     * cannot be easily incrementally updated. 
     * </p>
     * 
     */
    public void refresh() {
        refresh(0, null);
    }

    
    private void refresh(int additionalRotation, ImageOperation convolveOp){
        try {
            this.rotation = 0;
            this.resizedScale = 1.0;
            this.resizeTesting = 1.0;
            double resizeByAtEnd = 1.0;
            BufferedImage result = deepCopy(original);
            int totalRotation = additionalRotation;
            for (ImageOperation op : ops) {
                // apply all operation that are not rotations or flips
                if (op instanceof ResizeImage) {
                    ResizeImage r = (ResizeImage) op;
                    resizeByAtEnd *= r.getResizeScale();
                    this.resizeTesting *= r.getResizeScale();
                } 
                else if (op instanceof FlipImage) {
                    result = op.apply(result);
                    this.rotation += ((90-this.rotation%360))*2;
                } else {
                    result = op.apply(result);
                    if (op instanceof RotateImage) {
                        RotateImage r = (RotateImage) op;
                        this.rotation += r.getRotation();
                        BufferedImage orig = deepCopy(original);
                        RotateImage rorig = new RotateImage(this.rotation);
                        orig = rorig.apply(orig);
                        int width = orig.getWidth();
                        int height = orig.getHeight();

                        width *= this.resizedScale;
                        height *= this.resizedScale;
            
                        int x1 = (result.getWidth() - width)/2;
                        int y1 = (result.getHeight() - height)/2;
            
                        int x2 = x1 + width;
                        int y2 = y1 + height;
            
                        CropImage crop = new CropImage(new Point(x1, y1),
                                                        new Point(x2, y2));
                        result = crop.apply(result);
                    }
                }
            }
            // convolve 
            if(convolveOp != null) result = convolveOp.apply(result);

            // apply total rotation
            RotateImage rotate = new RotateImage(totalRotation);
            result = rotate.apply(result);

            RotateImage manscape = new RotateImage(-this.rotation);
            result = manscape.apply(result);

            BufferedImage orig = deepCopy(original);
            int width = orig.getWidth();
            int height = orig.getHeight();

            int x1 = (result.getWidth() - width)/2;
            int y1 = (result.getHeight() - height)/2;

            int x2 = x1 + width;
            int y2 = y1 + height;

            CropImage crop = new CropImage(new Point(x1, y1),
                                            new Point(x2, y2));
            result = crop.apply(result);

            manscape = new RotateImage(this.rotation);
            result = manscape.apply(result);

            ResizeImage resize = new ResizeImage((int) (resizeByAtEnd*100));
            result = resize.apply(result);
    
            if (result != null) { //Only store the result on the 'current' data field if it returns successfully.
                current = result;
            }
            
        } catch (Exception ex) { //There could be no operations in the file, so using refresh would throw an error. Don't want to alert the user since this isn't a problem.
            return;
        }
    }

        /**
     * Get whether ANDIE is currently in the macro recording state.
     * 
     * @return True if ANDIE is in the macro recording state
     */
    public boolean isRecording(){
        return recording;
    }

    /**
     * Change the current recording state
     * 
     * @param recording Whether to set ANDIE in the recording state. 
     */
    public void setRecording(boolean recording){
        this.recording = recording;
        if(recording) macroOps = new Stack<ImageOperation>();
    }

}
