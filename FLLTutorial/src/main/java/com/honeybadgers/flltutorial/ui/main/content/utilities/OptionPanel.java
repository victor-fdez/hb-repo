/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.ui.main.content.utilities;

import com.honeybadgers.flltutorial.model.Option;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.JTextArea;

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
    JTextArea description;
    public OptionPanel(Option option)
    {
        this.option = option;
        this.state = OptionState.UNSELECTED;
        //add text area
        description.setText("description of option");
        description.setEditable(false);
        //add label to store picture if option contains picture
        
        //add all components
        this.add(this.description);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        //customize behaviour option panel
    }
    
}
