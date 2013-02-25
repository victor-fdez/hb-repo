/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.ui.main.content;

import com.honeybadgers.flltutorial.model.Option;
import com.honeybadgers.flltutorial.model.OptionTracker;
import com.honeybadgers.flltutorial.ui.main.content.utilities.OptionPanel;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
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
     * @return      0 when option drop was correct and needs to draw option green.
     *              1 when option drop was incorrect and needs to draw option red.
     *              2 when option drop has no effect and nothing needs to be done.
     */
    abstract int dropOptionPanel(OptionPanel optionPanel);
    /**
     * This method is to be implemented by any subclass of StagePanel. This method
     * delivers point clicks (press and release) on the given stage subclass panel
     * 
     * @param point  a Point object denoting the position clicked
     */
    abstract void clicked(Point point);
    /**
     * 
     * @param option
     * @param optionTracker
     * @return 
     */
    protected List<OptionPanel> generateOptionPanels(Option option, OptionTracker optionTracker)
    {
        ArrayList<OptionPanel> optionPanels = new ArrayList<OptionPanel>();
        for(Option childOption : option.getOptions())
        {
            OptionPanel optionPanel = new OptionPanel(childOption);
            if(childOption.isCorrect())
            {
                if(optionTracker.isChoosed(childOption))
                {
                    optionPanel.setState(OptionPanel.OptionState.CORRECT);
                }
                else
                {
                    optionPanel.setState(OptionPanel.OptionState.NORMAL);
                }
            }
            else
            {
                optionPanel.setState(OptionPanel.OptionState.INCORRECT);
            }
            optionPanels.add(optionPanel);
        }
        return optionPanels;
    }
}
