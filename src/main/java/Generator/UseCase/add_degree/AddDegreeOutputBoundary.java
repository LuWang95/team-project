package Generator.UseCase.add_degree;

public interface AddDegreeOutputBoundary {

    /**
     * prepares the success view for the AddDegree use case
     * @param addDegreeOutputData the output data
     */
    void prepareAddDegreeSuccessView(AddDegreeOutputData addDegreeOutputData);

    /**
     * prepares the failure view for the AddDegree use case
     * @param errorMessage the explanation of the failure
     */
    void prepareAddDegreeFailureView(String errorMessage);
}
