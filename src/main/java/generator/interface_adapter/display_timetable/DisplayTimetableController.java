package generator.interface_adapter.display_timetable;


import generator.use_case.generate_timetable.GenerateTimetableInputBoundary;
import generator.use_case.regenerate_timetable.RegenerateTimetableInputBoundary;
import generator.use_case.return_to_prefs.ReturnToPrefsInputBoundary;

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
