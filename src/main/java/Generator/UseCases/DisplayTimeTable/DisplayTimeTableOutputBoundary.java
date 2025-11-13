package Generator.UseCases.DisplayTimeTable;

public interface DisplayTimeTableOutputBoundary {
    void prepareErrorView(String errorMessage);

    void prepareSuccessView(DisplayTimeTableInputData displayTimeTableInputData);
}
