/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.ui.main.content.stages;

import com.honeybadgers.flltutorial.model.Option;

/**
 *
 * @author chingaman
 */
public class ConsiderationsAndConstraintsPanel extends StageSelectorPanel{
    public ConsiderationsAndConstraintsPanel(Option rootOption)
    {
        super(rootOption);
        this.stageName = "Considerations and Constraints";
        this.initComponents();
    }
}
