/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.ui.main.content.utilities;

import com.honeybadgers.flltutorial.model.Option;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author chingaman
 */
public class OptionPanel extends JPanel 
{
    private enum OptionState
    {
        UNSELECTED, SELECTED, DRAGGED, DROPPED
    }
    OptionState state;
    Option option;
    JLabel description;
    public OptionPanel(Option option)
    {
        super();
        this.option = option;
        this.state = OptionState.UNSELECTED;
        //add text area
        this.description = new JLabel(option.getDescription());
        //add label to store picture if option contains picture
        //add all components
        this.setBackground(Color.red);
        this.setVisible(true);
        this.setOpaque(true);
        this.add(this.description);
    }

    public void paint(Graphics g) {
        super.paint(g);
        //System.out.println("repainted "+this.option.getDescription());
    }
    
}
