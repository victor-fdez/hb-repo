/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.ui.main.navigation;

import com.honeybadgers.flltutorial.ui.main.content.stages.StagePanel;
import com.honeybadgers.flltutorial.ui.utilities.PanelsScrollPane;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

/**
 * NavigationPanel shows a panel with the different stages of a tutorial. As the user
 * goes through the tutorial the navigation panel will show the stage the user is in.
 * 
 * @author chingaman
 */
public class NavigationPanel extends JPanel implements ComponentListener
{
    private Dimension preferedDimension = new Dimension(150, 500);
    private Dimension minDimension = new Dimension(150, 400);
    private Dimension maxDimension = new Dimension(32767, 32767);
    private JPanel videoPanel;
    private PanelsScrollPane scrollPane;
    private ArrayList<StagePanel> stages;
    private float aspectRatio = 1.0f;
    //private HashMap stagesMap;
    
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
        this.scrollPane = new PanelsScrollPane();
        this.add(this.scrollPane);
        
            //add all stage panels of application
            for(StagePanel stagePanel : this.stages)
            {
                JPanel stagePanelNav = new JPanel(new GridLayout(1,1));
                
                //creating title for stage
                JEditorPane stageTitle = new JEditorPane();
                //JTextPane stageTitle = new JTextPane();
                stageTitle.setBorder(null);
                stageTitle.setContentType("text/html");
                stageTitle.setEditable(false);
                HTMLEditorKit editorKit = (HTMLEditorKit)stageTitle.getEditorKit();
                StyleSheet styleSheet = editorKit.getStyleSheet();
                styleSheet.addRule("html{background: blue; padding: 3px}");
                editorKit.setStyleSheet(styleSheet);
                stageTitle.setEditorKit(editorKit);
                stageTitle.setText("<html><body><h2>"+(stagePanel.getStageName())+"</h2></body></html>");
                
                stagePanelNav.add(stageTitle);
                stagePanelNav.setOpaque(true);
                stagePanelNav.setBackground(Color.RED);
                //add each stage title panel to the navigation panel
                this.scrollPane.appendPanel(stagePanelNav);
            }
        this.addComponentListener(this);
        this.setPreferredSize(preferedDimension);
        this.setMinimumSize(minDimension);
        this.setMaximumSize(maxDimension);
    }
    
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

    public void componentMoved(ComponentEvent e) {
    }

    public void componentShown(ComponentEvent e) {
    }

    public void componentHidden(ComponentEvent e) {
    }
}
