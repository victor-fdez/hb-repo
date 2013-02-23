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
    private String id;
    private Option parent;
    public Option(String description, boolean correct, List<Option> options, Option parent, String id)
    {
        super();
        this.description = description;
        this.correct = correct;
        this.options = options;
        this.parent = parent;
        this.id = id;
    }
    public Option(String description, boolean correct, List<Option> options, Option parent)
    {
        this(description, correct, options, parent, "");
        /*root identifier has nothing, there can only be one identifer*/
    }
    public Option(String description, boolean correct, List<Option> options)
    {
        this(description, correct, options, null, "");
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Option getParent() {
        return parent;
    }

    public void setParent(Option parent) {
        this.parent = parent;
    }
    /**
     * Add the given Option as a child of this Option.
     * 
     * @param option 
     */
    public void addChild(Option option)
    {
        /*setup identifer of child*/
        if(this.options == null)
        {
            this.options = new ArrayList<Option>();
        }
        option.setId(this.id + " " + this.options.size());
        option.setParent(this);
        /*TODO: maybe later this would count the number of correct and incorrect options*/
        this.options.add(option);
    }
    public Option getChildAtIndex(int index) {
        return this.options.get(index);
    }
    /**
     * Gets a descendent child of the input ancestor option specified by the id.
     * 
     * @param id                String denotes the identifier of the descendent
     * @param optionAncestor    Ancestor option
     * @return                  
     */
    public Option getChildWithId(String id)
    {
        /*check that the id of the ancestor is a prefix of the id of the child*/
        if(!id.startsWith(this.id)) {
            return null;
        }
        String childSubstringId = id.substring(this.id.length());
        ArrayList<Integer> childIdentifier = Option.getIntegerIdentifier(childSubstringId);
        /*go through the string id and find the given option*/
        Option childOption = this;
        for(Integer childIndex : childIdentifier)
        {
            if(childOption == null)
            {
                return null;
            }
            childOption = childOption.getChildAtIndex(childIndex);
        }
        return childOption;
    }
    /**
     * Splits up the input id into an ArrayList of integers
     * @param id               A string identifier of an Option.
     * @return 
     */
    static private ArrayList<Integer> getIntegerIdentifier(String id)
    {
        ArrayList<Integer> integerIdentifier;   
        String  stringIdentifier[] = id.substring(1).split(" ");
        integerIdentifier = new ArrayList<Integer>(stringIdentifier.length);
        for(String currentId : stringIdentifier)
        {
            integerIdentifier.add(Integer.parseInt(currentId));
        }
        return integerIdentifier;
    }
    public boolean equals(Option anotherOption)
    {
        /*TODO: this needs to be amplified*/
        if(super.equals(anotherOption))
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
