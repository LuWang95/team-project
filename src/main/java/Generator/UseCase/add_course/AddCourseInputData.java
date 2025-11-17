package Generator.UseCase.add_course;

public class AddCourseInputData {

    private final String course;

    public AddCourseInputData(String course) {
        this.course = course;
    }

    String getCourse() {
        return course;
    }
}
