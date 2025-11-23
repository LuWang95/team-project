package Generator.InterfaceAdapter.display_timetable;


import Generator.UseCase.generate_timetable.GenerateTimetableInputBoundary;
import Generator.UseCase.regenerate_timetable.RegenerateTimetableInputBoundary;
import Generator.UseCase.return_to_prefs.ReturnToPrefsInputBoundary;

public class DisplayTimetableController {
    private final GenerateTimetableInputBoundary generateTimeTableInteractor;
    private final ReturnToPrefsInputBoundary returnToPrefsInteractor;
    private final RegenerateTimetableInputBoundary regenerateTimeTableInteractor;

    public DisplayTimetableController(GenerateTimetableInputBoundary generateTimeTableInteractor,
                                      ReturnToPrefsInputBoundary returnToPrefsInteractor,
                                      RegenerateTimetableInputBoundary regenerateTimeTableInteractor) {
        this.generateTimeTableInteractor = generateTimeTableInteractor;
        this.returnToPrefsInteractor = returnToPrefsInteractor;
        this.regenerateTimeTableInteractor = regenerateTimeTableInteractor;
    }

    public void returnToPrefs() {
        returnToPrefsInteractor.execute();
    }

    public void regenerateTimetable() {
        regenerateTimeTableInteractor.execute();
    }
}
