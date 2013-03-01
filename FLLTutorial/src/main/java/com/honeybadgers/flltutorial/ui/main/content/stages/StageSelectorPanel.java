/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.ui.main.content.stages;

import com.honeybadgers.flltutorial.model.Option;
import com.honeybadgers.flltutorial.model.OptionTracker;
import com.honeybadgers.flltutorial.ui.main.content.OptionsPanel;
import com.honeybadgers.flltutorial.ui.main.content.OptionsSelectorPanel;
import com.honeybadgers.flltutorial.ui.main.content.utilities.OptionPanel;
import com.honeybadgers.flltutorial.ui.utilities.PanelsScrollPane;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author chingaman
 */
public class StageSelectorPanel extends StagePanel{

    private OptionTracker currentTracker;
    private PanelsScrollPane scrollPane;
    private Option mainOption;
    private int lastPosition;
    StageSelectorPanel()
    {
        super();
        this.mainOption = new Option("this is the generic problem, please follow this directions before processing to perform this problem", true, null);
        mainOption.addChild(new Option("some description", false, null));
        mainOption.addChild(new Option("larger description that gives a really boring, and uncomprenhensive detailed, and I forget to say more boring explanation", true, null));
        mainOption.addChild(new Option("some description", true, null));
        this.currentTracker = new OptionTracker(mainOption);
        //this.stageName = "Generic Stage Selector";
        List<OptionPanel> optionPanels = this.generateOptionPanels(this.currentTracker, 1);
        this.optionsPanel = new OptionsSelectorPanel(optionPanels);
    }
    
    protected final void initComponents()
    {
        //setup title of stage
        JTextArea titleLabel = new JTextArea(this.stageName);
        titleLabel.setLineWrap(true);
        titleLabel.setWrapStyleWord(true);
        titleLabel.setBorder(new EmptyBorder(4, 4, 4, 4));
        //titleLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
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
        
        //add main option description option panel
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(10, 4, 4, 4);
        c.ipady = 0;
        c.weightx = 1.0;
        c.anchor = GridBagConstraints.PAGE_START;
        this.add(new OptionPanel(this.mainOption), c);
        
        //setup scroll pane
        this.scrollPane = new PanelsScrollPane();
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(4, 4, 4, 4);
        c.ipady = 0;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.anchor = GridBagConstraints.PAGE_START;
        
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
        System.out.println("x : "+x+" y : "+y);
        System.out.println("scroll sx: "+sx+" sy: "+sy);
        Point dropPoint = new Point(x-sx, y-sy);
        //check whether the option fell inside the answers panel
        if(this.scrollPane.contains(dropPoint))
        {
            System.out.println("CONTAINS POINT");
            //check with option tracker
            Option option = optionPanel.getOption();
            boolean newTracked = this.currentTracker.addOptionAt(this.lastPosition, option);
            if(newTracked)
            {
                if(option.isCorrect())
                {
                    optionPanel.setState(OptionPanel.OptionState.CORRECT);
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
    public final void clicked(Point point) {
        //do nothing, since options don't have options
    }

    @Override
    public void scrolled(AWTEvent e) {
        this.scrollPane.dispatchEvent(e);
    }
}
