package Generator.UseCase.add_course;

public class AddCourseOutputData {
    private final String course;

    public AddCourseOutputData(String course) {
        this.course = course;
    }

    public String getCourse() {
        return course;
    }
}
