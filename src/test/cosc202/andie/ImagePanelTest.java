package test.cosc202.andie;

import cosc202.andie.ImagePanel;

import org.junit.jupiter.api.*;

public class ImagePanelTest {

    @Test
    void dummyTest(){}

    @Test
    void getImageTest(){
        ImagePanel p = new ImagePanel();
        Assertions.assertNotNull(p.getImage(), "There should be an instance of EditableImage.");
    }

    @Test
    void getSelectionTest() {
        ImagePanel p = new ImagePanel();
        Assertions.assertNotNull(p.getSelection(), "There should be an instance of Selection");
    }

    @Test
    void getZoomTest() {
        ImagePanel p = new ImagePanel();
        Assertions.assertEquals(100, p.getZoom(), 1e-10);
    }

    @Test
    void setZoomTest() {
        ImagePanel p = new ImagePanel();
        p.setZoom(25);
        Assertions.assertEquals(50, p.getZoom(), 1e-10);

        p.setZoom(500);
        Assertions.assertEquals(200, p.getZoom(), 1e-10);

        p.setZoom(107);
        Assertions.assertEquals(107, p.getZoom(), 1e-10);
    }

}