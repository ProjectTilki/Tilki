package com.kasirgalabs.tilki.server;

import com.kasirgalabs.tilki.utils.Exam;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExamManager {

    private static volatile ExamManager instance = null;

    public static ExamManager getInstance() {
        if(instance == null) {
            synchronized(ExamManager.class) {
                if(instance == null) {
                    instance = new ExamManager();
                }
            }
        }
        return instance;
    }
    private final List<Exam> examList = Collections.synchronizedList(
            new ArrayList<>());

    private ExamManager() {
    }

    public synchronized boolean addExam(Exam exam) {
        if(examList.contains(exam)) {
            return false;
        }
        examList.add(exam);
        return true;
    }

    public boolean isExamExists(String examName) {
        for(Exam exam : examList) {
            if(exam.getName().equals(examName)) {
                return true;
            }
        }
        return false;
    }

    public String getExamDescription(String examName) {
        for(Exam exam : examList) {
            if(exam.getName().equals(examName)) {
                return exam.getDescription();
            }
        }
        return null;
    }

    public char[] getExamKey(String examName) {
        for(Exam exam : examList) {
            if(exam.getName().equals(examName)) {
                return exam.getKey();
            }
        }
        return null;
    }
}
