/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.ui.main.navigation;

import com.honeybadgers.flltutorial.ui.FLLTutorialUI;
import com.honeybadgers.flltutorial.ui.main.content.PanelReceiver;
import com.honeybadgers.flltutorial.ui.main.content.stages.StagePanel;
import com.honeybadgers.flltutorial.ui.utilities.PanelsScrollPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * NavigationPanel shows a panel with the different stages of a tutorial. As the
 * user goes through the tutorial the navigation panel will show the stage the
 * user is in.
 *
 * @author chingaman
 */
public class NavigationPanel extends JPanel {

    private Dimension preferedDimension = new Dimension(300, 500);
    private Dimension minDimension = new Dimension(300, 400);
    private Dimension maxDimension = new Dimension(32767, 32767);
    private JPanel videoPanel;
    private PanelsScrollPane scrollPane;
    private ArrayList<StagePanel> stages;
    private ArrayList<NavigationOption> options;
    private float aspectRatio = 1.0f;
    private boolean test = false;
    //mapping from text editor to stage panels
    private HashMap stagesMap;
    private PanelReceiver reciever;
    private HashMap stageNumber;
    private int lastUnlocked;   /*number goes from 0 ... numberOfStage-1*/

    /**
     * Constructor of navigation panel sets up the panels that will be used to
     * navigate thru the different stages.
     * 
     * @param stages        the list of stages used in the navigation
     * @param reciever      the responsible class for updating the content pane,
     *                      whenever a another stage is desired by the user, if 
     *                      the user has clicked a desired stage
     */
    public NavigationPanel(ArrayList<StagePanel> stages, PanelReceiver reciever) {
        super();
        this.stages = stages;
        this.reciever = reciever;
        this.options = new ArrayList<>();  
        this.initComponents();
    }
    /**
     * Initializes all of the UI components in the navigation panel.
     */
    private void initComponents() {

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        //TODO: setup video panel
        this.videoPanel = new JPanel();
        this.videoPanel.setBackground(Color.BLACK);
        this.videoPanel.setBorder(null);
        this.videoPanel.setLayout(new GridLayout(1,1));

        this.add(this.videoPanel);
        this.stagesMap = new HashMap();
        this.stageNumber = new HashMap();

        this.add(Box.createVerticalStrut(5));
        
        //initiallized fitted scroll pane for stage panels
        this.scrollPane = new PanelsScrollPane(true);
        this.add(this.scrollPane);

        int i = 0;
        //add all stage panels of application
        for (StagePanel stagePanel : this.stages) {
            JPanel stagePanelNav = new JPanel(new GridLayout(1, 1));
            stagePanelNav.setLayout(new BorderLayout());
            stagePanelNav.setBorder(new LineBorder(Color.BLACK));

            //setup text area inside navigation panels
            NavigationOption option = new NavigationOption(stagePanel);
            this.options.add(option);

            this.stagesMap.put(option, stagePanel);

            this.stageNumber.put(option, i++);

            if (stagePanel.isFinished()) {
                this.lastUnlocked = i;
            }
            //creating title for stage
            stagePanelNav.add(option);
            stagePanelNav.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    System.out.println("clicked this");
                }
            });

            this.scrollPane.appendPanel(stagePanelNav);
        }
        if (test) {
            this.lastUnlocked = this.stages.size() - 1;
        }

        i = 0;
        for (NavigationOption option : this.options) {
            if (test == false) {
                if (i < this.lastUnlocked) {
                    option.setType(NavigationOptionType.Finished);
                } else if (i == this.lastUnlocked) {
                    option.setType(NavigationOptionType.StartedButNotFinished);
                    StagePanel stage = (StagePanel)this.stagesMap.get(option);
                    this.reciever.receivePanel(stage, null);
                    this.setVideoPanel(i);
                } else {
                    option.setType(NavigationOptionType.Locked);
                }
            } else {
                if (option.getStagePanel().isFinished()) {
                    option.setType(NavigationOptionType.Finished);
                } else {
                    option.setType(NavigationOptionType.StartedButNotFinished);
                }
            }
            i++;
        }

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                //resize the video panel accordingly
                int parentWidth = getSize().width;
                int parentHeight = getSize().height;
                int videoHeight = (int) (((float) parentWidth) / aspectRatio);
                float videoToScrollHeightRatio = (((float) videoHeight) / ((float) parentHeight));
                //System.out.println("actuall size of window "+this.videoPanel.getSize());
                //System.out.println("resizing this window "+parentWidth+" "+(int)(((float)parentWidth)/this.aspectRatio));
                if (videoToScrollHeightRatio < 0.75f) {
                    videoPanel.setPreferredSize(new Dimension(parentWidth, videoHeight));
                    videoPanel.repaint();
                    scrollPane.setPreferredSize(new Dimension(parentWidth, parentHeight - videoHeight));
                    revalidate();
                } else {
                    //don't allow the user to resize more than this
                }
            }
        });
        this.setPreferredSize(preferedDimension);
        this.setMinimumSize(minDimension);
        this.setMaximumSize(maxDimension);
    }
    /**
     * This method is called by the Video Panel whenever an aspect ration has been
     * determined for the loaded video. This method must be invoked on the dispatcher
     * thread, because it will be called by another thread used by javafx.
     * 
     * @param aspect       a float denoting the aspect ratio of the video
     */
    public void setAspectRatio(final float aspect)
    {
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                aspectRatio = aspect;
                invalidate();
                setSize(new Dimension(getWidth(), getHeight()+1));
                setSize(new Dimension(getWidth(), getHeight()-1));
            }  
        });
    }
    /**
     * Setup up the video panel with the ith filepath stored in the FLLTutorialUI
     * 
     * @param i         int i denotes the ith filepath
     */
    private void setVideoPanel(int i)
    {
        VideoPanel videoPlayer = new VideoPanel((String)FLLTutorialUI.videoFiles.get(i), this);
        if(this.videoPanel.getComponentCount() > 0){
            VideoPanel oldVideoPlayer = (VideoPanel)this.videoPanel.getComponent(0);
            this.videoPanel.removeAll();
            oldVideoPlayer.kill();
        }else{
            this.videoPanel.removeAll();
        }
        this.videoPanel.add(videoPlayer);
       
        //get video contrast ratio
        this.videoPanel.invalidate();
    }
    /**
     * After the user has finished a stage, the FLLTutorialUI calls this method to
     * make the navigation panel update the colors of the navigation option panels
     * inside it.
     */
    public void updateStages() {
        if (test == false) {
            if (this.stages.get(this.lastUnlocked).isFinished()) {
                //update navigation types, and change stage if there is a next stage
                this.options.get(this.lastUnlocked).setType(NavigationOptionType.Finished);
                if (this.lastUnlocked + 1 < this.options.size()) {
                    JOptionPane.showMessageDialog((JFrame)this.reciever, "finished the "+this.stages.get(this.lastUnlocked).getStageName()+" stage", "stage information", JOptionPane.INFORMATION_MESSAGE);
                    this.lastUnlocked++;
                    this.options.get(this.lastUnlocked).setType(NavigationOptionType.StartedButNotFinished);
                    this.reciever.receivePanel(this.stages.get(this.lastUnlocked), null);
                    this.setVideoPanel(this.lastUnlocked);
                    return;
                }
                JOptionPane.showMessageDialog((JFrame)this.reciever, "finished the "+this.stages.get(this.lastUnlocked).getStageName()+" stage\n\nCongratulations! You have the design process of this tutorial. Your results may be found in file > Project Details menu", "stage information", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            for (NavigationOption option : this.options) {
                if (option.getStagePanel().isFinished()) {
                    option.setType(NavigationOptionType.Finished);
                } else {
                    option.setType(NavigationOptionType.StartedButNotFinished);
                }
            }
        }
    }

    private void resizeImage() {
    }

    private enum NavigationOptionType {

        Finished,
        StartedButNotFinished,
        Locked
    };
    /**
     * Class is specifically used to draw the background of the navigation option
     * panels with different colored gradients.
     */
    private class NavigationOption extends JTextArea {

        NavigationOptionType type;
        StagePanel stagePanel;
        private Color backgroundColor;

        public Color getBackgroundColor() {
            return backgroundColor;
        }

        public void setBackgroundColor(Color backgroundColor) {
            this.backgroundColor = backgroundColor;
        }

        NavigationOption(StagePanel stagePanel) {
            this.stagePanel = stagePanel;
            this.setText(stagePanel.getStageName());
            this.setEditable(false);
            this.setLineWrap(true);
            this.setWrapStyleWord(true);
            this.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
            this.setBorder(new EmptyBorder(4, 4, 4, 4));
            this.setBackground(new Color(0, 0, 0, 0));
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getSource() == null) {
                        return;
                    }
                    NavigationOption option = (NavigationOption) e.getSource();
                    StagePanel stagePanel = (StagePanel) stagesMap.get(e.getSource());
                    int optionIndex = (int) stageNumber.get(option);
                    if (test == false) {
                        if (optionIndex <= lastUnlocked) {
                            reciever.receivePanel(stagePanel, null);
                            setVideoPanel(optionIndex);
                        }
                    } else {
                        reciever.receivePanel(stagePanel, null);
                        setVideoPanel(optionIndex);
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {

                    if (SwingUtilities.isEventDispatchThread()) {
                        if (e.getSource() instanceof JTextArea) {
                            NavigationOption option = (NavigationOption) e.getSource();
                            option.setBackgroundColor(option.getBackgroundColor().darker());
                            option.repaint();
                        } else {
                            System.err.println("NavigationPanel.MouseEntered : source of event is not a JTextArea");
                        }
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (SwingUtilities.isEventDispatchThread()) {
                        if (e.getSource() instanceof JTextArea) {
                            NavigationOption option = (NavigationOption) e.getSource();
                            option.setBackgroundColor(option.getBackgroundColor().brighter());
                            option.repaint();
                        } else {
                            System.err.println("NavigationPanel.MouseExited : source of event is not a JTextArea");
                        }
                    }
                }
            });
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            GradientPaint gradPaint = new GradientPaint(0, 0, backgroundColor, 0, getHeight(), backgroundColor.darker());

            Paint oldPaint = g2.getPaint();

            g2.setPaint(gradPaint);
            g2.fillRect(0, 0, this.getWidth(), this.getHeight());

            g2.setPaint(oldPaint);
            super.paintComponent(g);
        }

        public NavigationOptionType getType() {
            return type;
        }

        public StagePanel getStagePanel() {
            return stagePanel;
        }

        public void setType(NavigationOptionType type) {
            this.type = type;
            switch (this.type) {
                case Finished:
                    backgroundColor = Color.GREEN.darker();
                    break;
                case StartedButNotFinished:
                    backgroundColor = Color.GREEN;
                    break;
                case Locked:
                    backgroundColor = Color.GRAY.brighter();
                    break;
            }
            this.repaint();
        }
    }
}
