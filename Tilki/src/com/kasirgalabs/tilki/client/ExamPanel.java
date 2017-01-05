package com.kasirgalabs.tilki.client;

import com.kasirgalabs.tilki.utils.ExamList;
import com.kasirgalabs.tilki.utils.ExamListModel;
import com.kasirgalabs.tilki.utils.TilkiColor;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionListener;

public class ExamPanel extends JPanel implements ActionListener,
        MouseListener,
        ComponentListener,
        ListSelectionListener,
        PropertyChangeListener {

    private final MainScreen mainScreen;
    private ExamDescriptionScreen examDescriptionScreen;

    public ExamPanel(MainScreen mainScreen) {
        this.mainScreen = mainScreen;
        initComponents();
        instructorPasswordField.getDocument().addDocumentListener(
                new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                checkPassword();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checkPassword();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                checkPassword();
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        infoLabel = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        refreshButton = new javax.swing.JButton();
        refreshStatusLabel = new RefreshStatusLabel();
        instructorLabel = new javax.swing.JLabel();
        examNameLabel = new javax.swing.JLabel();
        examNameScrollPane = new javax.swing.JScrollPane();
        examNameList = new ExamNameList();
        examDetailsButton = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        instructorPasswordField = new javax.swing.JPasswordField();
        passwordStatusLabel = new PasswordStatusLabel();
        jSeparator2 = new javax.swing.JSeparator();
        previousButton = new javax.swing.JButton();
        nextButton = new ExamPanelNextButton();

        addComponentListener(this);

        infoLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        infoLabel.setText("S\u0131nav Se\u00E7im Ekran\u0131");

        refreshButton.setText("Yenile");
        refreshButton.addActionListener(this);

        refreshStatusLabel.setForeground(TilkiColor.BLUE);
        refreshStatusLabel.setText("Bağlanıyor...");

        instructorLabel.setText("\u015Eifre");

        examNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        examNameLabel.setText("S\u0131navlar");

        examNameList.setModel(new ExamListModel());
        examNameList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        examNameList.addListSelectionListener(this);
        examNameScrollPane.setViewportView(examNameList);

        examDetailsButton.setText("Sınav Detayları");
        examDetailsButton.setEnabled(false);
        examDetailsButton.addMouseListener(this);

        instructorPasswordField.addPropertyChangeListener(this);

        previousButton.setText("Geri");
        previousButton.addMouseListener(this);

        nextButton.setText("\u0130leri");
        nextButton.setEnabled(false);
        nextButton.addMouseListener(this);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jSeparator2)
                    .add(jSeparator3)
                    .add(jSeparator1)
                    .add(examNameLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(passwordStatusLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(examNameScrollPane)
                    .add(infoLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(instructorLabel)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(instructorPasswordField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(examDetailsButton))
                        .add(0, 0, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .add(refreshButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(refreshStatusLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .add(previousButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(nextButton)))
                .addContainerGap())
        );

        layout.linkSize(new java.awt.Component[] {nextButton, previousButton}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(infoLabel)
                .add(18, 18, 18)
                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 12, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(refreshButton)
                    .add(refreshStatusLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(examNameLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(examNameScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 130, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(examDetailsButton)
                .add(18, 18, 18)
                .add(jSeparator3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(instructorPasswordField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(instructorLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(passwordStatusLabel)
                .add(18, 18, 18)
                .add(jSeparator2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(previousButton)
                    .add(nextButton))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.linkSize(new java.awt.Component[] {instructorLabel, passwordStatusLabel}, org.jdesktop.layout.GroupLayout.VERTICAL);

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
        else if (evt.getSource() == nextButton) {
            ExamPanel.this.nextButtonMousePressed(evt);
        }
        else if (evt.getSource() == examDetailsButton) {
            ExamPanel.this.examDetailsButtonMousePressed(evt);
        }
    }

    public void mouseReleased(java.awt.event.MouseEvent evt) {
    }

    public void propertyChange(java.beans.PropertyChangeEvent evt) {
        if (evt.getSource() == instructorPasswordField) {
            ExamPanel.this.instructorPasswordFieldPropertyChange(evt);
        }
    }

    public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
        if (evt.getSource() == examNameList) {
            ExamPanel.this.examNameListValueChanged(evt);
        }
    }// </editor-fold>//GEN-END:initComponents

    private void previousButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_previousButtonMousePressed
        mainScreen.previousScreen();
    }//GEN-LAST:event_previousButtonMousePressed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        fetchExamList();
    }//GEN-LAST:event_formComponentShown

    private void instructorPasswordFieldPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_instructorPasswordFieldPropertyChange
        passwordStatusLabel.setText("");
        if(!instructorPasswordField.isEnabled()) {
            passwordStatusLabel.setText("Yukarıdaki listeden sınav seçiniz.");
            passwordStatusLabel.setForeground(TilkiColor.RED);
        }
    }//GEN-LAST:event_instructorPasswordFieldPropertyChange

    private void nextButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nextButtonMousePressed
        if(!nextButton.isEnabled()) {
            evt.consume();
            return;
        }
        mainScreen.nextScreen();
    }//GEN-LAST:event_nextButtonMousePressed

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
        fetchExamList();
    }//GEN-LAST:event_refreshButtonActionPerformed

    private void examNameListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_examNameListValueChanged
        instructorPasswordField.setEnabled(true);
        examDetailsButton.setEnabled(true);
        if(examNameList.getSelectedIndex() == -1) {
            instructorPasswordField.setEnabled(false);
            instructorPasswordField.setText("");
            examDetailsButton.setEnabled(false);
            return;
        }
        User user = mainScreen.getUser();
        user.setExam(examNameList.getSelectedValue());
    }//GEN-LAST:event_examNameListValueChanged

    private void examDetailsButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_examDetailsButtonMousePressed
        examDescriptionScreen.setExam(examNameList.getSelectedValue());
        examDescriptionScreen.setVisible(true);
    }//GEN-LAST:event_examDetailsButtonMousePressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton examDetailsButton;
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
        examDescriptionScreen = new ExamDescriptionScreen();
        instructorPasswordField.setEnabled(false);
        instructorPasswordField.setText("");
        nextButton.setEnabled(false);
        examNameList.setModel(new ExamListModel());
        refreshStatusLabel.setText("Bağlanıyor...");
        refreshStatusLabel.setForeground(TilkiColor.BLUE);
        ArrayList<ServiceListener<ExamList>> listeners = new ArrayList<>();
        listeners.add((ServiceListener<ExamList>) refreshStatusLabel);
        listeners.add((ServiceListener<ExamList>) examNameList);
        listeners.add(examDescriptionScreen);
        Service<ExamList, Object> service = new GetExams(listeners);
        service.request(null);
    }

    private void checkPassword() {
        char[] key = instructorPasswordField.getPassword();
        if(key.length == 0) {
            return;
        }
        passwordStatusLabel.setText("Şifre kontrol ediliyor.");
        passwordStatusLabel.setForeground(TilkiColor.BLUE);
        ArrayList<ServiceListener<Boolean>> listeners = new ArrayList<>();
        listeners.add((ServiceListener<Boolean>) passwordStatusLabel);
        listeners.add((ServiceListener<Boolean>) nextButton);
        Service<Boolean, char[]> service = new KeyVerifyRefactored(listeners);
        service.request(key);
    }
}
