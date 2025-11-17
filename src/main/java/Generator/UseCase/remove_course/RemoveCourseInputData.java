package Generator.UseCase.remove_course;

public class RemoveCourseInputData {

    private final String course;

    public RemoveCourseInputData(String course) {
        this.course = course;
    }

    String getCourse() {
        return course;
    }
}
