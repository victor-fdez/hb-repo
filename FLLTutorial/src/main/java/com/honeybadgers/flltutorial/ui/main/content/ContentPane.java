/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.ui.main.content;

import com.honeybadgers.flltutorial.ui.main.content.stages.ConsiderationsAndConstraintsPanel;
import com.honeybadgers.flltutorial.ui.main.content.stages.MorphChartPanel;
import com.honeybadgers.flltutorial.ui.main.content.stages.ProblemDescriptionPanel;
import com.honeybadgers.flltutorial.ui.main.content.stages.StagePanel;
import com.honeybadgers.flltutorial.ui.main.content.stages.TaskDiagramPanel;
import com.honeybadgers.flltutorial.ui.main.content.utilities.OptionPanel;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
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
import javax.swing.SwingUtilities;
/**
 *
 * @author chingaman
 */
public class ContentPane extends JLayeredPane implements ComponentListener, MouseListener, MouseMotionListener, MouseWheelListener
{
    private Dimension preferedDimension = new Dimension(750, 500);
    private Dimension minDimension = new Dimension(750, 400);
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
    OptionPanel selectedOptionPanel;
    OptionPanel draggingOptionPanel;
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
       
        //init content panel
        //this.contentPanel.setBackground(Color.red);
        this.initComponents();

    }
    /**
     * Private method initializes the layout of the default content panel.
     */
    private void initComponents()
    {
        //this.stagePanel = new TaskDiagramPanel();
        this.stagePanel = new ProblemDescriptionPanel();
        //this.stagePanel = new MorphChartPanel();
        this.optionsPanel = stagePanel.getOptionsPanel();
        this.contentPanel = new JPanel();
        this.glassPanel = new JPanel();
        /*set view configurations*/
        this.glassPanel.setOpaque(false);
        this.glassPanel.setVisible(true);
        this.glassPanel.setLayout(null);
        
        
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
        //System.out.println("paint called on jlayered pane");
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
                + ((OptionsSelectorPanel)this.optionsPanel);
    }
    /*component listener methods*/
    @Override
    public void componentResized(ComponentEvent e) {
        this.contentPanel.setSize(this.getSize());
        this.glassPanel.setSize(this.getSize());
        this.contentPanel.revalidate();
    }
    @Override
    public void componentMoved(ComponentEvent e) {}
    @Override
    public void componentShown(ComponentEvent e) {}
    @Override
    public void componentHidden(ComponentEvent e) {}
    /*mouse listener methods*/
    @Override
    public void mouseClicked(MouseEvent e) {
        Component component = this.contentPanel.getComponentAt(e.getPoint());
        
        if(component == this.stagePanel)
        {
            this.stagePanel.clicked(e.getPoint()); 
            return;
        }
        this.redispatchEvent(e);
    }
    @Override
    public void mousePressed(MouseEvent e)
    {    
        Component component = this.contentPanel.getComponentAt(e.getPoint());
        //System.out.println("component -> "+component);
        //if
        if(component == this.optionsPanel)
        {
            this.selectedOptionPanel = (OptionPanel) this.optionsPanel.getButtonAt(e.getPoint());
            if(this.selectedOptionPanel != null)
            {
                this.draggingOptionPanel = this.selectedOptionPanel.copy();
                this.draggingOptionPanel.setBounds(this.selectedOptionPanel.getBounds());
                this.draggingOptionPanel.setState(this.selectedOptionPanel.getState());
                //add dragging option panel to glass pane
                //revalidate button after adding it to the glass panel
                this.glassPanel.add(this.draggingOptionPanel);
                this.draggingOptionPanel.revalidate();
                //set option panel as hidden
                this.selectedOptionPanel.setState(OptionPanel.OptionState.HIDDEN_OCCUPY);
                //draw panel initially as is selected
                int halfWidth = this.draggingOptionPanel.getWidth()/2;
                int halfHeight = this.draggingOptionPanel.getHeight()/2;
                this.draggingOptionPanel.setBounds(e.getX()-halfWidth, e.getY()-halfHeight, this.draggingOptionPanel.getSize().width, this.draggingOptionPanel.getSize().height);
                this.glassPanel.repaint(this.draggingOptionPanel.getVisibleRect());
                return;
            }
        }
        this.redispatchEvent(e);

    }
    @Override
    public void mouseReleased(MouseEvent e) {
        /*check if there is a selectoption to drop*/
        if(this.selectedOptionPanel != null && this.draggingOptionPanel != null)
        {
            Component component = this.contentPanel.getComponentAt(e.getPoint());
            //Rectangle rect = this.draggingOptionPanel.getVisibleRect();
            this.glassPanel.remove(this.draggingOptionPanel);
            this.glassPanel.repaint();
            if(component == this.stagePanel)
            {/*don't do anything, althought this will send points to stage panel*/
                int status = this.stagePanel.dropOptionPanel(this.draggingOptionPanel);
                System.out.println("dropping on stage panel "+status);
                switch(status)
                {
                    case 0:
                        this.selectedOptionPanel.setState(OptionPanel.OptionState.CORRECT);
                        break;
                    case 1:
                        this.selectedOptionPanel.setState(OptionPanel.OptionState.INCORRECT);
                        break;
                    case 2:
                        this.selectedOptionPanel.setState(this.draggingOptionPanel.getState());
                        break;
                    case 3:
                        this.selectedOptionPanel.setState(OptionPanel.OptionState.FINISHED);
                    default:
                        System.err.println("Content Pane - this should not happen");
                        break;
                }
            }
            else
            {
                this.selectedOptionPanel.setState(this.draggingOptionPanel.getState());
            }
            this.draggingOptionPanel = null;
            this.selectedOptionPanel = null;
            return;
        }
        this.redispatchEvent(e);
        //System.out.println("mouse released");
    }
    /* mouse movement listener*/
    @Override
    public void mouseEntered(MouseEvent e) {
        this.redispatchEvent(e);
    }
    /*TODO:
     * remember last component that was entered so that it may be exited
     * 
     * -scroll bar show highlight effect after they have been exited
     * -other problems
     */
    @Override
    public void mouseExited(MouseEvent e) {
        this.redispatchEvent(e);
    } 
    @Override
    public void mouseDragged(MouseEvent e) 
    {
        if(this.selectedOptionPanel != null)
        {
            //System.out.println("dragging panel "+this.draggingOptionPanel+" children "+this.draggingOptionPanel.getComponents());
            //System.out.println("x: "+e.getX()+" y: "+e.getY());
            int halfWidth = this.draggingOptionPanel.getWidth()/2;
            int halfHeight = this.draggingOptionPanel.getHeight()/2;
            this.draggingOptionPanel.setBounds(e.getX()-halfWidth, e.getY()-halfHeight, this.draggingOptionPanel.getSize().width, this.draggingOptionPanel.getSize().height);            
            this.glassPanel.repaint(this.draggingOptionPanel.getVisibleRect());
        }
        else
        {
            this.redispatchEvent(e);
        }
        //System.out.println("mouse dragged");
    }
    @Override
    public void mouseMoved(MouseEvent e) {
        this.redispatchEvent(e);
    }
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        //Component component = this.contentPanel.getComponentAt(e.getPoint());
        this.redispatchEvent(e); 
    }
    private void redispatchEvent(MouseEvent e)
    {
         Component component = SwingUtilities.getDeepestComponentAt(this.contentPanel, e.getX(), e.getY());
         if(component != null){
            e.setSource(component);
            Point point = e.getPoint();
            Point newPoint = SwingUtilities.convertPoint(this.contentPanel, e.getPoint(), component);
            e.translatePoint(-((int)point.getX()), -((int)point.getY()));
            e.translatePoint(((int)newPoint.getX()), ((int)newPoint.getY()));
            component.dispatchEvent(e);
            System.out.println(e+"");
            System.out.println(""+component);
            //component.dispatchEvent(e);
         }
    }
}
