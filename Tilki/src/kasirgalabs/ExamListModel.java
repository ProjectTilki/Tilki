package kasirgalabs;

import java.util.ArrayList;

import javax.swing.AbstractListModel;

public class ExamListModel extends AbstractListModel {

    private final ArrayList<Exam> examList;

    public ExamListModel(ArrayList<Exam> examList) {
        this.examList = examList;
    }

    @Override
    public int getSize() {
        if(examList == null) {
            return 0;
        }
        return examList.size();
    }

    @Override
    public String getElementAt(int index) {
        if(examList == null) {
            return "";
        }
        return examList.get(index).getName();
    }

}
