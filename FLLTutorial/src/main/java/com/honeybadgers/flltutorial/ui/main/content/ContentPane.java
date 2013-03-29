/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.ui.main.content;

import com.honeybadgers.flltutorial.ui.main.content.stages.StagePanel;
import com.honeybadgers.flltutorial.ui.main.content.utilities.OptionPanel;
import com.honeybadgers.flltutorial.ui.main.content.utilities.OptionPanel.OptionState;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
/**
 *
 * @author chingaman
 */
public class ContentPane extends JLayeredPane implements ComponentListener, MouseListener, MouseMotionListener, MouseWheelListener
{
    private Dimension preferedDimension = new Dimension(750, 500);
    private Dimension minDimension = new Dimension(750, 400);
    private Dimension maxDimension = new Dimension(32767, 32767);
    private OptionState draggingOptionState;
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
    public ContentPane(StagePanel stage)
    {
        super();
       
        //init content panel
        //this.contentPanel.setBackground(Color.red);
        this.messageTimer = new Timer(2000, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                clickedBlocked = false;
                System.out.println("timer fired");
            }
        });
        this.messageTimer.setRepeats(false);
        this.stagePanel = stage;
        this.initComponents();
    }
    /**
     * Private method initializes the layout of the default content panel.
     */
    private void initComponents()
    {
        this.optionsPanel = stagePanel.getOptionsPanel();
        this.contentPanel = new JPanel();
        this.glassPanel = new JPanel(){
            @Override
            public void paintComponent(Graphics g)
            {        
                System.out.println("painted glass panel");
                if(eventsBlocked)
                {
                    Rectangle rect = g.getClipBounds();

                    AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.85f);
                    Graphics2D g2 = (Graphics2D)g;

                    g2.setComposite(alpha);
                    //later change to gradient
                    g2.setColor(Color.BLACK);
                    g2.fillRect(rect.x, rect.y, rect.width, rect.height);
                    super.paintComponent(g);

                }
                else
                {
                    super.paintComponent(g);
                }
            }
        };
        /*set view configurations*/
        this.glassPanel.setOpaque(false);
        this.glassPanel.setLayout(null);
        
        this.setPreferredSize(preferedDimension);
        this.setMinimumSize(minDimension);
        this.setMaximumSize(maxDimension);

        this.add(this.contentPanel, JLayeredPane.DEFAULT_LAYER);
        this.add(this.glassPanel, JLayeredPane.DRAG_LAYER);
        
        this.addStagePanelContent();
        
    }
    public void updateStagePanel(StagePanel newStagePanel)
    {
        this.stagePanel = newStagePanel;
        this.optionsPanel = newStagePanel.getOptionsPanel();
        this.removeStagePanelContent();
        this.addStagePanelContent();
    }
    private void removeStagePanelContent(){
        //stop all listeners while removing panels
        this.removeComponentListener(this);
        
        this.glassPanel.removeMouseListener(this);
        this.glassPanel.removeMouseListener(this);
        this.glassPanel.removeMouseMotionListener(this);
        
        //remove all panels from content panel
        this.contentPanel.removeAll();
        
        //clear up the previous layout
        this.contentPanel.setLayout(null);
        
        
    }
    private void addStagePanelContent(){
        
        //setup the layouts for this content panel
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
        
        this.revalidate();

        //add all listeners
        this.glassPanel.addMouseListener(this);
        this.glassPanel.addMouseMotionListener(this);
        this.glassPanel.addMouseWheelListener(this);
        this.addComponentListener(this);
        
    }
    @Override
    public void paintComponent(Graphics g)
    {        
        System.out.println("painted content panel");
        super.paintComponent(g);
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
        if(clickedBlocked)
        {
            return;
        }
        else
        {
            this.glassPanel.removeAll();
            this.repaint();
            this.eventsBlocked = false;
        }
        this.redispatchEvent(e);
    }
    @Override
    public void mousePressed(MouseEvent e)
    {    
        if(eventsBlocked)
            return;
        Component component = this.contentPanel.getComponentAt(e.getPoint());
        //System.out.println("component -> "+component);

        //System.out.println(""+e.getSource());
        if(component == this.optionsPanel)
        {
            this.selectedOptionPanel = (OptionPanel) this.optionsPanel.getButtonAt(e.getPoint());
            if(this.selectedOptionPanel != null)
            {
                this.draggingOptionPanel = this.selectedOptionPanel.copy();
                this.draggingOptionPanel.setBounds(this.selectedOptionPanel.getBounds());
                //this.draggingOptionPanel.setState(this.selectedOptionPanel.getState());
                this.draggingOptionState = this.selectedOptionPanel.getState();
                this.draggingOptionPanel.setState(OptionState.TRANSPARENT);
                //may need to repaint the dragging option panel
                
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
                this.glassPanel.paintImmediately(this.draggingOptionPanel.getVisibleRect());
                return;
            }
        }
        this.redispatchEvent(e);

    }
 
    private Timer messageTimer;
    private boolean clickedBlocked = false;
    private boolean eventsBlocked = false;
    @Override
    public void mouseReleased(MouseEvent e) {
        if(eventsBlocked)
            return;
        /*check if there is a selectoption to drop*/
        if(this.selectedOptionPanel != null && this.draggingOptionPanel != null)
        {
            Component component = this.contentPanel.getComponentAt(e.getPoint());
            //Rectangle rect = this.draggingOptionPanel.getVisibleRect();
            this.glassPanel.remove(this.draggingOptionPanel);
            this.glassPanel.repaint();
            this.draggingOptionPanel.setState(this.draggingOptionState);
            if(component == this.stagePanel)
            {/*don't do anything, althought this will send points to stage panel*/
                int status = this.stagePanel.dropOptionPanel(this.draggingOptionPanel);
                //System.out.println("dropping on stage panel "+status);
                switch(status)
                {
                    case 0:
                        this.selectedOptionPanel.setState(OptionPanel.OptionState.CORRECT);
                        break;
                    case 1:
                        this.selectedOptionPanel.setState(OptionPanel.OptionState.INCORRECT);
                        //tell 
                        this.eventsBlocked = true;
                        this.clickedBlocked = true;
                        this.showMessageInGlassPanel();
                        this.repaint();
                        this.messageTimer.start();
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
    
    
    JScrollPane moralityScrollPane;
    private void showMessageInGlassPanel()
    {
        JTextArea moralityMessage = new JTextArea(this.selectedOptionPanel.getOption().getReason(), 5, 40);
        moralityMessage.setLineWrap(true);
        moralityMessage.setEditable(false);
        moralityMessage.setFocusable(false);
        moralityMessage.setRequestFocusEnabled(false);
        
        moralityScrollPane = new JScrollPane(moralityMessage);
        int halfWidth = this.glassPanel.getWidth()/2;
        int halfHeight = this.glassPanel.getHeight()/2;
        int width = 300;
        int height = 100;
        this.moralityScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.moralityScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        moralityScrollPane.setBounds(halfWidth-(width/2), halfHeight-(height/2), width, height);
        
        System.out.println("height: "+this.contentPanel.getSize()+"\n"+this.selectedOptionPanel.getOption().getReason());
        this.glassPanel.add(this.moralityScrollPane);
        this.glassPanel.invalidate();
    }
    /* mouse movement listener*/
    @Override
    public void mouseEntered(MouseEvent e) {
        //useless
        //this.redispatchEvent(e);
    }
    @Override
    public void mouseExited(MouseEvent e) {
        //useless
        //this.redispatchEvent(e);
    }
    @Override
    public void mouseDragged(MouseEvent e) 
    {
        if(eventsBlocked)
            return;
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
    }
    private Component lastMouseMovedComponent;
    @Override
    public void mouseMoved(MouseEvent e) {
        if(eventsBlocked)
            return;
        //System.out.println("moved ");
        Component component = SwingUtilities.getDeepestComponentAt(this.contentPanel, e.getX(), e.getY());
        
        if(lastMouseMovedComponent == null)
        {
            this.redispatchEvent(e, component);
            lastMouseMovedComponent = component;
            return;
        }
        
        if(lastMouseMovedComponent != component)
        {
            Point exitPoint = e.getPoint();
            //System.out.println("last component global coordinates "+exitPoint);
            exitPoint.setLocation(SwingUtilities.convertPoint(this.contentPanel, exitPoint, lastMouseMovedComponent));      
            //System.out.println("last component local coordinates "+exitPoint);
            MouseEvent exitEvent = new MouseEvent(lastMouseMovedComponent, MouseEvent.MOUSE_EXITED, e.getWhen(), e.getModifiers(), exitPoint.x, exitPoint.y, e.getClickCount(), e.isPopupTrigger());
            lastMouseMovedComponent.dispatchEvent(exitEvent);
        }
        this.redispatchEvent(e, component);
        lastMouseMovedComponent = component;
    }
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if(eventsBlocked)
            return;
        this.redispatchEvent(e); 
    }
    /**
     * Redispatches an event received events on the glass panel, to components behind it.
     * 
     * @param e  event to redispatch
     */
    private void redispatchEvent(MouseEvent e)
    {
          Component component = SwingUtilities.getDeepestComponentAt(this.contentPanel, e.getX(), e.getY());
          this.redispatchEvent(e, component);
    }
    
    private void redispatchEvent(MouseEvent e, Component component)
    {
         if(component != null){
            e.setSource(component);
            Point point = e.getPoint();
            Point newPoint = SwingUtilities.convertPoint(this.contentPanel, e.getPoint(), component);
            e.translatePoint(-((int)point.getX()), -((int)point.getY()));
            e.translatePoint(((int)newPoint.getX()), ((int)newPoint.getY()));
            component.dispatchEvent(e);
         }
    }
}
