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
    
    public static String projectsFolderPath = "src/main/resources/Projects/";
    public static String tutorialsFolderPath = "src/main/resources/Tutorials/";
    
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
    
    public static ArrayList<Tutorial> getAllTutorialBaseProjects(TutorialBase tutorialBase)
    {
        ArrayList<Tutorial> projectBases = new ArrayList<>();
        
        File tutorialsParentDirectory = new File("src/main/resources/Projects/"+tutorialBase.getTitle());
        File[] tutorialProjectFiles = tutorialsParentDirectory.listFiles();
        for(File projectFile : tutorialProjectFiles){
            if(projectFile.isFile()){
                Tutorial projectBase = XMLBase.loadTutorialProjectBase(projectFile);
                projectBase.setFileName(projectFile.getName());
                projectBases.add(projectBase);
            }
        }
        return projectBases;
    }
    
    public static Tutorial getNewProject(TutorialBase tutorialBase)
    {
        //System.out.println("TutorialManager.getNewProject : getting clean tutorial "+tutorialBase.getTitle()+" "+cleanTutorial);
        
        //find the main directory for tutorial's projects
        File projectsDirectory = new File("src/main/resources/Tutorials/"+tutorialBase.getTitle());
       
        //get the clean tutorial file
        File cleanTutorial = new File(projectsDirectory, "tutorial.xml");
        Tutorial initialTutorial = XMLBase.loadTutorial(cleanTutorial);
        return initialTutorial;
    }
    
    public static void saveNewProject(Tutorial projectTutorial)
    {
        File projectsDirectory = new File("src/main/resources/Projects/"+projectTutorial.getTutorialName());
        int numberOfFiles = projectsDirectory.list().length;

        if(projectTutorial.getFileName().isEmpty())
        {
            projectTutorial.setFileName("project"+(numberOfFiles+1)+".xml");
        }
        XMLBase.saveTutorial(projectTutorial);
    }

    public static Tutorial getTutorialBaseProject(Tutorial tutorial) {
        File projectFile = new File(TutorialManager.projectsFolderPath+tutorial.getTutorialName()+"/"+tutorial.getFileName());
        Tutorial projectTutorial = XMLBase.loadTutorial(projectFile);
        projectTutorial.setFileName(tutorial.getFileName());
        return projectTutorial;
    }

    public static void saveProject(Tutorial currentTutorial) {
        XMLBase.saveTutorial(currentTutorial);
    }
    
}
