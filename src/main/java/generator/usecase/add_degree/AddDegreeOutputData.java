package generator.usecase.add_degree;
import java.util.*;

public class AddDegreeOutputData {
    private final String degreeCode;
    private final String degreeName;
    private final ArrayList<String> courses;

    public AddDegreeOutputData(String degreeCode, String degreeName, ArrayList<String> courses){
        this.degreeCode = degreeCode;
        this.degreeName = degreeName;
        this.courses = courses;

    }

    public String getDegreeCode() {
        return degreeCode;
    }
    public String getDegreeName() { return degreeName;}
    public ArrayList<String> getCourses() { return courses;}
}
