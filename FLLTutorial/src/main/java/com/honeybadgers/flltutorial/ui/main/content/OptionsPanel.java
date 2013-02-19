/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.ui.main.content;

import com.honeybadgers.flltutorial.model.Option;
import com.honeybadgers.flltutorial.ui.main.content.utilities.OptionPanel;
import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
/**
 *
 * @author chingaman
 */
abstract public class OptionsPanel extends JPanel{
    private Dimension preferedDimension = new Dimension(300, 500);
    private Dimension minDimension = new Dimension(300, 400);
    private Dimension maxDimension = new Dimension(300, 32767);
    JScrollPane selections;
    JPanel clickedPanel;
    JPanel selectionsViewPort;
    List<Option> options;
    ArrayList<OptionPanel> optionPanels;
    OptionsPanel(List<Option> options)
    {
        super();
        this.setPreferredSize(preferedDimension);
        this.setMinimumSize(minDimension);
        this.setMaximumSize(maxDimension);
        this.options = options;
    }
    OptionPanel getButtonAt(Point point) 
    {
        Component component;
        /*translate the point relative to the current viewport offset*/
        point.translate(-this.getX(), -this.getY());
        point.translate(0, -this.selectionsViewPort.getY());
        component = this.selectionsViewPort.getComponentAt(point);
        /*check this is an OptionPanel else no button was clicked*/
        if(component instanceof OptionPanel) 
        {
            OptionPanel optionPanel = (OptionPanel)component;
            return optionPanel;
        }
        else 
        {
            return null;
        }
    }
    //TODO: add support for deleting button on either incorrect or correct option
    //selected
    void removeOption(OptionPanel optionPanel)
    {
        
    }
    void dispatchInterceptedEvent(AWTEvent e)
    {
        if(e instanceof MouseWheelEvent)
        {
            /*this dispatches the given mousewheelevent to the option selections panel*/
            this.selections.dispatchEvent(e);
        }
    }
 
}
