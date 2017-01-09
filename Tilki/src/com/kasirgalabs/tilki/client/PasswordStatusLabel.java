package com.kasirgalabs.tilki.client;

import com.kasirgalabs.tilki.utils.TilkiColor;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import javax.swing.JLabel;

public class PasswordStatusLabel extends JLabel implements
        ServiceListener<Boolean> {

    @Override
    public void servicePerformed(Future<Boolean> future) {
        try {
            if(future.get()) {
                setText("Şifre doğru.");
                setForeground(TilkiColor.GREEN);
                return;
            }
            setText("Şifre yanlış.");
            setForeground(TilkiColor.RED);
        }
        catch(InterruptedException | ExecutionException ex) {
            setText("Bağlanamadı.");
            setForeground(TilkiColor.BLUE);
        }
    }
}
