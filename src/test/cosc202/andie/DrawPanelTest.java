package test.cosc202.andie;

import java.awt.Color;

import cosc202.andie.draw.DrawPanel;

import org.junit.jupiter.api.*;

public class DrawPanelTest {
    
    @Test
    void dummyTest(){}

    @Test
    @Order(1)
    void getToolTest(){
        Assertions.assertEquals(DrawPanel.SELECTION, DrawPanel.getTool(), "The default tool should be 'SELECTION'.");
    }

    @Test
    @Order(2)
    void getFillTypeTest() {
        Assertions.assertEquals(DrawPanel.BORDER_ONLY, DrawPanel.getFillType(), "The default fill type should be 'BORDER_ONLY'.");
    }

    @Test
    @Order(3)
    void getShapeTypeTest() {
        Assertions.assertEquals(DrawPanel.RECTANGLE, DrawPanel.getShapeType(), "The default shape type should be 'RECTANGLE'.");
    }

    @Test
    @Order(4)
    void getStrokeSizeTest() {
        Assertions.assertEquals(2, DrawPanel.getStrokeSize(), "The default stroke size should be 2.");
    }

    @Test
    @Order(5)
    void getPrimaryColourTest() {
        Assertions.assertEquals(Color.BLACK, DrawPanel.getPrimary(), "Default primary colour should be black.");
    }

    @Test
    @Order(6)
    void getSecondaryColourTest() {
        Assertions.assertEquals(Color.BLACK, DrawPanel.getSecondary(), "Default secondary colour should be black.");
    }

}
