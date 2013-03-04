/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.ui.main.content.stages;

import com.honeybadgers.flltutorial.model.Option;
import com.honeybadgers.flltutorial.model.OptionTracker;
import com.honeybadgers.flltutorial.ui.main.content.OptionsPanel;
import com.honeybadgers.flltutorial.ui.main.content.OptionsSelectorPanel;
import com.honeybadgers.flltutorial.ui.main.content.utilities.OptionPanel;
import com.honeybadgers.flltutorial.ui.utilities.PanelsScrollPane;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

/**
 * This class display a morph chart, with a specified number of rows, where a selected row
 * can be chosen and then be filled in with the correct answers.
 * 
 * @author chingaman
 */
public class MorphChartPanel extends StagePanel implements MouseListener{

    
    protected enum BeaconType
    {
        //ChildPanel,
        SelectedParentPanel,
        ParentPanel
    };
    protected PanelsScrollPane scrollPane;
    protected OptionPanel selectedPanel;
    /*used to look up the kind of panel that was clicked*/
    protected HashMap panelTypeHashes;
    public MorphChartPanel()
    {
        super();
        this.stageName = "Morph Chart";
        this.morphChartGenerator();
        
        //show the options of the first child
        List<OptionPanel> optionPanels = this.generateOptionPanels(this.solutionTracker.getCorrectChild(0), 1);
        this.optionsPanel = new OptionsSelectorPanel(optionPanels);
        this.panelTypeHashes = new HashMap();
        this.initComponents();
    }
    private void initComponents()
    {
        //setup title of stage
        JTextArea titleLabel = new JTextArea(this.stageName);
        titleLabel.setLineWrap(true);
        titleLabel.setWrapStyleWord(true);
        titleLabel.setBorder(new EmptyBorder(4, 4, 4, 4));
        //titleLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        titleLabel.setBackground(Color.GREEN);
        
        //add title to selector problem
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(10, 4, 4, 4);
        c.ipady = 0;
        c.weightx = 1.0;
        c.anchor = GridBagConstraints.PAGE_START;
        this.add(titleLabel, c);
        
        //add main option description option panel
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(10, 4, 4, 4);
        c.ipady = 0;
        c.weightx = 1.0;
        c.anchor = GridBagConstraints.PAGE_START;
        this.add(new OptionPanel(this.problem), c);
        
        //setup scroll pane
        this.scrollPane = new PanelsScrollPane(true);
        
            //add row panels to scroll pane
            int i = 0;
            for(OptionPanel leftMostOptionPanel : this.generateOptionPanels(this.solutionTracker, 1))
            {
                //setup beacon for top most elements
                Component topBeacon = leftMostOptionPanel.getBeacon();
                topBeacon.addMouseListener(this);
                if(i == 0)
                {
                    this.selectedPanel = leftMostOptionPanel;
                    this.panelTypeHashes.put(topBeacon, BeaconType.SelectedParentPanel);
                }
                else
                {
                   this.panelTypeHashes.put(topBeacon, BeaconType.ParentPanel);
                }
                //create a JPanel with one elements at the front, and then create empty
                //option panels in a sub panel. All of the options panels in the subpanel
                //will be equally sized
                JPanel rowPanel = new JPanel();
                rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
                rowPanel.setBackground(Color.WHITE);
                
                rowPanel.add(Box.createRigidArea(new Dimension(0,100)));
                
                leftMostOptionPanel.setPreferredSize(new Dimension(100,100));
                leftMostOptionPanel.setMaximumSize(new Dimension(100,100));
                rowPanel.add(leftMostOptionPanel);
                
                    //setup the right children panel
                    //JPanel childrenContainer = new JPanel(new GridLayout(1,1));
                    PanelsScrollPane childrenPanel = new PanelsScrollPane(false);//new JPanel(new GridLayout(0,1,2,2));
                
                //rowPanel.add(Box.createRigidArea(new Dimension(0,100)));
                //childrenContainer.add(childrenPanel);
                rowPanel.add(childrenPanel);
                //rowPanel.add(Box.createRigidArea(new Dimension(0,100)));
                //rowPanel.add(childrenPanel, c);
                
                        //add panels to children panel                        
                        for(OptionPanel childPanel : this.generateOptionPanels(this.solutionTracker.getCorrectChild(i),0))
                        {
                            childrenPanel.appendPanel(childPanel);
                        }
                
                this.scrollPane.appendPanel(rowPanel);
                rowPanel.revalidate();
                i++;
            }
    
        //add scroll pane 
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(4, 4, 4, 4);
        c.ipady = 0;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.anchor = GridBagConstraints.PAGE_START;
        this.add(this.scrollPane, c);
        
        this.setBackground(Color.GRAY);
        this.revalidate();
        this.scrollPane.revalidate();
        this.repaint();
    }
    private void morphChartGenerator()
    {
        Option morphChartOption = new Option("Click a row, then chose whichever options you think will enable you to do the given functionality", true);
        OptionTracker morphChartTracker;
        //create an array of rows for the chart
        for(int i = 0; i < 8; i++)
        {
            Option rowOption = new Option("functionality "+i, true);
            for(int j = 0; j < (((i % 4) == 0) ? 8 : 3); j++)
            {
                Option childOption = new Option("you should clearly choose this options becuase all the others just don't make sense", ((j % 3) != 0));
                rowOption.addChild(childOption);
            }
            morphChartOption.addChild(rowOption);
        }
        morphChartTracker = new OptionTracker(morphChartOption);
        int k = 0;
        for(Option rowOption : morphChartTracker.getOption().getOptions())
        {
            morphChartTracker.addOptionAt(k, rowOption);
            k++;
        }
        
        //assign problem and tracker
        this.problem = morphChartOption;
        this.solutionTracker = morphChartTracker;
    }
    /**
     * Returns the kind of options panel used to give the user multiple options as
     * possible answers.
     * 
     * @return 
     */
    @Override
    public OptionsPanel getOptionsPanel() {
        return this.optionsPanel;
    }
    /**
     * The panel will be dropped on the row which is currently selected, and checked
     * for correctness.
     * 
     * @param optionPanel
     * @return 
     */
    @Override
    public int dropOptionPanel(OptionPanel optionPanel) {
        //check whether panel falls in any of the child option panels, of the currently
        //selected panel.
        
        //
        return 0;
    }
    /**
     * When the user picks a specific row the possible answers for that row will be displayed
     * and then he or she will be able to fill that row with the answers.
     * 
     * @param point 
     */
    @Override
    public void clicked(Point point) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void scrolled(AWTEvent e) {
        this.scrollPane.dispatchEvent(e);
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("MorphChartPanel.mouseClicked : clicked something");

        Component beacon = (Component)e.getSource();
        if(beacon == null)
        {
            System.out.println("MorphChartPanel.mouseClicked : got a null beacon");
            return;
        }
        Object result = this.panelTypeHashes.get(beacon);
        if(result == null || !(result instanceof BeaconType))
        {
            System.out.println("MorphChartPanel.mouseClicked : no result for beacon");
            return;
        }
        BeaconType beaconType = (BeaconType)result;
        switch(beaconType)
        {
            case SelectedParentPanel:
                //this does nothing, because the parent panel is already selected
                System.out.println("MorphChartPanel.mouseClicked : clicked selected parent panel");
                break;
            case ParentPanel:
                //this changes the options panel, and adds it's own children to it. Also All child
                //panels are deleted from hash, and new ones are added. Parent becomes the selected parent panel.
                System.out.println("MorphChartPanel.mouseClicked : clicked parent panel");
                break;
            default:
                break;
        }
        
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
    
    
    
}
