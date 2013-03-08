/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.ui;

import com.honeybadgers.flltutorial.model.Stage;
import com.honeybadgers.flltutorial.model.TutorialBase;
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
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    /**
     * Creates new form BeginTopComponent
     */
    public FLLTutorialUI() {
        
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
    private FLLTutorialUI tutorialUI;
    public void startTutorial(List<Stage> stages)
    {
        this.stagesList = stages;
        this.tutorialUI = this;
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                //swap out beginnings and start tutorial
                tutorialUI.getContentPane().removeAll();
               
                ArrayList<StagePanel> stagePanels = new ArrayList<StagePanel>();
                stagePanels.add(new MorphChartPanel());
                stagePanels.add(new TaskDiagramPanel());
                stagePanels.add(new ProblemDescriptionPanel());
                stagePanels.add(new ConsiderationsAndConstraintsPanel());
                stagePanels.add(new ProblemDescriptionPanel());

                contentPane = new ContentPane(stagePanels.get(0));
                navigationPanel = new NavigationPanel(stagePanels, tutorialUI);
                splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

                /* init split pane */
                splitPane.setLeftComponent(navigationPanel);
                splitPane.setRightComponent(contentPane);

                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                getContentPane().add(splitPane);
                pack();    
                setVisible(true);
            }
        });
    }

    @Override
    public void receivePanel(JPanel panelSent, Point point) {
         
         //ignore the point
         this.contentPane.updateStagePanel((StagePanel)panelSent);
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
