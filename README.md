# Big Fish ANDIE information


## Brief user guide

ANDIE is a non-destructive image editor which can be used to easily edit and manipulate images. The sequence of operations applied to the image is stored in an operations file, meaning that they can be undone or redone. None of the original image information is lost in this process. 

[Click here](ANDIE.jar) to download the ANDIE.jar file.


### Running the program
To open ANDIE in an IDE, open the project from the project's directory `ANDIE`, and run `Andie.java`.

Or, to open ANDIE from the `ANDIE.jar` file run the following shell command within the directory the JAR file is saved in:

`java -jar ANDIE.jar`

The first time that you run the program, an `ANDIE` directory will be created in your computer's AppData/Home directory (depending on your operating system). This directory is where the configuration file and the saved macros can be found.


### Changing preferences
To change the language, click the **Settings > Language** menu options and select the desired language. 

To change the theme (the look and feel), click the **Settings > Themes** menu options and select the desired theme.

These preferences will be stored in a configuration file and remembered when you next open the program.


### Opening an image
Before you can apply any operations, you must open an image file. To do so, select the open icon from the toolbar, or the **File > Open** menu options. 

You can also paste an image from the clipboard into the program. 

The type of image that ANDIE can support will depend on the machine that you are using. 

Note: if you have any unsaved changes, a confirmation dialog will pop-up before allowing you to open a new image.

### Applying operations
To apply any operation to image select the operation that you wish to apply. If a value is required, a prompt window will pop-up allowing you to see the changes before committing to applying. 

To undo/redo operations, select the undo or redo icon from the toolbar, or the **File > *Undo/Redo*** menu options. 

### Saving and exporting
To save an image select the save icon from the toolbar, or the **File > Save** menu options. 

To save the image as a copy with a different name, or in a different file location, select the save as icon from the toolbar, or the **File > Save as** menu options.

Simply saving an image in ANDIE will not make any changes to the image itself, rather, it will update the sequence of operations stored in the operations file for that image. 

To export the image with the operations applied to it, select the **File > Export** menu options. 


## Who did what

**Josh:**
* Exception handling; `UserMessage`, refactored a lot of `EditableImage`, try/catch statements in most files
* Error avoidance; confirming before exiting with unsaved changes, confirming before overwriting files, etc. Wrote `AndieFileChooser` to help with error avoidance.
* Altered multilingual support so that it refreshes without needing to restart the program.
* Translated JFileChooser and JColorChooser so that they match the current language.
* Drawing functions; added the drawing tools and a brush tool, with different fill, size, and colour selection options.
* Added the ability to change the theme (look and feel); the ANDIE icon changes to match the colour of the theme.
* Storing preferences (language, theme, etc.) within the file system.
* Set up the continuous integration pipeline to automatically generate the JAR file and Javadocs, and to run the JUnit tests.
* Added copy / paste functionality; can paste several images at the same time and choose which file to use.
* Created the JUnit tests

**Anthony:**
* Gaussian blur filter
* Sharpen filter
* Median filter 
* Emboss filters
* Sobel filters
* Toolbar; the button icons change with light/dark themes so that they are visible.

**Corban:**
* Image resize
* Image rotations
* Custom Image rotations (can rotate to any angle)
* Image flip
* Previewing the changes being made with image operations before applying them
* Adding JSlider to several of the image operations
* Mouse selection
* Crop to selection
* Filtering only the selection; altered a lot of the code in the `EditableImage` refresh method and the image transformation operations so that the filters within the selection are applied correctly after transforming an image. 

**Chris:** 
* Image export
* Multilingual support
* Keyboard shortcuts

**Abby:**
* Brightness adjustment
* Contrast adjustment
* File chooser opening to the most recent directory
* New logo
* Reimplemented `ConvolveOp` as `AndieConvolveOp` which handles the edges of an image by taking the value of the nearest pixel. `AndieConvolveOp` can also handle negative results by offsetting pixel values with a mid-value. 
* Filtering only the selection; altered most of the colour and filter operations to optionally take the coordinates of a selection and only modify within that selection.
* Restructured the emboss and Sobel filters created by Anthony so that they are each contained within a single class and menu. 
* Macros; altered the `AndieFileChooser` class so that it can optionally filter to ops files rather than image files, added a button to the toolbar for the new macro dialog pop-up. 


## How code was tested
* By inspecting the code to ensure that it is logically correct and will do what we expect
* By manually testing the features as they are added
* By peer-reviewing code - all merges to the main branch need to be approved
* By adding JUnit tests and a CI pipeline so that we can make sure all the tests pass within the main branch
* By having people other than computer science students test the program. 