/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.ui.main.content.stages;

import com.honeybadgers.flltutorial.model.Option;
import com.honeybadgers.flltutorial.model.OptionTracker;
import com.honeybadgers.flltutorial.ui.FLLTutorialUI;
import com.honeybadgers.flltutorial.ui.main.content.OptionsPanel;
import com.honeybadgers.flltutorial.ui.main.content.OptionsSelectorPanel;
import com.honeybadgers.flltutorial.ui.main.content.utilities.OptionPanel;
import com.honeybadgers.flltutorial.ui.main.content.utilities.TextOptionPanel;
import com.honeybadgers.flltutorial.ui.utilities.PanelsScrollPane;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.util.List;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author chingaman
 */
public class StageSelectorPanel extends StagePanel{

    private PanelsScrollPane scrollPane;
    private int lastPosition;
    private OptionPanel parentOptionPanel;
    StageSelectorPanel(Option rootOption)
    {
        super();
        this.tutorialOption = rootOption;
        this.solutionTracker = OptionTracker.generateOptionTrackerTree(rootOption);
     
        
        List<OptionPanel> optionPanels = this.generateOptionPanels(this.solutionTracker, 1);
        this.optionsPanel = new OptionsSelectorPanel(optionPanels);
    }
    
    protected final void initComponents()
    {
        //setup title of stage
        JTextArea titleLabel = new JTextArea(this.stageName);
        titleLabel.setLineWrap(true);
        titleLabel.setWrapStyleWord(true);
        titleLabel.setBorder(new EmptyBorder(4, 4, 4, 4));
        titleLabel.setBackground(Color.GREEN);
        
        //add title to selector problem
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(10, 4, 4, 4);
        c.ipady = 0;
        c.weightx = 1.0;
        c.anchor = GridBagConstraints.PAGE_START;
        this.add(titleLabel, c);
        
        //add a mission description label
        /*
        JTextArea missionLabel = new JTextArea(FLLTutorialUI.currentTutorial.getMission());
        missionLabel.setLineWrap(true);
        missionLabel.setWrapStyleWord(true);
        missionLabel.setBorder(new EmptyBorder(4, 4, 4, 4));
        missionLabel.setBackground(Color.GREEN);
        
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(10, 4, 4, 4);
        c.ipady = 0;
        c.weightx = 1.0;
        c.anchor = GridBagConstraints.PAGE_START;
        this.add(missionLabel, c);*/
        
        //add main option description option panel
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(10, 4, 4, 4);
        c.ipady = 0;
        c.weightx = 1.0;
        c.anchor = GridBagConstraints.PAGE_START;
        this.parentOptionPanel = new TextOptionPanel(this.tutorialOption);
        if(this.solutionTracker.isFinished())
        {
            this.parentOptionPanel.setState(OptionPanel.OptionState.FINISHED);
        }
        else
        {
            this.parentOptionPanel.setState(OptionPanel.OptionState.CORRECT);
        }
        this.add(this.parentOptionPanel, c);
        
        //setup scroll pane
        this.scrollPane = new PanelsScrollPane(true);
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(4, 4, 4, 4);
        c.ipady = 0;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.anchor = GridBagConstraints.PAGE_START;
        
        //add options that have been selected
        for(OptionTracker optionTracker : this.solutionTracker.getAllCorrectTrackers())
        {
            if(optionTracker == null)
            {
                continue;
            }
            //add correct option panel
            this.lastPosition++;
            OptionPanel optionPanel = new TextOptionPanel(optionTracker.getOption());
            optionPanel.setState(OptionPanel.OptionState.FINISHED);
            this.scrollPane.appendPanel(optionPanel);
        }
        
        this.add(this.scrollPane, c);
        
        this.setBackground(Color.GRAY);
        this.revalidate();
        this.repaint();
    }
    @Override
    public final OptionsPanel getOptionsPanel() {
        return this.optionsPanel;
    }

    @Override
    public final int dropOptionPanel(OptionPanel optionPanel) {
        int x = (int)optionPanel.getBounds().getCenterX();
        int y = (int)optionPanel.getBounds().getCenterY();
        int sx = (int)this.scrollPane.getX();
        int sy = (int)this.scrollPane.getY();
        //System.out.println("x : "+x+" y : "+y);
        //System.out.println("scroll sx: "+sx+" sy: "+sy);
        Point dropPoint = new Point(x-sx, y-sy);
        //check whether the option fell inside the answers panel
        if(this.scrollPane.contains(dropPoint))
        {
            //check with option tracker
            Option option = optionPanel.getOption();
            boolean newTracked = this.solutionTracker.addOptionAt(this.lastPosition, option);
            if(newTracked)
            {
                OptionTracker optionTracker = this.solutionTracker.getCorrectChild(this.lastPosition);
                if(option.isCorrect())
                {
                    if(optionTracker.isFinished())
                    {
                        optionPanel.setState(OptionPanel.OptionState.FINISHED);
                        if(this.solutionTracker.isFinished())
                        {
                            this.parentOptionPanel.setState(OptionPanel.OptionState.FINISHED);
                        }
                    }
                    else
                    {
                        //this should not happen, because there should not be more leveles
                        optionPanel.setState(OptionPanel.OptionState.CORRECT);
                    }
                    this.scrollPane.appendPanel(optionPanel);
                    this.lastPosition++;
                    return 0;
                }
                else
                {
                    return 1;
                }
                
            }
            //fix
        }
        return 2;
    }
    
    @Override
    public void scrolled(AWTEvent e) {
        this.scrollPane.dispatchEvent(e);
    }
}
