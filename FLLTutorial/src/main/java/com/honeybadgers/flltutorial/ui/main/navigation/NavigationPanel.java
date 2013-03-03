/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.ui.main.navigation;

import com.honeybadgers.flltutorial.ui.main.content.stages.StagePanel;
import com.honeybadgers.flltutorial.ui.utilities.PanelsScrollPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * NavigationPanel shows a panel with the different stages of a tutorial. As the user
 * goes through the tutorial the navigation panel will show the stage the user is in.
 * 
 * @author chingaman
 */
public class NavigationPanel extends JPanel implements ComponentListener, MouseListener
{
    private Dimension preferedDimension = new Dimension(150, 500);
    private Dimension minDimension = new Dimension(150, 400);
    private Dimension maxDimension = new Dimension(32767, 32767);
    private JPanel videoPanel;
    private PanelsScrollPane scrollPane;
    private ArrayList<StagePanel> stages;
    private float aspectRatio = 1.0f;
    //mapping from text editor to stage panels
    private HashMap stagesMap;
    
    public NavigationPanel(ArrayList<StagePanel> stages)
    {
        super();
        this.stages = stages;
        this.initComponents(); 
    }

    private void initComponents() {
        
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        //TODO: setup video panel
        this.videoPanel = new JPanel();
        this.videoPanel.setBackground(Color.BLACK);
        this.add(this.videoPanel);
        
        this.add(Box.createVerticalStrut(5));
        //initiallized fitted scroll pane for stage panels
        this.scrollPane = new PanelsScrollPane(true);
        this.add(this.scrollPane);
        
            //add all stage panels of application
            for(StagePanel stagePanel : this.stages)
            {
                JPanel stagePanelNav = new JPanel(new GridLayout(1,1));
                stagePanelNav.setLayout(new BorderLayout());
                stagePanelNav.setBorder(new LineBorder(Color.BLACK));
                
                //setup text area inside navigation panels
                JTextArea textArea = new JTextArea();
                textArea.setText(stagePanel.getStageName());
                textArea.setEditable(false);
                textArea.setLineWrap(true);
                textArea.setWrapStyleWord(true);
                textArea.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
                textArea.setBorder(new EmptyBorder(4,4,4,4));
                textArea.setBackground(Color.white);
                textArea.addMouseListener(this);

                
                //creating title for stage
                stagePanelNav.add(textArea);
                //stagePanelNav.addMouseListener(this);

                this.scrollPane.appendPanel(stagePanelNav);
            }
        this.addComponentListener(this);
        this.setPreferredSize(preferedDimension);
        this.setMinimumSize(minDimension);
        this.setMaximumSize(maxDimension);
    }
    
    @Override
    public void componentResized(ComponentEvent e) {
        //resize the video panel accordingly
        int parentWidth = this.getSize().width;
        int parentHeight = this.getSize().height;
        int videoHeight = (int)(((float)parentWidth)/this.aspectRatio);
        float videoToScrollHeightRatio = (((float)videoHeight)/((float)parentHeight));
        //System.out.println("actuall size of window "+this.videoPanel.getSize());
        //System.out.println("resizing this window "+parentWidth+" "+(int)(((float)parentWidth)/this.aspectRatio));
        if(videoToScrollHeightRatio < 0.75f)
        {
            this.videoPanel.setPreferredSize(new Dimension(parentWidth, videoHeight));
            this.scrollPane.setPreferredSize(new Dimension(parentWidth, parentHeight - videoHeight));
            this.revalidate();
        }
        else
        {
            //don't allow the user to resize more than this
        }
    }

    @Override
    public void componentMoved(ComponentEvent e) {}
    @Override
    public void componentShown(ComponentEvent e) {}
    @Override
    public void componentHidden(ComponentEvent e) {}
    @Override
    public void mouseClicked(MouseEvent e) {
        //on click tell application to change stage panel if it is possible
        
    }
    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {
        //System.out.println(e.getSource()+"");
        if(SwingUtilities.isEventDispatchThread())
        {
            if(e.getSource() instanceof JTextArea)
            {
                JTextArea textArea = (JTextArea)e.getSource();
                textArea.setBackground(Color.white.darker());
            }
            else
            {
                System.err.println("NavigationPanel.MouseEntered : source of event is not a JTextArea");
            }
        }
    }
    @Override
    public void mouseExited(MouseEvent e) {
        //System.out.println(e.getSource()+"");
        if(SwingUtilities.isEventDispatchThread())
        {
            if(e.getSource() instanceof JTextArea)
            {
                JTextArea textArea = (JTextArea)e.getSource();
                textArea.setBackground(Color.white);
            }
            else
            {
                System.err.println("NavigationPanel.MouseExited : source of event is not a JTextArea");
            }
        }
    }  
}
