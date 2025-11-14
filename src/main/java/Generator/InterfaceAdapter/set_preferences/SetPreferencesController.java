package Generator.InterfaceAdapter.set_preferences;
import Generator.UseCase.add_course.*;
import Generator.UseCase.remove_course.RemoveCourseInputBoundary;
import Generator.UseCase.remove_course.RemoveCourseInputData;

public class SetPreferencesController {

    private final AddCourseInputBoundary addCourseUseCaseInteractor;
    private final RemoveCourseInputBoundary removeCourseUseCaseInteractor;

    public SetPreferencesController(AddCourseInputBoundary addCourseUseCaseInteractor,
                                    RemoveCourseInputBoundary removeCourseUseCaseInteractor) {
        this.addCourseUseCaseInteractor = addCourseUseCaseInteractor;
        this.removeCourseUseCaseInteractor = removeCourseUseCaseInteractor;
    }

    /**
     * Executes the addCourse Use Case.
     * @param course the course to be added
     */
    public void addCourse(String course) {
        final AddCourseInputData addCourseInputData = new AddCourseInputData(course);
        addCourseUseCaseInteractor.execute(addCourseInputData);
    }

    /**
     * Executes the removeCourse Use Case.
     * @param course the course to be removed
     */
    public void removeCourse(String course) {
        final RemoveCourseInputData removeCourseInputData = new RemoveCourseInputData(course);
        removeCourseUseCaseInteractor.execute(removeCourseInputData);
    }
}
