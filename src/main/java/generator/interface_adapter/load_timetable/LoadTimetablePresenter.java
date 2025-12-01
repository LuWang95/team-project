package generator.interface_adapter.load_timetable;

import generator.interface_adapter.ViewManagerModel;
import generator.interface_adapter.display_timetable.DisplayTimetableState;
import generator.interface_adapter.display_timetable.DisplayTimetableViewModel;
import generator.use_case.load_timetable.LoadTimetableOutputBoundary;
import generator.use_case.load_timetable.LoadTimetableOutputData;

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
