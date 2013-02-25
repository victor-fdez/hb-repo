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
        this.optionsPanel = new OptionsSelectorPanel(null);
    }
    @Override
    OptionsPanel getOptionsPanel() {
        return this.optionsPanel;
    }

    @Override
    int dropOptionPanel(OptionPanel optionPanel) {
        return 0;
    }

    @Override
    void clicked(Point point) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
