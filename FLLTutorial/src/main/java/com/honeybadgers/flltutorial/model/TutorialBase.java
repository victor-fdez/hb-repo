/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.model;

/**
 *
 * @author chingaman
 */
public class TutorialBase {
    private String description;
    private String author;
    public TutorialBase(String author, String description)
    {
        this.description = description;
        this.author = author;
    }
}
