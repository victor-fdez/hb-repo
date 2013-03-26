/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.ui.begin;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author chingaman
 */
public class AllTutorialsPanel extends javax.swing.JPanel {

    /**
     * Creates new form Beginnings
     */
    public AllTutorialsPanel() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        titleLabel = new javax.swing.JLabel();
        startButton = new javax.swing.JButton();
        listPanel = new javax.swing.JPanel();
        openButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        descriptionArea = new javax.swing.JTextArea();

        titleLabel.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        titleLabel.setText("long title");

        startButton.setText("start new project");

        listPanel.setBackground(new java.awt.Color(204, 204, 204));
        listPanel.setLayout(new java.awt.GridLayout(1, 1));

        openButton.setText("open previous project");

        descriptionArea.setEditable(false);
        descriptionArea.setColumns(20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setRows(5);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setFocusable(false);
        descriptionArea.setRequestFocusEnabled(false);
        jScrollPane1.setViewportView(descriptionArea);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, listPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(0, 319, Short.MAX_VALUE)
                        .add(openButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(startButton))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(titleLabel)
                        .add(0, 0, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(titleLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 127, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(listPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(startButton)
                    .add(openButton))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea descriptionArea;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel listPanel;
    private javax.swing.JButton openButton;
    private javax.swing.JButton startButton;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables

    public JPanel getListPanel() {
        return listPanel;
    }

    public JButton getOpenButton() {
        return openButton;
    }

    public JButton getStartButton() {
        return startButton;
    }
    
    public void setTitle(String title)
    {
        this.titleLabel.setText(title);
    }
    
    public void setDescription(String description)
    {
        if(description == null)
        {
            this.descriptionArea.setVisible(false);
            return;
        }
        this.descriptionArea.setText(description);
    }
}
