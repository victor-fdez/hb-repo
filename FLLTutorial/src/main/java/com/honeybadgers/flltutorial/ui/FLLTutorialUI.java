/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.ui;

import com.honeybadgers.flltutorial.model.Stage;
import com.honeybadgers.flltutorial.model.Tutorial;
import com.honeybadgers.flltutorial.model.TutorialBase;
import com.honeybadgers.flltutorial.model.backend.XMLBase;
import com.honeybadgers.flltutorial.ui.begin.Beginnings;
import com.honeybadgers.flltutorial.ui.begin.TutorialPanel;
import com.honeybadgers.flltutorial.ui.main.content.ContentPane;
import com.honeybadgers.flltutorial.ui.main.content.PanelReceiver;
import com.honeybadgers.flltutorial.ui.main.content.stages.ConsiderationsAndConstraintsPanel;
import com.honeybadgers.flltutorial.ui.main.content.stages.MorphChartPanel;
import com.honeybadgers.flltutorial.ui.main.content.stages.ProblemDescriptionPanel;
import com.honeybadgers.flltutorial.ui.main.content.stages.StagePanel;
import com.honeybadgers.flltutorial.ui.main.content.stages.TaskDiagramPanel;
import com.honeybadgers.flltutorial.ui.main.navigation.NavigationPanel;
import com.honeybadgers.flltutorial.ui.utilities.PanelsScrollPane;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author chingaman
 */
public class FLLTutorialUI extends javax.swing.JFrame implements PanelReceiver{

    private PanelsScrollPane tutorialsScrollPane;
    private Beginnings beginnings;
    private FLLTutorialUI tutorialUI;
    /**
     * Creates new form BeginTopComponent
     */
    public FLLTutorialUI() {
        super();
        this.tutorialUI = this;
        this.beginnings(null);
    }
    
    public void beginnings(List<TutorialBase> tutorials)
    {
        //all of this will be called from the controller so it should be wrapped in
        //swing runnable
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Container contentPane = getContentPane();
                
                beginnings = new Beginnings();
                tutorialsScrollPane = new PanelsScrollPane(true);
        
                List<TutorialBase> tutorialsList = new ArrayList<>();
                tutorialsList.add(new TutorialBase("car tutorial","Victor Fernandez", "The design process of a car factory"));
                tutorialsList.add(new TutorialBase("robot exercise","Taylor Peet", "The design process of a robot exercise"));
                tutorialsList.add(new TutorialBase("cake factory tutorial","Pushkara", "The design process of a cake baking robot"));
        
                //add every tutorial to the scroll pane in the content pane
                for(TutorialBase tutorialBase : tutorialsList)
                {
                    TutorialPanel tutorialPanel = new TutorialPanel(tutorialBase);
                    tutorialPanel.getBeacon().addMouseListener(new MouseAdapter(){
                        @Override
                        public void mouseClicked(MouseEvent event)
                        {
                            //this will actually ask for the list of stages, but still need xml base for that
                            TutorialBase tutorialBase = TutorialPanel.getTutorialBaseFromBeacon((Component)event.getSource());
                            //show some animation while loading
                            setTitle(tutorialBase.getTitle());
                            //whenever we get real tutorials this will be changed
                            startTutorial(null);
                            //System.out.println(tutorialBase.getTitle());
                        }
                    });
                    tutorialsScrollPane.appendPanel(tutorialPanel);
                }
            
                beginnings.getListPanel().add(tutorialsScrollPane);
        
                contentPane.setLayout(new GridLayout(1,1));
                contentPane.add(beginnings);
        
                pack();
                setInCenterOfScreen();
                setVisible(true);
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });
    }
    /**
     * Called by TutorialManager when it has finished getting the stages of this tutorial
     * 
     * @param stages 
     */
    private List<Stage> stagesList;
    private NavigationPanel navigationPanel;
    private JSplitPane splitPane;
    private ContentPane contentPane;
    public void startTutorial(List<Stage> stages)
    {
        this.stagesList = stages;
        this.tutorialUI = this;
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                //swap out beginnings and start tutorial
                tutorialUI.getContentPane().removeAll();
               
                //this would be obtained from the caller
                Tutorial tutorial = XMLBase.loadTutorial(new File("src/main/resources/sampleTutorial/tut1-project.xml"));
                stagesList = tutorial.getStages();
                
                ArrayList<StagePanel> stagePanels = new ArrayList<>();
                for(Stage stage : stagesList)
                {
                    switch(stage.getName())
                    {
                        case "Problem Statement":
                            stagePanels.add(new ProblemDescriptionPanel(stage.getRootOption()));
                            System.out.println("generating problem statement");
                            break;
                        case "Limitations and Constraints":
                            stagePanels.add(new ConsiderationsAndConstraintsPanel(stage.getRootOption()));
                            System.out.println("generating lims and consts");
                            break;
                        case "Task Diagram":
                            stagePanels.add(new TaskDiagramPanel(stage.getRootOption()));
                            System.out.println("generating task diagram");
                            break;
                        case "Morphological Chart":
                            stagePanels.add(new MorphChartPanel(stage.getRootOption()));
                            System.out.println("generating morph chart");
                            break;
                    }
                    
                }
                
                contentPane = new ContentPane(stagePanels.get(0));
                navigationPanel = new NavigationPanel(stagePanels, tutorialUI);
                splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

                //initial split pane
                splitPane.setLeftComponent(navigationPanel);
                splitPane.setRightComponent(contentPane);

                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                getContentPane().add(splitPane);
                pack();   
                setInCenterOfScreen();
                setVisible(true);
            }
        });
    }

    @Override
    public void receivePanel(JPanel panelSent, Point point) {
         
         //ignore the point
         this.contentPane.updateStagePanel((StagePanel)panelSent);
    }
    
    public void setInCenterOfScreen()
    {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
    }
    
    public static void startUI()
    {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FLLTutorialUI();
            }
        });
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FLLTutorialUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FLLTutorialUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FLLTutorialUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FLLTutorialUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        FLLTutorialUI.startUI();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
