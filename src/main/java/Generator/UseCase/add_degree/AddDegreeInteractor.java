package Generator.UseCase.add_degree;

import CourseInfo.Degree;
import Generator.DataAccess.JsonDegreeDataAccess;
import Generator.InterfaceAdapter.set_preferences.SetPreferencesState;
import Generator.InterfaceAdapter.set_preferences.SetPreferencesViewModel;

public class AddDegreeInteractor implements AddDegreeInputBoundary {
    private final AddDegreeDataAccessInterface addDegreeDataAccessObject;
    private final AddDegreeOutputBoundary addDegreePresenter;

    public AddDegreeInteractor(AddDegreeDataAccessInterface addDegreeDataAccessObject,
                               AddDegreeOutputBoundary addDegreeOutputBoundary) {
        this.addDegreeDataAccessObject = addDegreeDataAccessObject;
        this.addDegreePresenter = addDegreeOutputBoundary;
    }



    // checks if the degree to be added is already selected
    // if so, then sends an error message to be displayed
    // if not, then records the added degree in the files and then tells the presenter to display the added course
    @Override
    public void execute(AddDegreeInputData addDegreeInputData) {
        String input = addDegreeInputData.getDegree().trim().toUpperCase();
        SetPreferencesState  setPreferencesState = new SetPreferencesState();
        System.out.println();

        if (input.isEmpty()) {
            addDegreePresenter.prepareAddDegreeFailureView("Enter a degree code");
        }
        else if (!addDegreeDataAccessObject.degreeExists(input)) {
            addDegreePresenter.prepareAddDegreeFailureView("Degree does not exist");
        }
        else if (addDegreeDataAccessObject.degreeAlreadyAdded(addDegreeInputData.getDegree())) {
            addDegreePresenter.prepareAddDegreeFailureView("Degree already selected");
        }
        else {
            final Degree degree = addDegreeDataAccessObject.getDegreeByCode(input);
            addDegreeDataAccessObject.add(degree);
            final AddDegreeOutputData addDegreeOutputData = new AddDegreeOutputData(degree.getDegreeCode(),
                    degree.getDegreeName(),
                    degree.getCourses());
            addDegreePresenter.prepareAddDegreeSuccessView(addDegreeOutputData);
/*            System.out.println(degree.getDegreeCode());
            System.out.println(degree.getDegreeName());
            System.out.println(degree.getCourses());
  */      }
    }
}
