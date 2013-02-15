/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.ui.main.content;

import com.honeybadgers.flltutorial.model.Option;
import com.honeybadgers.flltutorial.ui.main.content.utilities.OptionPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author chingaman
 */
public class OptionsSelectorPanel extends OptionsPanel{
    JScrollPane selections;
    JPanel selectionsViewPort;
    JPanel columnViewPort;
    OptionsSelectorPanel(List<Option> options)
    {
        super(options);
        this.initComponents();
    }
    private void initComponents()
    {
        this.columnViewPort = new JPanel();
        //selection view port contains a column panel
        this.selectionsViewPort = new JPanel();
        this.selectionsViewPort.setLayout(new GridLayout(0,1,0,1));

        this.selections = new JScrollPane();
        this.selections.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.selections.getVerticalScrollBar().setVisible(true);
        //TESTING: add options
        this.options = new ArrayList();
        this.optionPanels = new ArrayList();
        //create a new panel for each option
        for(int i = 0; i < 20; i++)
        {
            Option option = new Option("button "+i, true, null);
            OptionPanel optionPanel = new OptionPanel(option);
            this.optionPanels.add(optionPanel);
            optionPanel.setBackground(Color.GREEN);
            optionPanel.setPreferredSize(new Dimension(270, 40));
        }
        //add each option panel to selection scroll pane
        this.add(this.selections);
        for(OptionPanel option : this.optionPanels)
        {
            this.selectionsViewPort.add(option);
        }
        this.selections.setBorder(null);
        this.selections.setViewportView(this.selectionsViewPort);
        
        //this.setBorder(BorderFactory.createLoweredBevelBorder());
        this.add(this.selections);
        this.setVisible(true);
        this.setOpaque(true);
        //this.revalidate();
        this.selections.setLocation(0, 0);
        this.selections.setPreferredSize(this.getPreferredSize());
    }
    @Override
    public void paint(Graphics g) {
        //this.setBackground(Color.red);
        //this.selections.setSize(this.getSize());
        //System.out.println("outer bounds "+this.getBounds()+" size "+this.getSize());
        //System.out.println("inner bounds "+this.selections.getBounds()+" size "+this.selections.getSize());
        //System.out.println("-------repainting--------");
        this.selections.setLocation(0, 0);
        this.selections.setPreferredSize(this.selections.getParent().getSize());
        super.paint(g);
       // System.out.println("-------------------------");

       //this.invalidate();
        //columnViewPort.paint(g);
    }
 

    
}