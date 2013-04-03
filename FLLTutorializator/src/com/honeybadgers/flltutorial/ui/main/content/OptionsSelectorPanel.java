/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.ui.main.content;

import com.honeybadgers.flltutorial.ui.main.content.utilities.OptionPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.List;
import javax.swing.border.LineBorder;

/**
 *
 * @author chingaman
 */
public class OptionsSelectorPanel extends OptionsPanel implements ComponentListener{

    public OptionsSelectorPanel(List<OptionPanel> optionsPanels)
    {
        super(optionsPanels);
        this.initComponents();
    }
    private void initComponents()
    {
        //add extra features in the extraFeaturesPanel if it is preferred
        this.setBackground(Color.GRAY);
        this.optionPanelsScrollPane.setBorder(new LineBorder(Color.BLACK));
        this.addComponentListener(this);
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }
    public void updateOptionPanels(List<OptionPanel> optionPanels)
    {
        this.optionPanelsScrollPane.removeAllPanels();
        this.optionPanels = optionPanels;
        
        if(this.optionPanels == null)
        {
            return;
        }
        //add all of the new option panels
        for(OptionPanel optionPanel : this.optionPanels)
        {
            this.optionPanelsScrollPane.appendPanel(optionPanel);
        }
        
        this.revalidate();
    }
    @Override
    public void componentResized(ComponentEvent e) {
        //this.selections.setSize(this.getSize());
        //this.revalidate();
    }
    @Override
    public void componentMoved(ComponentEvent e) {}
    @Override
    public void componentShown(ComponentEvent e) {}
    @Override
    public void componentHidden(ComponentEvent e) {} 
}
