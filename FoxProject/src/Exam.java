import java.io.Serializable;

/**
 * The {@code Exam} class represents exams.
 */
public class Exam implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String name;
    private final String description;

    public Exam(String name, String description) {
        this.name = name;
        this.description = description;
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
}
