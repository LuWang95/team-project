package generator.interface_adapter.set_preferences;

import java.util.ArrayList;

import generator.use_case.add_course.*;
import generator.use_case.add_degree.*;
import generator.use_case.generate_timetable.GenerateTimetableInputBoundary;
import generator.use_case.generate_timetable.GenerateTimetableInputData;
import generator.use_case.remove_course.*;
import generator.use_case.remove_degree.*;

public class SetPreferencesController {

    private final AddCourseInputBoundary addCourseUseCaseInteractor;
    private final RemoveCourseInputBoundary removeCourseUseCaseInteractor;
    private final AddDegreeInputBoundary addDegreeUseCaseInteractor;
    private final RemoveDegreeInputBoundary removeDegreeUseCaseInteractor;
    private final GenerateTimetableInputBoundary generateTimetableUseCaseInteractor;
    private final SetPreferencesViewModel viewModel;

    public SetPreferencesController(AddCourseInputBoundary addCourseUseCaseInteractor,
                                    RemoveCourseInputBoundary removeCourseUseCaseInteractor,
                                    AddDegreeInputBoundary addDegreeUseCaseInteractor,
                                    RemoveDegreeInputBoundary removeDegreeUseCaseInteractor,
                                    GenerateTimetableInputBoundary generateTimetableUseCaseInteractor,
                                    SetPreferencesViewModel viewModel) {
        this.addCourseUseCaseInteractor = addCourseUseCaseInteractor;
        this.removeCourseUseCaseInteractor = removeCourseUseCaseInteractor;
        this.addDegreeUseCaseInteractor = addDegreeUseCaseInteractor;
        this.removeDegreeUseCaseInteractor = removeDegreeUseCaseInteractor;
        this.generateTimetableUseCaseInteractor = generateTimetableUseCaseInteractor;
        this.viewModel = viewModel;
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

    /**
     * Executes displaying timetable with no time preference.
     * @param timePreferences arraylist which stores the preferred time of the user
     */
    public void displayTimetable(ArrayList<String> timePreferences) {
        boolean sortEnabled = viewModel.getState().isSortEnabled();
        final GenerateTimetableInputData inputData = new GenerateTimetableInputData(sortEnabled, timePreferences);
        generateTimetableUseCaseInteractor.execute(inputData);
    }
}
