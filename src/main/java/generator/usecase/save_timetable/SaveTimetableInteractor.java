package generator.usecase.save_timetable;

import generator.dataaccess.SaveTimetableDataAccessInterface;
import java.io.IOException;

/**
 * Interactor for saving timetables.  Talks to the data layer
 * and presenter.
 */
public class SaveTimetableInteractor implements SaveTimetableInputBoundary {
    private final SaveTimetableDataAccessInterface timetableDataAccess;
    private final SaveTimetableOutputBoundary saveTimetablePresenter;

    public SaveTimetableInteractor(
            SaveTimetableDataAccessInterface timetableDataAccess,
            SaveTimetableOutputBoundary saveTimetablePresenter) {
        this.timetableDataAccess = timetableDataAccess;
        this.saveTimetablePresenter = saveTimetablePresenter;
    }

    @Override
    public void saveTimetable(SaveTimetableInputData inputData) {
        try {
            timetableDataAccess.saveTimetable(
                    inputData.getFallTimetable(),
                    inputData.getWinterTimetable(),
                    inputData.getFileName());
            SaveTimetableOutputData outputData =
                    new SaveTimetableOutputData(inputData.getFileName(), true);
            saveTimetablePresenter.prepareSuccessView(outputData);
        } catch (IOException e) {
            saveTimetablePresenter.prepareFailView(
                    "Failed to save timetable: " + e.getMessage());
        }
    }
}
