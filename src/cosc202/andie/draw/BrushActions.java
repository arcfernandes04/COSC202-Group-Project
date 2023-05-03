package cosc202.andie.draw;

import java.awt.Color;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import cosc202.andie.Language;


/**
 * <p>
 * Provides a slider to choose different brush sizes. This class extends
 * JMenu and can thus be added to JMenuBar instances.
 * </p>
 * 
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Joshua Carter
 * @see DrawPanel
 */
public class BrushActions extends JMenuBar {

    private JMenu menu;

    /**
     * Default constructor which creates an instance of JSlider
     * to display to the user, and thus update the brush size choice.
     * 
     * @param background The background colour to use for the JMenuBar;
     * if set to transparent, it will blend in nicely with the rest of the GUI.
     */
    public BrushActions(Color background) {

        setBackground(background);
        setBorderPainted(false);

        menu = new JMenu();
        this.add(menu);
        
        JSlider slider = new JSlider(JSlider.HORIZONTAL);
        slider.setSnapToTicks(true);
        slider.setMajorTickSpacing(2); slider.setMinorTickSpacing(1);
        slider.setMaximum(20); slider.setMinimum(2);
        slider.setValue(DrawPanel.getStrokeSize());

        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider slider = (JSlider) e.getSource();
                DrawPanel.setStrokeSize(slider.getValue());
            }
        });
        menu.add(slider);
    }

    /**
     * Updates the text on this JMenu.
     * Called once the user's drawing tools choices have changed.
     */
    public void update(){
        menu.setText(Language.getWord("Current_Brush") + " " + DrawPanel.getStrokeSize());
    }
}