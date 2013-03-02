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
    
    public PanelsScrollPane()
    {
        super();
        //initialize panel scroll pane
        this.numberOfPanels = 0;
        this.initComponents();
    }
    private void initComponents()
    {
        this.fittedViewportPanel = new FittedViewportPanel();
        this.fittedViewportPanel.setLayout(new GridBagLayout());
        this.fittedViewportPanel.setBackground(Color.GRAY);
        
        //add glue at the end of fitted viewport panel
        this.glueConstraints = new GridBagConstraints();
        this.glueConstraints.weightx = 1.0;
        this.glueConstraints.weighty = 1.0;
        this.glueConstraints.fill = GridBagConstraints.BOTH;
        this.glueConstraints.gridx = 0;
        this.glueConstraints.gridy = this.numberOfPanels;
        this.glueConstraints.insets = new Insets(2,2,2,2);
        this.glueConstraints.anchor = GridBagConstraints.PAGE_START;
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
        constraint.weightx = 1.0;
        constraint.weighty = 0.0;
        constraint.fill = GridBagConstraints.HORIZONTAL;
        constraint.gridx = 0;
        constraint.gridy = this.numberOfPanels++;
        constraint.gridheight = 1;
        constraint.gridwidth = 1;
        constraint.insets = new Insets(2,2,2,2);
        constraint.anchor = GridBagConstraints.PAGE_START;
        appendedPanel.setOpaque(true);
        this.fittedViewportPanel.add(appendedPanel, constraint);
        
        //update glue constraints and add glue at the end of panel list
        this.glueConstraints.gridy = this.numberOfPanels;
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
        this.glueConstraints.gridy = 0;
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
    /**
     * Custom JPanel class update automatically it's size to the size of the viewport
     * in a given JScrollPane
     */
    protected class FittedViewportPanel extends JPanel implements Scrollable
    {
        public FittedViewportPanel()
        {
            super();
        }
        @Override
        public Dimension getPreferredScrollableViewportSize() {
            Dimension preferredSize = this.getPreferredSize();
            if (getParent() instanceof JViewport) {
                preferredSize.width += ((JScrollPane) getParent().getParent()).getVerticalScrollBar()
                        .getPreferredSize().width;
            }
            return preferredSize;
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            return orientation == SwingConstants.HORIZONTAL ? Math.max(visibleRect.width * 9 / 10, 1)
                    : Math.max(visibleRect.height * 9 / 10, 1);
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            if (getParent() instanceof JViewport) {
                JViewport viewport = (JViewport) getParent();
                return getPreferredSize().height < viewport.getHeight();
            }
            return true;
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            return orientation == SwingConstants.HORIZONTAL ? Math.max(visibleRect.width / 10, 1)
                    : Math.max(visibleRect.height / 10, 1);
        }
    }
}
