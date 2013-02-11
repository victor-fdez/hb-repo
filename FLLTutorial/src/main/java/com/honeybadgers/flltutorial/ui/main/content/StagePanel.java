/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.ui.main.content;

import com.honeybadgers.flltutorial.ui.main.content.OptionsPanel;
import java.awt.Dimension;
import javax.swing.JPanel;

/**
 *
 * @author chingaman
 */
abstract public class StagePanel extends JPanel
{
    private Dimension preferedDimension = new Dimension(600, 500);
    private Dimension minDimension = new Dimension(600, 400);
    private Dimension maxDimension = new Dimension(32767, 32767);
    OptionsPanel optionsPanel;
    StagePanel()
    {
        super();
        this.setPreferredSize(preferedDimension);
        this.setMinimumSize(minDimension);
        this.setMaximumSize(maxDimension);
        this.setOpaque(true);
        this.setVisible(true);
    }
    abstract OptionsPanel getOptionsPanel();
    //abstract void setupStage();
    //abstract void recieveOption();
}
