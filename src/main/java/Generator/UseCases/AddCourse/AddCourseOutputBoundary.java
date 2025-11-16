package Generator.UseCases.AddCourse;

public interface AddCourseOutputBoundary {
    void prepareSuccessView(AddCourseOutputData addcourseOutputData);

    void prepareErrorView(String errorMessage);
}
