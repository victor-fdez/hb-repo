/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.ui;

import com.honeybadgers.flltutorial.model.OptionTracker;
import com.honeybadgers.flltutorial.model.Stage;
import com.honeybadgers.flltutorial.model.Tutorial;
import com.honeybadgers.flltutorial.model.TutorialBase;
import com.honeybadgers.flltutorial.model.backend.TutorialManager;
import com.honeybadgers.flltutorial.ui.begin.AllTutorialsPanel;
import com.honeybadgers.flltutorial.ui.begin.DetailedProjectPanel;
import com.honeybadgers.flltutorial.ui.begin.ProjectPanel;
import com.honeybadgers.flltutorial.ui.begin.TutorialPanel;
import com.honeybadgers.flltutorial.ui.main.content.ContentPane;
import com.honeybadgers.flltutorial.ui.main.content.PanelReceiver;
import com.honeybadgers.flltutorial.ui.main.content.stages.ConsiderationsAndConstraintsPanel;
import com.honeybadgers.flltutorial.ui.main.content.stages.MorphChartPanel;
import com.honeybadgers.flltutorial.ui.main.content.stages.ProblemDescriptionPanel;
import com.honeybadgers.flltutorial.ui.main.content.stages.StagePanel;
import com.honeybadgers.flltutorial.ui.main.content.stages.TaskDiagramPanel;
import com.honeybadgers.flltutorial.ui.main.navigation.NavigationPanel;
import com.honeybadgers.flltutorial.ui.utilities.Blocked;
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
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

/**
 * This class is the main UI class. It works by going through some states and displays
 * different UI depending on the state that is being visited. It initially gives the user
 * the option of either working on a previous project, or creating a new project.
 * Afterwards it displays the project in either the start state of the state in which
 * the project was right when it was saved.
 * 
 * @author chingaman
 */
public class FLLTutorialUI extends javax.swing.JFrame implements PanelReceiver{

    public static Tutorial currentTutorial;
    private PanelsScrollPane tutorialsScrollPane;
    private AllTutorialsPanel allTutorialsPanel;
    private FLLTutorialUI tutorialUI;
    private TutorialBase selectedTutorialBase;
    private Tutorial selectedTutorial;
    private WindowFocusListener windowListener;
    private JFrame extraFrame;
    
