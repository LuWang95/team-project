package Generator.InterfaceAdapter.set_preferences;
import Generator.UseCase.add_course.*;
import Generator.UseCase.add_degree.*;
import Generator.UseCase.generate_timetable.GenerateTimetableInputBoundary;
import Generator.UseCase.remove_course.*;
import Generator.UseCase.remove_degree.*;

public class SetPreferencesController {

    private final AddCourseInputBoundary addCourseUseCaseInteractor;
    private final RemoveCourseInputBoundary removeCourseUseCaseInteractor;
    private final AddDegreeInputBoundary addDegreeUseCaseInteractor;
    private final RemoveDegreeInputBoundary removeDegreeUseCaseInteractor;
    private final GenerateTimetableInputBoundary generateTimetableUseCaseInteractor;

    public SetPreferencesController(AddCourseInputBoundary addCourseUseCaseInteractor,
                                    RemoveCourseInputBoundary removeCourseUseCaseInteractor,
                                    AddDegreeInputBoundary addDegreeUseCaseInteractor,
                                    RemoveDegreeInputBoundary removeDegreeUseCaseInteractor,
                                    GenerateTimetableInputBoundary generateTimetableUseCaseInteractor) {
        this.addCourseUseCaseInteractor = addCourseUseCaseInteractor;
        this.removeCourseUseCaseInteractor = removeCourseUseCaseInteractor;
        this.addDegreeUseCaseInteractor = addDegreeUseCaseInteractor;
        this.removeDegreeUseCaseInteractor = removeDegreeUseCaseInteractor;
        this.generateTimetableUseCaseInteractor = generateTimetableUseCaseInteractor;
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

    /**
     * Executes the addDegree Use Case.
     * @param degree the degree to be added
     */
    public void addDegree(String degree) {
        final AddDegreeInputData addDegreeInputData = new AddDegreeInputData(degree);
        addDegreeUseCaseInteractor.execute(addDegreeInputData);
    }

    /**
     * Executes the removeDegree Use Case.
     * @param degree the degree to be removed
     */
    public void removeDegree(String degree) {
        final RemoveDegreeInputData removeDegreeInputData = new RemoveDegreeInputData(degree);
        removeDegreeUseCaseInteractor.execute(removeDegreeInputData);
    }

    public void displayTimetable() {
        generateTimetableUseCaseInteractor.execute();
    }
}
