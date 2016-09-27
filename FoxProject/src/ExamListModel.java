import javax.swing.AbstractListModel;

public class ExamListModel extends AbstractListModel {
    private final Exam[] examList;

    public ExamListModel(Exam[] examList) {
        this.examList = examList;
    }

    @Override
    public int getSize() {
        if(examList == null)
            return 0;
        return examList.length;
    }

    @Override
    public String getElementAt(int index) {
        if(examList == null)
            return "";
        return examList[index].getName();
    }

}
