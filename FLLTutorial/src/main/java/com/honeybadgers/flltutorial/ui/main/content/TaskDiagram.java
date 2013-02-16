/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.ui.main.content;

import com.honeybadgers.flltutorial.model.Option;
import com.honeybadgers.flltutorial.ui.main.content.utilities.OptionPanel;
import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author chingaman
 */
public class TaskDiagram extends StagePanel{

    TaskDiagram()
    {
        super();
        this.setBackground(Color.GRAY);
        ArrayList<Option> options = new ArrayList<Option>();
        this.optionsPanel = new OptionsSelectorPanel(options);
    }
    @Override
    OptionsPanel getOptionsPanel() {
        return null;
    }

    @Override
    boolean dropOptionPanel(OptionPanel optionPanel) {
        return true;
    }
    
}
