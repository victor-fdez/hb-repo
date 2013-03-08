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
    private List<Option> options;
    private String videoPath;
    private long timeOnStage;
    
    /**
     * Constructor
     * @param options  Stage options
     * @param videoPath  Path to video for this stage
     * @param timeOnStage  The time that a student spends on this stage
     */
    public Stage(List<Option> options, String videoPath, long timeOnStage){
        this.options = options;
        this.videoPath = videoPath;
        this.timeOnStage = timeOnStage;
    }
    
    /**
     * Getter for the list of options
     * @return 
     */
    public List<Option> getOptions(){
        return options;
    }
    
    /**
     * Getter for the video path
     * @return 
     */
    public String getVideoPath(){
        return videoPath;
    }
    
    /**
     * Getter for the time spent on the stage.
     * @return 
     */
    public long getTimeOnStage(){
        return timeOnStage;
    }

}
