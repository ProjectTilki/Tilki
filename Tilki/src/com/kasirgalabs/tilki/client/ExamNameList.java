package com.kasirgalabs.tilki.client;

import com.kasirgalabs.tilki.utils.ExamList;
import com.kasirgalabs.tilki.utils.ExamListModel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JList;

public class ExamNameList extends JList<String> implements
        ServiceListener<ExamList> {

    @Override
    public void servicePerformed(Future<ExamList> future) {
        ExamList examList = null;
        try {
            examList = future.get();
        }
        catch(InterruptedException | ExecutionException ex) {
            Logger.getLogger(ExamNameList.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
        setModel(new ExamListModel(examList));

    }

}
