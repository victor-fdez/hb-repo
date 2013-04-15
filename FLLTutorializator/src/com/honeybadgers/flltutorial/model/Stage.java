/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.model;

import java.util.List;

/**
 *
 * @author Dan
 */
public class Stage {
    private String name;
    private Option rootOption;
    private String videoPath;
    private long timeOnStage;
    
    /**
     * Constructor
     * @param options  Stage options
     * @param videoPath  Path to video for this stage
     * @param timeOnStage  The time that a student spends on this stage
     */
    public Stage(String name, Option rootOption, String videoPath, long timeOnStage){
        this.name = name;
        this.rootOption = rootOption;
        this.videoPath = videoPath;
        this.timeOnStage = timeOnStage;
    }
    
    /**
     * Getter for the name
     * @return  The name of this Stage
     */
    public String getName(){
        return name;
    }
    
    /**
     * Getter for the root options
     * @return  The root option of this Stage
     */
    public Option getRootOption(){
        return rootOption;
    }
    
    /**
     * Getter for the video path
     * @return The video path of this Stage
     */
    public String getVideoPath(){
        return videoPath;
    }
    
    /**
     * Getter for the time spent on the stage.
     * @return The time a student spends on this Stage
     */
    public long getTimeOnStage(){
        return timeOnStage;
    }
    
    

}
