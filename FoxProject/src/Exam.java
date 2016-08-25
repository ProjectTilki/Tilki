
import java.io.Serializable;

/**
 * Immutable class which represents an exam.
 */
public class Exam implements Serializable{
    private final String name;
    // Description will set to null in case of exam description does not exists.
    private final String description;
    
    public Exam(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    /**
     * 
     * @return name of the exam
     */
    public String getName() {
        return name;
    }
    
    /**
     * 
     * @return description of the exam
     */
    public String getDescription() {
        return description;
    }
}
