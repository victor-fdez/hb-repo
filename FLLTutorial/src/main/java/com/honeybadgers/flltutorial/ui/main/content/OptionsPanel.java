/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.ui.main.content;

import com.honeybadgers.flltutorial.model.Option;
import java.awt.Color;
import java.awt.Dimension;
import java.util.List;
import javax.swing.JPanel;

/**
 *
 * @author chingaman
 */
abstract public class OptionsPanel extends JPanel {
    private Dimension preferedDimension = new Dimension(200, 500);
    private Dimension minDimension = new Dimension(150, 400);
    private Dimension maxDimension = new Dimension(32767, 32767);
    JPanel clickedPanel;
    List<Option> options;
    OptionsPanel(List<Option> options)
    {
        super();
        this.setPreferredSize(preferedDimension);
        this.setMinimumSize(minDimension);
        this.setMaximumSize(maxDimension);
        this.setBackground(Color.BLACK);
        //this.clickedPanel = new JPanel();
        //this.setBackground(Color.GRAY);
        //this.setSize(new Dimension(100,100));
        //this.add(clickedPanel);
        this.options = options;
    }
    
}
