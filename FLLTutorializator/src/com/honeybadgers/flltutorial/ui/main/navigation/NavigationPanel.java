/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.ui.main.navigation;

import com.honeybadgers.flltutorial.model.backend.TutorialManager;
import com.honeybadgers.flltutorial.ui.main.content.PanelReceiver;
import com.honeybadgers.flltutorial.ui.main.content.stages.StagePanel;
import com.honeybadgers.flltutorial.ui.main.content.utilities.PictureOptionPanel;
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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
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
    private BufferedImage image;
    private int lastUnlocked;   /*number goes from 0 ... numberOfStage-1*/


    public NavigationPanel(ArrayList<StagePanel> stages, PanelReceiver reciever) {
        super();
        this.stages = stages;
        this.reciever = reciever;
        this.options = new ArrayList<>();
        File moviePicFile = new File(TutorialManager.generalMediaPath + "moviePlaceholder.png");
        try {
            this.image = ImageIO.read(moviePicFile);
            this.aspectRatio = (((float) this.image.getWidth()) / ((float) this.image.getHeight()));
            this.resizeImage();
        } catch (IOException ex) {
            Logger.getLogger(PictureOptionPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.initComponents();
    }

    private void initComponents() {

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        //TODO: setup video panel
        this.videoPanel = new VideoPanel(VideoPanel.MEDIA_URL);/*new JPanel(){
         @Override
         public void paintComponent(Graphics g)
         {
         super.paintComponent(g);
         Graphics2D g2 = (Graphics2D)g;
         g2.drawImage(image, 0, 0, videoPanel.getWidth(), videoPanel.getHeight(), null);
         }
         };*/

        //this.videoPanel.setBackground(Color.BLACK);


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
                //System.out.println("i : "+);
                if (i < this.lastUnlocked) {
                    option.setType(NavigationOptionType.Finished);
                } else if (i == this.lastUnlocked) {
                    option.setType(NavigationOptionType.StartedButNotFinished);
                } else {
                    System.out.println("hey");
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

    public void updateStages() {
        if (test == false) {
            if (this.stages.get(this.lastUnlocked).isFinished()) {
                //update navigation types
                this.options.get(this.lastUnlocked).setType(NavigationOptionType.Finished);
                if (this.lastUnlocked + 1 < this.options.size()) {
                    this.lastUnlocked++;
                    this.options.get(this.lastUnlocked).setType(NavigationOptionType.StartedButNotFinished);
                }
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
                        }
                    } else {
                        reciever.receivePanel(stagePanel, null);
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
                    //System.out.println(e.getSource()+"");
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
