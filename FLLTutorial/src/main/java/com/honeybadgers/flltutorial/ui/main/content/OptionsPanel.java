/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.ui.main.content;

import com.honeybadgers.flltutorial.model.Option;
import com.honeybadgers.flltutorial.ui.main.content.utilities.OptionPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
/**
 *
 * @author chingaman
 */
abstract public class OptionsPanel extends JPanel {
    private Dimension preferedDimension = new Dimension(300, 500);
    private Dimension minDimension = new Dimension(300, 400);
    private Dimension maxDimension = new Dimension(300, 32767);
    JPanel clickedPanel;
    List<Option> options;
    ArrayList<OptionPanel> optionPanels;
    OptionsPanel(List<Option> options)
    {
        super();
        this.setPreferredSize(preferedDimension);
        this.setMinimumSize(minDimension);
        this.setMaximumSize(maxDimension);
        this.options = options;
    }
    
}
