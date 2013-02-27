/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.ui.main.content;

import com.honeybadgers.flltutorial.model.Option;
import com.honeybadgers.flltutorial.model.OptionTracker;
import com.honeybadgers.flltutorial.ui.main.content.utilities.MultiLineLabel;
import com.honeybadgers.flltutorial.ui.main.content.utilities.OptionPanel;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author chingaman
 */
public class StageSelectorPanel extends StagePanel{

    private OptionTracker currentTracker;
    private JPanel scrollPanel;
    StageSelectorPanel()
    {
        super();
        Option option = new Option("some description", true, null);
        option.addChild(new Option("some description", true, null));
        option.addChild(new Option("some description", true, null));
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
        //titleLabel.addText("hello world");
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
        
        this.scrollPanel = new JPanel();
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
        scrollPane.getSize();
        this.add(scrollPane, c);
        this.setBackground(Color.GRAY);
    }
    @Override
    OptionsPanel getOptionsPanel() {
        return this.optionsPanel;
    }

    @Override
    int dropOptionPanel(OptionPanel optionPanel) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    void clicked(Point point) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
