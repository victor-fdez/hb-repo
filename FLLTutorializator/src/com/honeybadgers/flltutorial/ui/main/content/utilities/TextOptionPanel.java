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
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author chingaman
 */
public class TextOptionPanel extends OptionPanel{
    
    protected DepthTextArea description;
    
    public TextOptionPanel(Option option)
    {
        super(option);
        
        this.initComponents();
    }
    private void initComponents()
    {
        //System.out.println("TextOptionPanel.initComponents() : started");
        try
        {
            this.description = new DepthTextArea("");
            this.description.setBorder(null);
            if(this.option != null){
                this.description.setText(this.option.getDescription());
            }
            //this.setState(HIDDEN_OCCUPY);
        }
        catch(Exception e)
        {
            System.err.println("Exception");
        }
        
        this.contentPanel.setLayout(new BorderLayout());
        
        //make content panel atleast this height
        Font currentFont = this.description.getFont();
        int height = this.description.getFontMetrics(currentFont).getHeight();
        this.setMinimumSize(new Dimension(100, height*4+8));
        this.setPreferredSize(new Dimension(100, height*4+8));
        
        this.description.setBorder(new EmptyBorder(4,4,4,4));
        this.description.setEditable(false);
        this.description.setLineWrap(true);
        this.description.setWrapStyleWord(true);
        this.description.setVisible(true);
        
        this.contentPanel.setOpaque(true);
        this.contentPanel.setBackground(Color.GRAY);
        this.contentPanel.add(this.description);
        
        //this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }
    
    public OptionPanel copy()
    {
            OptionPanel optionPanel = new TextOptionPanel(this.option);
            return optionPanel;
    }
    
    @Override
    public void transferOption(Option option)
    {
        this.option = option;
        if(this.description != null)
        {
            this.description.setText(option.getDescription());
            if(this.description.getParent() != null)
            {
                this.description.revalidate();
            }
        }
    }
    
    @Override
    public void setState(OptionState state)
    {
        this.state = state;
        switch(this.state)
        {
            case NORMAL:
                this.description.setVisible(true);
                this.description.changeBackgroundColor(Color.yellow);
                this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                break;
            case HIDDEN_OCCUPY:
                //maybe do somekind of animation here
                this.description.setVisible(false);
                this.description.repaint();
                this.setBorder(null);
                this.contentPanel.repaint();
                break;
            case UNOCCUPIED:
                this.description.setVisible(true);
                this.description.changeBackgroundColor(Color.GRAY);
                this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                break;
            case CORRECT:
                this.description.setVisible(true);
                this.description.changeBackgroundColor(Color.GREEN);
                this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                break;
            case INCORRECT:
                this.description.setVisible(true);
                this.description.changeBackgroundColor(Color.RED);
                this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                break;
           case FINISHED:
                this.description.setVisible(true);
                this.description.changeBackgroundColor(Color.GREEN.darker());
                this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                break;
           case TRANSPARENT:
                this.setOpaque(false);
                break;
            default:
                break;
        }
    }   
    
    public class DepthTextArea extends JTextArea
    {
        public Color backgroundColor = Color.YELLOW;
        
        public DepthTextArea(String text)
        {
            super(text);
            this.setOpaque(true);
            this.setBackground(new Color(0,0,0,0));
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
    /*
    static public void main(String[] args)
    {
        OptionPanel optionPanel = new TextOptionPanel(new Option("hello world this is a test, please tell me if I said hello", false));
        Component beacon = optionPanel.getBeacon();
        optionPanel = OptionPanel.getOptionPanelFromBeacon(beacon);
        
        PanelsScrollPane tutorialsScrollPane = new PanelsScrollPane(true);
        
        tutorialsScrollPane.appendPanel(optionPanel);
        
        JFrame frame = new JFrame("FrameDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(tutorialsScrollPane);
        frame.getContentPane().setLayout(new GridLayout(1,1));
        frame.pack();
        frame.setVisible(true);
    }
    */
}
