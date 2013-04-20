/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.model.backend;

import com.honeybadgers.flltutorial.model.Tutorial;
import com.honeybadgers.flltutorial.model.TutorialBase;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Manages tutorials and project in the resources/Tutorials and resources/Projects
 * folders
 * 
 * @author chingaman
 */
public class TutorialManager {
    
    public static String projectsFolderPath = "resources/Projects/";
    public static String tutorialsFolderPath = "resources/Tutorials/";
    public static String generalMediaPath = "resources/General/media/";
    public static String generalVideoPath = "resources/General/videos/";
    /**
     * Gets all the tutorials bases for the tutorial folder
     * 
     * @return 
     */
    public static ArrayList<TutorialBase> getAllTutorialBases()
    {
        ArrayList<TutorialBase> tutorialBases = new ArrayList<>();
        
        File tutorialsParentDirectory = new File(TutorialManager.tutorialsFolderPath);
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
    /**
     * For every project in the given base tutorial, store it's base information, and
     * return it in a list.
     * 
     * @param tutorialBase          base information of the given tutorial
     * @return 
     */
    public static ArrayList<Tutorial> getAllTutorialBaseProjects(TutorialBase tutorialBase)
    {
        ArrayList<Tutorial> projectBases = new ArrayList<>();
        File tutorialsParentDirectory = new File(TutorialManager.projectsFolderPath+tutorialBase.getTitle());              
        for(String projectName : TutorialManager.listOfFileNames(tutorialsParentDirectory.getAbsolutePath()))
        {
                Tutorial projectBase = XMLBase.loadTutorialProjectBase(new File(tutorialsParentDirectory, projectName));
                projectBase.setFileName(projectName);
                projectBases.add(projectBase);
        }
        return projectBases;
    }
    /**
     * Get a new project for the given tutorial
     * 
     * @param tutorialBase      base tutorial information
     * @return 
     */
    public static Tutorial getNewProject(TutorialBase tutorialBase)
    {
        //find the main directory for tutorial's projects
        File projectsDirectory = new File(TutorialManager.tutorialsFolderPath+tutorialBase.getTitle());
       
        //get the clean tutorial file
        File cleanTutorial = new File(projectsDirectory, "tutorial.xml");
        Tutorial initialTutorial = XMLBase.loadTutorial(cleanTutorial);
        return initialTutorial;
    }
    /**
     * Create a new project file for the give project base
     * 
     * @param projectTutorial 
     */
    public static void saveNewProject(Tutorial projectTutorial)
    {
        if(projectTutorial.getFileName().isEmpty())
        {
            //add the filename to this new project
            projectTutorial.setFileName(TutorialManager.findNextFreeFilename(TutorialManager.projectsFolderPath+projectTutorial.getTutorialName()));
        }
        XMLBase.saveTutorial(projectTutorial);
    }
    /**
     * 
     * @param tutorial
     * @return 
     */
    public static Tutorial getTutorialBaseProject(Tutorial tutorial) {
        File projectFile = new File(TutorialManager.projectsFolderPath+tutorial.getTutorialName()+"/"+tutorial.getFileName());
        Tutorial projectTutorial = XMLBase.loadTutorial(projectFile);
        projectTutorial.setFileName(tutorial.getFileName());
        return projectTutorial;
    }
    /**
     * Save the project tutorial using the base information
     * 
     * @param currentTutorial 
     */
    public static void saveProject(Tutorial currentTutorial) {
        XMLBase.saveTutorial(currentTutorial);
    }
    /**
     * Very inefficient implementation that finds the first name of the form project[0-9].xml
     * within a folder that is not currently used.
     * 
     * @param FolderPath
     * @return 
     */
    private static String findNextFreeFilename(String FolderPath)
    {
        File folderFile = new File(FolderPath);
        String[] fileNames = folderFile.list(null);
        //assuming the fileUsed is initialized to all zeros
        int[] filesUsed = new int[fileNames.length];
        Pattern p = Pattern.compile("project([0-9])+\\.xml");
        for(String fileName : fileNames)
        {
            Matcher m = p.matcher(fileName);
            //System.out.println("filename: "+fileName+" matches: "+m.matches());
            if(m.matches())
            {
                //get the number and set the array position as 1
                //System.out.println("matched: "+m.group(1));
                int fileIndex = Integer.decode(m.group(1));
                if(fileIndex >= filesUsed.length)
                {
                    filesUsed = Arrays.copyOf(filesUsed, fileIndex*2);                    
                }
                filesUsed[fileIndex] = 1;
            }
        }
        
        for(int i = 0; i < filesUsed.length; i++)
        {
            if(filesUsed[i] == 0)
            {
                //System.out.println("index: "+i+" is unused");
                return "project"+i+".xml";
            }
            //System.out.println("index: "+i+" is used");
        }
        return "project"+filesUsed.length+".xml";
    }
    /**
     * Gets a list of all files matching the pattern project[0-9]+.xml in the given
     * folder path.
     * 
     * @param FolderPath    folder path in string form
     * @return 
     */
    private static List<String> listOfFileNames(String FolderPath)
    {
        File folderFile = new File(FolderPath);
        String[] fileNames = folderFile.list(null);
        ArrayList<String> listFileNames = new ArrayList<>();
        Pattern p = Pattern.compile("project([0-9])+\\.xml");
        for(String fileName : fileNames)
        {
            Matcher m = p.matcher(fileName);
            if(m.matches())
            {
                listFileNames.add(fileName);
            }
        }
        return listFileNames;
    }
    
    public static void main(String[] args)
    {
        //System.out.println(TutorialManager.listOfFileNames(TutorialManager.projectsFolderPath+"Tutorial1/"));
        //System.out.println("file: "+findNextFreeFilename(TutorialManager.projectsFolderPath+"Tutorial1/")+" found");
    }
}
