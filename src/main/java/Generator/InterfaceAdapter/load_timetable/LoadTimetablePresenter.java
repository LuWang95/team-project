package Generator.InterfaceAdapter.load_timetable;

import Generator.InterfaceAdapter.ViewManagerModel;
import Generator.InterfaceAdapter.display_timetable.DisplayTimetableState;
import Generator.InterfaceAdapter.display_timetable.DisplayTimetableViewModel;
import Generator.UseCase.load_timetable.LoadTimetableOutputBoundary;
import Generator.UseCase.load_timetable.LoadTimetableOutputData;

import javax.swing.JOptionPane;

public class LoadTimetablePresenter implements LoadTimetableOutputBoundary {

    private final DisplayTimetableViewModel displayTimetableViewModel;
    private final ViewManagerModel viewManagerModel;

    public LoadTimetablePresenter(DisplayTimetableViewModel displayTimetableViewModel,
                                  ViewManagerModel viewManagerModel) {
        this.displayTimetableViewModel = displayTimetableViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(LoadTimetableOutputData outputData) {
        DisplayTimetableState state = displayTimetableViewModel.getState();

        state.setFallTimetables(outputData.getFallTimetables());
        state.setWinterTimetables(outputData.getWinterTimetables());
        state.setCourses(outputData.getCourseCodes());
        state.setCourseNames(outputData.getCourseNames());
        state.setCredit(outputData.getCredits());
        state.resetTimetableIndex();

        displayTimetableViewModel.setState(state);
        displayTimetableViewModel.firePropertyChange();

        // Switch to display view
        viewManagerModel.setState(displayTimetableViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
        JOptionPane.showMessageDialog(null,
                error,
                "Load Timetable Error",
                JOptionPane.ERROR_MESSAGE);
    }
}
