package Generator.UseCase.add_course;

public interface AddCourseOutputBoundary {

    /**
     * prepares the success view for the AddCourse use case
     * @param addCourseOutputData the output data
     */
    void prepareAddCourseSuccessView(AddCourseOutputData addCourseOutputData);

    /**
     * prepares the failure view for the AddCourse use case
     * @param errorMessage the explanation of the failure
     */
    void prepareAddCourseFailureView(String errorMessage);
}
