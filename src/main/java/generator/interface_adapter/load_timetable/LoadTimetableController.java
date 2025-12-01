package generator.interface_adapter.load_timetable;

import generator.use_case.load_timetable.LoadTimetableInputBoundary;
import generator.use_case.load_timetable.LoadTimetableInputData;

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
