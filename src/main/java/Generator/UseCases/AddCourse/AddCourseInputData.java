package Generator.UseCases.AddCourse;


public class AddCourseInputData {
    private final String coursesCode;

    public AddCourseInputData(String coursesCode) {
        this.coursesCode = coursesCode;
    }

    public String getCourseCode() {
        return coursesCode;
    }
}
