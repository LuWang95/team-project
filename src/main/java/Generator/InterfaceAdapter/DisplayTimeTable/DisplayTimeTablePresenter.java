package Generator.InterfaceAdapter.DisplayTimeTable;

import Generator.UseCases.DisplayTimeTable.DisplayTimeTableOutputBoundary;
import Generator.UseCases.DisplayTimeTable.DisplayTimeTableOutputData;

public class DisplayTimeTablePresenter implements DisplayTimeTableOutputBoundary {
    private final DisplayTimeTableViewModel displayTimeTableViewModel;

    public DisplayTimeTablePresenter(DisplayTimeTableViewModel displayTimeTableViewModel) {
        this.displayTimeTableViewModel = displayTimeTableViewModel;
    }


    @Override
    public void prepareErrorView(String errorMessage){
        DisplayTimeTableState displayTimeTableState = displayTimeTableViewModel.getState();
        displayTimeTableState.setNoCourseError(errorMessage);
        displayTimeTableViewModel.firePropertyChange();
    }

    /**
     * Presenter method called when timetable the generation process succeeds.
     * It writes the list of TimeTableDTOs into the ViewModel state.
     * View code can get the updated state through the property change listener.
     */
    @Override
    public void prepareSuccessView(DisplayTimeTableOutputData outputData) {
        DisplayTimeTableState displayTimeTableState = displayTimeTableViewModel.getState();
        displayTimeTableState.setAllTimeTables(outputData.getAllTimetables());
        displayTimeTableViewModel.firePropertyChange();

    }


}
