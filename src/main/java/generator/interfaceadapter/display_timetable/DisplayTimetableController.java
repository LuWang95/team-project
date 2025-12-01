package generator.interfaceadapter.display_timetable;


import generator.usecase.generate_timetable.GenerateTimetableInputBoundary;
import generator.usecase.regenerate_timetable.RegenerateTimetableInputBoundary;
import generator.usecase.return_to_prefs.ReturnToPrefsInputBoundary;

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
