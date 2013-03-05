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
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
public class TaskDiagramPanel extends StagePanel implements MouseListener{

    /*selected options, there are the options the student chooses*/
    private OptionTracker projectTracker;
    /*pointers into respective trees*/
    private OptionTracker currentTrackerPointer;
    /*array of panels to hold current options shown*/
    private HashMap[] depthPanelsHashes = new HashMap[11];
    private JPanel[] depthPanels = new JPanel[11];
    private HashMap childPanelBeaconsHashes;
    private HashMap parentPanelBeaconsHashes;
    /*the current depth of the leaves shown*/
    private int currentDepth;
    public TaskDiagramPanel()
    {
        super();
        //setup object variables
        this.stageName = "Task Diagram";
        this.setBackground(Color.GRAY);
        this.currentDepth = 1; //current depth
        this.childPanelBeaconsHashes = new HashMap();
        this.parentPanelBeaconsHashes = new HashMap();
        
        //creating some fake options for testing purposes
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
        
        //setup option panel
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
               ArrayList<Option> subOptions = tutorialGenerator(depth-1, description+" longer description");
               option = new Option("t option "+i+description, true);
               for(Option subOption : subOptions)
               {
                   option.addChild(subOption);
               }
               options.add(option);
               
            }
            for(int i = 0; i < 20; i++)
            {
                option = new Option("f option "+i, false);
                options.add(option);
            }
            return options;
        }
    }
    private void initComponents()
    {
        Component beacon;

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
        
            //add all of the depth panels to the stage panel
            for(int i = 0; i < this.depthPanels.length; i++)
            {
                //setup the constraints for each panel added
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
        //should make to setup the tutorial at a specific depth
        JPanel setupPanel = this.depthPanels[0];
        OptionPanel setupOption = new OptionPanel(this.tutorialOption);
        setupOption.setState(OptionPanel.OptionState.CORRECT);
        setupPanel.add(setupOption);
        this.depthPanelsHashes[0].put(setupOption, 0);
        
        //setup head beacon
        beacon = setupOption.getBeacon();
        beacon.addMouseListener(this);
        this.parentPanelBeaconsHashes.put(beacon, 0);
               
        //setup unoccupied option panels
        this.addOptionPanels(this.currentDepth, this.currentTrackerPointer);
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
        
        //remove all mouse listeners for this panel
        for(Component optionPanel : depthPanel.getComponents())
        {
            optionPanel.removeMouseListener(this);
        }
        this.childPanelBeaconsHashes.clear();
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
        this.childPanelBeaconsHashes.clear();
        //rm
        depthPanelHash.clear();
        for(OptionPanel optionPanel : optionPanels)
        {
            Component beacon = optionPanel.getBeacon();
            //rm
            depthPanelHash.put(optionPanel, i);
            beacon.addMouseListener(this);
            this.childPanelBeaconsHashes.put(beacon, i);
            
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
    /**
     * TODO: to make this work we need to add mouse listener to all the clickable
     * components.
     * 
     * @param e 
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("TaskDiagram.mouseClicked : clicked something");
        //everything will be handeled here instead of clicked
        Component beacon = (Component)e.getSource();
        if(beacon == null)
        {
            return;
        }
        
        //check if the beacon is for a child
        Object childIndex = this.childPanelBeaconsHashes.get(beacon);
        if(childIndex != null)
        {
            int cIndex;
            OptionPanel childPanel;
            OptionsSelectorPanel childOptionsPanel;

            System.out.println("TaskDiagram.mouseClicked : child clicked");
            
            //get child data structs
            cIndex = (int)childIndex;
            childPanel = OptionPanel.getOptionPanelFromBeacon(beacon);
            
            if(childPanel.getOption() == null) //don't do anything
            {
                return;
            }
            //clear up all option panels in this panel except this child, and add children
            //of the current child, if it has any children
            this.clearRowOfOptionPanels(this.currentDepth);
            
            //setup has for beacon of child, because add singledoes not add hash
            this.parentPanelBeaconsHashes.put(beacon, this.currentDepth);
           
            //add child as only panel at the current depth 
            this.addSingleOptionPanel(this.currentDepth, childPanel);
            
            if(this.currentTrackerPointer.isEmptyCorrectChildren())
            {
                return;
            }
            
            //setup tracking information
            this.currentDepth++;
            this.currentTrackerPointer = this.currentTrackerPointer.getCorrectChild(cIndex);
            
            //add children panels if it has any
            childOptionsPanel = ((OptionsSelectorPanel)this.optionsPanel);
            if(this.currentTrackerPointer.isEmptyCorrectChildren())
            {
                childOptionsPanel.updateOptionPanels(null);
                return;
            }
            this.addOptionPanels(this.currentDepth, this.currentTrackerPointer);
            List<OptionPanel> childPanels = this.generateOptionPanels(this.currentTrackerPointer, 1);
            childOptionsPanel.updateOptionPanels(childPanels); 
            return;
        }
        
        Object parentIndex = this.parentPanelBeaconsHashes.get(beacon);
        if(parentIndex != null)
        {
            int pIndex;
            OptionPanel parentPanel;
            OptionsSelectorPanel childOptionsPanel;
            System.out.println("TaskDiagram.mouseClicked : parent clicked");

            
            //parent data structs
            pIndex = (int)parentIndex;
            parentPanel = OptionPanel.getOptionPanelFromBeacon(beacon);

            //remove all panels at depth smaller than this parent, and
            for(int depthIndex = this.currentDepth; depthIndex > pIndex; depthIndex--)
            {
                this.clearRowOfOptionPanels(depthIndex);
                //setup parent current tracker point
                if((depthIndex - 1) > pIndex)
                {
                    this.currentTrackerPointer = this.currentTrackerPointer.getParent();
                }
            }
            this.currentDepth = pIndex + 1;
            
            //if current tracker does not have any empty correct children
            childOptionsPanel = (OptionsSelectorPanel)this.optionsPanel;
            if(this.currentTrackerPointer.isEmptyCorrectChildren())
            {
                System.out.println("TaskDiagram.mouseClicked : parent childs not added");
                childOptionsPanel.updateOptionPanels(null);
                return;
            }
            System.out.println("TaskDiagram.mouseClicked : parent childs added");
            //else add children and finish
            this.addOptionPanels(this.currentDepth, this.currentTrackerPointer);
            
            //change panels in options selector
            List<OptionPanel> optionPanels = this.generateOptionPanels(this.currentTrackerPointer, 1);
            childOptionsPanel.updateOptionPanels(optionPanels);   
            return;
        }
        
        System.err.println("TaskDiagram.mouseClicked : a beacon that is not a parent or child has been clicked");
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
    private class ChildPanelResult
    {
        int depthIndex;
        int optionIndex;
        OptionPanel optionPanel;
        JPanel optionsPanel;
    }
}
