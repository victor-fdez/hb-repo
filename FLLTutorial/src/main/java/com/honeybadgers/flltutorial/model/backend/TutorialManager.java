/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.model.backend;

import com.honeybadgers.flltutorial.model.Tutorial;
import com.honeybadgers.flltutorial.model.TutorialBase;
import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author chingaman
 */
public class TutorialManager {
    
    public static ArrayList<TutorialBase> getAllTutorialBases()
    {
        ArrayList<TutorialBase> tutorialBases = new ArrayList<>();
        
        File tutorialsParentDirectory = new File("src/main/resources/Tutorials");
        File[] tutorialDirectories = tutorialsParentDirectory.listFiles();
        for(File directory : tutorialDirectories){
            if(directory.isDirectory()){
                File tutorialFile = new File(directory, "tutorial".concat(".xml"));
                TutorialBase tutorialBase = XMLBase.loadTutorialBase(tutorialFile);
                tutorialBases.add(tutorialBase);
            }
        }
        return tutorialBases;
    }
    
    public static ArrayList<TutorialBase> getAllTutorialBaseProjects(TutorialBase tutorialBase)
    {
        ArrayList<TutorialBase> projectBases = new ArrayList<>();
        
        File tutorialsParentDirectory = new File("src/main/resources/Projects/"+tutorialBase.getTitle());
        File[] tutorialProjectFiles = tutorialsParentDirectory.listFiles();
        for(File projectFile : tutorialProjectFiles){
            if(projectFile.isFile()){
                TutorialBase projectBase = XMLBase.loadTutorialBase(projectFile);
                projectBases.add(projectBase);
            }
        }
        return projectBases;
    }
    
    public static Tutorial getNewProject(TutorialBase tutorialBase)
    {
        //File cleanTutorial = new File("src/main/resources/Projects/Tutorial1/project1.xml");
        //System.out.println("TutorialManager.getNewProject : getting clean tutorial "+tutorialBase.getTitle()+" "+cleanTutorial);
        
        //find the main directory for tutorial's projects
        File projectsDirectory = new File("src/main/resources/Tutorials/"+tutorialBase.getTitle());
       
        //get the clean tutorial file
        File cleanTutorial = new File(projectsDirectory, "tutorial.xml");
        Tutorial initialTutorial = XMLBase.loadTutorial(cleanTutorial);
        return initialTutorial;
    }
    
    public static void saveProject(Tutorial projectTutorial)
    {
        File projectsDirectory = new File("src/main/resources/Tutorials/"+projectTutorial.getTutorialName());
        int numberOfFiles = projectsDirectory.list().length;

        if(projectTutorial.getFileName().isEmpty())
        {
            projectTutorial.setFileName("project"+numberOfFiles+".xml");
        }
        XMLBase.saveTutorial(projectTutorial);
    }
    
}
