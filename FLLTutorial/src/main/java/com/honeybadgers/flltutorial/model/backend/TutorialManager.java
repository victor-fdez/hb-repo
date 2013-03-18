/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.model.backend;

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
                File tutorialFile = new File(directory, directory.getName().concat(".xml"));
                TutorialBase tutorialBase = XMLBase.loadTutorialBase(tutorialFile);
                tutorialBases.add(tutorialBase);
            }
        }
        return tutorialBases;
        
        /*ArrayList<TutorialManager> tutorialManagers = new ArrayList<TutorialManager>();
        File folder = new File(".");
        if(folder.mkdirs())
        {
            System.out.println("success creating directories");
        }
        System.out.println("this is were tutorials should be"+folder.getAbsolutePath());
        return null;*/
    }
}
