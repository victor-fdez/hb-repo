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
public class OptionPanel extends JPanel implements Cloneable
{
    public enum OptionState
    {
        UNSELECTED, SELECTED, DRAGGED, DROPPED
    }
    OptionState state;
    public Option option;
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

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        //System.out.println("repainted "+this.option.getDescription());
    }
    //TODO: create methods that will draw a button under different STATES
    public OptionPanel copy()
    {
        try
        {
            return (OptionPanel)this.clone();
        }
        catch(Exception e)
        {System.out.println("OptionPanel.copy is not cloning");}
        return null;
    }
    
}
