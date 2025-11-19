package Generator.UseCase.add_degree;

public class AddDegreeOutputData {
    private final String degree;
    private final int coursesAdded;  // ADD THIS FIELD

    public AddDegreeOutputData(String degree, int coursesAdded) {
        this.degree = degree;
        this.coursesAdded = coursesAdded;
    }

    public String getDegree() {
        return degree;
    }

    public int getCoursesAdded() {
        return coursesAdded;
    }
}
