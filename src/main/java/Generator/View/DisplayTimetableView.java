package Generator.InterfaceAdapter.display_timetable;

import Generator.UseCase.generate_timetable.GenerateTimetableInputBoundary;
import Generator.UseCase.generate_timetable.TimetableDTO;
import Generator.UseCase.regenerate_timetable.RegenerateTimetableInputBoundary;
import Generator.UseCase.return_to_prefs.ReturnToPrefsInputBoundary;
import Generator.UseCase.save_timetable.SaveTimetableInputBoundary;
import Generator.UseCase.save_timetable.SaveTimetableInputData;

import java.util.ArrayList;
import java.util.List;

public class DisplayTimetableController {
    private final GenerateTimetableInputBoundary generateTimeTableInteractor;
    private final ReturnToPrefsInputBoundary returnToPrefsInteractor;
    private final RegenerateTimetableInputBoundary regenerateTimeTableInteractor;
    private final SaveTimetableInputBoundary saveTimetableInteractor;

    public DisplayTimetableController(GenerateTimetableInputBoundary generateTimeTableInteractor,
                                      ReturnToPrefsInputBoundary returnToPrefsInteractor,
                                      RegenerateTimetableInputBoundary regenerateTimeTableInteractor,
                                      SaveTimetableInputBoundary saveTimetableInteractor) {
        this.generateTimeTableInteractor = generateTimeTableInteractor;
        this.returnToPrefsInteractor = returnToPrefsInteractor;
        this.regenerateTimeTableInteractor = regenerateTimeTableInteractor;
        this.saveTimetableInteractor = saveTimetableInteractor;
    }

    public void returnToPrefs() {
        returnToPrefsInteractor.execute();
    }

    public void regenerateTimetable() {
        regenerateTimeTableInteractor.execute();
    }

    public void saveTimetables(List<TimetableDTO> fallTimetables,
                               List<TimetableDTO> winterTimetables,
                               String filePath) {
        ArrayList<TimetableDTO> fallList =
                fallTimetables == null ? new ArrayList<>() : new ArrayList<>(fallTimetables);
        ArrayList<TimetableDTO> winterList =
                winterTimetables == null ? new ArrayList<>() : new ArrayList<>(winterTimetables);

        SaveTimetableInputData inputData =
                new SaveTimetableInputData(fallList, winterList, filePath);
        saveTimetableInteractor.execute(inputData);
    }
}