    public FLLTutorialUI() {
        super();
        this.tutorialUI = this;
        this.windowListener = new WindowFocusListener(this);
        this.selectedTutorialBase = null;
        this.selectedTutorial = null;
        this.extraFrame = null;
        addWindowListener(windowListener);
        this.setVisible(false);
    }
    //this variable is used change blurry graphics on a previously panel, that was
    //non blurry.
    private Blocked previousSelectedTutorialPanel;
    /**
     * This methods shows all of the available Tutorials, from which either a new project
     * instance can be created, or old projects instances may be listed.
     * 
     * @param tutorials     A list of all Tutorials 
     */
    public void showAllTutorials(final List<TutorialBase> tutorials)
    {
        //all of this will be called from the controller so it should be wrapped in
        //swing runnable
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                extraFrame = new JFrame();
                Container contentPane = extraFrame.getContentPane();
                contentPane.removeAll();
                
                previousSelectedTutorialPanel = null;
                selectedTutorialBase = null;
                allTutorialsPanel = new AllTutorialsPanel();
                allTutorialsPanel.setTitle("All Tutorials");
                allTutorialsPanel.setDescription(null);
                allTutorialsPanel.setUserMessage("select one of the following tutorials");
                allTutorialsPanel.getRemoveAllButton().addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JPanel panel = new JPanel();
                        JLabel label = new JLabel("Enter a password:");
                        JPasswordField pass = new JPasswordField(10);
                        panel.add(label);
                        panel.add(pass);
                        String[] options = new String[]{"OK", "Cancel"};
                        int option = JOptionPane.showOptionDialog(extraFrame, panel, "The title",
                                                 JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                                                 null, options, options[1]);
                        if(option == 0) // pressing OK button
                        {
                            String password = new String(pass.getPassword());
                            if(password.equals("flltutorial"))
                            {
                                File projectFolder = new File(TutorialManager.projectsFolderPath);
                                for(String tutorialProjectFolder : projectFolder.list())
                                {
                                    //System.out.println("--remvoing all files in "+tutorialProjectFolder);
                                    File tutorialProjects = new File(projectFolder, tutorialProjectFolder);
                                    if(!tutorialProjects.isDirectory())
                                    {
                                        continue;
                                    }
                                    
                                    for(String fileName : tutorialProjects.list())
                                    {
                                        //System.out.println("-removing "+fileName);
                                        File project = new File(tutorialProjects, fileName);
                                        project.delete();
                                    }
                                }
                                JOptionPane.showMessageDialog(extraFrame, "cleaned all old project for all tutorials", "cleaned", JOptionPane.INFORMATION_MESSAGE);
                            }
                            else
                            {
                                JOptionPane.showMessageDialog(extraFrame, "you have entered an incorrect password!", "incorrect", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                });
                tutorialsScrollPane = new PanelsScrollPane(true);
                
                //add every tutorial to the scroll pane in the content pane
                allTutorialsPanel.getOpenButton().addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ///System.out.println("opened clicked");
                        if(selectedTutorialBase == null)
                        {
                            //display dialog, telling user he has not selected a tutorial
                            JOptionPane.showMessageDialog(extraFrame, "select a tutorial before continuing", "no tutorial selected", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        
                        showAllOldProjects(selectedTutorialBase);
                        
                    }
                    
                });
                
                allTutorialsPanel.getStartButton().addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ///System.out.println("start clicked");
                        if(selectedTutorialBase == null)
                        {
                            //display dialog, telling user he has not selected a tutorial
                            JOptionPane.showMessageDialog(extraFrame, "select a tutorial before continuing", "no tutorial selected", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        
                        showNewProjectDetails(selectedTutorialBase);
                        
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
                            selectedTutorialBase = TutorialPanel.getTutorialBaseFromBeacon((Component)event.getSource());
                            //show some animation while loading
                            if(previousSelectedTutorialPanel != null)
                            {
                                previousSelectedTutorialPanel.setBlocked(true);
                            }
                            previousSelectedTutorialPanel = TutorialPanel.getTutorialPanelFromBeacon((Component)event.getSource());
                            previousSelectedTutorialPanel.setBlocked(false);
                            //System.out.println("selected "+selectedTutorialBase.getTitle());
                        }
                    });
                    tutorialsScrollPane.appendPanel((JComponent)tutorialPanel);
                }
            
                allTutorialsPanel.getListPanel().add(tutorialsScrollPane);
        
                contentPane.setLayout(new GridLayout(1,1));
                contentPane.add(allTutorialsPanel);
        
                extraFrame.pack();
                setInCenterOfScreen(extraFrame);
                extraFrame.setVisible(true);
                extraFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });
    }
    
    private DetailedProjectPanel detailedProjectPanel; 
    /**
     * This methods shows the details of a new project (to be created), from a given
     * Tutorial. The methods also provides features, to edit the name of the project,
     * the name of the team, and the names of the members of the team. Afterwards if user
     * chooses to create this new project, then it will be created, else the user may
     * choose to not continue.
     * 
     * @param selectedTutorialBase  The tutorial used to create a project instance
     */
    public void showNewProjectDetails(final TutorialBase selectedTutorialBase)
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Container contentPane = extraFrame.getContentPane();
                contentPane.removeAll();
                
                currentTutorial = TutorialManager.getNewProject(selectedTutorialBase);
                detailedProjectPanel = new DetailedProjectPanel(currentTutorial);
               
                detailedProjectPanel.getSaveProjectButton().addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        boolean saveTutorial = detailedProjectPanel.checkSaveIsCorrect(tutorialUI);
                        if(saveTutorial)
                        {
                            //System.out.println("FLLTutorialUI.showNewProjectDetailes : saving new project");
                            Tutorial newProject = detailedProjectPanel.getTutorial();
                            extraFrame.dispose();
                            extraFrame = null;
                            TutorialManager.saveNewProject(newProject);
                            currentTutorial = newProject;
                            startTutorial();
                        }
                    }
                    
                });
                detailedProjectPanel.getCancelButton().addActionListener(new ActionListener(){  
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        showAllTutorials(TutorialManager.getAllTutorialBases());
                        extraFrame.dispose();
                        extraFrame = null;
                    }
                });
                contentPane.setLayout(new GridLayout(1,1));
                contentPane.add(detailedProjectPanel);
        
                extraFrame.pack();
                setInCenterOfScreen(extraFrame);
                extraFrame.setVisible(true);
                extraFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });
    }
    /**
     * This methods displays a list of old projects that were previously created. The
     * User may the proceed to select on of the project to work on what ever was last saved.
     * 
     * @param selectedTutorialBase  the tutorial for which all project instances come from
     */
    private List<Tutorial> projects;
    private void showAllOldProjects(final TutorialBase allProjectsBaseTutorial) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Container contentPane = extraFrame.getContentPane();
                contentPane.removeAll();
                
                selectedTutorial = null;
                allTutorialsPanel = new AllTutorialsPanel();
                allTutorialsPanel.setTitle("All Projects - "+allProjectsBaseTutorial.getTitle());
                allTutorialsPanel.setDescription(allProjectsBaseTutorial.getDescription());
                allTutorialsPanel.setUserMessage("select one of the following projects");
                tutorialsScrollPane = new PanelsScrollPane(true);
        
                allTutorialsPanel.getRemoveAllButton().setVisible(false);
                allTutorialsPanel.getStartButton().setText("cancel");
                allTutorialsPanel.getStartButton().addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ///System.out.println("opened clicked");
                        showAllTutorials(TutorialManager.getAllTutorialBases());
                        extraFrame.dispose();
                        //extraFrame.dispatchEvent(new WindowEvent(extraFrame, WindowEvent.WIN));
                        extraFrame = null;
                    }
                });
                
                allTutorialsPanel.getOpenButton().setText("start project");
                allTutorialsPanel.getOpenButton().addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ///System.out.println("start clicked");
                        if(selectedTutorial == null)
                        {
                            //display dialog, telling user he has not selected a tutorial
                            JOptionPane.showMessageDialog(extraFrame, "select a tutorial before continuing", "no tutorial selected", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        currentTutorial = TutorialManager.getTutorialBaseProject(selectedTutorial);
                        startTutorial();                        
                    }
                });
                
                projects = TutorialManager.getAllTutorialBaseProjects(allProjectsBaseTutorial);
                //add every tutorial to the scroll pane in the content pane
                for(Tutorial tutorialBase : projects)
                {
                    ProjectPanel projectPanel = new ProjectPanel(tutorialBase);
                    projectPanel.getBeacon().addMouseListener(new MouseAdapter(){
                        @Override
                        public void mouseClicked(MouseEvent event)
                        {
                            //System.out.println("this listened to mouse click");
                            //this will actually ask for the list of stages, but still need xml base for that
                            selectedTutorial = ProjectPanel.getTutorialFromBeacon((Component)event.getSource());
                            //show some animation while loading
                            if(previousSelectedTutorialPanel != null)
                            {
                                previousSelectedTutorialPanel.setBlocked(true);
                            }
                            previousSelectedTutorialPanel = ProjectPanel.getTutorialPanelFromBeacon((Component)event.getSource());
                            previousSelectedTutorialPanel.setBlocked(false);
                            //whenever we get real tutorials this will be changed
                        }
                    });
                    tutorialsScrollPane.appendPanel((JComponent)projectPanel);
                }
            
                allTutorialsPanel.getListPanel().add(tutorialsScrollPane);
        
                contentPane.setLayout(new GridLayout(1,1));
                contentPane.add(allTutorialsPanel);
        
                extraFrame.pack();
                setInCenterOfScreen(extraFrame);
                extraFrame.setVisible(true);
                extraFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });
    }
    //some variables storing information of the project tutorial, and the user
    //interfaces
    private List<Stage> stagesList;
    private NavigationPanel navigationPanel;
    private JSplitPane splitPane;
    private ContentPane contentPane;
    public static HashMap videoFiles;
    static
    {
        videoFiles = new HashMap();
        videoFiles.put(0, TutorialManager.generalVideoPath+"video0.flv");
        videoFiles.put(1, TutorialManager.generalVideoPath+"video1.flv");
        videoFiles.put(2, TutorialManager.generalVideoPath+"video2.flv");
        videoFiles.put(3, TutorialManager.generalVideoPath+"video3.flv");
    }
    /**
     * This method will display the current tutorial on the main frame of this class. It
     * will also show a menu at the top with some options the user can take advantage of.
     */
    public void startTutorial()
    {
        this.tutorialUI = this;
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                tutorialUI.setVisible(false);
                //swap out allTutorialsPanel and start tutorial
                tutorialUI.getContentPane().removeAll();
               
                //this would be obtained from the caller
                stagesList = currentTutorial.getStages();
                
                final ArrayList<StagePanel> stagePanels = new ArrayList<>();
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
                
                //create the menu bar
                JMenuBar menuBar = new JMenuBar();
                JMenu fileMenu = new JMenu("File");
                menuBar.add(fileMenu);
                
                
                JMenuItem openMI = new JMenuItem("Open Project");
                openMI.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //System.out.println("FLLTutorialUI.showTutorial : open project");
                        ArrayList<TutorialBase> tutorialBases = TutorialManager.getAllTutorialBases();
                        showAllTutorials(tutorialBases);
                    }
                });
                fileMenu.add(openMI);
                
                JMenuItem saveMI = new JMenuItem("Save Project");
                saveMI.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //System.out.println("FLLTutorialUI.showTutorial : saving project");
                        TutorialManager.saveProject(currentTutorial);
                        JOptionPane.showMessageDialog(extraFrame, "the project \""+currentTutorial.getProjectName()+"\" has been saved!", "project information", JOptionPane.INFORMATION_MESSAGE);
                    }
                });
                fileMenu.add(saveMI);
                
                fileMenu.addSeparator();
                JMenuItem showDetailsMI = new JMenuItem("Project Details");
                showDetailsMI.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //System.out.println("FLLTutorialUI.showTutorial : show project details");
                        ArrayList<OptionTracker> stageOptionTrackers = new ArrayList<OptionTracker>();
                        for(StagePanel stagePanel : stagePanels)
                            stageOptionTrackers.add(stagePanel.getSolutionTracker());
                        ReportUI report = new ReportUI(currentTutorial, stageOptionTrackers);
                    }
                });
                fileMenu.add(showDetailsMI);
                setJMenuBar(menuBar);
                
                contentPane = new ContentPane((PanelReceiver)tutorialUI, stagePanels.get(0));
                navigationPanel = new NavigationPanel(stagePanels, tutorialUI);
                splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

                //initial split pane
                splitPane.setLeftComponent(navigationPanel);
                splitPane.setRightComponent(contentPane);

                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                getContentPane().add(splitPane);
                pack();   
                setInCenterOfScreen(tutorialUI);
                setVisible(true);
                
                tutorialUI.setMinimumSize(tutorialUI.getSize());
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
            return TutorialManager.tutorialsFolderPath+currentTutorial.getTutorialName()+"/";
        }
    }
    /**
     * This method is either used by the content panel or the navigation panel. It
     * is used by the navigation panel whenever the user wants to look at another stage
     * or by the content panel whenever a stage is finished to update the navigation panel.
     * 
     * @param panelSent
     * @param point 
     */
    @Override
    public void receivePanel(JPanel panelSent, Point point) {
        if(panelSent == null)
        {
            //should display message here, and 
            this.navigationPanel.updateStages();
            return;
        }
        //this case used by the navigation panel
        this.contentPane.updateStagePanel((StagePanel)panelSent);
    }
    /**
     * Set the input frame in the center of the screen.
     * 
     * @param frame     the frame to be centered
     */
    public static void setInCenterOfScreen(JFrame frame)
    {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
    }
    /*For testing purposes*/
    public static void setCurrentTutorial(Tutorial currentTutorial)
    {
        FLLTutorialUI.currentTutorial = currentTutorial;
    }
    /**
     * This is used to close frames whenever the main frame is focused.
     */
    public void gotFocus()
    {
        if(this.extraFrame == null)
            return;
        this.extraFrame.dispose();
        this.extraFrame = null;
    }
    /**
     * TESTING
     */
    public static void main(String[] args)
    {
        //look and feel
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
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() 
            {
                ArrayList<TutorialBase> tutorialBases = TutorialManager.getAllTutorialBases();
                ArrayList<Tutorial> projectBases = TutorialManager.getAllTutorialBaseProjects(tutorialBases.get(1));
                Tutorial project = TutorialManager.getTutorialBaseProject(projectBases.get(0));
                FLLTutorialUI topComp = new FLLTutorialUI(); 
                //topComp.showAllTutorials(tutorialBases);
                FLLTutorialUI.setCurrentTutorial(project);
                topComp.startTutorial();
                topComp.setVisible(true);
            }
        });
    }
    
    private class WindowFocusListener implements WindowListener
    {
        private FLLTutorialUI fllTutorial;
        public WindowFocusListener(FLLTutorialUI fllTutorial)
        {
            this.fllTutorial = fllTutorial;
        }
        @Override
        public void windowOpened(WindowEvent e) {
            //System.out.println("opened");
        }

        @Override
        public void windowClosing(WindowEvent e) {
        }

        @Override
        public void windowClosed(WindowEvent e) {
            ///should later save content here
        }

        @Override
        public void windowIconified(WindowEvent e) {
        }

        @Override
        public void windowDeiconified(WindowEvent e) {
        }

        @Override
        public void windowActivated(WindowEvent e) {
            gotFocus();
        }

        @Override
        public void windowDeactivated(WindowEvent e) {
            //System.out.println("not active");
        }
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
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
