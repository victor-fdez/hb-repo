/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.ui.main.content;

import com.honeybadgers.flltutorial.ui.main.content.utilities.OptionPanel;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.GroupLayout;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
/**
 *
 * @author chingaman
 */
public class ContentPane extends JLayeredPane implements ComponentListener, MouseListener, MouseMotionListener, MouseWheelListener
{
    private Dimension preferedDimension = new Dimension(900, 500);
    private Dimension minDimension = new Dimension(900, 400);
    private Dimension maxDimension = new Dimension(32767, 32767);
    /*
     * this panel covers the whole layered pane, but it will be transparent and
     * it will mainly intercept all mouse and keyboard event to jcomponents on other
     * layers
     */
    JPanel glassPanel;  
    /*
     * this panel contains all the main panels that will contain the activity panels
     * and options panel needed for the user to go throught a stage
     */
    JPanel contentPanel;
    StagePanel stagePanel;
    OptionsPanel optionsPanel;
    /**
     * Class constructor initializes all panels for the first stage of the robot
     * design. Both a stage panel, and options panel create by the stage panel are
     * embedded within a content panel, which is setup in the default layer of 
     * class. Whenever an option is transferred to the stage panel this class will
     * act as an intermediary. All stage panels, and options panels must inherit
     * from the abstract classes StagePanel, and OptionsPanel.
     */
    public ContentPane()
    {
        super();
       
        /* init content panel*/
        //this.contentPanel.setBackground(Color.red);
        this.initComponents();

    }
    /**
     * Private method initializes the layout of the default content panel.
     */
    private void initComponents()
    {
         this.stagePanel = new ProblemDescriptionPanel();
        this.optionsPanel = stagePanel.getOptionsPanel();
        this.contentPanel = new JPanel();
        this.glassPanel = new JPanel();
        /*set view configurations*/
        this.glassPanel.setOpaque(false);
        this.glassPanel.setVisible(true);
        
        this.setPreferredSize(preferedDimension);
        this.setMinimumSize(minDimension);
        this.setMaximumSize(maxDimension);

        GroupLayout contentLayout = new GroupLayout(this.contentPanel);
        this.contentPanel.setLayout(contentLayout);
        
        contentLayout.setHorizontalGroup(
            contentLayout.createSequentialGroup()
                .addComponent(this.stagePanel)
                .addComponent(this.optionsPanel)
            );
        
        contentLayout.setVerticalGroup(
            contentLayout.createParallelGroup()
                .addComponent(this.stagePanel)
                .addComponent(this.optionsPanel)
        );  
        
        this.add(this.contentPanel, JLayeredPane.DEFAULT_LAYER);
        this.add(this.glassPanel, JLayeredPane.DRAG_LAYER);
        /* event listeners defined here*/
        this.glassPanel.addMouseListener(this);
        this.glassPanel.addMouseMotionListener(this);
        this.glassPanel.addMouseWheelListener(this);
        this.addComponentListener(this);
    }
    @Override
    public void paint(Graphics g)
    {        
        /*checking performance*/
        System.out.println("paint called on jlayered pane");
        super.paint(g);
    }
    /**
     * Override of default to string method for debugging purposes.
     */
    @Override
    public String toString()
    {
        return super.toString() + "\n"
                + this.contentPanel + "\n"
                + this.stagePanel + "\n"
                + this.optionsPanel+ "\n"
                + ((OptionsSelectorPanel)this.optionsPanel).selections;
    }
    /*component listener methods*/
    public void componentResized(ComponentEvent e) {
        this.contentPanel.setSize(this.getSize());
        this.glassPanel.setSize(this.getSize());
        this.contentPanel.revalidate();
    }
    public void componentMoved(ComponentEvent e) {}
    public void componentShown(ComponentEvent e) {}
    public void componentHidden(ComponentEvent e) {}
    /*mouse listener methods*/
    public void mouseClicked(MouseEvent e) {
        System.out.println("mouse clicked");
    }
    public void mousePressed(MouseEvent e)
    {    
        Component component = this.contentPanel.getComponentAt(e.getPoint());
        System.out.println("component -> "+component);
        //if
        if(component == this.stagePanel)
        {/*don't do anything, althought this will send points to task diagram */}
        else if(component == this.optionsPanel)
        {
            OptionPanel panel = (OptionPanel) this.optionsPanel.getButtonAt(e.getPoint());
            if(panel != null)
            {
                System.out.println("pressed button "+panel.option.getDescription());
            }
        }
        else //there is only two components this should no happen
        {
            System.out.println("mouse pressed unknown component");
        }
    }
    public void mouseReleased(MouseEvent e) {
        System.out.println("mouse released");
    }
    /* mouse movement listener*/
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {} 
    public void mouseDragged(MouseEvent e) {
        System.out.println("mouse dragged");
    }
    public void mouseMoved(MouseEvent e) {}
    public void mouseWheelMoved(MouseWheelEvent e) {
        Component component = this.contentPanel.getComponentAt(e.getPoint());
        //if
        if(component == this.stagePanel)
        {/*don't do anything, althought this will send points to task diagram */}
        else if(component == this.optionsPanel)
        {
            
                //e.translatePoint(-this.optionsPanel.getX(), -this.optionsPanel.getY());
                //e.setSource(this.optionsPanel.selections);
                this.optionsPanel.selections.dispatchEvent(e);
        }
        else //there is only two components this should no happen
        {
            System.out.println("mouse pressed unknown component");
        }
    }
}
