package Generator.InterfaceAdapter.set_preferences;

import Generator.InterfaceAdapter.ViewManagerModel;
import Generator.UseCase.add_course.AddCourseOutputBoundary;
import Generator.UseCase.add_course.AddCourseOutputData;
import Generator.UseCase.remove_course.RemoveCourseOutputBoundary;
import Generator.UseCase.remove_course.RemoveCourseOutputData;

public class SetPreferencesPresenter implements AddCourseOutputBoundary, RemoveCourseOutputBoundary {
    private final SetPreferencesViewModel setPreferencesViewModel;
    private final ViewManagerModel viewManagerModel;

    public SetPreferencesPresenter(ViewManagerModel viewManagerModel, SetPreferencesViewModel setPreferencesViewModel) {
        this.setPreferencesViewModel = setPreferencesViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareAddCourseSuccessView(AddCourseOutputData addCourseOutputData) {
        final SetPreferencesState setPreferencesState = setPreferencesViewModel.getState();
        setPreferencesState.getCourses().add(addCourseOutputData.getCourse());
        setPreferencesState.setCourseError(null);
        setPreferencesViewModel.firePropertyChange();

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

        viewManagerModel.setState(setPreferencesViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }
}
