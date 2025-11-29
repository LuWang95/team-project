package Generator.UseCase.add_degree;

import CourseInfo.Degree;
import Generator.InterfaceAdapter.set_preferences.SetPreferencesState;
import Generator.UseCase.add_course.*;

public class AddDegreeInteractor implements AddDegreeInputBoundary {
    private final AddDegreeDataAccessInterface addDegreeDataAccessObject;
    private final AddDegreeOutputBoundary addDegreePresenter;
    private final AddCourseDataAccessInterface addCourseDataAccessObject;
    private final AddCourseOutputBoundary addCoursePresenter;


    public AddDegreeInteractor(AddDegreeDataAccessInterface addDegreeDataAccessObject,
                               AddDegreeOutputBoundary addDegreeOutputBoundary, AddCourseDataAccessInterface addCourseDataAccessObject, AddCourseOutputBoundary addCoursePresenter) {
        this.addDegreeDataAccessObject = addDegreeDataAccessObject;
        this.addDegreePresenter = addDegreeOutputBoundary;
        this.addCoursePresenter = addCoursePresenter;
        this.addCourseDataAccessObject = addCourseDataAccessObject;
    }



    // checks if the degree to be added is already selected
    // if so, then sends an error message to be displayed
    // if not, then records the added degree in the files and then tells the presenter to display the added course
    @Override
    public void execute(AddDegreeInputData addDegreeInputData) {
        String input = addDegreeInputData.getDegree().trim().toUpperCase();
        SetPreferencesState  setPreferencesState = new SetPreferencesState();

        if (input.isEmpty()) {
            addDegreePresenter.prepareAddDegreeFailureView("Enter a degree code");
        }
        else if (!addDegreeDataAccessObject.degreeExists(input)) {
            addDegreePresenter.prepareAddDegreeFailureView("Degree does not exist");
        }
        else if (addDegreeDataAccessObject.degreeAlreadyAdded(input)) {
            addDegreePresenter.prepareAddDegreeFailureView("Degree already selected");
        }
        else {
            final Degree degree = addDegreeDataAccessObject.getDegreeByCode(input);
            addDegreeDataAccessObject.add(degree);
            AddDegreeOutputData addDegreeOutputData = new AddDegreeOutputData(degree.getDegreeCode(),
                    degree.getDegreeName(),
                    degree.getCourses());
            addDegreePresenter.prepareAddDegreeSuccessView(addDegreeOutputData);

            for (String S : addDegreeDataAccessObject.getDegreeByCode(input).getCourses()) {
                if (S.charAt(6)=='Y' && S.length()<9) {
                    Course course = addCourseDataAccessObject.getCoursebyCode(S);
                    AddCourseOutputData addCourseOutputData = new AddCourseOutputData(course.getCourseCode(),
                            course.getCourseTitle(), course.getLectureSections(), course.getTutorialSections(),
                            course.getPracticalSections(), course.getCredit(), String.valueOf(course.getSessionCode()));
                    addCoursePresenter.prepareAddCourseSuccessView(addCourseOutputData);
                    addCourseDataAccessObject.add(course);

                }
                System.out.println(S);
            }
            //        }
//            }

      //      AddCourseInteractor addCourseInteractor = new AddCourseInteractor(addCourseDataAccessObject,addCoursePresenter);
//            System.out.println(degree.getDegreeCode());
  //          System.out.println(degree.getDegreeName());
    //        System.out.println(degree.getCourses());
                   }
    }
}
