package Generator.UseCase.regenerate_timetable;

public class RegenerateTimetableInteractor implements RegenerateTimetableInputBoundary {

    private final RegenerateTimetableOutputBoundary regenerateTimetableOutputBoundary;

    public RegenerateTimetableInteractor(RegenerateTimetableOutputBoundary regenerateTimetableOutputBoundary) {
        this.regenerateTimetableOutputBoundary = regenerateTimetableOutputBoundary;
    }

    public void execute() {
        regenerateTimetableOutputBoundary.prepareRegenerateSuccessView();
    }
}
