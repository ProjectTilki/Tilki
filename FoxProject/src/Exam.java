
import java.io.Serializable;

/**
 * Immutable class which represents an exam.
 */
public class Exam implements Serializable{
    private final String name;
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
     * @return description of exam
     */
    public String getDescription() {
        return description;
    }
}
