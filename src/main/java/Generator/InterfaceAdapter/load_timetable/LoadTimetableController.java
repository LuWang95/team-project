package Generator.InterfaceAdapter.load_timetable;

import Generator.UseCase.load_timetable.LoadTimetableInputBoundary;
import Generator.UseCase.load_timetable.LoadTimetableInputData;

public class LoadTimetableController {

    private final LoadTimetableInputBoundary interactor;

    public LoadTimetableController(LoadTimetableInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void loadTimetable(String filePath) {
        LoadTimetableInputData inputData = new LoadTimetableInputData(filePath);
        interactor.loadTimetable(inputData);
    }
}
