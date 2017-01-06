package com.kasirgalabs.tilki.client;

import com.kasirgalabs.tilki.utils.Exam;
import com.kasirgalabs.tilki.utils.ExamList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import javax.swing.JFrame;

public class ExamDescriptionScreen extends JFrame implements
        ServiceListener<ExamList> {

    private static volatile ExamDescriptionScreen instance = null;
    private ExamList examList;

    private ExamDescriptionScreen() {
        super("Tilki");
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        examDescriptionTextPane = new javax.swing.JTextPane();
        examNameLabel = new javax.swing.JLabel();

        examDescriptionTextPane.setEditable(false);
        examDescriptionTextPane.setFocusable(false);
        jScrollPane1.setViewportView(examDescriptionTextPane);

        examNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                    .addComponent(examNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(examNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextPane examDescriptionTextPane;
    private javax.swing.JLabel examNameLabel;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

    public static ExamDescriptionScreen getInstance() {
        if(instance == null) {
            synchronized(ExamDescriptionScreen.class) {
                if(instance == null) {
                    instance = new ExamDescriptionScreen();
                }
            }
        }
        return instance;
    }

    private String findExamDescription(String examName) {
        for(Exam exam : examList) {
            if(exam.getName().equals(examName)) {
                return exam.getDescription();
            }
        }
        return null;
    }

    @Override
    public void servicePerformed(Future<ExamList> future) {
        try {
            this.examList = future.get();
        }
        catch(InterruptedException | ExecutionException ex) {
        }
    }

    public void showExamDescription(String examName) {
        String examDescription = findExamDescription(examName);
        examDescriptionTextPane.setText(examDescription);
        examNameLabel.setText(examName);
        pack();
        setVisible(true);
    }
}
