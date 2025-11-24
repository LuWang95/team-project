package Generator.UseCase.save_timetable;

public interface SaveTimetableOutputBoundary {
    void prepareSaveSuccessView(SaveTimetableOutputData outputData);

    void prepareSaveFailureView(String errorMessage);
}
