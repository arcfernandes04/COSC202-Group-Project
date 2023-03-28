package cosc202.andie;

import java.util.*;
import java.io.*;
import java.awt.image.*;
import javax.imageio.*;

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
class EditableImage {

    /** The original image. This should never be altered by ANDIE. */
    private BufferedImage original;
    /** The current image, the result of applying {@link ops} to {@link original}. */
    private BufferedImage current;
    /** The sequence of operations currently applied to the image. */
    private Stack<ImageOperation> ops;
    /** A memory of 'undone' operations to support 'redo'. */
    private Stack<ImageOperation> redoOps;
    /** The file where the original image is stored/ */
    private String imageFilename;
    /** The file where the operation sequence is stored. */
    private String opsFilename;
    /** Whether or not there are unsaved changes. */
    private boolean unsavedChanges;
    /** The file extension of the most recently opened image. */
    private String extension;

    /** The allowed extensions for files that ANDIE can open. */
    protected static String[] allowedExtensions = {"jpeg","jpg","png","gif","tiff","tif","rgb","ppm"};

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
     * Check if there is an image loaded.
     * </p>
     * 
     * @return True if there is an image, false otherwise.
     */
    public boolean hasImage() {
        return current != null;
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
     * This requires knoweldge of some details about the internals of the BufferedImage,
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
        try{
            ColorModel cm = bi.getColorModel();
            boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
            WritableRaster raster = bi.copyData(null);
            result = new BufferedImage(cm, raster, isAlphaPremultiplied, null);
        }catch(NullPointerException ex){ // If there is a NullPointerException, then "bi" is null and this is not an image file!
            UserMessage.showWarning(UserMessage.NON_IMG_FILE_WARN);
        }catch(java.awt.image.RasterFormatException ex){ // The image's data is in an incorrect format.
            UserMessage.showWarning(UserMessage.INVALID_IMG_FILE_WARN);
        } catch (Exception ex) { // Just in case!
            UserMessage.showWarning(UserMessage.GENERIC_WARN);
        }
        return result;
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
        imageFilename = filePath;
        opsFilename = imageFilename + ".ops";
        try {
            File imageFile = new File(imageFilename);
            BufferedImage originalCheck = ImageIO.read(imageFile); //Need to make sure these don't return null, otherwise we're gonna reset the current image :(
            BufferedImage currentCheck = deepCopy(originalCheck);
            String extensionCheck = imageFilename.substring(1 + imageFilename.lastIndexOf(".")).toLowerCase();

            if (originalCheck != null && currentCheck != null) {
                original = originalCheck;
                current = currentCheck;
                extension = extensionCheck;
            } else {
                //UserMessage.showWarning(UserMessage.NON_IMG_FILE_WARN); //Tell the user off and leave before more damage is done.
                return;
            }

            try{
                FileInputStream fileIn = new FileInputStream(this.opsFilename);
                ObjectInputStream objIn = new ObjectInputStream(fileIn);
                // Silence the Java compiler warning about type casting.
                // Understanding the cause of the warning is way beyond
                // the scope of COSC202, but if you're interested, it has
                // to do with "type erasure" in Java: the compiler cannot
                // produce code that fails at this point in all cases in
                // which there is actually a type mismatch for one of the
                // elements within the Stack, i.e., a non-ImageOperation.
                @SuppressWarnings("unchecked")
                Stack<ImageOperation> opsFromFile = (Stack<ImageOperation>) objIn.readObject();
                redoOps.clear();
                objIn.close();
                fileIn.close();
                if(opsFromFile != null) ops = opsFromFile; // If there is an ops file, then set it.
            }catch(FileNotFoundException e){// This Exception means that there is no associated operations file - so need to reset it in case there was a previous file open.
                ops = new Stack<ImageOperation>();
                redoOps = new Stack<ImageOperation>();
            }

            this.refresh();
            unsavedChanges = false;
        }catch (javax.imageio.IIOException ex) { //File doesn't exist
            UserMessage.showWarning(UserMessage.FILE_NOT_FOUND_WARN);
        }catch(java.io.StreamCorruptedException ex) { // The operations file is incorrectly formatted
            UserMessage.showWarning(UserMessage.INVALID_OPS_FILE_WARN);
        }catch(Exception ex){
            UserMessage.showWarning(UserMessage.GENERIC_WARN);
        }
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
     */
    public void save() {
        if (this.opsFilename == null) {
            this.opsFilename = this.imageFilename + ".ops";
        }
        try{
            // Write image file based on file extension
            //String extensionCheck = imageFilename.substring(1+imageFilename.lastIndexOf(".")).toLowerCase();
            //if(extensionCheck != null) extension = extensionCheck;
            ImageIO.write(original, extension, new File(imageFilename));
            // Write operations file
            FileOutputStream fileOut = new FileOutputStream(this.opsFilename);
            ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
            objOut.writeObject(this.ops);
            objOut.close();
            fileOut.close();

            //Make sure the program knows that there are no unsaved changes.
            unsavedChanges = false;
        }catch(IllegalArgumentException | NullPointerException ex){ //There is no file currently open
            UserMessage.showWarning(UserMessage.NULL_FILE_WARN);
        } catch (Exception ex) {
            UserMessage.showWarning(UserMessage.GENERIC_WARN);
        }
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
     * @param imageFilename The file location to save the image to.
     */
    public void saveAs(String imageFilename) {
        int lastDotIndex = imageFilename.lastIndexOf(".");
        if (lastDotIndex == -1) { // There isn't an extension, so use the existing one.
            this.imageFilename = imageFilename + "." + this.extension;
        } else { //Check what the extension actually is.
            String extensionCheck = imageFilename.substring(lastDotIndex + 1).toLowerCase();
            boolean allowed = false;
            for(String ext: allowedExtensions){ //If it's in the list of allowed extensions, then that's fine.
                if(ext.equalsIgnoreCase(extensionCheck)){
                    this.extension = extensionCheck;
                    this.imageFilename = imageFilename;
                    allowed = true;
                    break;
                }
            }
            if(!allowed){ //If not in the allowed list, then add the current extension.
                this.imageFilename = imageFilename.substring(0, lastDotIndex + 1).toLowerCase() + this.extension;
            }
        }
        this.opsFilename = this.imageFilename + ".ops";
        save();
    }

    public void export(String imageFilename) {
        try{
            // Write image file based on file extension
            String extensionCheck;
            int lastDotIndex = imageFilename.lastIndexOf(".");
            if(lastDotIndex == -1){ //There isn't an extension, so use the one from the current file.
                imageFilename = imageFilename + "." + this.extension;
                extensionCheck = this.extension;
            } else { //Check what the extension actually is.
                extensionCheck = imageFilename.substring(lastDotIndex + 1).toLowerCase();
                boolean allowed = false;
                for(String ext: allowedExtensions){ //If it's in the list of allowed extensions, then that's fine.
                    if(ext.equalsIgnoreCase(extensionCheck)){
                        allowed = true;
                        break;
                    }
                }
                if(!allowed){ //If not in the allowed list, then add the current extension.
                    imageFilename = imageFilename.substring(0, lastDotIndex + 1).toLowerCase() + this.extension;
                    extensionCheck = this.extension;
                }
            }
            ImageIO.write(current, extensionCheck, new File(imageFilename));
        } catch (IllegalArgumentException | NullPointerException ex) {
            UserMessage.showWarning(UserMessage.NULL_FILE_WARN);
        } catch (Exception ex){
            UserMessage.showWarning(UserMessage.GENERIC_WARN);
        }

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
            BufferedImage result = op.apply(current);
            if(result != null){ //Only count this as a valid operation if it returns non-null value.
                current = result;
                ops.add(op);
                unsavedChanges = true;
            }
        }catch(Exception ex){ //In case the filter forgets to catch exceptions inside the class
            UserMessage.showWarning(UserMessage.GENERIC_WARN);
        }
    }

    /**
     * <p>
     * Undo the last {@link ImageOperation} applied to the image.
     * </p>
     */
    public void undo(){
        try{
            redoOps.push(ops.pop());
            refresh();
            unsavedChanges = true;
        }catch(EmptyStackException ex){
            UserMessage.showWarning(UserMessage.EMPTY_UNDO_STACK_WARN);
        }
    }

    /**
     * <p>
     * Reapply the most recently {@link undo}ne {@link ImageOperation} to the image.
     * </p>
     */
    public void redo() {
        try{
            apply(redoOps.pop());
            unsavedChanges = true;
        }catch(EmptyStackException ex){
            UserMessage.showWarning(UserMessage.EMPTY_REDO_STACK_WARN);
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
    private void refresh() {
        current = deepCopy(original);
        try{
            for (ImageOperation op: ops) {
            current = op.apply(current);
            }
        }catch(Exception ex){
            UserMessage.showWarning(UserMessage.GENERIC_WARN);
        }
    }

}
