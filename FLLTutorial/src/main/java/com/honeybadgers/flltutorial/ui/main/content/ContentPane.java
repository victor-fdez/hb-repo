/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.ui.main.content;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.GroupLayout;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
/**
 *
 * @author chingaman
 */
public class ContentPane extends JLayeredPane
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
    ContentMouseAdapter contentMouseAdapter;
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
        this.stagePanel = new ProblemDescriptionPanel();
        this.optionsPanel = stagePanel.getOptionsPanel();
        this.contentPanel = new JPanel();
        this.glassPanel = new JPanel();
        /* init content panel*/
        //this.contentPanel.setBackground(Color.red);
        this.initComponents();
        this.add(this.contentPanel, JLayeredPane.DEFAULT_LAYER);
        this.contentMouseAdapter = new ContentMouseAdapter();
        this.contentPanel.addMouseListener(this.contentMouseAdapter);
        this.contentPanel.addMouseMotionListener(this.contentMouseAdapter);
        this.setBackground(Color.BLACK);
        this.setOpaque(true);
        this.setVisible(true);
        //
        Toolkit.getDefaultToolkit();
    }
    /**
     * Private method initializes the layout of the default content panel.
     */
    private void initComponents()
    {
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
    }
    @Override
    public void paint(Graphics g)
    {
        this.contentPanel.setSize(this.getSize());
        this.glassPanel.setSize(this.getSize());
        // this seems to work this.contentPanel.validate();
        this.contentPanel.validate();
        
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
    private class ContentMouseAdapter extends MouseAdapter{
        @Override
        public void mousePressed(MouseEvent mouseEvent)
        {
            System.out.println("mouse pressed");
        }
        @Override
        public void mouseDragged(MouseEvent mouseEvent)
        {
            System.out.println("mouse dragged");
        }
        @Override
        public void mouseReleased(MouseEvent mouseEvent)
        {
            System.out.println("mouse released");
        }
    }
}
