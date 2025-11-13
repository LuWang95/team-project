package Generator.UseCases.AddCourse;


public class AddCourseInputData {
    public String coursesCode;

    public AddCourseInputData(String coursesCode) {
        this.coursesCode = coursesCode;
    }

    public String getCourseCode() {
        return coursesCode;
    }
}
