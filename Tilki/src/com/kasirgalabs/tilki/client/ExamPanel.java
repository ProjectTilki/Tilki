package com.kasirgalabs.tilki.client;

import com.kasirgalabs.tilki.utils.ExamList;
import com.kasirgalabs.tilki.utils.ExamListModel;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class ExamPanel extends javax.swing.JPanel implements ActionListener,
        MouseListener,
        ComponentListener {

    private final MainScreen mainScreen;
    private ExamList examList;
    private static final Color BLUE = new Color(0, 0, 204);

    public ExamPanel(MainScreen mainScreen) {
        this.mainScreen = mainScreen;
        initComponents();
        instructorPasswordField.getDocument().addDocumentListener(
                new EmptyTextFieldDocumentListener(instructorPasswordField,
                        nextButton));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        infoLabel = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        instructorLabel = new javax.swing.JLabel();
        instructorPasswordField = new javax.swing.JPasswordField();
        passwordStatusLabel = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        examNameLabel = new javax.swing.JLabel();
        refreshButton = new javax.swing.JButton();
        refreshStatusLabel = new RefreshStatusLabel();
        examNameScrollPane = new javax.swing.JScrollPane();
        examNameList = new ExamNameList();
        examDescriptionLabel = new javax.swing.JLabel();
        examDescriptionScrollPane = new javax.swing.JScrollPane();
        examDescriptionTextPane = new javax.swing.JTextPane();
        jSeparator2 = new javax.swing.JSeparator();
        previousButton = new javax.swing.JButton();
        nextButton = new javax.swing.JButton();

        setMaximumSize(new java.awt.Dimension(524, 429));
        setMinimumSize(new java.awt.Dimension(524, 429));
        addComponentListener(this);

        infoLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        infoLabel.setText("S\u0131nav Se\u00E7im Ekran\u0131");

        instructorLabel.setText("\u015Eifre");

        passwordStatusLabel.setMaximumSize(new java.awt.Dimension(0, 17));
        passwordStatusLabel.setMinimumSize(new java.awt.Dimension(0, 17));
        passwordStatusLabel.setPreferredSize(new java.awt.Dimension(0, 17));

        examNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        examNameLabel.setText("S\u0131navlar");

        refreshButton.setText("Yenile");
        refreshButton.addActionListener(this);

        refreshStatusLabel.setForeground(BLUE);
        refreshStatusLabel.setText("Bağlanıyor...");
        refreshStatusLabel.setMaximumSize(new java.awt.Dimension(0, 17));
        refreshStatusLabel.setMinimumSize(new java.awt.Dimension(0, 17));
        refreshStatusLabel.setPreferredSize(new java.awt.Dimension(0, 17));

        examNameList.setModel(new ExamListModel());
        examNameList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        examNameScrollPane.setViewportView(examNameList);

        examDescriptionLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        examDescriptionLabel.setText("S\u0131nav A\u00E7\u0131klamas\u0131");

        examDescriptionScrollPane.setViewportView(examDescriptionTextPane);

        previousButton.setText("Geri");
        previousButton.addMouseListener(this);

        nextButton.setText("\u0130leri");
        nextButton.setEnabled(false);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jSeparator2)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jSeparator1)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, infoLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(previousButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(nextButton))
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(instructorLabel)
                                .add(18, 18, 18)
                                .add(instructorPasswordField))
                            .add(examNameScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jSeparator3)
                            .add(layout.createSequentialGroup()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                    .add(passwordStatusLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 234, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(layout.createSequentialGroup()
                                        .add(refreshButton)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(refreshStatusLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .add(org.jdesktop.layout.GroupLayout.TRAILING, examNameLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .add(0, 0, Short.MAX_VALUE)))
                        .add(18, 18, 18)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(examDescriptionScrollPane)
                            .add(examDescriptionLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE))))
                .addContainerGap())
        );

        layout.linkSize(new java.awt.Component[] {nextButton, previousButton}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(infoLabel)
                .add(18, 18, 18)
                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(instructorLabel)
                    .add(instructorPasswordField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(examDescriptionLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(passwordStatusLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jSeparator3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(0, 0, Short.MAX_VALUE)
                                .add(refreshButton))
                            .add(refreshStatusLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(examNameLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(examNameScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 148, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(examDescriptionScrollPane))
                .add(18, 18, 18)
                .add(jSeparator2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(nextButton)
                    .add(previousButton))
                .addContainerGap())
        );
    }

    // Code for dispatching events from components to event handlers.

    public void actionPerformed(java.awt.event.ActionEvent evt) {
        if (evt.getSource() == refreshButton) {
            ExamPanel.this.refreshButtonActionPerformed(evt);
        }
    }

    public void componentHidden(java.awt.event.ComponentEvent evt) {
    }

    public void componentMoved(java.awt.event.ComponentEvent evt) {
    }

    public void componentResized(java.awt.event.ComponentEvent evt) {
    }

    public void componentShown(java.awt.event.ComponentEvent evt) {
        if (evt.getSource() == ExamPanel.this) {
            ExamPanel.this.formComponentShown(evt);
        }
    }

    public void mouseClicked(java.awt.event.MouseEvent evt) {
    }

    public void mouseEntered(java.awt.event.MouseEvent evt) {
    }

    public void mouseExited(java.awt.event.MouseEvent evt) {
    }

    public void mousePressed(java.awt.event.MouseEvent evt) {
        if (evt.getSource() == previousButton) {
            ExamPanel.this.previousButtonMousePressed(evt);
        }
    }

    public void mouseReleased(java.awt.event.MouseEvent evt) {
    }// </editor-fold>//GEN-END:initComponents

    private void previousButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_previousButtonMousePressed
        mainScreen.previousScreen();
    }//GEN-LAST:event_previousButtonMousePressed

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
        fetchExamList();
    }//GEN-LAST:event_refreshButtonActionPerformed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        fetchExamList();
    }//GEN-LAST:event_formComponentShown

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel examDescriptionLabel;
    private javax.swing.JScrollPane examDescriptionScrollPane;
    private javax.swing.JTextPane examDescriptionTextPane;
    private javax.swing.JLabel examNameLabel;
    private javax.swing.JList<String> examNameList;
    private javax.swing.JScrollPane examNameScrollPane;
    private javax.swing.JLabel infoLabel;
    private javax.swing.JLabel instructorLabel;
    private javax.swing.JPasswordField instructorPasswordField;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JButton nextButton;
    private javax.swing.JLabel passwordStatusLabel;
    private javax.swing.JButton previousButton;
    private javax.swing.JButton refreshButton;
    private javax.swing.JLabel refreshStatusLabel;
    // End of variables declaration//GEN-END:variables

    private void fetchExamList() {
        refreshStatusLabel.setText("Bağlanıyor...");
        refreshStatusLabel.setForeground(BLUE);
        ArrayList<ServiceListener<ExamList>> listeners = new ArrayList<>();
        listeners.add((ServiceListener<ExamList>) refreshStatusLabel);
        listeners.add((ServiceListener<ExamList>) examNameList);
        Service<ExamList> service = new GetExams(listeners);
        service.request();
    }
}
