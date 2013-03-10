/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.model;

import com.honeybadgers.flltutorial.model.backend.XMLBase;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class mainly tracks the progress of a given project, that references a tutorial.
 * 
 * @author chingaman
 */
public class OptionTracker {
    //option being tracked
    private Option option;
    private OptionTracker parent;
    /*replacing variables*/
    private int numberOfCorrect, numberOfIncorrect;
    private ArrayList<OptionTracker> correctOptionsList, incorrectOptionsList;
    /*checking quickly weather a correct or wrong options is contained already*/
    private HashMap correctHashes;
    private HashMap incorrectHashes;
    private int finishedCorrect;
    private boolean finished;
    /**
     * Initialized an option tracker, to contain an array of correct answers
     * 
     * @param option 
     */
    public OptionTracker(Option option)
    {
        this.option = option;
        
        //only add options if is correct, incorrect options don't have children
        if(this.option.isCorrect())
        {
            int numberCorrect = 0;
            int numberIncorrect = 0;
            ArrayList<Option> options = (ArrayList<Option>)option.getOptions();
            if(options == null || options.isEmpty())
            {
                this.finished = true;
                //advise parent that he might be finished
                
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
            
            //fill array lists with specific number of null
            this.correctOptionsList = new ArrayList<>(numberCorrect);
            this.incorrectOptionsList = new ArrayList<>(numberIncorrect);
            for(int i = 0; i < numberCorrect; i++)
            {
                this.correctOptionsList.add(null);
            }
            for(int i = 0; i < numberIncorrect; i++)
            {
                this.incorrectOptionsList.add(null);
            }
          
            this.numberOfCorrect = 0;
            this.numberOfIncorrect = 0;
            this.finishedCorrect = 0;
        }
    }
    /**
     * Add an option that should be tracked. If the given options is correct, and the place
     * it was placed is correct
     * 
     * @param index         the index where to place the option to be tracked
     * @param option        the option to add as tracked in the tracker
     * @return 
     */
    public boolean addOptionAt(int index, Option answerOption)
    {
        OptionTracker answerOptionTracker;
        if(answerOption.isCorrect())
        {
            //this kind of option replaces whatever is there, if it has not been added. It also checks
            //the spot is not occupied by another option tracker.
            if(this.correctHashes.get(answerOption.getId()) != null || this.correctOptionsList.get(index) != null)
            {
                //this means the option is already stored
                return false;
            }
            
            //check whether this is a leaf option
            if(answerOption.getOptions() == null || answerOption.getOptions().isEmpty())
            {
                //System.out.println("finished child option tracker");
                this.finishedAnotherCorrectOption();
            }
            
            //track the new option, and setup so it won't be tracked again
            this.correctHashes.put(answerOption.getId(), answerOption);
            answerOptionTracker = new OptionTracker(answerOption);
            this.correctOptionsList.set(index, answerOptionTracker);
            answerOptionTracker.setParent(this);
            this.numberOfCorrect++;
            return true;
        }
        else
        {
            if(this.incorrectHashes.get(answerOption.getId()) != null)
            {
                /*this means the option is already stored*/
                return false;
            }
            
            //track new incorrect option
            this.incorrectHashes.put(answerOption.getId(), answerOption);
            answerOptionTracker = new OptionTracker(answerOption);
            this.incorrectOptionsList.set(this.numberOfIncorrect, answerOptionTracker);
            //not needed but just in case
            answerOptionTracker.setParent(this);
            return true;
        }
    }
    /**
     * Gets the tracker at the specified index of correct if there is any else it returns null
     * 
     * @param index
     * @return          the correct option tracker at that index
     */
    public OptionTracker getCorrectChild(int index)
    {
        Object object = this.correctOptionsList.get(index);
        if(object instanceof OptionTracker)
        {
            return (OptionTracker) object;
        }
        return null;
    }
    /**
     * Gets an array the describes the current state of correctly chosen options
     * 
     * @return      an array of options currently correctly chosen
     */
    public Option[] getAllCorrectChildren(){
        return (Option[])this.correctOptionsList.toArray();
    }
    
    public List<OptionTracker> getAllCorrectTrackers()
    {
        return (List<OptionTracker>)this.correctOptionsList.clone();
    }
    /**
     * Returns whether this option tracker can have any correct children
     * 
     * @return 
     */
    public boolean isEmptyCorrectChildren()
    {
        if(this.correctOptionsList.isEmpty())
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
        if(this.correctOptionsList.size() == this.finishedCorrect)
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
    /**
     * Get the option being tracked
     * 
     * @return 
     */
    public Option getOption() {
        return option;
    }
    /**
     * Get the parent tracker
     * 
     * @return 
     */
    public OptionTracker getParent() {
        return parent;
    }
    /**
     * Set the parent of this tracker
     * 
     * @param parent 
     */
    public void setParent(OptionTracker parent) {
        this.parent = parent;
    }
    /**
     * Print a recursive depiction of the current tracker tree.
     */
    public void printTree()
    {
        this.printTreeRecursive(0);
    }
    private void printTreeRecursive(int level)
    {
        String tabs = "";
        for(int i = 0; i < level; i++)
        {
            tabs += "\t\t";
        }
        //print info of this tracker
        System.out.println(tabs+this.option.getId()+" -> "+this.option.getDescription());
        tabs += "\t";
        if(!this.option.isCorrect())
        {
            System.out.println(tabs+"{");
            System.out.println(tabs+"}");
            return;
        }
        System.out.println(tabs+"{");
        System.out.println(tabs+"+correct choosen");
        for(OptionTracker correctOption : this.correctOptionsList)
        {
            if(correctOption == null)
            {
                continue;
            }
            correctOption.printTreeRecursive(level+1);
        }
        System.out.println(tabs+"-incorrect choosen");
        for(OptionTracker incorrectOption : this.incorrectOptionsList)
        {
            if(incorrectOption == null)
            {
                continue;
            }
            incorrectOption.printTreeRecursive(level+1);
        }       
        System.out.println(tabs+"}");
    }
    /**
     * Generates an Option Tracker tree based on an Option tree.
     * 
     * @param rootOption        the top most element of a tree
     * @return 
     */
    public static OptionTracker generateOptionTrackerTree(Option rootOption)
    {
        OptionTracker rootTracker = new OptionTracker(rootOption);
        recursiveOptionTrackerTree(rootTracker);
        return rootTracker;
    }
    public static void recursiveOptionTrackerTree(OptionTracker parentOptionTracker)
    {
        //assume that if this has been called, then the options tracker has zero or more
        //correct or incorrect children
        Option parentOption = parentOptionTracker.getOption();
        int virtualOptionPosition = 0;
        for(Option option : parentOption.getOptions())
        {
            int optionPosition = option.getPosition();
            //recursive into children
            if(optionPosition >= 0)
            {
                parentOptionTracker.addOptionAt(optionPosition, option);
                recursiveOptionTrackerTree(parentOptionTracker.getCorrectChild(optionPosition));
            }
            else if(optionPosition == -2)
            {
                parentOptionTracker.addOptionAt(virtualOptionPosition, option);
                recursiveOptionTrackerTree(parentOptionTracker.getCorrectChild(virtualOptionPosition));
            }
            else if(optionPosition == -3)
            {
                //just set add the option, if there are only incorrect options this will fail
                parentOptionTracker.addOptionAt(0, option);
            }
            else if(optionPosition == -1)
            {
                //do nothing, because the options has not been selected
            }
            else
            {
                //might add other kinds of options later, but this should not be used at all
                //at the moment
            }
            virtualOptionPosition++;
        }
    }
    public static void main(String[] args)
    {
        System.out.println("Opening Tutorial");
        Tutorial tutorial = XMLBase.loadTutorial(new File("src/main/resources/sampleTutorial/tut1-project.xml"));
        List<Stage> stages = tutorial.getStages();
        for(Stage stage : stages)
        {
            Option rootOption = stage.getRootOption();
            System.out.println("Generating Option Tracker");
            OptionTracker rootTracker = OptionTracker.generateOptionTrackerTree(rootOption);
            System.out.println("Printing Option Tracker");
            rootTracker.printTree();
        }
    }
}
