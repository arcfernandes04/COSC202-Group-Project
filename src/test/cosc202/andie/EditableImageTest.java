package test.cosc202.andie;

import cosc202.andie.EditableImage;

import org.junit.jupiter.api.*;

public class EditableImageTest {

    @Test
    void dummyTest(){}
    
    @Test
    void constructorTest(){
        EditableImage e = new EditableImage();
    }

    @Test 
    void hasImageTest(){
        EditableImage e = new EditableImage();
        Assertions.assertFalse(e.hasImage(), "The EditableImage should not have an image.");
    }

    @Test
    void getCurrentImageTest() {
        EditableImage e = new EditableImage();
        Assertions.assertNull(e.getCurrentImage(), "The EditableImage should not have an image.");
    }

    @Test
    void hasLocalImageTest() {
        EditableImage e = new EditableImage();
        Assertions.assertFalse(e.hasLocalImage(), "The EditableImage should not have a localised image.");
    }

}
