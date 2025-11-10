package Generator.UseCases;

public interface AddCourseOutputBoundary {
    void prepareSuccessView(AddCourseOutputData addcourseOutputData);

    void prepareErrorView(String errorMessage);
}
