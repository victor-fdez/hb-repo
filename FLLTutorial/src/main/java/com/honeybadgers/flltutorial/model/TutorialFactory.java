/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.model;

import java.io.File;

/**
 *
 * @author chingaman
 */
public class TutorialFactory {
    public boolean verifyXML()
    {
        return true;
    }
    public Project loadProject(File file)
    {
        return null;
    }
    public Tutorial loadTutorial(File file)
    {
        return null; 
    }
    public File saveTutorial(Tutorial tutorial)
    {
        return null;
    }
    public File saveProject(Project project)
    {
        return null;
    }
}
