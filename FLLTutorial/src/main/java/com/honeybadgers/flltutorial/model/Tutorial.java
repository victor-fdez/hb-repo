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
    /*private List<Option> subTasks;
    private List<Option> taskDiagram;
    private List<Option> morphologicalChart;
    private List<Option> limitationsAndConstraints;*/
    
    private String mission;
    List<Stage> stages;
    private Stage problemStatement;
    private Stage limitations;
    private Stage taskDiagram;
    private Stage morphChart;
    
    public Tutorial(String mission, Stage problemStatement, Stage limitations, Stage taskDiagram, Stage morphChart)
    {
        this.mission = mission;
        this.problemStatement = problemStatement;
        this.limitations = limitations;
        this.taskDiagram = taskDiagram;
        this.morphChart = morphChart;
    }
    public int equals(Tutorial anotherTutorial)
    {
       return 0; 
    }
}
