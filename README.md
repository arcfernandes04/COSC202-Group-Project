## Big Fish ANDIE information


### Brief user guide


ANDIE is a non-destructive image editor which can be used to easily edit and manipulate images. The sequence of operations applied to the image is stored in an operations file, meaning that they can be undone or redone. None of the original image information is lost in this process. 


To open ANDIE in an IDE, open the project from the project's directory `ANDIE`, and run `Andie.java`.


To change the language of the program, click the **Language > *Desired language*** menu options. 


To export the image with the applied operations, click **File > Export** menu options. 


### Who did what


**Josh:**
* Exception handling; `UserMessage.java`, refactored a lot of `EditableImage.java`, try/catch statements in most files
* Error avoidance; confirming before exiting with unsaved changes, confirming before overwriting files, etc. Wrote `AndieFileChooser`, inner class of `FileActions.java`, to help with error avoidance.
* Altered multilingual support so that it refreshes without needing to restart the program.


**Anthony:**
* Gaussian blur filter
* Sharpen filter
* Median filter 


**Corban:**
* Image resize
* Image rotations
* Image flip
* Previewing the changes being made with image operations before applying them
* Adding JSlider to several of the image operations


**Chris:** 
* Image export
* Multilingual support


**Abby:**
* Brightness adjustment
* Contrast adjustment
* File chooser opening to the most recent directory
* New logo


### How code was tested
* By inspecting the code to ensure that it is logically correct and will do what we expect
* By manually testing the features as they are added
* By peer-reviewing code - all merges to the main branch need to be approved