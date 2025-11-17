package Generator.InterfaceAdapter.display_timetable;


import Generator.UseCase.generate_timetable.GenerateTimetableInputBoundary;
import Generator.UseCase.return_to_prefs.ReturnToPrefsInputBoundary;

public class DisplayTimetableController {
    private final GenerateTimetableInputBoundary generateTimeTableInteractor;
    private final ReturnToPrefsInputBoundary returnToPrefsInteractor;

    public DisplayTimetableController(GenerateTimetableInputBoundary generateTimeTableInteractor,
                                      ReturnToPrefsInputBoundary returnToPrefsInteractor) {
        this.generateTimeTableInteractor = generateTimeTableInteractor;
        this.returnToPrefsInteractor = returnToPrefsInteractor;
    }

    public void returnToPrefs() {
        returnToPrefsInteractor.execute();
    }
}
