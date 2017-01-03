package com.kasirgalabs.tilki.utils;

import javax.swing.AbstractListModel;

public class ExamListModel extends AbstractListModel {

    private final Exam[] examList;

    public ExamListModel() {
        this.examList = null;
    }

    public ExamListModel(Exam[] examList) {
        this.examList = examList;
    }

    public ExamListModel(ExamList examList) {
        this.examList = new Exam[examList.size()];
        for(int i = 0; i < this.examList.length; i++) {
            this.examList[i] = examList.get(i);
        }
    }

    @Override
    public int getSize() {
        if(examList == null) {
            return 0;
        }
        return examList.length;
    }

    @Override
    public String getElementAt(int index) {
        if(examList == null) {
            return "";
        }
        return examList[index].getName();
    }

}
