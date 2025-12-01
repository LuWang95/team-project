package generator.usecase.load_timetable;

public interface LoadTimetableOutputBoundary {
    void prepareSuccessView(LoadTimetableOutputData outputData);
    void prepareFailView(String error);
}
