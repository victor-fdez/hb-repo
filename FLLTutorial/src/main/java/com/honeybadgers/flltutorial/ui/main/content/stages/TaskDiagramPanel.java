/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.ui.main.content.stages;
//REMEMBER, lowest level is having bugs.
import com.honeybadgers.flltutorial.model.Option;
import com.honeybadgers.flltutorial.model.OptionTracker;
import com.honeybadgers.flltutorial.ui.main.content.OptionsPanel;
import com.honeybadgers.flltutorial.ui.main.content.OptionsSelectorPanel;
import com.honeybadgers.flltutorial.ui.main.content.utilities.OptionPanel;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author chingaman
 */
public class TaskDiagramPanel extends StagePanel{

    /*selected options, there are the options the student chooses*/
    private OptionTracker projectTracker;
    /*pointers into respective trees*/
    private OptionTracker currentTrackerPointer;
    /*array of panels to hold current options shown*/
    private HashMap[] depthPanelsHashes = new HashMap[11];
    private JPanel[] depthPanels = new JPanel[11];
    /*the current depth of the leaves shown*/
    private int currentDepth;
    public TaskDiagramPanel()
    {
        super();
        /*setup name*/
        this.stageName = "Task Diagram";
        this.setBackground(Color.GRAY);
        this.currentDepth = 1; //current depth
        
        /*creating some fake options for testing purposes*/
        ArrayList<Option> options = this.tutorialGenerator(3, "");
        this.tutorialOption = new Option("problem description option - tops", true);
        for(Option option : options)
        {
            this.tutorialOption.addChild(option);
        }
        this.currentTrackerPointer = new OptionTracker(this.tutorialOption);
        for(int i = 0; i < this.depthPanelsHashes.length; i++)
        {
            this.depthPanelsHashes[i] = new HashMap();
            this.depthPanelsHashes[i].clear();
        }
        this.initComponents();
        List<OptionPanel> optionPanels = this.generateOptionPanels(this.currentTrackerPointer, 1);
        this.optionsPanel = new OptionsSelectorPanel(optionPanels);
    }
    /**
     * TESTING
     */
    private ArrayList<Option> tutorialGenerator(int depth, String description)
    {
        if(depth == 0)
        {
            return new ArrayList<Option>();
        }
        else
        {
            ArrayList<Option> options = new ArrayList<Option>();
            Option option;
            for(int i = 0; i < 3; i++)
            {
               ArrayList<Option> subOptions = tutorialGenerator(depth-1, description);
               option = new Option("t option "+i+description, true);
               for(Option subOption : subOptions)
               {
                   option.addChild(subOption);
               }
               options.add(option);
               
            }
            for(int i = 0; i < 3; i++)
            {
                option = new Option("f option "+i, false);
                options.add(option);
            }
            return options;
        }
    }
    private void initComponents()
    {
        //create components
        JLabel titleLabel = new JLabel(this.stageName);
        titleLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        titleLabel.setBackground(Color.GREEN);
        titleLabel.setOpaque(true);
        //setup vertical gridlayout
        GridBagLayout gridBagLayout = new GridBagLayout();
        this.setLayout(gridBagLayout);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(1, 1, 1, 1);
        c.ipady = 0;
        c.anchor = GridBagConstraints.PAGE_START;
        this.add(titleLabel, c);
        
        /*add all of the depth panels to the stage panel*/
        for(int i = 0; i < this.depthPanels.length; i++)
        {
            /*setup the constraints for each panel added*/
            JPanel panel = new JPanel();
            c = new GridBagConstraints();
            c.fill = GridBagConstraints.BOTH;
            c.gridx = 0;
            c.gridy = i + 1;
            c.weightx = 1.0;
            c.weighty = 1.0;
            c.ipady = 10;
            c.insets = new Insets(2,1,2,1);
            c.anchor = GridBagConstraints.PAGE_START;
            panel.setBackground(Color.GRAY);
            panel.setLayout(new GridLayout(1,0,4,4));
            this.depthPanels[i] = panel;
            this.add(panel, c);
        }        
        //setup problem description option
        JPanel setupPanel = this.depthPanels[0];
        OptionPanel setupOption = new OptionPanel(this.tutorialOption);
        setupOption.setState(OptionPanel.OptionState.CORRECT);
        setupPanel.add(setupOption);
        this.depthPanelsHashes[0].put(setupOption, 0);
        //setup unoccupied option panels
        this.addOptionPanels(this.currentDepth, this.currentTrackerPointer);
        //this.createRowOfEmptyOptionPanels(1, this.tutorialOption.getOptions().size());
    }
    @Override
    public OptionsPanel getOptionsPanel() {
        return this.optionsPanel;
    }
    /**
     * drop option panel at a specified location by the x, and y location of the bounds.
     * 
     * @param optionPanel
     * @return 
     */
    @Override
    public int dropOptionPanel(OptionPanel optionPanel) {
        //check if the give option panel can be dropped as one of the children
        int x = (int)optionPanel.getBounds().getCenterX();
        int y = (int)optionPanel.getBounds().getCenterY();
        Point point = new Point(x,y);
        ChildPanelResult childPanelResult = this.getChildPanel(point, 0);
        if(childPanelResult != null)
        {
            OptionPanel childPanel = childPanelResult.optionPanel;
            int optionIndex = childPanelResult.optionIndex;
            Option dropOption = optionPanel.getOption();
            //check whether it was dropped correctly
            OptionTracker optionTracker = this.currentTrackerPointer;
            boolean added = optionTracker.addOptionAt(optionIndex, dropOption);
            if(added)
            {
                if(dropOption.isCorrect())
                {
                    OptionTracker childOptionTracker = optionTracker.getCorrectChild(optionIndex);
                    childPanel.transferOption(dropOption);
                    if(childOptionTracker.isFinished())
                    {
                        //update the child and all parents color
                        childPanel.setState(OptionPanel.OptionState.FINISHED);
                        this.updateParentsColors(childOptionTracker);
                    }
                    else
                    {
                        //green color, for correct, in options selector panel
                        childPanel.setState(OptionPanel.OptionState.CORRECT);
                    }
                    return 0;
                }
                else
                {
                    //color red, for incorrect, in options selector panel
                    return 1;
                }
            }
            else
            {
                //dropOption has already been drop so no
            }
        }
        return 2;
    }
    /**
     * 
     * 
     * @param point         the point that was clicked in the task diagram panel
     */
    @Override
    public void clicked(Point point) {
        //get the panel clicked
        ChildPanelResult childPanelResult = this.getChildPanel(point, 1);
        if(childPanelResult != null)
        {
            OptionPanel optionPanel = childPanelResult.optionPanel;
            //JPanel optionsPanels = childPanelResult.optionsPanel;
            int optionIndex = childPanelResult.optionIndex;
            int depthIndex = childPanelResult.depthIndex;
            
            if(optionPanel.getOption() != null) //this happens when the clicked panel has not option
            {
                Option clickedOption = optionPanel.getOption();
                Option[] optionsList;
                //check here the specific option panel is equals to the given child of 
                //assert(currentTracker.getOption() == clickedOption);
                //get the options panel selector
                OptionsSelectorPanel optionsSelectorPanel = (OptionsSelectorPanel)this.optionsPanel;
                //if option panel is at currentDepth == depth of this panel
                //then this panel should grow to the width of stage panel, and
                //show children
                if(depthIndex == this.currentDepth)
                {
                    if(!this.currentTrackerPointer.isEmptyCorrectChildren()){

                        this.clearRowOfOptionPanels(this.currentDepth);
                        this.addSingleOptionPanel(this.currentDepth, optionPanel);
                        //set up the current tracker
                        this.currentDepth++;
                        this.currentTrackerPointer = this.currentTrackerPointer.getCorrectChild(optionIndex);
                        //add panels according to the input options
                        if(!this.currentTrackerPointer.isEmptyCorrectChildren())
                        {
                            this.addOptionPanels(this.currentDepth, this.currentTrackerPointer);
                            //change panels in options selector
                            List<OptionPanel> optionPanels = this.generateOptionPanels(this.currentTrackerPointer, 1);
                            optionsSelectorPanel.changeOptionPanels(optionPanels);   
                        }
                        else
                        {
                            optionsSelectorPanel.changeOptionPanels(null);
                        }
                    }
                    else
                    {
                        this.currentDepth++;
                    }
                }
                //else if option panel is at currentDepth > depth of this panel
                //then all children depth panels should be removed, and only the
                //children of this panel should be expanded
                else if (depthIndex < this.currentDepth)
                {
                    //clear all rows below the choosen depth
                    for(int i = this.currentDepth; i > depthIndex; i--)
                    {
                        this.clearRowOfOptionPanels(i);
                        if(i - 1 != depthIndex)
                        {
                            this.currentTrackerPointer = this.currentTrackerPointer.getParent();
                        }
                    }
                    this.currentDepth = depthIndex + 1;
                    if(!this.currentTrackerPointer.isEmptyCorrectChildren())
                    {
                        //add panels according to the input options
                        this.addOptionPanels(this.currentDepth, this.currentTrackerPointer);
                        //change panels in options selector
                        List<OptionPanel> optionPanels = this.generateOptionPanels(this.currentTrackerPointer, 1);
                        optionsSelectorPanel.changeOptionPanels(optionPanels);   
                    }
                    else
                    {
                        optionsSelectorPanel.changeOptionPanels(null);
                    }
                }
                else
                {
                    System.err.println("Task Diagram - this should not happen");
                }
            }
        }
    }
    private ChildPanelResult getChildPanel(Point point, int checkAllPanels)
    {
        /*get panel that was clicked*/
        JPanel childrenPanel = null;
        int index;
        if(checkAllPanels == 1)
        {
            for(index = this.depthPanels.length-1; index >= 0; index--)
            {
                /*search for the given panel*/
                JPanel panel = this.depthPanels[index];
                if(this.getComponentAt(point) == panel)
                {
                    childrenPanel = panel;
                    break;
                }
            }
        }
        else         /*check if current depth contains the drop position*/
        {
            index = this.currentDepth;
            JPanel panel = this.depthPanels[index];
            if(this.getComponentAt(point) == panel)
            {
                childrenPanel = panel;
            }
        }
        /*if no panel contained this point*/
        if(childrenPanel == null) {
            return null;
        }
        /*normalize point to */
        int x = (int)childrenPanel.getBounds().getMinX();
        int y = (int)childrenPanel.getBounds().getMinY();
        Point originChildrenPanel = new Point(x, y);
        Point relativePoint = point;
        relativePoint.x = relativePoint.x - originChildrenPanel.x;
        relativePoint.y = relativePoint.y - originChildrenPanel.y;
        /*get which child contains this relative point*/
        JComponent panel = (JComponent)childrenPanel.getComponentAt(relativePoint);
        //System.out.println(""+panel);
        if(panel != null && panel instanceof OptionPanel)
        {
            OptionPanel childPanel = (OptionPanel)panel;
            if(childPanel != null)
            {
                ChildPanelResult childResult = new ChildPanelResult();
                childResult.depthIndex = index;
                childResult.optionIndex = ((Integer)this.depthPanelsHashes[index].get(childPanel)).intValue();
                childResult.optionPanel = childPanel;
                childResult.optionsPanel = childrenPanel;
                return childResult;
            }
        }
        return null;
    }
    private void updateParentsColors(OptionTracker optionTracker)
    {
        int index = this.currentDepth - 1;
        optionTracker = optionTracker.getParent();
        for(; index >= 0; index--)
        {
            OptionPanel optionPanel = (OptionPanel)this.depthPanels[index].getComponent(0);
            if(optionTracker.isFinished())
            {
                optionPanel.setState(OptionPanel.OptionState.FINISHED);
            }
            else
            {
                //don't do anything more
                break;
            }
            optionTracker = optionTracker.getParent();
        }
    }
    private void clearRowOfOptionPanels(int depthRow)
    {
        JPanel depthPanel = this.depthPanels[depthRow];
        HashMap depthPanelHash = this.depthPanelsHashes[depthRow];
        depthPanelHash.clear();
        depthPanel.removeAll();
        depthPanel.revalidate();
    }
    
    private void addOptionPanels(int depthRow, OptionTracker optionTracker)
    {
        HashMap depthPanelHash = this.depthPanelsHashes[depthRow];
        JPanel depthPanel = this.depthPanels[depthRow];
        List<OptionPanel> optionPanels = this.generateOptionPanels(optionTracker, 0);
        int i = 0;
        depthPanelHash.clear();
        for(OptionPanel optionPanel : optionPanels)
        {
            depthPanelHash.put(optionPanel, i);
            depthPanel.add(optionPanel);
            i++;
        }
        depthPanel.revalidate();
        depthPanel.repaint();
    }
    private void addSingleOptionPanel(int depthRow, OptionPanel optionPanel)
    {
        HashMap depthPanelHash = this.depthPanelsHashes[depthRow];
        JPanel depthPanel = this.depthPanels[depthRow];
        depthPanelHash.put(optionPanel, 0);
        depthPanel.add(optionPanel);
        depthPanel.revalidate();
    }

    @Override
    public void scrolled(AWTEvent e) {
        //may later use if the tree is embedded in a scroll pane
    }
    private class ChildPanelResult
    {
        int depthIndex;
        int optionIndex;
        OptionPanel optionPanel;
        JPanel optionsPanel;
    }
}
