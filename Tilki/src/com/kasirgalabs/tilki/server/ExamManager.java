package com.kasirgalabs.tilki.server;

public class ExamManager {

    private static ExamManager instance = null;

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

    private ExamManager() {
    }

    public boolean isExamExists(String examName) {
        return true;
    }

    public char[] getExamKey(String examName) {
        return new char[]{'1', '2', '3'};
    }
}
