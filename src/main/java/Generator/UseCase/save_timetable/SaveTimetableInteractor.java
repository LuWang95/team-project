package Generator.UseCase.save_timetable;

import Generator.UseCase.generate_timetable.TimetableDTO;

import java.io.IOException;
import java.util.List;

public class SaveTimetableInteractor implements SaveTimetableInputBoundary {

    private final SaveTimetableDataAccessInterface dataAccess;
    private final SaveTimetableOutputBoundary presenter;

    public SaveTimetableInteractor(SaveTimetableDataAccessInterface dataAccess,
                                   SaveTimetableOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(SaveTimetableInputData inputData) {
        List<TimetableDTO> fall = inputData.getFallTimetables();
        List<TimetableDTO> winter = inputData.getWinterTimetables();

        boolean hasFall = fall != null && !fall.isEmpty();
        boolean hasWinter = winter != null && !winter.isEmpty();

        // Blueprint alternative: no generated timetable → error:contentReference[oaicite:1]{index=1}
        if (!hasFall && !hasWinter) {
            presenter.prepareSaveFailureView("There is no generated timetable.");
            return;
        }

        String filePath = inputData.getFilePath();
        if (filePath == null || filePath.trim().isEmpty()) {
            presenter.prepareSaveFailureView("No file name specified.");
            return;
        }

        try {
            dataAccess.save(filePath, fall, winter);
            SaveTimetableOutputData outputData = new SaveTimetableOutputData(filePath);
            presenter.prepareSaveSuccessView(outputData);
        } catch (IOException e) {
            presenter.prepareSaveFailureView("Failed to save timetables: " + e.getMessage());
        }
    }
}
