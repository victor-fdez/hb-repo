/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.ui.main.content.stages;

import com.honeybadgers.flltutorial.model.Option;
import com.honeybadgers.flltutorial.model.OptionTracker;
import com.honeybadgers.flltutorial.ui.main.content.OptionsPanel;
import com.honeybadgers.flltutorial.ui.main.content.utilities.OptionPanel;
import com.honeybadgers.flltutorial.ui.main.content.utilities.TextOptionPanel;
import java.awt.AWTEvent;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

/**
 *
 * @author chingaman
 */
abstract public class StagePanel extends JPanel
{
    /*tutorial options*/
    protected Option tutorialOption;
    protected String stageName;
    protected Option problem;
    protected OptionTracker solutionTracker;
    private Dimension preferedDimension = new Dimension(500, 500);
    private Dimension minDimension = new Dimension(500, 400);
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
    public abstract OptionsPanel getOptionsPanel();
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
    public abstract int dropOptionPanel(OptionPanel optionPanel);
    /**
     * 
     */
    public abstract void scrolled(AWTEvent e); 
    /**
     * Default option panel generator
     * @param option
     * @return 
     */
    protected OptionPanel createOptionPanel(Option option)
    {
        return (OptionPanel)new TextOptionPanel(option);
    } 
    /**
     * This methods generates an List of OptionPanels. If the type is 1, the options panels
     * are generated based on the child options of the options being tracked by the options tracker.
     * Else if the type is 0, then the option panels are generated based on the correct options 
     * @param option
     * @param optionTracker
     * @return 
     */ 
    protected List<OptionPanel> generateOptionPanels(OptionTracker optionTracker, int type)
    {
        ArrayList<OptionPanel> optionPanels = new ArrayList<OptionPanel>();
        if(type == 0)
        {
            List<OptionTracker> optionTrackers  = optionTracker.getAllCorrectTrackers();
            for(OptionTracker optionTrackerChild : optionTrackers)
            {
                OptionPanel optionPanel;
                if(optionTrackerChild != null)
                {
                    Option option = optionTrackerChild.getOption();
                    optionPanel = this.createOptionPanel(option);
                    if(optionTrackerChild.isFinished())
                    {
                        optionPanel.setState(OptionPanel.OptionState.FINISHED);
                    }
                    else
                    {
                        if(optionTracker.isChoosed(option))
                        {
                            optionPanel.setState(OptionPanel.OptionState.CORRECT);
                        }
                        else
                        {
                            optionPanel.setState(OptionPanel.OptionState.NORMAL);
                        }
                    }
                }
                else
                {
                    optionPanel = this.createOptionPanel(null);
                    optionPanel.setState(OptionPanel.OptionState.NORMAL);
                }
                optionPanels.add(optionPanel);
            }
        }
        else
        {
            ArrayList<Option> options = (ArrayList<Option>)(optionTracker.getOption().getOptions());
            for(Option childOption : options)
            {
                OptionPanel optionPanel;
                if(childOption != null)
                {
                    optionPanel = this.createOptionPanel(childOption);
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
                        if(optionTracker.isChoosed(childOption))
                        {
                            optionPanel.setState(OptionPanel.OptionState.INCORRECT);
                        }
                        else
                        {
                            optionPanel.setState(OptionPanel.OptionState.NORMAL);
                        }
                    }
                }
                else
                {
                    optionPanel = this.createOptionPanel(null);
                    optionPanel.setState(OptionPanel.OptionState.NORMAL);
                }
                optionPanels.add(optionPanel);
            }
        }
        return optionPanels;
    }

    public String getStageName() {
        return stageName;
    }
    
    public boolean isFinished()
    {
        return this.solutionTracker.isFinished();
    }
}
