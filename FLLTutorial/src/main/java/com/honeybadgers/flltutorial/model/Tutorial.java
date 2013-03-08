/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.model;

import java.util.List;

/**
 *
 * @author chingaman
 */
public class Tutorial extends TutorialBase
{
    private List<Option> subTasks;
    private List<Option> taskDiagram;
    private List<Option> morphologicalChart;
    private List<Option> limitationsAndConstraints;
    Tutorial()
    {
        super(null,null,null);
    }
    public int equals(Tutorial anotherTutorial)
    {
       return 0; 
    }
}
