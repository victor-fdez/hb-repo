/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.ui.main.content;

import com.honeybadgers.flltutorial.model.Option;
import java.awt.Color;
import java.util.ArrayList;


/**
 *
 * @author chingaman
 */
public class ProblemDescriptionPanel extends StagePanel
{
    ProblemDescriptionPanel()
    {
        super();
        //TODO: get actual options
        this.setBackground(Color.GRAY);
        ArrayList<Option> options = new ArrayList<Option>();
        this.optionsPanel = new OptionsSelectorPanel(options);
    }
    @Override
    OptionsPanel getOptionsPanel() {
        return this.optionsPanel;
    }
}