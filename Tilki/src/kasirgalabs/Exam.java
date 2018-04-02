package kasirgalabs;

import java.io.Serializable;

/**
 * The {@code Exam} class represents exams.
 */
public class Exam implements Serializable {

    private static final long serialVersionUID = 1L;
    private final String name;
    private final String description;
    private Boolean examStatus;

    public Exam(String name, String description, Boolean examStatus) {
        this.name = name;
        this.description = description;
        this.examStatus = examStatus;
    }

    /**
     * @return Name of the exam.
     */
    public String getName() {
        return name;
    }

    /**
     * @return Description of the exam.
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * @return ExamStatus of the exam.
     */
    public Boolean getExamStatus() {
        return examStatus;
    }
    
    /**
     * sets ExamStatus of the exam.
     */
    public void setExamStatus(Boolean examStatus) {
    		this.examStatus = examStatus;
    }
}
