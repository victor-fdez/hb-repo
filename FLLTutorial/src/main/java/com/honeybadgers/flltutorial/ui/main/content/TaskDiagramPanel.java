/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.ui.main.content;

import com.honeybadgers.flltutorial.model.Option;
import com.honeybadgers.flltutorial.ui.main.content.utilities.OptionPanel;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author chingaman
 */
public class TaskDiagramPanel extends StagePanel{
    String subtaskArray[] = {"subtask 1", "subtask 2", "subtask 3"};
    /*tutorial options*/
    Option problemOption;
    /*selected options, there are the options the student chooses*/
    Option selectedOption;
    /*pointers into respective trees*/
    Option[] currentOption = new Option[2];
    /*array of panels to hold current options shown*/
    JPanel[] depthPanels = new JPanel[11];
    TaskDiagramPanel()
    {
        super();
        /*setup name*/
        this.stageName = "Task Diagram";
        this.setBackground(Color.GRAY);
        /*TODO: pass in actual options for this panel*/
        /*creating some fake options for testing purposes*/
        ArrayList<Option> options = new ArrayList<Option>();
        for(int i = 0; i < 3; i++)
        {
            ArrayList<Option> optionsChild = new ArrayList<Option>();
            options.add(new Option(subtaskArray[i], true, optionsChild));
            for(int j = 0; j < 3; j++)
            {
                optionsChild.add(new Option(subtaskArray[i], ((i%2 == 0) ? true : false), null));
            }
        }
        this.problemOption = new Option("problem description option - tops", true, options);
        this.selectedOption = new Option(this.problemOption.getDescription(), true, new ArrayList<Option>(options.size()));
        /*load initial set of options*/
        this.initComponents();
        this.optionsPanel = new OptionsSelectorPanel(options);
    }
    private void initComponents()
    {
        /*create components*/
        JLabel titleLabel = new JLabel(this.stageName);
        titleLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        titleLabel.setBackground(Color.GREEN);
        titleLabel.setOpaque(true);
        /*create necessary options panels*/
        
        
        /*setup vertical gridlayout*/
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
        /*add all of the depth panels to the stage panel*/
        for(int i = 0; i < this.depthPanels.length; i++)
        {
            /*setup the constraints for each panel added*/
            JPanel panel = new JPanel();
            c = new GridBagConstraints();
            c.fill = GridBagConstraints.BOTH;
            c.gridx = 0;
            c.gridy = i + 1;
            c.weightx = 1.0;
            c.weighty = 1.0;
            c.ipady = 10;
            c.insets = new Insets(2,1,2,1);
            c.anchor = GridBagConstraints.PAGE_START;
            panel.setBackground(Color.GRAY);
            panel.setLayout(new GridLayout(1,0,4,4));
            this.depthPanels[i] = panel;
            this.add(panel, c);
        }
        /*add options panels selected depending on initialization*/
        
        /*if selectedOption has something selected*/
        
        /*if first time initialization*/
        //setup problem description option
        JPanel setupPanel = this.depthPanels[0];
        OptionPanel setupOption = new OptionPanel(this.problemOption);
        setupPanel.add(setupOption);
        //setup unoccupied option panels
        setupPanel = this.depthPanels[1];
        for(int i = 0; i < this.problemOption.getOptions().size(); i++)
        {
            OptionPanel optionPanel = new OptionPanel();
            optionPanel.setState(OptionPanel.OptionState.UNOCCUPIED);
            setupPanel.add(optionPanel);
        }
        /*
        for(int i = 0; i < this.depthPanels.length; i++)
        {
            for(int j = 0; j < 10; j++)
            {
                JPanel panel = new JPanel();
                panel.setBackground(Color.WHITE);
                if(i == 2 && j > 4)
                {
                    this.depthPanels[i].remove(0);
                    break;
                }
                this.depthPanels[i].add(panel);
            }
        }
        */
        //this.setBackground(Color.red);
    }
    @Override
    OptionsPanel getOptionsPanel() {
        return this.optionsPanel;
    }

    @Override
    boolean dropOptionPanel(OptionPanel optionPanel) {
        return true;
    }
    
}
