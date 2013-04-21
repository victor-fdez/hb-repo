/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.ui.main.content;

import com.honeybadgers.flltutorial.model.backend.TutorialManager;
import com.honeybadgers.flltutorial.ui.FLLTutorialUI;
import com.honeybadgers.flltutorial.ui.main.content.stages.StagePanel;
import com.honeybadgers.flltutorial.ui.main.content.utilities.OptionPanel;
import com.honeybadgers.flltutorial.ui.main.content.utilities.OptionPanel.OptionState;
import com.honeybadgers.flltutorial.ui.main.content.utilities.PictureOptionPanel;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
/**
 *
 * @author chingaman
 */
public class ContentPane extends JLayeredPane implements ComponentListener, MouseListener, MouseMotionListener, MouseWheelListener
{
    private Dimension preferedDimension = new Dimension(550, 500);
    private Dimension minDimension = new Dimension(550, 400);
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
    PanelReceiver receiver;
    boolean drawClose = false;
    private BufferedImage image;
    private JTextArea missionLabel;
    private JTextArea missionTitleLabel;
    private JScrollPane missionPane;
    /**
     * Class constructor initializes all panels for the first stage of the robot
     * design. Both a stage panel, and options panel create by the stage panel are
     * embedded within a content panel, which is setup in the default layer of 
     * class. Whenever an option is transferred to the stage panel this class will
     * act as an intermediary. All stage panels, and options panels must inherit
     * from the abstract classes StagePanel, and OptionsPanel.
     */
    public ContentPane(PanelReceiver receiver, StagePanel stage)
    {
        super();
        this.setBackground(Color.GRAY);
        //init content panel
        this.messageTimer = new Timer(2000, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                clickedBlocked = false;
                drawClose = true;
                SwingUtilities.invokeLater(new Runnable(){  
                    @Override
                    public void run() {
                        glassPanel.repaint();
                    }
                });
                System.out.println("timer fired");
            }
        });
        //get the close image
        try {
                this.image = ImageIO.read(new File(TutorialManager.generalMediaPath+"close.png"));
                
        } catch (IOException ex) {
            Logger.getLogger(PictureOptionPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.receiver = receiver;
        this.messageTimer.setRepeats(false);
        this.stagePanel = stage;
        
        //setup the misson title on top of each stage
        this.missionTitleLabel = new JTextArea("Mission");
        Font titleFont = this.missionTitleLabel.getFont();
        titleFont = titleFont.deriveFont(titleFont.getStyle() | Font.BOLD);
        titleFont = titleFont.deriveFont(titleFont.getSize() + 3.0f);
        this.missionTitleLabel.setFont(titleFont);
        this.missionTitleLabel.setLineWrap(true);
        this.missionTitleLabel.setWrapStyleWord(true);
        this.missionTitleLabel.setBorder(new EmptyBorder(4, 4, 4, 4));
        this.missionTitleLabel.setBackground(Color.GRAY);
        this.missionTitleLabel.setMinimumSize(new Dimension(StagePanel.minDimension.width, 30));
        this.missionTitleLabel.setPreferredSize(new Dimension(StagePanel.preferedDimension.width, 30));
        this.missionTitleLabel.setMaximumSize(new Dimension(StagePanel.maxDimension.width, 30));

        //setup the mission statement on top of each stage
        this.missionLabel = new JTextArea(FLLTutorialUI.currentTutorial.getMission());
        this.missionLabel.setLineWrap(true);
        this.missionLabel.setWrapStyleWord(true);
        this.missionLabel.setBackground(Color.GRAY.brighter());
        
        this.missionPane = new JScrollPane(this.missionLabel);
        this.missionPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.missionLabel.setBorder(new EmptyBorder(2, 4, 4, 6));
        this.missionPane.setMinimumSize(new Dimension(StagePanel.minDimension.width, 70));
        this.missionPane.setPreferredSize(new Dimension(StagePanel.preferedDimension.width, 70));
        this.missionPane.setMaximumSize(new Dimension(StagePanel.maxDimension.width,100));
        
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
                //System.out.println("painted glass panel");
                if(eventsBlocked)
                {
                    Rectangle rect = g.getClipBounds();

                    AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.85f);
                    Graphics2D g2 = (Graphics2D)g;

                    g2.setComposite(alpha);
                    g2.setColor(Color.BLACK);
                    g2.fillRect(rect.x, rect.y, rect.width, rect.height);
                    if(drawClose)
                     {
                        float scale = 0.5f;
                        AffineTransform transform = new AffineTransform();
                        
                        //get top position of text area
                        int x = moralityScrollPane.getX();
                        int y = moralityScrollPane.getY();
                        
                        transform.translate(x+(10*scale), y-((image.getHeight()+20)*scale));
                        transform.scale(scale, scale);
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
                        g2.drawImage(image, transform, this);
                        
                        //g2.drawImage(image, x, y-image.getHeight(), this);
                        //g2.dispose();
                     }
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
        this.contentPanel.setBackground(Color.GRAY);
        //stage title setup
        JTextArea stageTitleLabel = new JTextArea(this.stagePanel.getStageName());
        Font titleFont = stageTitleLabel.getFont();
        titleFont = titleFont.deriveFont(titleFont.getStyle() | Font.BOLD);
        titleFont = titleFont.deriveFont(titleFont.getSize() + 3.0f);
        stageTitleLabel.setFont(titleFont);
        stageTitleLabel.setLineWrap(true);
        stageTitleLabel.setWrapStyleWord(true);
        stageTitleLabel.setBorder(new EmptyBorder(4, 4, 0, 4));
        stageTitleLabel.setBackground(Color.GRAY);
        stageTitleLabel.setMinimumSize(new Dimension(StagePanel.minDimension.width, 30));
        stageTitleLabel.setPreferredSize(new Dimension(StagePanel.preferedDimension.width, 30));
        stageTitleLabel.setMaximumSize(new Dimension(StagePanel.maxDimension.width, 30));
                        
        contentLayout.setHorizontalGroup(
                contentLayout.createSequentialGroup()
                .addGroup(
                    contentLayout.createParallelGroup()
                        .addComponent(this.missionTitleLabel)
                        .addComponent(this.missionPane)
                        .addComponent(stageTitleLabel)
                        .addComponent(this.stagePanel))
                .addComponent(this.optionsPanel));
        
        contentLayout.setVerticalGroup(
                contentLayout.createParallelGroup()
                    .addGroup(
                        contentLayout.createSequentialGroup()
                            .addComponent(this.missionTitleLabel)
                            .addComponent(this.missionPane)
                            .addComponent(stageTitleLabel)
                            .addComponent(this.stagePanel))
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
        //System.out.println("painted content panel");
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
    public void componentMoved(ComponentEvent e) {
    }
    @Override
    public void componentShown(ComponentEvent e) {}
    @Override
    public void componentHidden(ComponentEvent e) {}
    /*mouse listener methods*/
    @Override
    public void mouseClicked(MouseEvent e) {
        //System.out.println("is EDT in clicked"+SwingUtilities.isEventDispatchThread());
        if(clickedBlocked)
        {
            return;
        }
        else
        {
            this.glassPanel.removeAll();
            this.repaint();
            this.eventsBlocked = false;
            this.drawClose = false;
        }
        this.redispatchEvent(e);
    }
    @Override
    public void mousePressed(MouseEvent e)
    {    
        //System.out.println("is EDT in pressed"+SwingUtilities.isEventDispatchThread());
        if(eventsBlocked)
            return;
        if(this.selectedOptionPanel != null && this.draggingOptionPanel != null)
        {
            //this may happen if the dropped methods is not called
            this.selectedOptionPanel.setState(this.draggingOptionState);
            this.selectedOptionPanel = null;
            this.draggingOptionPanel = null;
            this.draggingOptionState = null;
        }
        Component component = this.contentPanel.getComponentAt(e.getPoint());
        if(component == this.optionsPanel)
        {
            this.selectedOptionPanel = (OptionPanel) this.optionsPanel.getButtonAt(e.getPoint());
            if(this.selectedOptionPanel != null)
            {
                this.draggingOptionPanel = this.selectedOptionPanel.copy();
                this.draggingOptionPanel.setBounds(this.selectedOptionPanel.getBounds());
                //this.draggingOptionPanel.setState(this.selectedOptionPanel.getState());
                this.draggingOptionState = this.selectedOptionPanel.getState();
                this.draggingOptionPanel.setState(draggingOptionState);
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
                //this.glassPanel.paintImmediately(this.draggingOptionPanel.getVisibleRect());
                Rectangle vR = this.draggingOptionPanel.getVisibleRect();
                this.glassPanel.repaint(0, vR.y-2 , this.glassPanel.getWidth(), vR.height+4);
                this.glassPanel.repaint(vR.x-2, 0 , vR.width+4, this.glassPanel.getHeight());
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
        //System.out.println("is EDT in released"+SwingUtilities.isEventDispatchThread());
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
                        if(this.stagePanel.isFinished())
                        {
                            //this.stagePanel.printTracker();
                            this.receiver.receivePanel(null, null);
                        }
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
                this.selectedOptionPanel.setState(this.draggingOptionState);
                this.selectedOptionPanel.repaint();
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
        moralityMessage.setWrapStyleWord(true);
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
        
        //System.out.println("height: "+this.contentPanel.getSize()+"\n"+this.selectedOptionPanel.getOption().getReason());
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
            this.draggingOptionPanel.hasMoved();
            Rectangle vR = this.draggingOptionPanel.getVisibleRect();
            this.glassPanel.repaint(0, vR.y-2 , this.glassPanel.getWidth(), vR.height+4);
            this.glassPanel.repaint(vR.x-2, 0 , vR.width+4, this.glassPanel.getHeight());
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
        
        if(this.selectedOptionPanel != null && this.draggingOptionPanel != null)
        {
            //this may happen if the dropped methods is not called
            this.selectedOptionPanel.setState(this.draggingOptionState);
            this.selectedOptionPanel = null;
            this.draggingOptionPanel = null;
            this.draggingOptionState = null;
        }
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
