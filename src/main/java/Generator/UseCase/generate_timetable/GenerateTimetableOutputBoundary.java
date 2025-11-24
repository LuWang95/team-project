package Generator.UseCase.generate_timetable;

public interface GenerateTimetableOutputBoundary {
    void prepareGenerateTimetableFailureView(String errorMessage);

    void prepareGenerateTimetableSuccessView(GenerateTimetableOutputData generateTimeTableOutputData);
}
