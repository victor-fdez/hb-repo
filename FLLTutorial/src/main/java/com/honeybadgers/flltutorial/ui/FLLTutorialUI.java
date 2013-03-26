/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.ui;

import com.honeybadgers.flltutorial.model.Stage;
import com.honeybadgers.flltutorial.model.Tutorial;
import com.honeybadgers.flltutorial.model.TutorialBase;
import com.honeybadgers.flltutorial.model.backend.TutorialManager;
import com.honeybadgers.flltutorial.ui.begin.AllTutorialsPanel;
import com.honeybadgers.flltutorial.ui.begin.DetailedProjectPanel;
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author chingaman
 */
public class FLLTutorialUI extends javax.swing.JFrame implements PanelReceiver{

    public static Tutorial currentTutorial;
    private PanelsScrollPane tutorialsScrollPane;
    private AllTutorialsPanel beginnings;
    private FLLTutorialUI tutorialUI;
    private TutorialBase selectedTutorial;
    /**
     * Creates new form BeginTopComponent
     */
    public FLLTutorialUI() {
        super();
        this.tutorialUI = this;
        this.selectedTutorial = null;
        this.showAllTutorials(TutorialManager.getAllTutorialBases());
    }
    
    public void showAllTutorials(final List<TutorialBase> tutorials)
    {
        //all of this will be called from the controller so it should be wrapped in
        //swing runnable
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Container contentPane = getContentPane();
                
                beginnings = new AllTutorialsPanel();
                tutorialsScrollPane = new PanelsScrollPane(true);
        
                /*List<TutorialBase> tutorialsList = new ArrayList<>();
                tutorialsList.add(new TutorialBase("car tutorial","Victor Fernandez", "The design process of a car factory"));
                tutorialsList.add(new TutorialBase("robot exercise","Taylor Peet", "The design process of a robot exercise aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));
                tutorialsList.add(new TutorialBase("cake factory tutorial","Pushkara", "The design process of a cake baking robot"));
                tutorialsList.add(tutorials.remove(0));*/
                //add every tutorial to the scroll pane in the content pane
                beginnings.getOpenButton().addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("opened clicked");
                    }
                });
                
                beginnings.getStartButton().addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("start clicked");
                        if(selectedTutorial == null)
                        {
                            //display dialog, telling user he has not selected a tutorial
                            JFrame frame = new JFrame();
                            JOptionPane.showMessageDialog(null, "select a tutorial before starting", "no tutorial selected", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        
                        showNewProjectDetails(selectedTutorial);
                        
                    }
                });
                
                for(TutorialBase tutorialBase : tutorials)
                {
                    TutorialPanel tutorialPanel = new TutorialPanel(tutorialBase);
                    tutorialPanel.getBeacon().addMouseListener(new MouseAdapter(){
                        @Override
                        public void mouseClicked(MouseEvent event)
                        {
                            //this will actually ask for the list of stages, but still need xml base for that
                            selectedTutorial = TutorialPanel.getTutorialBaseFromBeacon((Component)event.getSource());
                            //show some animation while loading
                            System.out.println("selected "+selectedTutorial.getTitle());
                        }
                    });
                    tutorialsScrollPane.appendPanel((JComponent)tutorialPanel);
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
    
    private DetailedProjectPanel detailedProjectPanel; 
    
    public void showNewProjectDetails(final TutorialBase selectedTutorialBase)
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Container contentPane = getContentPane();
                contentPane.removeAll();
                
                currentTutorial = TutorialManager.getNewProject(selectedTutorialBase);
                detailedProjectPanel = new DetailedProjectPanel(currentTutorial);
               
                detailedProjectPanel.getSaveProjectButton().addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        boolean saveTutorial = detailedProjectPanel.checkSaveIsCorrect();
                        if(saveTutorial)
                        {
                            System.out.println("FLLTutorialUI.showNewProjectDetailes : saving new project");
                            Tutorial newProject = detailedProjectPanel.getTutorial();
                            TutorialManager.saveNewProject(newProject);
                            currentTutorial = newProject;
                            startTutorial();
                        }
                    }
                    
                });
                contentPane.setLayout(new GridLayout(1,1));
                contentPane.add(detailedProjectPanel);
        
                pack();
                setInCenterOfScreen();
                setVisible(true);
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });
    }
    
    public void showProjects(final List<TutorialBase> projects)
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Container contentPane = getContentPane();
                contentPane.removeAll();
                
                beginnings = new AllTutorialsPanel();
                tutorialsScrollPane = new PanelsScrollPane(true);
        
                /*List<TutorialBase> tutorialsList = new ArrayList<>();
                tutorialsList.add(new TutorialBase("car tutorial","Victor Fernandez", "The design process of a car factory"));
                tutorialsList.add(new TutorialBase("robot exercise","Taylor Peet", "The design process of a robot exercise aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));
                tutorialsList.add(new TutorialBase("cake factory tutorial","Pushkara", "The design process of a cake baking robot"));
                tutorialsList.add(tutorials.remove(0));*/
                //add every tutorial to the scroll pane in the content pane
                for(TutorialBase tutorialBase : projects)
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
                            startTutorial();
                            //System.out.println(tutorialBase.getTitle());
                        }
                    });
                    tutorialsScrollPane.appendPanel((JComponent)tutorialPanel);
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
    public void startTutorial()
    {
        this.tutorialUI = this;
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                //swap out beginnings and start tutorial
                tutorialUI.getContentPane().removeAll();
               
                //create the menu bar
                JMenuBar menuBar = new JMenuBar();
                JMenu fileMenu = new JMenu("File");
                menuBar.add(fileMenu);
                
                
                JMenuItem openMI = new JMenuItem("Open Project");
               openMI.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("FLLTutorialUI.showTutorial : open project");
                    }
                });
                fileMenu.add(openMI);
                
                JMenuItem saveMI = new JMenuItem("Save Project");
                saveMI.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("FLLTutorialUI.showTutorial : saving project");
                    }
                });
                fileMenu.add(saveMI);
                
                fileMenu.addSeparator();
                JMenuItem showDetailsMI = new JMenuItem("Show Project Details");
                showDetailsMI.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("FLLTutorialUI.showTutorial : show project details");
                    }
                });
                fileMenu.add(showDetailsMI);
                
                fileMenu.addSeparator();
                JMenuItem createReportMI = new JMenuItem("Create Project Report");
                createReportMI.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("FLLTutorialUI.showTutorial : create project report");
                    }
                });
                fileMenu.add(createReportMI);
                
                setJMenuBar(menuBar);
                
                //this would be obtained from the caller
                stagesList = currentTutorial.getStages();
                
                ArrayList<StagePanel> stagePanels = new ArrayList<>();
                for(Stage stage : stagesList)
                {
                    switch(stage.getName())
                    {
                        case "Problem Statement":
                            stagePanels.add(new ProblemDescriptionPanel(stage.getRootOption()));
                            //System.out.println("generating problem statement");
                            break;
                        case "Limitations and Constraints":
                            stagePanels.add(new ConsiderationsAndConstraintsPanel(stage.getRootOption()));
                            //System.out.println("generating lims and consts");
                            break;
                        case "Task Diagram":
                            stagePanels.add(new TaskDiagramPanel(stage.getRootOption()));
                            //System.out.println("generating task diagram");
                            break;
                        case "Morphological Chart":
                            stagePanels.add(new MorphChartPanel(stage.getRootOption()));
                            //System.out.println("generating morph chart");
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
    
    public static String getMainTutorialDirectoryPath()
    {
        if(currentTutorial == null)
        {
            return "";
        }
        else
        {
            return "src/main/resources/Tutorials/"+currentTutorial.getTutorialName()+"/";
        }
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
            @Override
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
