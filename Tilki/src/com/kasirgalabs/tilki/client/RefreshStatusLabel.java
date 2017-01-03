/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasirgalabs.tilki.client;

import com.kasirgalabs.tilki.utils.ExamList;
import com.kasirgalabs.tilki.utils.TilkiColor;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import javax.swing.JLabel;

public class RefreshStatusLabel extends JLabel implements
        ServiceListener<ExamList> {

    @Override

    public void servicePerformed(Future<ExamList> future) {
        ExamList examList;
        try {
            examList = future.get();
        }
        catch(InterruptedException | ExecutionException ex) {
            setText("Bağlantı kurulamadı.");
            setForeground(TilkiColor.RED);
            return;
        }
        setText("Bağlantı kuruldu.");
        setForeground(TilkiColor.GREEN);
    }

}
