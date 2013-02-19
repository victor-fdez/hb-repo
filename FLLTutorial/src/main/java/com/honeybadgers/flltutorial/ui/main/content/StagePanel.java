/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.ui.main.content;

import com.honeybadgers.flltutorial.ui.main.content.utilities.OptionPanel;
import java.awt.Dimension;
import java.awt.Point;
import javax.swing.JPanel;

/**
 *
 * @author chingaman
 */
abstract public class StagePanel extends JPanel
{
    protected String stageName;
    private Dimension preferedDimension = new Dimension(600, 500);
    private Dimension minDimension = new Dimension(600, 400);
    private Dimension maxDimension = new Dimension(32767, 32767);
    OptionsPanel optionsPanel;
    StagePanel()
    {
        super();
        this.setPreferredSize(preferedDimension);
        this.setMinimumSize(minDimension);
        this.setMaximumSize(maxDimension);
        this.setOpaque(true);
        this.setVisible(true);
    }
    /**
     * 
     * @return the OptionsPanel that implements how options are given as choices.
     */
    abstract OptionsPanel getOptionsPanel();
    /**
     * This method is to be implemented by any subclass of StagePanel. This method
     * delivers an OptionsPanel that can be used to find the location where a dragged panel
     * was putted over. The options panel also contains an option, that denotes the option
     * shown in the panel. true should only be returned when the OptionPanel was 
     * dropped in the correct place in the panel. Correct here means either the panel
     * is a correct option or it is incorrect and the OptionPanel was dropped inside
     * the correct panel. In the second case an morality message will be displayed.
     * 
     * @param optionPanel
     * @return whether this option panel actually dropped correctly.
     */
    abstract boolean dropOptionPanel(OptionPanel optionPanel);
    /**
     * This method is to be implemented by any subclass of StagePanel. This method
     * delivers point clicks (press and release) on the given stage subclass panel
     * 
     * @param point  a Point object denoting the position clicked
     */
    abstract void clicked(Point point);
}
