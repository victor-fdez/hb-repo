/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.ui.utilities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

/**
 * This class displays a list of JPanels, with all of the aligned to the top of the
 * ScrollPane viewport. Every time a panel is added it is appended behind the last
 * panel.
 * 
 * @author chingaman
 */
public class PanelsScrollPane extends JScrollPane{
    protected int numberOfPanels;
    protected FittedViewportPanel fittedViewportPanel;
    protected JPanel glue;
    protected GridBagConstraints glueConstraints;
    protected ArrayList<JPanel> panelsList;
    protected boolean isVertical;
    public PanelsScrollPane(boolean isVertical)
    {
        super();
        //initialize panel scroll pane
        this.isVertical = isVertical;
        this.numberOfPanels = 0;
        this.initComponents();
    }
    private void initComponents()
    {
        this.fittedViewportPanel = new FittedViewportPanel(this);
        this.fittedViewportPanel.setLayout(new GridBagLayout());
        this.fittedViewportPanel.setBackground(Color.GRAY);
        
        //add glue at the end of fitted viewport panel
        this.glueConstraints = new GridBagConstraints();
        this.glueConstraints.weightx = 1.0;
        this.glueConstraints.weighty = 1.0;
        this.glueConstraints.fill = GridBagConstraints.BOTH;
        if(this.isVertical){
            this.glueConstraints.gridx = 0;
            this.glueConstraints.gridy = this.numberOfPanels;
            this.glueConstraints.anchor = GridBagConstraints.PAGE_START;
        }else{
            this.glueConstraints.gridx = this.numberOfPanels;
            this.glueConstraints.gridy = 0;
            this.glueConstraints.anchor = GridBagConstraints.LINE_START;
        }
        this.glueConstraints.insets = new Insets(2,2,2,2);
        this.glue = new JPanel();
        this.glue.setBackground(Color.GRAY);
        this.fittedViewportPanel.add(this.glue, this.glueConstraints);
        
        
        //add fitted panel to viewport
        this.setViewportView(this.fittedViewportPanel);
    }
    /**
     * Appends the input panel behind the last panel on the list.
     * 
     * @param appendedPanel 
     */
    public void appendPanel(JPanel appendedPanel)
    {
        //remove glue from fitted panel
        this.fittedViewportPanel.remove(this.glue);
        
        //create new constraints for new panel
        GridBagConstraints constraint = new GridBagConstraints();
        if(this.isVertical){
            constraint.weightx = 1.0;
            constraint.weighty = 0.0;
            constraint.fill = GridBagConstraints.HORIZONTAL;
            constraint.anchor = GridBagConstraints.PAGE_START;
            constraint.gridx = 0;
            constraint.gridy = this.numberOfPanels++;
        }else{
            constraint.weightx = 0.0;
            constraint.weighty = 1.0;
            constraint.fill = GridBagConstraints.VERTICAL;
            constraint.anchor = GridBagConstraints.LINE_START;
            constraint.gridx = this.numberOfPanels++;
            constraint.gridy = 0;
        }
        constraint.insets = new Insets(2,2,2,2);
        appendedPanel.setOpaque(true);
        this.fittedViewportPanel.add(appendedPanel, constraint);
        
        //update glue constraints and add glue at the end of panel list
        if(this.isVertical){
            this.glueConstraints.gridy = this.numberOfPanels;
        }else{
            this.glueConstraints.gridx = this.numberOfPanels;
        }
        this.fittedViewportPanel.add(this.glue, this.glueConstraints);
        
        //revalidate layout of scroll pane
        this.revalidate();
        this.repaint();
    }
    /**
     * Removes the panel located at index.
     * 
     * @param index 
     */
    public void removePanel(int index)
    {
        
    }
    /**
     *  
     */
    public void removeAllPanels()
    {
        this.numberOfPanels = 0;
        
        this.fittedViewportPanel.removeAll();
        
        //setup the glue again
        if(this.isVertical)
        {
            this.glueConstraints.gridy = 0;
        }else{
            this.glueConstraints.gridx = 0;
        }
        this.fittedViewportPanel.add(this.glue, this.glueConstraints);
        //setup the glue
    }
    /**
     * 
     * @param point
     * @return 
     */
    public JPanel getPanelAtPoint(Point point)
    {
        //Point translated
        point.translate(-this.getX(), -this.getY());
        point.translate(-this.fittedViewportPanel.getX(), -this.fittedViewportPanel.getY());
        System.out.println(point.toString());
        JPanel panelAtPoint = (JPanel)this.fittedViewportPanel.getComponentAt(point);
        if(panelAtPoint == this.glue)
        {
            return null;
        }
        return panelAtPoint;
    }
    /**
     * 
     * @param panels 
     */
    public void addListPanels(List<JPanel> panels)
    {
        
    }

    public boolean isVertical() {
        return isVertical;
    }  
    /**
     * Custom JPanel class update automatically it's size to the size of the viewport
     * in a given JScrollPane
     */
    protected class FittedViewportPanel extends JPanel implements Scrollable, ComponentListener
    {
        private PanelsScrollPane scrollPane;
        public FittedViewportPanel(PanelsScrollPane scrollPane)
        {
            super();
            this.scrollPane = scrollPane;
        }
        @Override
        public Dimension getPreferredScrollableViewportSize() {
            if(this.scrollPane.isVertical())
            {
                Dimension preferredSize = this.getPreferredSize();
                if (getParent() instanceof JViewport) {
                    preferredSize.width += ((JScrollPane) getParent().getParent()).getVerticalScrollBar()
                            .getPreferredSize().width;
                }
                return preferredSize;
            }
            else
            {
                Dimension prefferedSize = this.getPreferredSize();
                if(getParent() instanceof JViewport)
                {
                    prefferedSize.height += ((JScrollPane) getParent().getParent()).getHorizontalScrollBar()
                            .getPreferredSize().height;
                }
                return prefferedSize;
            }
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            return orientation == SwingConstants.HORIZONTAL ? Math.max(visibleRect.width * 9 / 10, 1)
                    : Math.max(visibleRect.height * 9 / 10, 1);
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            if(this.scrollPane.isVertical())
            {
                if (getParent() instanceof JViewport) {
                    JViewport viewport = (JViewport) getParent();
                    return getPreferredSize().height < viewport.getHeight();
                }
                return true;
            }
            else
            {
                return true;
            }
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            if(this.scrollPane.isVertical())
            {
                return true;
            }
            else
            {
                if(getParent() instanceof JViewport)
                {
                    JViewport viewport = (JViewport) getParent();
                    return getPreferredSize().width < viewport.getWidth();
                }
                return true;
            }
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            return orientation == SwingConstants.HORIZONTAL ? Math.max(visibleRect.width / 10, 1)
                    : Math.max(visibleRect.height / 10, 1);
        }

        @Override
        public void componentResized(ComponentEvent e) {
            this.revalidate();
        }

        @Override
        public void componentMoved(ComponentEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void componentShown(ComponentEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void componentHidden(ComponentEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
}
