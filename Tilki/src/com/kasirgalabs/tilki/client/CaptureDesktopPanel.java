package com.kasirgalabs.tilki.client;

import com.kasirgalabs.tilki.utils.TilkiColor;
import java.awt.Panel;

public class CaptureDesktopPanel extends Panel implements Observer<String> {

    private final MainScreen mainScreen;

    public CaptureDesktopPanel(MainScreen mainScreen) {
        this.mainScreen = mainScreen;
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        infoLabel = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        timeLabel = new javax.swing.JLabel();
        elapsedTimeLabel = new javax.swing.JLabel();
        examDetailsButton = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        readyCheckBox = new javax.swing.JCheckBox();
        nextButton = new javax.swing.JButton();

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                formComponentHidden(evt);
            }
        });

        infoLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        infoLabel.setText("Sınavınız Başlamıştır");

        timeLabel.setText("Geçen Süre");

        elapsedTimeLabel.setForeground(TilkiColor.BLUE);
        elapsedTimeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        elapsedTimeLabel.setText("00:00:00");

        examDetailsButton.setText("Sınav Detayları");
        examDetailsButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                examDetailsButtonMousePressed(evt);
            }
        });

        readyCheckBox.setForeground(TilkiColor.RED);
        readyCheckBox.setText("Sınavı bitirmeye hazırım.");
        readyCheckBox.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                readyCheckBoxStateChanged(evt);
            }
        });

        nextButton.setText("İleri");
        nextButton.setEnabled(false);
        nextButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                nextButtonMousePressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2)
                    .addComponent(infoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(readyCheckBox, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(timeLabel)
                                .addGap(18, 18, 18)
                                .addComponent(elapsedTimeLabel))
                            .addComponent(examDetailsButton, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(nextButton)))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {elapsedTimeLabel, timeLabel});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(infoLabel)
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(timeLabel)
                    .addComponent(elapsedTimeLabel))
                .addGap(18, 18, 18)
                .addComponent(examDetailsButton)
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(readyCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nextButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void readyCheckBoxStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_readyCheckBoxStateChanged
        readyCheckBox.setForeground(TilkiColor.RED);
        nextButton.setEnabled(false);
        if(readyCheckBox.isSelected()) {
            readyCheckBox.setForeground(TilkiColor.GREEN);
            nextButton.setEnabled(true);
        }
    }//GEN-LAST:event_readyCheckBoxStateChanged

    private void examDetailsButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_examDetailsButtonMousePressed
        ExamDescriptionScreen examDescriptionScreen = ExamDescriptionScreen.getInstance();
        examDescriptionScreen.setVisible(true);
    }//GEN-LAST:event_examDetailsButtonMousePressed

    private void nextButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nextButtonMousePressed
        if(!nextButton.isEnabled()) {
            evt.consume();
            return;
        }
        mainScreen.nextScreen();
    }//GEN-LAST:event_nextButtonMousePressed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        SimpleTimerActionListener listener = SimpleTimerActionListener.getInstance();
        listener.addObserver(this);
    }//GEN-LAST:event_formComponentShown

    private void formComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentHidden
        readyCheckBox.setSelected(false);
    }//GEN-LAST:event_formComponentHidden

    @Override
    public void update(String time) {
        elapsedTimeLabel.setText(time);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel elapsedTimeLabel;
    private javax.swing.JButton examDetailsButton;
    private javax.swing.JLabel infoLabel;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JButton nextButton;
    private javax.swing.JCheckBox readyCheckBox;
    private javax.swing.JLabel timeLabel;
    // End of variables declaration//GEN-END:variables
}
