/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.ui.main.navigation;

import java.awt.Dimension;
import javax.swing.JPanel;

/**
 *
 * @author chingaman
 */
public class NavigationPanel extends JPanel
{
    private Dimension preferedDimension = new Dimension(200, 500);
    private Dimension minDimension = new Dimension(150, 400);
    private Dimension maxDimension = new Dimension(32767, 32767);
    public NavigationPanel()
    {
        this.initComponents();
        
    }

    private void initComponents() {
        this.setPreferredSize(preferedDimension);
        this.setMinimumSize(minDimension);
        this.setMaximumSize(maxDimension);
    }
}
