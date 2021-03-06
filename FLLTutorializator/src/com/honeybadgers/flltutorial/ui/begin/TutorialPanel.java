/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.ui.begin;

import com.honeybadgers.flltutorial.model.TutorialBase;
import com.honeybadgers.flltutorial.ui.utilities.Blocked;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.JPanel;

/**
 *
 * @author chingaman
 */
public class TutorialPanel extends javax.swing.JPanel implements Blocked{

    /**
     * Creates new form tutorialPanel
     */
    private TutorialBase tutorialBase;
    private boolean blocked;
    public TutorialPanel(TutorialBase tutorialBase) {
        initComponents();
        this.blocked = true;
        this.tutorialBase = tutorialBase;
        this.titleLabel.setText(tutorialBase.getTitle());
        this.authorLabel.setText(tutorialBase.getAuthor());
        this.descriptionTextArea.setText(tutorialBase.getDescription());
        this.descriptionTextArea.setWrapStyleWord(true);
        this.descriptionTextArea.setLineWrap(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        layeredPane = new javax.swing.JLayeredPane();
        tutorialContentPanel = new javax.swing.JPanel();
        titleLabel = new javax.swing.JLabel();
        authorLabel = new javax.swing.JLabel();
        descriptionTextArea = new javax.swing.JTextArea();
        beaconPanel = new javax.swing.JPanel(){
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                if(blocked)
                {
                    Rectangle rect = g.getClipBounds();

                    AlphaComposite alpha = AlphaComposite.SrcOver.derive(0.65f);
                    Graphics2D g2 = (Graphics2D)g;

                    g2.setComposite(alpha);
                    //later change to gradient
                    g2.setColor(Color.WHITE);
                    g2.fillRect(rect.x, rect.y, rect.width, rect.height);
                }
            }
        };

        layeredPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        layeredPane.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                tutorialPanelResized(evt);
            }
        });

        tutorialContentPanel.setBackground(new java.awt.Color(153, 153, 153));

        titleLabel.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        titleLabel.setText("tutorial title");

        authorLabel.setText("by author");

        descriptionTextArea.setEditable(false);
        descriptionTextArea.setColumns(20);
        descriptionTextArea.setRows(5);
        descriptionTextArea.setText("description");

        org.jdesktop.layout.GroupLayout tutorialContentPanelLayout = new org.jdesktop.layout.GroupLayout(tutorialContentPanel);
        tutorialContentPanel.setLayout(tutorialContentPanelLayout);
        tutorialContentPanelLayout.setHorizontalGroup(
            tutorialContentPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(tutorialContentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(tutorialContentPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, tutorialContentPanelLayout.createSequentialGroup()
                        .add(0, 0, Short.MAX_VALUE)
                        .add(authorLabel))
                    .add(tutorialContentPanelLayout.createSequentialGroup()
                        .add(titleLabel)
                        .add(0, 318, Short.MAX_VALUE)))
                .addContainerGap())
            .add(tutorialContentPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(org.jdesktop.layout.GroupLayout.TRAILING, tutorialContentPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .add(descriptionTextArea, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        tutorialContentPanelLayout.setVerticalGroup(
            tutorialContentPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(tutorialContentPanelLayout.createSequentialGroup()
                .add(12, 12, 12)
                .add(titleLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 99, Short.MAX_VALUE)
                .add(authorLabel)
                .addContainerGap())
            .add(tutorialContentPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(tutorialContentPanelLayout.createSequentialGroup()
                    .add(35, 35, 35)
                    .add(descriptionTextArea)
                    .add(35, 35, 35)))
        );

        tutorialContentPanel.setBounds(0, 0, 410, 150);
        layeredPane.add(tutorialContentPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        beaconPanel.setBackground(new java.awt.Color(255, 153, 153));
        beaconPanel.setOpaque(false);

        org.jdesktop.layout.GroupLayout beaconPanelLayout = new org.jdesktop.layout.GroupLayout(beaconPanel);
        beaconPanel.setLayout(beaconPanelLayout);
        beaconPanelLayout.setHorizontalGroup(
            beaconPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 404, Short.MAX_VALUE)
        );
        beaconPanelLayout.setVerticalGroup(
            beaconPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 144, Short.MAX_VALUE)
        );

        beaconPanel.setBounds(0, 0, 0, 0);
        layeredPane.add(beaconPanel, javax.swing.JLayeredPane.DRAG_LAYER);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layeredPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layeredPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tutorialPanelResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_tutorialPanelResized
        this.beaconPanel.setSize(layeredPane.getSize());
        this.tutorialContentPanel.setSize(layeredPane.getSize());
        this.tutorialContentPanel.revalidate();
        this.beaconPanel.revalidate();
    }//GEN-LAST:event_tutorialPanelResized

    public Component getBeacon()
    {
        return (Component)this.beaconPanel;
    }
    
    public static TutorialPanel getTutorialPanelFromBeacon(Component beacon)
    {
        if(beacon instanceof JPanel)
        {
            TutorialPanel tutorialPanel = (TutorialPanel)beacon.getParent().getParent();
            return tutorialPanel;
        }
        return null;
    }
    
    public static TutorialBase getTutorialBaseFromBeacon(Component beacon)
    {
        if(beacon instanceof JPanel)
        {
            TutorialPanel tutorialPanel = (TutorialPanel)beacon.getParent().getParent();
            return tutorialPanel.tutorialBase;
        }
        return null;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel authorLabel;
    private javax.swing.JPanel beaconPanel;
    private javax.swing.JTextArea descriptionTextArea;
    private javax.swing.JLayeredPane layeredPane;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JPanel tutorialContentPanel;
    // End of variables declaration//GEN-END:variables

    
    @Override
    public void setBlocked(boolean block) {
        this.blocked = block;
        this.repaint();
    }
}
