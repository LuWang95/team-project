package Generator.UseCase.remove_course;

public class RemoveCourseOutputData {
    private final String course;

    public RemoveCourseOutputData(String course) {
        this.course = course;
    }

    public String getCourse() {
        return course;
    }
}
