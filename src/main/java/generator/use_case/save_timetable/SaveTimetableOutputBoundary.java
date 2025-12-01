package generator.use_case.save_timetable;

/**
 * Output boundary for saving a timetable.
 * Implemented by a presenter.
 */
public interface SaveTimetableOutputBoundary {
    void prepareSuccessView(SaveTimetableOutputData outputData);
    void prepareFailView(String error);
}
