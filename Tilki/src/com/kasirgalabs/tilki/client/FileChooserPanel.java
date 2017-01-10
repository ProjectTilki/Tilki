package com.kasirgalabs.tilki.client;

import com.kasirgalabs.tilki.utils.TilkiColor;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class FileChooserPanel extends JPanel implements Observer<String> {

    private final MainScreen mainScreen;

    public FileChooserPanel(MainScreen mainScreen) {
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

    private void checkPassword() {
        char[] key = instructorPasswordField.getPassword();
        if(key.length == 0) {
            return;
        }
        passwordStatusLabel.setText("Şifre kontrol ediliyor.");
        passwordStatusLabel.setForeground(TilkiColor.BLUE);
        ArrayList<ServiceListener<Boolean>> listeners = new ArrayList<>();
        listeners.add((ServiceListener<Boolean>) passwordStatusLabel);
        listeners.add((ServiceListener<Boolean>) endExamButton);
        Service<Boolean, char[]> service = new KeyVerifyRefactored(listeners);
        service.request(key);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        infoLabel = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        fileChooserButton = new javax.swing.JButton();
        timeLabel = new javax.swing.JLabel();
        elapsedTimeLabel = new javax.swing.JLabel();
        fileListLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        fileList = new javax.swing.JList<>();
        instructorPasswordLabel = new javax.swing.JLabel();
        instructorPasswordField = new javax.swing.JPasswordField();
        passwordStatusLabel = new PasswordStatusLabel();
        jSeparator2 = new javax.swing.JSeparator();
        backButton = new javax.swing.JButton();
        endExamButton = new PasswordEnabledButton();

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        infoLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        infoLabel.setText("Dosya Seç ve Sınavı Bitir");

        fileChooserButton.setText("Dosya Seç");

        timeLabel.setText("Geçen Süre");

        elapsedTimeLabel.setForeground(TilkiColor.BLUE);
        elapsedTimeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        elapsedTimeLabel.setText("00:00:00");

        fileListLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fileListLabel.setText("Seçtiğiniz Dosyalar");

        fileList.setToolTipText("Sürükle ve bırak.");
        jScrollPane1.setViewportView(fileList);

        instructorPasswordLabel.setText("\u015Eifre");

        backButton.setText("Geri");
        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                backButtonMousePressed(evt);
            }
        });

        endExamButton.setText("Sınavı Bitir");
        endExamButton.setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator1)
                    .addComponent(infoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(fileChooserButton)
                                .addGap(18, 18, 18)
                                .addComponent(timeLabel)
                                .addGap(18, 18, 18)
                                .addComponent(elapsedTimeLabel))
                            .addComponent(jScrollPane1)
                            .addComponent(fileListLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(instructorPasswordLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(instructorPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(passwordStatusLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(backButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(endExamButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(infoLabel)
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fileChooserButton)
                    .addComponent(timeLabel)
                    .addComponent(elapsedTimeLabel))
                .addGap(18, 18, 18)
                .addComponent(fileListLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(instructorPasswordLabel)
                    .addComponent(instructorPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(passwordStatusLabel)
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(backButton)
                    .addComponent(endExamButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {instructorPasswordLabel, passwordStatusLabel});

    }// </editor-fold>//GEN-END:initComponents

    private void backButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_backButtonMousePressed
        mainScreen.previousScreen();
    }//GEN-LAST:event_backButtonMousePressed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        SimpleTimerActionListener listener = SimpleTimerActionListener.getInstance();
        listener.addObserver(this);
    }//GEN-LAST:event_formComponentShown

    @Override
    public void update(String time) {
        elapsedTimeLabel.setText(time);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backButton;
    private javax.swing.JLabel elapsedTimeLabel;
    private javax.swing.JButton endExamButton;
    private javax.swing.JButton fileChooserButton;
    private javax.swing.JList<String> fileList;
    private javax.swing.JLabel fileListLabel;
    private javax.swing.JLabel infoLabel;
    private javax.swing.JPasswordField instructorPasswordField;
    private javax.swing.JLabel instructorPasswordLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel passwordStatusLabel;
    private javax.swing.JLabel timeLabel;
    // End of variables declaration//GEN-END:variables
}
