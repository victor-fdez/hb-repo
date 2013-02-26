/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class mainly tracks the progress of a given project, that references a tutorial.
 * 
 * @author chingaman
 */
public class OptionTracker {
    //option being tracked
    private Option option;
    private OptionTracker parent;
    private OptionTracker[] correctOptions, incorrectOptions;
    private int incorrectSize;
    private int correctSize;
    /*checking quickly weather a correct or wrong options is contained already*/
    private HashMap correctHashes;
    private HashMap incorrectHashes;
    private int finishedCorrect;
    private boolean finished;
    /**
     * 
     * @param option 
     */
    public OptionTracker(Option option)
    {
        this.option = option;
        /*only add options if is correct, incorrect options don't have children*/
        if(this.option.isCorrect())
        {
            int numberCorrect = 0;
            int numberIncorrect = 0;
            ArrayList<Option> options = (ArrayList<Option>)option.getOptions();
            if(options == null || options.isEmpty())
            {
                this.finished = true;
                //might have to tell parent
            }
            else
            {
                this.finished = false;
            }
            /*count the number of correct options*/
            if(options != null)
            {
                for(Option optionCorrect : options)
                {
                    if(optionCorrect.isCorrect()) {
                        numberCorrect++;
                    }
                }
                numberIncorrect = options.size() - numberCorrect;
            }
            else
            {
                numberIncorrect = 0;
                numberCorrect = 0;
            }
            //create the appropriate data structures
            this.correctHashes = new HashMap(numberCorrect*2);
            this.incorrectHashes = new HashMap(numberIncorrect*2);
            this.correctOptions = new OptionTracker[numberCorrect];
            this.incorrectOptions = new OptionTracker[numberIncorrect];
            this.finishedCorrect = 0;
            this.correctSize = 0;
            this.incorrectSize = 0;
        }
    }
    /**
     * Add an option that should be tracked. If the given options is correct, and the place
     * it was placed is correct
     * @param index
     * @param option
     * @return 
     */
    public boolean addOptionAt(int index, Option option)
    {
        if(option.isCorrect())
        {
            /*this kind of option replaces whatever is there, if it has not been added*/
            if(this.correctHashes.get(option.getId()) != null || this.correctOptions[index] != null)
            {
                /*this means the option is already stored*/
                return false;
            }
            /*check whether this options has no options*/
            if(option.getOptions() == null || option.getOptions().isEmpty())
            {
                this.finishedAnotherCorrectOption();
            }
            /*track the position of this option*/
            this.correctHashes.put(option.getId(), option);
            this.correctOptions[index] = new OptionTracker(option);
            this.correctOptions[index].setParent(this);
            this.correctSize++;
            return true;
        }
        else
        {
            if(this.incorrectHashes.get(option.getId()) != null)
            {
                /*this means the option is already stored*/
                return false;
            }
            this.incorrectHashes.put(option.getId(), option);
            this.incorrectOptions[this.incorrectSize] = new OptionTracker(option);
            //not needed but just in case
            this.incorrectOptions[this.incorrectSize++].setParent(this);
            return true;
        }
    }
    /**
     * 
     * @param index
     * @return          the correct option tracker at that index
     */
    public OptionTracker getCorrectChild(int index)
    {
        return this.correctOptions[index];
    }
    /**
     * 
     * @return          an array of options currently correctly chosen
     */
    public Option[] getAllCorrectChildren(){
        int i = 0;
        if(this.correctOptions.length == 0)
        {
            return null;
        }
        Option[] options = new Option[this.correctOptions.length];
        for(OptionTracker optionTracker : this.correctOptions)
        {
            if(optionTracker != null)
            {
               //option may or may not be null
               options[i] = optionTracker.getOption();
            }
            i++;
        }
        return options;
    }
    public boolean isEmptyCorrectChildren()
    {
        if(this.correctOptions.length == 0)
        {
            return true;
        }
        return false;
    }
    /**
     * Finds whether the parameter option has been chosen already. Returns true if the
     * option is chosen, else it returns false.
     * 
     * @param option    the option to check
     * @return          
     */
    public boolean isChoosed(Option option)
    {
        if(option.isCorrect())
        {
            if(this.correctHashes.get(option.getId()) != null)
            {
                return true;
            }
        }
        else
        {
            if(this.incorrectHashes.get(option.getId()) != null)
            {
                return true;
            }
        }
        return false;
    }
    /**
     * Called by either a child when it is finished, or when it adds another options
     * and that options does not have any child options.
     */
    protected void finishedAnotherCorrectOption()
    {
        /*this means one children is finished*/
        this.finishedCorrect++;
        if(this.correctOptions.length == this.finishedCorrect)
        {
            this.finished = true;
            /*whenever this option tracker is done parent is notified*/
            if(this.parent != null)
            {
                this.parent.finishedAnotherCorrectOption();
            }
        }
    }
    /**
     * Returns whether this options has all correct options chosen.
     * 
     * @return 
     */
    public boolean isFinished()
    {
        return this.finished;
    }

    public Option getOption() {
        return option;
    }

    public OptionTracker getParent() {
        return parent;
    }

    public void setParent(OptionTracker parent) {
        this.parent = parent;
    }
    
}
