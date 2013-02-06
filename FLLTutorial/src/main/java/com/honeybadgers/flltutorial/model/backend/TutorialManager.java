/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.model.backend;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author chingaman
 */
public class TutorialManager {
    public static ArrayList<TutorialManager> getAllTutorialsAtFolder()
    {
        ArrayList<TutorialManager> tutorialManagers = new ArrayList<TutorialManager>();
        File folder = new File(".");
        if(folder.mkdirs())
        {
            System.out.println("success creating directories");
        }
        System.out.println("this is were tutorials should be"+folder.getAbsolutePath());
        return null;
    }
}
