/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.ui.main.content.utilities;

import com.honeybadgers.flltutorial.model.Option;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author chingaman
 */
public class OptionPanel extends JPanel implements Cloneable
{
    public static enum OptionState
    {
        NORMAL, HIDDEN_OCCUPY, DRAGGED, DROPPED, UNOCCUPIED, CORRECT, INCORRECT, FINISHED
    }
    protected OptionState state;
    protected Option option;
    private JTextArea description;
    //JLabel description;
    public OptionPanel()
    {
        super();
        this.state = OptionState.UNOCCUPIED;
        this.initComponents();
        this.setOpaque(true);
    }
    public OptionPanel(Option option)
    {
        super();
        this.option = option;
        this.state = OptionState.NORMAL;
        if(this.getClass() == OptionPanel.class)
        {   
            this.initComponents();
        }
        this.setState(OptionState.NORMAL);
    }
    private void initComponents()
    {
        try
        {
            if(this.option == null)
            {
                this.description = new JTextArea("");
                //this.description.getDocument().insertString(0, "", null);
            }
            else{
                this.description = new JTextArea(this.option.getDescription());
                //this.description.getDocument().insertString(0, this.option.getDescription(), null);
            }
        }
        catch(Exception e)
        {
            System.err.println("Exception");
        }
        //this.description = new JLabel();
        this.setLayout(new BorderLayout());
        this.description.setBackground(new Color(0,0,0,0));
        this.description.setBorder(new EmptyBorder(2,2,2,2));
        this.description.setEditable(false);
        this.description.setLineWrap(true);
        this.description.setWrapStyleWord(true);
        this.description.setVisible(true);
        
        //this.setBorder(new LineBorder(Color.BLACK, 1,));
    }
    //TODO: create methods that will draw a button under different STATES
    public OptionPanel copy()
    {
            OptionPanel optionPanel = new OptionPanel(this.option);
            return optionPanel;
    }
    /**
     * This object method transfers in a give option, and stores it. It may not show
     * the option immediately, unless the state of the option panel is NORMAL.
     * 
     * @param option the Option object that will be stored in this OptionPanel
     */
    public void transferOption(Option option)
    {
        this.option = option;
        if(this.description != null)
        {
            //try
            //{this.description.getDocument().insertString(0, this.option.getDescription(), null);}
            //catch(Exception e){System.err.println("OptionPanel - exception");}
            this.description.setText(option.getDescription());
            if(this.description.getParent() != null)
            {
                this.description.revalidate();
            }
        }
    }
    /**
     * This object method sets the state of this OptionPanel. It is mainly used for display
     * purposes.
     * 
     * @param state the enumerate type OptionPanel.OptionState which denotes the state of the panel.
     */
    public void setState(OptionState state)
    {
        this.state = state;
        switch(this.state)
        {
            case NORMAL: /*beginning state*/
                if(this.description != null && this.description.getParent() != this)
                {
                    this.description.setVisible(true);
                    this.add(this.description);
                }
                this.setOpaque(true);
                this.setBackground(Color.YELLOW);
                this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                break;
            case HIDDEN_OCCUPY:
                this.setBorder(null);
                this.remove(this.description);
                this.setBackground(Color.GRAY);
                this.setBorder(null);
                break;
            case UNOCCUPIED:
                if(this.description != null && this.description.getParent() == this)
                {
                    this.remove(this.description);
                }
                this.setBackground(Color.LIGHT_GRAY);
                this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            case CORRECT:
                if(this.description != null && this.description.getParent() != this)
                {
                    this.description.setVisible(true);
                    this.add(this.description);
                }
                this.setOpaque(true);
                this.setBackground(Color.GREEN);
                this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                break;
            case INCORRECT:
                if(this.description != null && this.description.getParent() != this)
                {
                    this.description.setVisible(true);
                    this.add(this.description);
                }
                this.setOpaque(true);
                this.setBackground(Color.RED);
                this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                break;
           case FINISHED:
                if(this.description != null && this.description.getParent() != this)
                {
                    this.description.setVisible(true);
                    this.add(this.description);
                }
                this.setOpaque(true);
                this.setBackground(Color.GREEN.darker());
                this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                break;
            default:
                break;
        }
    }
  
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
        return this.description;
    }
    /**
     * Returns the corresponding option panel from the input beacon.
     * 
     * @param object
     * @return 
     */
    public static OptionPanel getOptionPanelFromBeacon(Component object)
    {
        if(object.getClass() == JTextArea.class)
        {
            System.out.println("Option Panel Beacon");
            JTextArea textArea = (JTextArea)object;
            if(textArea.getParent() instanceof OptionPanel)
            {
                return (OptionPanel)textArea.getParent();
            }
            return null;
        }
        else
        {
            System.out.println("Picture Option Panel Beacon");
            return PictureOptionPanel.getOptionPanelFromBeaconSpecific(object);
        }
    }
}
