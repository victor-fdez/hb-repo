/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.ui.main.content.utilities;

import com.honeybadgers.flltutorial.model.Option;
import static com.honeybadgers.flltutorial.ui.main.content.utilities.OptionPanel.OptionState.CORRECT;
import static com.honeybadgers.flltutorial.ui.main.content.utilities.OptionPanel.OptionState.FINISHED;
import static com.honeybadgers.flltutorial.ui.main.content.utilities.OptionPanel.OptionState.HIDDEN_OCCUPY;
import static com.honeybadgers.flltutorial.ui.main.content.utilities.OptionPanel.OptionState.INCORRECT;
import static com.honeybadgers.flltutorial.ui.main.content.utilities.OptionPanel.OptionState.NORMAL;
import static com.honeybadgers.flltutorial.ui.main.content.utilities.OptionPanel.OptionState.UNOCCUPIED;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Paint;
import java.awt.event.ComponentAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

/**
 *
 * @author chingaman
 */
public class PictureOptionPanel extends OptionPanel {

    // Variables declaration - do not modify                     
    private JLabel iconDisplay;
    protected BufferedImage image;
    public PictureOptionPanel(Option option) {
        super(option);
        
        this.initComponents();
        this.setState(NORMAL);
    }
    
    private void initComponents()
    {
        System.out.println("OptionPanel.initComponents() : started");
        
        this.setMinimumSize(new Dimension(100, 100));
        this.setPreferredSize(new Dimension(100, 100));
        
        //setup the new default layer
        this.remove(this.contentPanel);
        
        this.contentPanel = new DepthContentPanel();
        this.contentPanel.setVisible(true);
        this.contentPanel.setOpaque(true);
        this.contentPanel.setLayout(new GridLayout(1,1));
        this.contentPanel.setBackground(new Color(0,0,0,0));
        
        this.add(this.contentPanel, JLayeredPane.DEFAULT_LAYER);
        
        this.iconDisplay = new JLabel();
        this.iconDisplay.setHorizontalAlignment(JLabel.CENTER);
        this.iconDisplay.setVerticalAlignment(JLabel.TOP);
        this.iconDisplay.setHorizontalTextPosition(JLabel.CENTER);
        this.iconDisplay.setVerticalTextPosition(JLabel.BOTTOM);
        
        this.contentPanel.add(this.iconDisplay);
        
        this.loadOptionImage();
    }
    
    private void loadOptionImage()
    {
        
        File pictureFile;
        if(this.option == null || "".equals(option.getImagePath()))
        {
            //add a default image here
            pictureFile = new File("/Users/chingaman/Desktop/hb-repo/FLLTutorial/src/main/resources/sampleTutorial/media/motion-Legs.png");
            this.iconDisplay.setText("-");
        }
        else
        {
            pictureFile = new File("/Users/chingaman/Desktop/hb-repo/FLLTutorial/src/main/resources/sampleTutorial/"+option.getImagePath());
            this.iconDisplay.setText(option.getDescription());      
        }
                  
        //System.out.println(option.getImagePath());
        if(pictureFile.exists())
        {
            try {
                
                this.image = ImageIO.read(pictureFile);
                this.resizeImage();
                
            } catch (IOException ex) {
                Logger.getLogger(PictureOptionPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else
        {
            System.out.println("does not exist");
        }
        
        if(this.contentPanel.getComponentListeners().length == 0)
        {
            this.contentPanel.addComponentListener(new ComponentAdapter(){
                @Override
                public void componentResized(java.awt.event.ComponentEvent evt)
                {
                    resizeImage();
                }
                @Override
                public void componentShown(java.awt.event.ComponentEvent evt)
                {
                    resizeImage();
                }
            });
        }
    }
    
    public void resizeImage()
    {
            int sizeY = contentPanel.getSize().height - 25;
            if(sizeY < 50)
            {
                sizeY = 50;
            }
            iconDisplay.setIcon(new ImageIcon(image.getScaledInstance(sizeY, sizeY, Image.SCALE_SMOOTH)));
    }

    public static void main(String[] args)
    {
        JFrame frame = new JFrame("FrameDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Option option = new Option("hello", true);
        option.setImagePath("");
        frame.getContentPane().add(new PictureOptionPanel(option));
        frame.pack();
        frame.setVisible(true);
    }                                

    public OptionPanel copy()
    {
            OptionPanel optionPanel = new PictureOptionPanel(this.option);
            return optionPanel;
    }
    
    @Override
    public void transferOption(Option option)
    {
        this.option = option;
        this.loadOptionImage();
    }
    
    @Override
    public void setState(OptionPanel.OptionState state)
    {
        this.state = state;
        DepthContentPanel depthContentPanel = (DepthContentPanel)this.contentPanel;
        switch(this.state)
        {
            case NORMAL: /*beginning state*/
                this.setVisible(true);
                depthContentPanel.changeBackgroundColor(Color.yellow);
                this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                break;
            case HIDDEN_OCCUPY:
                this.setVisible(false);
                depthContentPanel.changeBackgroundColor(Color.yellow);
                this.setBorder(null);
                break;
            case UNOCCUPIED:
                depthContentPanel.changeBackgroundColor(Color.yellow);
                this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            case CORRECT:
                this.setVisible(true);
                depthContentPanel.changeBackgroundColor(Color.GREEN);
                this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                break;
            case INCORRECT:
                this.setVisible(true);
                depthContentPanel.changeBackgroundColor(Color.RED);
                this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                break;
           case FINISHED:
                this.setVisible(true);
                depthContentPanel.changeBackgroundColor(Color.GREEN.darker());
                this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                break;
           case TRANSPARENT:
                this.setOpaque(false);
                break;
            default:
                break;
        }
    }
  
    @Override
    public OptionPanel.OptionState getState()
    {
        return this.state;
    }
   
    @Override
    public Option getOption() {
        return option;
    }                
    
    private class DepthContentPanel extends JPanel
    {
        private Color backgroundColor = Color.YELLOW;
        
        public DepthContentPanel()
        {
            this.setOpaque(true);
        }
        
        public void changeBackgroundColor(Color newColor)
        {
            this.backgroundColor = newColor;
            this.repaint();
        }
        
        @Override
        protected void paintComponent(Graphics g)
        {
            Graphics2D g2 = (Graphics2D)g;
            GradientPaint gradPaint = new GradientPaint(0,0, backgroundColor, 0, getHeight(), backgroundColor.darker());

            Paint oldPaint = g2.getPaint();

            g2.setPaint(gradPaint);
            g2.fillRect(0, 0, this.getWidth(), this.getHeight());

            g2.setPaint(oldPaint);
            super.paintComponent(g);
        }
    }
}
