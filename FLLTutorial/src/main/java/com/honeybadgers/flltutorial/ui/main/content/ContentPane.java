/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.ui.main.content;

import java.awt.Color;
import java.awt.Graphics;
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
        stagePanel = new ProblemDescriptionPanel();
        optionsPanel = stagePanel.getOptionsPanel();
        contentPanel = new JPanel();
        /* init content panel*/
        this.contentPanel.setBackground(Color.red);
        this.initContentPanel();
        this.add(this.contentPanel, JLayeredPane.DEFAULT_LAYER);
        this.contentMouseAdapter = new ContentMouseAdapter();
        this.contentPanel.addMouseListener(this.contentMouseAdapter);
        this.contentPanel.addMouseMotionListener(this.contentMouseAdapter);
        this.setBackground(Color.BLACK);
        this.setOpaque(true);
        this.setVisible(true);
    }
    /**
     * Private method initializes the layout of the default content panel.
     */
    private void initContentPanel()
    {
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
        
        this.contentPanel.invalidate();
        //((GroupLayout)this.contentPanel.getLayout()).invalidateLayout(this.contentPanel);
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
                + this.optionsPanel;
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
