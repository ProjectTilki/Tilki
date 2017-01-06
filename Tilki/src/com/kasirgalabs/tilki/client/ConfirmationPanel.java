package com.kasirgalabs.tilki.client;

import com.kasirgalabs.tilki.utils.TilkiColor;
import javax.swing.JPanel;

public class ConfirmationPanel extends JPanel {

    private final MainScreen mainScreen;

    public ConfirmationPanel(MainScreen mainScreen) {
        this.mainScreen = mainScreen;
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        infoLabel = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        nameLabel = new javax.swing.JLabel();
        surnameLabel = new javax.swing.JLabel();
        idLabel = new javax.swing.JLabel();
        examLabel = new javax.swing.JLabel();
        name = new javax.swing.JLabel();
        surname = new javax.swing.JLabel();
        id = new javax.swing.JLabel();
        exam = new javax.swing.JLabel();
        confirmCheckBox = new javax.swing.JCheckBox();
        previousButton = new javax.swing.JButton();
        startExamButton = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        infoLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        infoLabel.setText("Öğrenci ve Sınav Bilgileri");

        nameLabel.setText("Ad:");

        surnameLabel.setText("Soyad:");

        idLabel.setText("Numara:");

        examLabel.setText("Sınav:");

        name.setForeground(TilkiColor.BLUE);

        surname.setForeground(TilkiColor.BLUE);

        id.setForeground(TilkiColor.BLUE);

        exam.setForeground(TilkiColor.BLUE);

        confirmCheckBox.setForeground(TilkiColor.RED);
        confirmCheckBox.setText("Bilgilerimi doğru şekilde girdim.");
        confirmCheckBox.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                confirmCheckBoxStateChanged(evt);
            }
        });

        previousButton.setText("Geri");
        previousButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                previousButtonMousePressed(evt);
            }
        });

        startExamButton.setText("Sınavı Başlat");
        startExamButton.setEnabled(false);
        startExamButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                startExamButtonMousePressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(idLabel)
                                .addGap(18, 18, 18)
                                .addComponent(id, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(examLabel)
                                .addGap(18, 18, 18)
                                .addComponent(exam, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(surnameLabel)
                                .addGap(18, 18, 18)
                                .addComponent(surname, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(nameLabel)
                                .addGap(18, 18, 18)
                                .addComponent(name, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jSeparator1)
                            .addComponent(infoLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(12, 12, 12))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(previousButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(startExamButton))
                            .addComponent(confirmCheckBox, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE))
                        .addContainerGap())))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {examLabel, idLabel, nameLabel, surnameLabel});

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {previousButton, startExamButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(infoLabel)
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameLabel)
                    .addComponent(name))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(surnameLabel)
                    .addComponent(surname))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(idLabel)
                    .addComponent(id))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(exam)
                    .addComponent(examLabel))
                .addGap(18, 18, 18)
                .addComponent(confirmCheckBox)
                .addGap(18, 18, 18)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(previousButton)
                    .addComponent(startExamButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {exam, examLabel, id, idLabel, name, nameLabel, surname, surnameLabel});

    }// </editor-fold>//GEN-END:initComponents

    private void previousButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_previousButtonMousePressed
        mainScreen.previousScreen();
    }//GEN-LAST:event_previousButtonMousePressed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        User user = mainScreen.getUser();
        name.setText(user.getName());
        surname.setText(user.getSurname());
        id.setText(user.getId());
        exam.setText(user.getExam().getName());
        confirmCheckBox.setSelected(false);
    }//GEN-LAST:event_formComponentShown

    private void confirmCheckBoxStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_confirmCheckBoxStateChanged
        confirmCheckBox.setForeground(TilkiColor.RED);
        startExamButton.setEnabled(false);
        if(confirmCheckBox.isSelected()) {
            confirmCheckBox.setForeground(TilkiColor.GREEN);
            startExamButton.setEnabled(true);
        }
    }//GEN-LAST:event_confirmCheckBoxStateChanged

    private void startExamButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_startExamButtonMousePressed
        if(!startExamButton.isEnabled()) {
            evt.consume();
            return;
        }
        mainScreen.nextScreen();
        TimerLabel label = TimerLabel.getInstance();
        SimpleTimerActionListener listener = new SimpleTimerActionListener(label);
        SimpleTimer timer = new SimpleTimer(1000, listener);
        timer.setRepeats(true);
        timer.start();

        CaptureDesktopRefactored captureDesktopRefactored = CaptureDesktopRefactored.getInstance();
        User user = mainScreen.getUser();
        captureDesktopRefactored.StartCaptureDesktop(user.getId());
    }//GEN-LAST:event_startExamButtonMousePressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox confirmCheckBox;
    private javax.swing.JLabel exam;
    private javax.swing.JLabel examLabel;
    private javax.swing.JLabel id;
    private javax.swing.JLabel idLabel;
    private javax.swing.JLabel infoLabel;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JLabel name;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JButton previousButton;
    private javax.swing.JButton startExamButton;
    private javax.swing.JLabel surname;
    private javax.swing.JLabel surnameLabel;
    // End of variables declaration//GEN-END:variables
}
