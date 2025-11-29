package Generator.InterfaceAdapter.save_timetable;

import Generator.UseCase.save_timetable.SaveTimetableOutputBoundary;
import Generator.UseCase.save_timetable.SaveTimetableOutputData;

public class SaveTimetablePresenter implements SaveTimetableOutputBoundary {

    private final SaveTimetableViewModel saveTimetableViewModel;

    public SaveTimetablePresenter(SaveTimetableViewModel saveTimetableViewModel) {
        this.saveTimetableViewModel = saveTimetableViewModel;
    }

    @Override
    public void prepareSuccessView(SaveTimetableOutputData outputData) {
        SaveTimetableState state = saveTimetableViewModel.getState();
        state.setSuccess(true);
        state.setFileName(outputData.getFileName());
        state.setMessage("Timetable saved successfully to " + outputData.getFileName());
        saveTimetableViewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
        SaveTimetableState state = saveTimetableViewModel.getState();
        state.setSuccess(false);
        state.setMessage(error);
        saveTimetableViewModel.firePropertyChange();
    }
}
