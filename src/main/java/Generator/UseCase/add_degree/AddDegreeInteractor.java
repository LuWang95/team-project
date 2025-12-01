package Generator.UseCase.add_degree;

import CourseInfo.Course;
import CourseInfo.Degree;
import Generator.DataAccess.FileUserDataAccessObject;
import Generator.UseCase.add_course.*;



public class AddDegreeInteractor implements AddDegreeInputBoundary {
    private final AddDegreeDataAccessInterface addDegreeDataAccessObject;
    private final AddDegreeOutputBoundary addDegreePresenter;
    private final AddCourseDataAccessInterface addCourseDataAccessObject;
    private final AddCourseOutputBoundary addCoursePresenter;

    public AddDegreeInteractor(AddDegreeDataAccessInterface addDegreeDataAccessObject,
                               AddDegreeOutputBoundary addDegreeOutputBoundary,
                               AddCourseDataAccessInterface addCourseDataAccessObject,
                               AddCourseOutputBoundary addCoursePresenter) {
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
        final String input = addDegreeInputData.getDegree().trim().toUpperCase();

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
            final AddDegreeOutputData addDegreeOutputData = new AddDegreeOutputData(degree.getDegreeCode(),
                    degree.getDegreeName(),
                    degree.getCourses());
            addDegreePresenter.prepareAddDegreeSuccessView(addDegreeOutputData);

            /* Loop through degree's course codes and add them, defaulting to F sessions. */
            for (String str : addDegreeDataAccessObject.getDegreeByCode(input).getCourses()) {
                int yearCode = Character.getNumericValue(str.charAt(3));
                if (yearCode == FileUserDataAccessObject.Year) {
                    if (str.charAt(6) == 'Y' && str.length() < 9) {
                        final Course course = addCourseDataAccessObject.getCoursebyCode(str);
                        addReqs(str);
                    }
                    if (str.charAt(6) == 'H' && str.length() < 9) {
                        try {
                            final String strF = str + "F";
                            addReqs(strF);
                        } catch (NullPointerException e) {
                            final String strS = str + "S";
                            addReqs(strS);

                        }
                    }
                }
            }
        }
    }

//extracted method to add courses.
    private void addReqs(String strS) {
        final Course course = addCourseDataAccessObject.getCoursebyCode(strS);
        final AddCourseOutputData addCourseOutputData = new AddCourseOutputData(course.getCourseCode(),
                course.getCourseTitle(), course.getLectureSections(), course.getTutorialSections(),
                course.getPracticalSections(), course.getCredit(), String.valueOf(course.getSessionCode()));
        addCourseDataAccessObject.add(course);
        addCoursePresenter.prepareAddCourseSuccessView(addCourseOutputData);
    }
}