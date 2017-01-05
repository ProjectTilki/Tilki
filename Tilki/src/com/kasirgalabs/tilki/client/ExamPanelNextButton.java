package com.kasirgalabs.tilki.client;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import javax.swing.JButton;

public class ExamPanelNextButton extends JButton implements
        ServiceListener<Boolean> {

    @Override
    public void servicePerformed(Future<Boolean> future) {
        setEnabled(false);
        try {
            if(future.get()) {
                setEnabled(true);
                return;
            }
            setEnabled(false);
        }
        catch(InterruptedException | ExecutionException ex) {
        }
    }

}
