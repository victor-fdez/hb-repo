/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author chingaman
 */
public class Option {
    private String description;
    private boolean correct;
    private List<Option> options;
    private int childId;
    private Option parent;
    public Option(String description, boolean correct, List<Option> options, Option parent, int childId)
    {
        this(description, correct, options, parent);
        this.childId = childId;
    }
    public Option(String description, boolean correct, List<Option> options, Option parent)
    {
        this(description, correct, options);
        this.parent = parent;
    }
    public Option(String description, boolean correct, List<Option> options)
    {
        this.description = description;
        this.correct = correct;
        this.options = options;
        this.parent = null;
        this.childId = -1;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public List<Option> getOptions() {
        return options;
    }
    
    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public int getChildId() {
        return childId;
    }

    public Option getParent() {
        return parent;
    }
    
    public boolean equals(Option anotherOption)
    {
        /*TODO: this needs to be amplified*/
        if(this.description.equals(anotherOption.getDescription()) && this.correct == anotherOption.isCorrect())
        {
            return true;
        }
       return false; 
    }
    public int getChildIndex(Option childOption)
    {
        for(int i = 0; i < this.options.size(); i++)
        {
            if(this.options.get(i).equals(childOption))
            {
                return i;
            }
        }
        return -1;
    }
}
