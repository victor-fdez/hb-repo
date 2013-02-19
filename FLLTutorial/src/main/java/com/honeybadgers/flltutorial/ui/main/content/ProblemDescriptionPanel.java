/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.ui.main.content;

import com.honeybadgers.flltutorial.model.Option;
import com.honeybadgers.flltutorial.ui.main.content.utilities.OptionPanel;
import java.awt.Color;
import java.awt.Point;
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

    @Override
    boolean dropOptionPanel(OptionPanel optionPanel) {
        return false;
    }

    @Override
    void clicked(Point point) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
