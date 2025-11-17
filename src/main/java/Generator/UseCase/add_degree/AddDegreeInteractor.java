package Generator.UseCase.add_degree;

import CourseInfo.Degree;

public class AddDegreeInteractor implements AddDegreeInputBoundary {
    private final AddDegreeDataAccessInterface addDegreeDataAccessObject;
    private final AddDegreeOutputBoundary addDegreePresenter;

    public AddDegreeInteractor(AddDegreeDataAccessInterface addDegreeDataAccessObject,
                               AddDegreeOutputBoundary addDegreeOutputBoundary) {
        this.addDegreeDataAccessObject = addDegreeDataAccessObject;
        this.addDegreePresenter = addDegreeOutputBoundary;
    }

    // checks if the course to be added is already selected
    // if so, then sends an error message to be displayed
    // if not, then records the added course in the files and then tells the presenter to display the added course
    @Override
    public void execute(AddDegreeInputData addDegreeInputData) {
        if (addDegreeDataAccessObject.degreeAlreadyAdded(addDegreeInputData.getDegree())) {
            addDegreePresenter.prepareAddDegreeFailureView("Degree already selected");
        }
        else {
            final Degree degree = new Degree(addDegreeInputData.getDegree(), null);
            addDegreeDataAccessObject.add(degree);

            final AddDegreeOutputData addDegreeOutputData = new AddDegreeOutputData(addDegreeInputData.getDegree());
            addDegreePresenter.prepareAddDegreeSuccessView(addDegreeOutputData);
        }
    }
}
