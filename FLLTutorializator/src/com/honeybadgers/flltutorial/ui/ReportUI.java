/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.honeybadgers.flltutorial.ui;

import com.honeybadgers.flltutorial.model.OptionTracker;
import com.honeybadgers.flltutorial.model.Stage;
import com.honeybadgers.flltutorial.model.Tutorial;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;


/**
 *
 * @author dsbx
 */
public class ReportUI extends javax.swing.JFrame {

    Tutorial tutorial;
    ArrayList<OptionTracker> stageOptionTrackers;
    
    /**
     * Creates new form ReportUI
     */
    public ReportUI(Tutorial tutorial, ArrayList<OptionTracker> stageOptionTrackers) {
        this.tutorial=tutorial;
        this.stageOptionTrackers=stageOptionTrackers;
        
        initComponents();
        //List<Stage> stages = tutorial.getStages();
        fillProjectDetails();
        fillTable();
        
        FLLTutorialUI.setInCenterOfScreen(this);
        setTitle("Project Details");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        setVisible(true);
        
        
    }
    
    private void fillProjectDetails(){
        teamNameJL.setText(tutorial.getTeamName());
        projectNameJL.setText(tutorial.getProjectName());
        List<String> teamMembers=tutorial.getMembers();
        if(teamMembers.size()>0){
            String teamMembersString=teamMembers.get(0);
            for(int i=1;i<teamMembers.size();i++){
                teamMembersString=teamMembersString.concat(", "+teamMembers.get(i));
            }
            teamMembersJL.setText(teamMembersString);
        }
    }
    
    private void fillTable(){
        List<Stage> stages=tutorial.getStages();
        /*int numberIncorrectSelected=0;
        int numberIncorrect=0;
        boolean allComplete=true;*/
        for(int i=0;i<stages.size();i++){
            table.setValueAt(stages.get(i).getName(), i, 0);
            table.setValueAt(stageOptionTrackers.get(i).isFinished() ? "yes" : "no", i, 1);
            int[] stats = stageOptionTrackers.get(i).findStatsOnTreeFromThisRoot();
            table.setValueAt(stats[2]+"/"+stats[3], i, 2);
            
            /*numberIncorrectSelected+=stageOptionTrackers.get(i).getNumberOfIncorrectSelected();
            numberIncorrect+=stageOptionTrackers.get(i).getTotalNumberOfIncorrect();
            if(!stageOptionTrackers.get(i).isFinished())
                allComplete=false;
            */
        }
        /*table.setValueAt("Total", stages.size(), 0);
        table.setValueAt(allComplete ? "yes" : "no", stages.size(), 1);
        table.setValueAt(numberIncorrectSelected+"/"+numberIncorrect, stages.size(), 2);*/
    }
    
    /** Returns an ImageIcon, or null if the path was invalid. */
    protected ImageIcon createImageIcon(String path, String description) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
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

        closeButton = new javax.swing.JButton();
        titleLabel = new javax.swing.JLabel();
        projectNameTitleJL = new javax.swing.JLabel();
        teamNameTitleJL = new javax.swing.JLabel();
        teamMembersTitleJL = new javax.swing.JLabel();
        teamMembersJL = new javax.swing.JLabel();
        projectNameJL = new javax.swing.JLabel();
        teamNameJL = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        titleLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        titleLabel.setText("Project Details");

        projectNameTitleJL.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        projectNameTitleJL.setText("Project Name:");

        teamNameTitleJL.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        teamNameTitleJL.setText("Team Name:");

        teamMembersTitleJL.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        teamMembersTitleJL.setText("Team Members:");

        teamMembersJL.setText("jLabel5");

        projectNameJL.setText("jLabel6");

        teamNameJL.setText("jLabel7");

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Stage", "Complete?", "Errors"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table.setColumnSelectionAllowed(true);
        table.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(table);
        table.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.getColumnModel().getColumn(0).setPreferredWidth(150);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(titleLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(closeButton))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 428, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 13, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(teamMembersTitleJL)
                            .addComponent(teamNameTitleJL)
                            .addComponent(projectNameTitleJL))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(projectNameJL)
                            .addComponent(teamNameJL)
                            .addComponent(teamMembersJL))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(closeButton)
                    .addComponent(titleLabel))
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(projectNameTitleJL)
                    .addComponent(projectNameJL))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(teamNameTitleJL)
                    .addComponent(teamNameJL, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(teamMembersTitleJL)
                    .addComponent(teamMembersJL))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_closeButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeButton;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel projectNameJL;
    private javax.swing.JLabel projectNameTitleJL;
    private javax.swing.JTable table;
    private javax.swing.JLabel teamMembersJL;
    private javax.swing.JLabel teamMembersTitleJL;
    private javax.swing.JLabel teamNameJL;
    private javax.swing.JLabel teamNameTitleJL;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables

    
}