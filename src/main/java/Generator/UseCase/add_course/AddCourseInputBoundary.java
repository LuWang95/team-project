package Generator.UseCase.add_course;

public interface AddCourseInputBoundary {

    void execute(AddCourseInputData addCourseInputData);

    void switchToTimetableView();
}
