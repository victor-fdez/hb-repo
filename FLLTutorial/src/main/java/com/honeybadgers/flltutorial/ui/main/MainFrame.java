/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.ui.main;

import com.honeybadgers.flltutorial.ui.main.content.ContentPane;
import com.honeybadgers.flltutorial.ui.main.content.stages.ProblemDescriptionPanel;
import com.honeybadgers.flltutorial.ui.main.content.stages.StagePanel;
import com.honeybadgers.flltutorial.ui.main.content.stages.StageSelectorPanel;
import com.honeybadgers.flltutorial.ui.main.content.stages.TaskDiagramPanel;
import com.honeybadgers.flltutorial.ui.main.navigation.NavigationPanel;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 *
 * @author chingaman
 */
public class MainFrame extends JFrame
{
    NavigationPanel navigationPanel;
    JSplitPane splitPane;
    ContentPane contentPane;
    MainFrame()
    {
        super();
        contentPane = new ContentPane();
        ArrayList<StagePanel> stagePanels = new ArrayList<StagePanel>();
        stagePanels.add(new TaskDiagramPanel());
        stagePanels.add(new ProblemDescriptionPanel());
        navigationPanel = new NavigationPanel(stagePanels);
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        
        /* init split pane */
        splitPane.setLeftComponent(this.navigationPanel);
        splitPane.setRightComponent(this.contentPane);
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().add(splitPane);
        this.pack();    
        this.setVisible(true);
        //this.validate();
        //this.repaint();

        System.out.println(""+contentPane);
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
