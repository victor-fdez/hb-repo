/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.ui.main.navigation;

import com.honeybadgers.flltutorial.model.backend.TutorialManager;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import javafx.animation.FadeTransition;
import javafx.animation.FadeTransitionBuilder;
import javafx.animation.ScaleTransition;
import javafx.animation.ScaleTransitionBuilder;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.control.SliderBuilder;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.media.MediaViewBuilder;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.LinearGradientBuilder;
import javafx.scene.paint.Stop;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.RectangleBuilder;
import javafx.util.Duration;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author chingaman
 */
public class VideoPanel extends JPanel {
    
    private static final int JFXPANEL_WIDTH_INT = 300;
    private static final int JFXPANEL_HEIGHT_INT = 250;
    public static final String MEDIA_URL = TutorialManager.generalVideoPath+"linus.flv";
    private static JFXPanel fxContainer;
    private static VideoPanel videoPanel;
    private Scene scene;
    private MediaPlayer mp;
    private String videoPathName;
    private Media videoMedia;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
                } catch (Exception e) {
                }
                
                JFrame frame = new JFrame("JavaFX 2 in Swing");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                
                VideoPanel panel = new VideoPanel(VideoPanel.MEDIA_URL);
                
                frame.getContentPane().add(panel);
                
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
    
    public VideoPanel(String videoFilePath)
    {
        super();
        this.videoPathName = new File(videoFilePath).toURI().toString();
        VideoPanel.videoPanel = this;
        this.setBackground(java.awt.Color.BLACK);
        this.setLayout(new BorderLayout());
        
        this.initMediaPlayer();
    }
    
    public void initMediaPlayer() {
        fxContainer = new JFXPanel();
        fxContainer.setMinimumSize(new Dimension(JFXPANEL_WIDTH_INT, JFXPANEL_HEIGHT_INT));
        fxContainer.setPreferredSize(new Dimension(JFXPANEL_WIDTH_INT, JFXPANEL_HEIGHT_INT));
        /*fxContainer.addComponentListener(new ComponentAdapter(){
            @Override
            public void componentResized(ComponentEvent cEv)
            {
                Platform.runLater(new Runnable(){
                    @Override
                    public void run() {
                        if(scene != null){
                            System.out.println("w "+scene.getWidth()+" h "+scene.getHeight());
                        }
                    }
                });
            }
        });*/

        this.add(fxContainer, BorderLayout.CENTER);
        
        this.setBorder(new EmptyBorder(0,0,0,0));
        
        //create java fx scene
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                createScene();
            }
        });
    }
    
    private void createScene() {        
        //setup root of media player
        System.out.println("VideoPanel.createScene: openning "+this.videoPathName);
        final StackPane stackPane = new StackPane();

        this.scene = new Scene(stackPane);
        fxContainer.setScene(this.scene);
        
        //setup the main video panel
        Media m = new Media(this.videoPathName);
        this.mp = new MediaPlayer(m);
        this.mp.setVolume(1.0f);
        
        MediaView mediaView = MediaViewBuilder.create()
                .mediaPlayer(this.mp)
                .preserveRatio(true)
                .opacity(1.0f)
                .smooth(true)
                .build();
        
        stackPane.getChildren().add(mediaView);
        
        mediaView.fitHeightProperty().bind(stackPane.heightProperty());
        mediaView.fitWidthProperty().bind(stackPane.widthProperty());
        
        //setup media player view settings
        this.mp.setOnError(new Runnable(){
            @Override
            public void run() {
                System.err.println("VideoPanel.createScene:mp.setOnError: error in media player");
            } 
        });
        
        this.mp.setOnReady(new Runnable(){
            @Override
            public void run() {
                mp.play();
            }    
        });
        
        //setup play and pause effect
        final Path playPath = createPlayPath();
        playPath.setScaleX(-1.0f);
        
        final Node pauseNode = createPauseNode();
        
        final SimpleBooleanProperty isPlayMode = new SimpleBooleanProperty(true);
        final ScaleTransition sT = ScaleTransitionBuilder.create()
                        .node(playPath)
                        .fromX(1.0f)
                        .fromY(1.0f)
                        .toX(1.5f)
                        .toY(1.5f)
                        .cycleCount(1)
                        .autoReverse(false)
                        .onFinished(new EventHandler<ActionEvent>(){ 
                            @Override
                            public void handle(ActionEvent t){
                                if(isPlayMode.getValue())
                                {
                                    stackPane.getChildren().remove(playPath);
                                }
                                else
                                {
                                    stackPane.getChildren().remove(pauseNode);
                                }
                            }
                        }).duration(Duration.millis(500.0f))
                        .build();
        
        EventHandler<MouseEvent> playPauseEventH = new EventHandler<MouseEvent>(){ 
            @Override
            public void handle(MouseEvent t) {
                System.out.println("Clicked media view");
                if(isPlayMode.getValue())
                {
                    isPlayMode.setValue(false);
                    sT.stop();
                    stackPane.getChildren().remove(playPath);
                    
                    //setup pause animation for next click
                    stackPane.getChildren().add(pauseNode);
                    sT.setNode(pauseNode);
                    sT.play();
                    
                    //pause the media
                    mp.pause();
                }
                else
                {
                    isPlayMode.setValue(true);
                    sT.stop();
                    stackPane.getChildren().remove(pauseNode);
                    
                    //play animation for next click
                    stackPane.getChildren().add(playPath);
                    sT.setNode(playPath);
                    sT.play();
                    
                    //play the media
                    mp.play();
                }
            }
        };
        
        //setup play pause handler
        playPath.setOnMouseClicked(playPauseEventH);
        pauseNode.setOnMouseClicked(playPauseEventH);
        mediaView.setOnMouseClicked(playPauseEventH);
        
        final Rectangle rect = RectangleBuilder.create()
                        .opacity(0.6f)
                        .fill(Color.BLACK)
                        .build();
        rect.setOnMouseClicked(playPauseEventH);
        rect.widthProperty().bind(stackPane.widthProperty());
        rect.heightProperty().bind(stackPane.heightProperty());
        
        final FadeTransition fT = FadeTransitionBuilder.create()
                        .node(rect)
                        .duration(Duration.millis(500))
                        .fromValue(0.5f)
                        .toValue(0.0f)
                        .onFinished(new EventHandler<ActionEvent>(){ 
                            @Override
                            public void handle(ActionEvent t) {
                                System.out.println("Hello world");
                                stackPane.getChildren().remove(rect);
                            }
                        })
                        .cycleCount(1)
                        .autoReverse(false)
                        .build();
                        
        
        //setup animation when mouse enters media player
        this.scene.setOnMouseEntered(new EventHandler<MouseEvent>(){  
            @Override
            public void handle(MouseEvent t) {
                //create 
                if(!stackPane.getChildren().contains(rect))
                {
                    System.out.println("Hello world entered");
                    stackPane.getChildren().add(rect);
                    fT.play();
                }
            }
        });
        
        AnchorPane mediaControlG = new AnchorPane();
        mediaControlG.setOnMouseClicked(playPauseEventH);
        stackPane.getChildren().add(mediaControlG);

        //setup slider that will be shown whenever the mouse of the user comes
        //inside the video panel
        final int mcWidth = 200;
        final int mcHeight = 50;
        final LinearGradient mcGradient = LinearGradientBuilder.create()
                .stops(new Stop[] {new Stop(0.0, Color.WHITE.darker()), new Stop(0.5f, Color.BLACK), new Stop(1.0f,Color.BLACK)})
                .startX(0.0f)
                .startY(0.0f)
                .endX(0.0f)
                .endY(1.0f)
                .cycleMethod(CycleMethod.NO_CYCLE)
                .proportional(true)
                .build();
        
        final DropShadow mcDropShadow = new DropShadow();
        mcDropShadow.setOffsetX(4.0f);
        mcDropShadow.setOffsetY(2.0f);
      
        
        final Rectangle mediaControl = RectangleBuilder.create()
                .arcHeight(10)
                .arcWidth(10)
                .effect(mcDropShadow)
                .fill(mcGradient)
                .height(mcHeight)
                .width(mcWidth)
                .build();
          
        mediaControl.xProperty().bind(this.scene.widthProperty().divide(2.0f).subtract(mcWidth/2));
        mediaControl.yProperty().bind(this.scene.heightProperty().subtract(mcHeight+10));
        mediaControlG.getChildren().add(mediaControl);
        
        //setup time slider
        final Slider timeSlider = SliderBuilder.create()
                .min(0)
                .max(100)
                .value(1)
                .showTickLabels(false)
                .showTickMarks(false)
                .build();
        timeSlider.translateYProperty().bind(mediaControl.yProperty().add((mcHeight/2)-10.0f));
        timeSlider.translateXProperty().bind(mediaControl.xProperty().add(10));
        mediaControlG.getChildren().add(timeSlider);
        
        timeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                long dur = newValue.intValue() * 1000;
                mp.seek(new Duration(dur));
            }
        });
        
        this.mp.setOnPlaying(new Runnable(){  
            @Override
            public void run() {
                
            }
        });
        
        
    }
    
    private Path createPlayPath()
    {
        Path path = new Path();
        
        path.getElements().add(new MoveTo(0.0f,0.0f));
        path.getElements().add(new LineTo(75.0f, 40.0f));
        path.getElements().add(new LineTo(75.0f, -40.0f));
        path.getElements().add(new LineTo(0.0f,0.0f));
        
        path.setFill(Color.WHITE);
        path.setOpacity(0.6f);
        path.setStroke(Color.BLACK);
        
        return path;
    }
    
    private Node createPauseNode()
    {
        Group pause = new Group();
        
        Rectangle rectLeft = RectangleBuilder.create()
                .x(0.0f)
                .y(0.0f)
                .height(100.0f)
                .width(30.f)
                .translateX(-20.0f)
                .fill(Color.WHITE)
                .stroke(Color.BLACK)
                .opacity(0.6f)
                .build();
        
        Rectangle rectRight = RectangleBuilder.create()
                .x(0.0f)
                .y(0.0f)
                .height(100.0f)
                .width(30.f)
                .translateX(+20.0f)
                .fill(Color.WHITE)
                .stroke(Color.BLACK)
                .opacity(0.6f)
                .build();
        
        pause.getChildren().addAll(rectLeft, rectRight);
        
        return pause;
    }
}
