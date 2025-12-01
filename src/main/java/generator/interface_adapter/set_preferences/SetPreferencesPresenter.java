package generator.interface_adapter.set_preferences;

import generator.interface_adapter.ViewManagerModel;
import generator.interface_adapter.display_timetable.DisplayTimetableState;
import generator.interface_adapter.display_timetable.DisplayTimetableViewModel;
import generator.use_case.add_course.AddCourseOutputBoundary;
import generator.use_case.add_course.AddCourseOutputData;
import generator.use_case.add_degree.AddDegreeOutputBoundary;
import generator.use_case.add_degree.AddDegreeOutputData;
import generator.use_case.generate_timetable.GenerateTimetableOutputBoundary;
import generator.use_case.generate_timetable.GenerateTimetableOutputData;
import generator.use_case.remove_course.RemoveCourseOutputBoundary;
import generator.use_case.remove_course.RemoveCourseOutputData;
import generator.use_case.remove_degree.RemoveDegreeOutputBoundary;
import generator.use_case.remove_degree.RemoveDegreeOutputData;

public class SetPreferencesPresenter implements AddCourseOutputBoundary, RemoveCourseOutputBoundary,
        AddDegreeOutputBoundary, RemoveDegreeOutputBoundary, GenerateTimetableOutputBoundary {
    private final SetPreferencesViewModel setPreferencesViewModel;
    private final DisplayTimetableViewModel displayTimetableViewModel;
    private final ViewManagerModel viewManagerModel;

    public SetPreferencesPresenter(ViewManagerModel viewManagerModel, SetPreferencesViewModel setPreferencesViewModel,
                                   DisplayTimetableViewModel displayTimetableViewModel) {
        this.setPreferencesViewModel = setPreferencesViewModel;
        this.displayTimetableViewModel = displayTimetableViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareAddCourseSuccessView(AddCourseOutputData addCourseOutputData) {
        final SetPreferencesState setPreferencesState = setPreferencesViewModel.getState();
        setPreferencesState.getCourses().add(addCourseOutputData.getCourseCode());
        setPreferencesState.setCourseError(null);
        setPreferencesViewModel.firePropertyChange();

        final DisplayTimetableState displayTimetableState = displayTimetableViewModel.getState();
        displayTimetableState.addCourseCode(addCourseOutputData.getCourseCode());
        displayTimetableState.addCourseName(addCourseOutputData.getCourseName());
        displayTimetableState.addCredit(addCourseOutputData.getCredit());
        displayTimetableState.resetTimetableIndex();

        viewManagerModel.setState(setPreferencesViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    // runs whenever the course to be added is already in the list
    @Override
    public void prepareAddCourseFailureView(String errorMessage) {
        final SetPreferencesState setPreferencesState = setPreferencesViewModel.getState();
        setPreferencesState.setCourseError(errorMessage);
        setPreferencesViewModel.firePropertyChange();

        viewManagerModel.setState(setPreferencesViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareRemoveCourseSuccessView(RemoveCourseOutputData removeCourseOutputData) {
        final SetPreferencesState setPreferencesState = setPreferencesViewModel.getState();
        setPreferencesState.setCourseError(null);
        setPreferencesState.getCourses().remove(removeCourseOutputData.getCourse());
        setPreferencesViewModel.firePropertyChange();

        final DisplayTimetableState displayTimetableState = displayTimetableViewModel.getState();
        displayTimetableState.removeCourse(removeCourseOutputData.getCourse());
        displayTimetableState.resetTimetableIndex();

        viewManagerModel.setState(setPreferencesViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareAddDegreeSuccessView(AddDegreeOutputData addDegreeOutputData) {
        final SetPreferencesState setPreferencesState = setPreferencesViewModel.getState();
        setPreferencesState.getDegrees().add(addDegreeOutputData.getDegreeCode());
        setPreferencesState.setDegreeError(null);
        setPreferencesViewModel.firePropertyChange();

        viewManagerModel.setState(setPreferencesViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    // runs whenever the degree to be added is already in the list
    @Override
    public void prepareAddDegreeFailureView(String errorMessage) {
        final SetPreferencesState setPreferencesState = setPreferencesViewModel.getState();
        setPreferencesState.setDegreeError(errorMessage);
        setPreferencesViewModel.firePropertyChange();

        viewManagerModel.setState(setPreferencesViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareRemoveDegreeSuccessView(RemoveDegreeOutputData removeDegreeOutputData) {
        final SetPreferencesState setPreferencesState = setPreferencesViewModel.getState();
        setPreferencesState.setDegreeError(null);
        setPreferencesState.getDegrees().remove(removeDegreeOutputData.getDegree());
        setPreferencesViewModel.firePropertyChange();

        viewManagerModel.setState(setPreferencesViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareGenerateTimetableSuccessView(GenerateTimetableOutputData generateTimetableOutputData) {
        final SetPreferencesState setPreferencesState = setPreferencesViewModel.getState();
        setPreferencesState.setNoSelectedCoursesError(null);
        setPreferencesViewModel.firePropertyChange();

        final DisplayTimetableState displayTimetableState = displayTimetableViewModel.getState();
        displayTimetableState.setFallTimetables(generateTimetableOutputData.getFallTimeTables());
        displayTimetableState.setWinterTimetables(generateTimetableOutputData.getWinterTimeTables());
        displayTimetableViewModel.firePropertyChange();

        viewManagerModel.setState(displayTimetableViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareGenerateTimetableFailureView(String errorMessage) {
        final SetPreferencesState setPreferencesState = setPreferencesViewModel.getState();
        setPreferencesState.setNoSelectedCoursesError(errorMessage);
        setPreferencesViewModel.firePropertyChange();

        viewManagerModel.setState(setPreferencesViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }
}
