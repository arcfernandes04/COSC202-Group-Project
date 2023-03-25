package cosc202.andie;

import java.util.*;
import java.io.*;
import java.awt.image.*;
import javax.imageio.*;
import cosc202.andie.exceptions.*;

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
     * @throws NonImageFileException This indicates that the program has tried to copy the data in a non-image file as though it is an image.
     * @throws InvalidImageFormatException This exception is thrown when the buffered image is in an invalid format. This may be due to
     * incorrect processing during image operations (filters, colour changes, etc.)
     * @throws Exception If an unexpected error occurs.
     */
    private static BufferedImage deepCopy(BufferedImage bi) throws NonImageFileException, InvalidImageFormatException, Exception {
        try{
            ColorModel cm = bi.getColorModel();
            boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
            WritableRaster raster = bi.copyData(null);
            return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
        }catch(NullPointerException ex){ // If there is a NullPointerException, then "bi" is null and this is not an image file!
            throw new NonImageFileException(ex);
        }catch(java.awt.image.RasterFormatException ex){ // The image's data is in an incorrect format.
            throw new InvalidImageFormatException(ex);
        }
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
     * @throws FileNotFoundException If the file being opened does not exist, this exception is thrown.
     * @throws NonImageFileException Occurs if the program attempts to open a non-image file.
     * @throws Exception If an unexpected issue arises.
     */
    public void open(String filePath) throws FileNotFoundException, NonImageFileException, Exception {
        imageFilename = filePath;
        opsFilename = imageFilename + ".ops";
        try {
            File imageFile = new File(imageFilename);
            original = ImageIO.read(imageFile);
            current = deepCopy(original);

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
            ops = opsFromFile;
            redoOps.clear();
            objIn.close();
            fileIn.close();

            this.refresh();
        }catch (javax.imageio.IIOException ex) { //File doesn't exist
            throw new FileNotFoundException();
        }catch(FileNotFoundException ex){
            //This Exception is not useful. It just means that there is no associated operations file, but it can keep running.
        }catch(java.io.StreamCorruptedException ex) { // The operations file is incorrectly formatted
            throw new InvalidOperationsFileException(ex);
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
     * @throws Exception If something goes wrong.
     * @throws NullFileException Raised if there is no current file open.
     */
    public void save() throws NullFileException, Exception {
        if (this.opsFilename == null) {
            this.opsFilename = this.imageFilename + ".ops";
        }
        try{
            // Write image file based on file extension
            String extension = imageFilename.substring(1+imageFilename.lastIndexOf(".")).toLowerCase();
            ImageIO.write(original, extension, new File(imageFilename));
            // Write operations file
            FileOutputStream fileOut = new FileOutputStream(this.opsFilename);
            ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
            objOut.writeObject(this.ops);
            objOut.close();
            fileOut.close();
        }catch(IllegalArgumentException ex){ // There is no current image to save.
            throw new NullFileException();
        }catch(NullPointerException ex){ // Again, no file.
            throw new NullFileException();
        }
    }


    /**
     * <p>
     * Save an image to a speficied file.
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
     * @throws NullFileException Raised if there is no current file open.
     * @throws Exception If something goes wrong.
     */
    public void saveAs(String imageFilename) throws NullFileException, Exception {
        this.imageFilename = imageFilename;
        this.opsFilename = imageFilename + ".ops";
        save();
    }

    public void export(String filename) throws Exception {
        try{
            // Write image file based on file extension
            String extension = imageFilename.substring(1+imageFilename.lastIndexOf(".")).toLowerCase();
            this.imageFilename = filename + "." + extension;
            ImageIO.write(current, extension, new File(imageFilename));
        } catch (IllegalArgumentException ex) { // There is no current image to save.
            throw new NullFileException();
        } catch (NullPointerException ex) { // Again, no file.
            throw new NullFileException();
        }

    }

    /**
     * <p>
     * Apply an {@link ImageOperation} to this image.
     * </p>
     * 
     * @param op The operation to apply.
     * @throws NullFileException If no file is currently open, this {@code Exception} is raised.
     * @throws Exception Raised if an unexpected error occurs.
     */
    public void apply(ImageOperation op) throws NullFileException, Exception {
        current = op.apply(current);
        ops.add(op);
    }

    /**
     * <p>
     * Undo the last {@link ImageOperation} applied to the image.
     * </p>
     * @throws EmptyUndoStackException Occurs if the program tries to pop() from 
     * the stack of currently applied operations when it is empty.
     * @throws Exception If an unexpected issue arises.
     */
    public void undo() throws EmptyUndoStackException, Exception {
        try{
            redoOps.push(ops.pop());
            refresh();
        }catch(EmptyStackException ex){
            throw new EmptyUndoStackException(ex);
        }
    }

    /**
     * <p>
     * Reapply the most recently {@link undo}ne {@link ImageOperation} to the image.
     * </p>
     * 
     * @throws EmptyUndoStackException Occurs if the program tries to pop() from
     * the stack of undone operations when the stack is empty.
     * @throws Exception If an unexpected issue arises.
     */
    public void redo() throws EmptyRedoStackException, Exception {
        try{
            apply(redoOps.pop());
        }catch(EmptyStackException ex){
            throw new EmptyRedoStackException(ex);
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
     * @throws NonImageFileException This indicates that the program has tried to copy the data in a non-image file as though it is an image.
     * @throws If an unexpected exception occurs.
     */
    private void refresh() throws NonImageFileException, Exception {
        current = deepCopy(original);
        for (ImageOperation op: ops) {
            current = op.apply(current);
        }
    }

}
