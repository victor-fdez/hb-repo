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
    private String tutorialName;
    private String projectName;
    private String teamName;
    private String fileName;
    private List<String> members;
    private String mission;
    private List<Stage> stages;
    
    public Tutorial(String name, String mission, List<Stage> stages, String projectName, String teamName, List<String> members)
    {
        super(null, null, null);
        this.tutorialName = name;
        this.mission = mission;
        this.stages = stages;
        this.projectName = projectName;
        this.teamName = teamName;
        this.members = members;
        this.fileName = "";
    }
    
    public String getName(){
        return tutorialName;
    }
    
    public String getMission(){
        return mission;
    }
    
    public List<Stage> getStages(){
        return stages;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public String getTutorialName() {
        return tutorialName;
    }

    public void setTutorialName(String tutorialName) {
        this.tutorialName = tutorialName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public int equals(Tutorial anotherTutorial)
    {
        //***do something here, do we actually need this method?
       return 0; 
    }
}
