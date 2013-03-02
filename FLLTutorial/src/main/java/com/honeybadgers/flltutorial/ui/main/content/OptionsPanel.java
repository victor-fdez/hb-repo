/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.ui.main.content;

import com.honeybadgers.flltutorial.ui.main.content.utilities.OptionPanel;
import com.honeybadgers.flltutorial.ui.utilities.PanelsScrollPane;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseWheelEvent;
import java.util.List;
import javax.swing.JPanel;
/**
 *
 * @author chingaman
 */
abstract public class OptionsPanel extends JPanel{
    private Dimension preferedDimension = new Dimension(250, 500);
    private Dimension minDimension = new Dimension(250, 400);
    private Dimension maxDimension = new Dimension(300, 32767);
    protected PanelsScrollPane optionPanelsScrollPane;
    protected JPanel extraFeaturesPanel;
    JPanel clickedPanel;
    protected List<OptionPanel> optionPanels;
    
    public OptionsPanel(List<OptionPanel> options)
    {
        super();
        
        this.setPreferredSize(preferedDimension);
        this.setMinimumSize(minDimension);
        this.setMaximumSize(maxDimension);
        this.optionPanels = options;
        
        this.initComponents();
    }
    
    private void initComponents()
    {
        //create the panel where the scroll pane were option panels will be laid
        this.optionPanelsScrollPane = new PanelsScrollPane();
        
            //for every option panel append it to the panels scroll pane
            for(OptionPanel optionPanel : this.optionPanels)
            {
                this.optionPanelsScrollPane.appendPanel(optionPanel);
            }
       
        //setup layout of this panel
        this.setLayout(new BorderLayout());
        this.add(this.optionPanelsScrollPane);
    }
    
    OptionPanel getButtonAt(Point point) 
    {
        JPanel panelAtPoint;
        point.translate(-this.getX(), -this.getY());
        panelAtPoint = this.optionPanelsScrollPane.getPanelAtPoint(point);
        if(panelAtPoint instanceof OptionPanel) 
        {
            OptionPanel optionPanel = (OptionPanel)panelAtPoint;
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
            this.optionPanelsScrollPane.dispatchEvent(e);
        }
    }
 
}
