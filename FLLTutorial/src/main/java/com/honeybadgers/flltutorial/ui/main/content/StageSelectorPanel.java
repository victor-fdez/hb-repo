/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.ui.main.content;

import com.honeybadgers.flltutorial.model.Option;
import com.honeybadgers.flltutorial.model.OptionTracker;
import com.honeybadgers.flltutorial.ui.main.content.utilities.OptionPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Scrollable;

/**
 *
 * @author chingaman
 */
public class StageSelectorPanel extends StagePanel{

    private OptionTracker currentTracker;
    private JPanel scrollPanel;
    private int lastPosition;
    StageSelectorPanel()
    {
        super();
        Option option = new Option("some description", true, null);
        option.addChild(new Option("some description", true, null));
        option.addChild(new Option("some descriptionaaaaaaaaaaaaaa        some descriptionaaaaaaaaaaaaaa           some descriptionaaaaaaaaaaaaaa", true, null));
        option.addChild(new Option("some description", true, null));
        this.currentTracker = new OptionTracker(option);
        this.stageName = "Stage Selector                                                              aaaaaaaaaaaaazazzzzz111111111111111111111111111111111111111";
        List<OptionPanel> optionPanels = this.generateOptionPanels(this.currentTracker, 1);
        this.optionsPanel = new OptionsSelectorPanel(optionPanels);
        this.initComponents();
    }
    
    final void initComponents()
    {
        //create components
        JTextArea titleLabel = new JTextArea(this.stageName);
        titleLabel.setLineWrap(true);
        titleLabel.setWrapStyleWord(true);
        //titleLabel.set
        titleLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        titleLabel.setBackground(Color.GREEN);
        titleLabel.setOpaque(true);
        //setup vertical gridlayout
        GridBagLayout gridBagLayout = new GridBagLayout();
        this.setLayout(gridBagLayout);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(1, 1, 1, 1);
        c.ipady = 0;
        c.anchor = GridBagConstraints.PAGE_START;
        this.add(titleLabel, c);
        
        this.scrollPanel = new FittedViewportPanel();
        this.scrollPanel.setOpaque(true);
        this.scrollPanel.setVisible(true);
        this.scrollPanel.setLayout(new GridBagLayout());
        this.scrollPanel.setBackground(Color.red);
        /*add all of the depth panels to the stage panel*/
        JScrollPane scrollPane = new JScrollPane();    
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.ipady = 10;
        c.insets = new Insets(2,1,2,1);
        c.anchor = GridBagConstraints.PAGE_START;
        //scrollPane
        scrollPane.setViewportView(this.scrollPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBar(null);
        scrollPane.getSize();
        this.add(scrollPane, c);
        this.revalidate();
        this.setBackground(Color.GRAY);
    }
    @Override
    OptionsPanel getOptionsPanel() {
        return this.optionsPanel;
    }

    @Override
    int dropOptionPanel(OptionPanel optionPanel) {
        JScrollPane scrollPane = (JScrollPane)this.getComponents()[1];
        int x = (int)optionPanel.getBounds().getCenterX();
        int y = (int)optionPanel.getBounds().getCenterY();
        int sx = (int)scrollPane.getX();
        int sy = (int)scrollPane.getY();
        System.out.println("x : "+x+" y : "+y);
        System.out.println("scroll sx: "+sx+" sy: "+sy);
        Point dropPoint = new Point(x-sx, y-sy);
        //check whether the option fell inside the answers panel
        if(this.scrollPanel.contains(dropPoint))
        {
            //check with option tracker
            Option option = optionPanel.getOption();
            boolean newTracked = this.currentTracker.addOptionAt(this.lastPosition, option);
            if(newTracked)
            {
                if(option.isCorrect())
                {
                    GridBagConstraints c = new GridBagConstraints();
                    c.weightx = 1.0;
                    c.weighty = 0.0;
                    c.fill = GridBagConstraints.HORIZONTAL;
                    c.gridx = 0;
                    c.gridy = this.lastPosition;
                    c.insets = new Insets(2,2,2,2);
                    c.anchor = GridBagConstraints.PAGE_START;
                    this.scrollPanel.add(optionPanel, c);
                    this.scrollPanel.revalidate();
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
    void clicked(Point point) {
        //do nothing, since options don't have options
    }
    
    protected class FittedViewportPanel extends JPanel
    {
        public FittedViewportPanel()
        {
            super();
        }
        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        public boolean getScrollableTracksViewportHeight() {
            return false;
        }
        
    }
}
