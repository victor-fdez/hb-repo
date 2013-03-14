/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.ui.main.content.utilities;

import com.honeybadgers.flltutorial.model.Option;
import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author chingaman
 */
public abstract class OptionPanel extends JLayeredPane implements Cloneable
{
    public static enum OptionState
    {
        NORMAL, HIDDEN_OCCUPY, DRAGGED, DROPPED, UNOCCUPIED, CORRECT, INCORRECT, FINISHED
    }
    protected OptionState state;
    protected Option option;
    protected JPanel beaconPanel;
    protected JPanel contentPanel;
    //JLabel description;
    public OptionPanel()
    {
        super();
        this.state = OptionState.UNOCCUPIED;
        
        
        this.initComponents();
    }
    public OptionPanel(Option option)
    {
        super();
        
        this.option = option;
        this.state = OptionState.NORMAL;
        
        this.initComponents();
    }
    private void initComponents()
    {
        this.contentPanel = new JPanel();
        this.contentPanel.setVisible(true);
        this.contentPanel.setOpaque(true);
        
        this.add(this.contentPanel, JLayeredPane.DEFAULT_LAYER);
        
        this.beaconPanel = new JPanel();
        
        this.beaconPanel.setVisible(true);          //might be more efficient to set invisible at times
        this.beaconPanel.setOpaque(false);
        
        this.add(this.beaconPanel, JLayeredPane.DRAG_LAYER);
        
        this.addComponentListener(new ComponentAdapter()
        {
             @Override
             public void componentResized(java.awt.event.ComponentEvent evt) {
                 beaconPanel.setSize(getSize());
                 contentPanel.setSize(getSize());
                 beaconPanel.revalidate();
                 contentPanel.revalidate();
             }
        });
        //this.setBorder(new LineBorder(Color.BLACK, 1,));
    }
    //TODO: create methods that will draw a button under different STATES
    abstract public OptionPanel copy();

    /**
     * This object method transfers in a give option, and stores it. It may not show
     * the option immediately, unless the state of the option panel is NORMAL.
     * 
     * @param option the Option object that will be stored in this OptionPanel
     */
    abstract public void transferOption(Option option);
    /**
     * This object method sets the state of this OptionPanel. It is mainly used for display
     * purposes.
     * 
     * @param state the enumerate type OptionPanel.OptionState which denotes the state of the panel.
     */
    abstract public void setState(OptionState state);
    
  
    public OptionState getState()
    {
        return this.state;
    }
    
    public Option getOption() {
        return option;
    }
    /**
     * Returns an Object that covers the whole panel and can be used to track, and
     * receive events on the given option panel.
     * 
     * @return 
     */
    public Component getBeacon()
    {
        return this.beaconPanel;
    }
    /**
     * Returns the corresponding option panel from the input beacon.
     * 
     * @param object
     * @return 
     */
    public static OptionPanel getOptionPanelFromBeacon(Component object)
    {
        System.out.println(""+object.getParent());
        if(object.getParent() instanceof OptionPanel)
        {
            return (OptionPanel)object.getParent();
        }
        return null;
    }
}
