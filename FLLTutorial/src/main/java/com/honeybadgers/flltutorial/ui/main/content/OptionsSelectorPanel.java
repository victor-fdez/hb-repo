/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.ui.main.content;

import com.honeybadgers.flltutorial.model.Option;
import com.honeybadgers.flltutorial.ui.main.content.utilities.OptionPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author chingaman
 */
public class OptionsSelectorPanel extends OptionsPanel implements ComponentListener{

    OptionsSelectorPanel(List<Option> options)
    {
        super(options);
        this.initComponents();
    }
    private void initComponents()
    {
        //selection view port contains a column panel
        this.selectionsViewPort = new JPanel();
        this.selectionsViewPort.setLayout(new GridBagLayout());
        //this.selectionsViewPort.setLayout(new GridLayout(0,1,2,2));
        this.selectionsViewPort.setBackground(Color.GRAY);

        this.selections = new JScrollPane();
        this.selections.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        //this.selections.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.selections.getVerticalScrollBar().setVisible(true);
        //TESTING: add options
        this.changeOptionPanels(this.options);
        
        this.selections.setBorder(null);
        this.selections.setViewportView(this.selectionsViewPort);
        this.add(this.selections);
        this.setVisible(true);
        this.setOpaque(true);
        this.addComponentListener(this);
        this.selections.setLocation(0, 0);
        Dimension preferredContainer = this.getPreferredSize();
        //this.selectionsViewPort.setSize(new Dimension(32000, 300));
        //this.selections.setPreferredSize(preferredContainer);
        this.selectionsViewPort.revalidate();
        //this.selectionsViewPort.setMinimumSize(preferredContainer);
    }
    @Override
    public void paint(Graphics g) {
        
        this.selections.setLocation(0, 0);
        this.selections.setPreferredSize(this.selections.getParent().getSize());
        super.paint(g);
    }
    void changeOptionPanels(List<Option> options)
    {
        this.selectionsViewPort.removeAll();
        int i = 0;
        /*swap in this list of options*/
        for(Option option : options)
        {
            GridBagConstraints c = new GridBagConstraints();
            c.weightx = 1.0;
            c.weighty = 0.0;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 0;
            c.gridy = i;
            c.insets = new Insets(2,2,2,2);
            c.anchor = GridBagConstraints.PAGE_START;
            this.selectionsViewPort.add(new OptionPanel(option), c);
            i++;
        }
        GridBagConstraints lastC = new GridBagConstraints();
        lastC.weightx = 1.0;
        lastC.weighty = 1.0;
        lastC.fill = GridBagConstraints.BOTH;
        lastC.gridx = 0;
        lastC.gridy = i;
        JPanel bottomFillerPanel = new JPanel();
        bottomFillerPanel.setOpaque(false);
        this.selectionsViewPort.add(bottomFillerPanel, lastC);
        this.selectionsViewPort.revalidate();
    }
    public void componentResized(ComponentEvent e) {
        this.selections.setSize(this.getSize());
        this.revalidate();
    }
    public void componentMoved(ComponentEvent e) {}
    public void componentShown(ComponentEvent e) {}
    public void componentHidden(ComponentEvent e) {} 
}
