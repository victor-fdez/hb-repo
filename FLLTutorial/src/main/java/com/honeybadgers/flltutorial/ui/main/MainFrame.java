/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.ui.main;

import com.honeybadgers.flltutorial.ui.main.content.ContentPane;
import com.honeybadgers.flltutorial.ui.main.content.PanelReceiver;
import com.honeybadgers.flltutorial.ui.main.content.stages.ConsiderationsAndConstraintsPanel;
import com.honeybadgers.flltutorial.ui.main.content.stages.MorphChartPanel;
import com.honeybadgers.flltutorial.ui.main.content.stages.ProblemDescriptionPanel;
import com.honeybadgers.flltutorial.ui.main.content.stages.StagePanel;
import com.honeybadgers.flltutorial.ui.main.content.stages.StageSelectorPanel;
import com.honeybadgers.flltutorial.ui.main.content.stages.TaskDiagramPanel;
import com.honeybadgers.flltutorial.ui.main.navigation.NavigationPanel;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 * 
 * 
 * @author chingaman
 */
public class MainFrame extends JFrame implements PanelReceiver
{
    NavigationPanel navigationPanel;
    JSplitPane splitPane;
    ContentPane contentPane;
    MainFrame()
    {
        super();
        ArrayList<StagePanel> stagePanels = new ArrayList<StagePanel>();
        stagePanels.add(new MorphChartPanel());
        stagePanels.add(new TaskDiagramPanel());
        stagePanels.add(new ProblemDescriptionPanel());
        stagePanels.add(new ConsiderationsAndConstraintsPanel());
        stagePanels.add(new ProblemDescriptionPanel());
        
        contentPane = new ContentPane(stagePanels.get(0));
        navigationPanel = new NavigationPanel(stagePanels, this);
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        
        /* init split pane */
        splitPane.setLeftComponent(this.navigationPanel);
        splitPane.setRightComponent(this.contentPane);
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().add(splitPane);
        this.pack();    
        this.setVisible(true);

        System.out.println(""+contentPane);
    }
 
     @Override
    public void receivePanel(JPanel panelSent, Point point) {
         
         //ignore the point
         this.contentPane.updateStagePanel((StagePanel)panelSent);
    }
    
    public static void main(String[] args)
    {
        /* setup nimbus look and feel if available */
        try {
            /*
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                System.out.println(""+info.getName());
            }
            */
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            /* if nimbus is not available */
        }
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                JFrame mainFrame = new MainFrame();
            }
        });
    }
}
