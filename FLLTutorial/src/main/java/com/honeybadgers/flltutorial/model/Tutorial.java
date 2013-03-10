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
    
    private String name;
    private String mission;
    List<Stage> stages;
    
    public Tutorial(String name, String mission, List<Stage> stages)
    {
        super(null, null, null);
        this.name = name;
        this.mission = mission;
        this.stages = stages;
    }
    
    public String getName(){
        return name;
    }
    
    public String getMission(){
        return mission;
    }
    
    public List<Stage> getStages(){
        return stages;
    }
    
    
    public int equals(Tutorial anotherTutorial)
    {
       return 0; 
    }
}
