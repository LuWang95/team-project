package Generator.InterfaceAdapter.save_timetable;

import Generator.InterfaceAdapter.ViewManagerModel;
import Generator.InterfaceAdapter.display_timetable.DisplayTimetableViewModel;
import Generator.UseCase.save_timetable.SaveTimetableOutputBoundary;
import Generator.UseCase.save_timetable.SaveTimetableOutputData;

public class SaveTimetablePresenter implements SaveTimetableOutputBoundary {

    private final DisplayTimetableViewModel displayTimetableViewModel;
    private final ViewManagerModel viewManagerModel;

    public SaveTimetablePresenter(DisplayTimetableViewModel displayTimetableViewModel,
                                  ViewManagerModel viewManagerModel) {
        this.displayTimetableViewModel = displayTimetableViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSaveSuccessView(SaveTimetableOutputData outputData) {
        SaveTimetableState state = displayTimetableViewModel.getSaveState();
        state.setSuccessMessage("Saved to: " + outputData.getFilePath());
        displayTimetableViewModel.firePropertyChange();
    }

    @Override
    public void prepareSaveFailureView(String errorMessage) {
        SaveTimetableState state = displayTimetableViewModel.getSaveState();
        state.setErrorMessage(errorMessage);
        displayTimetableViewModel.firePropertyChange();
    }
}
