package Generator.UseCase.add_degree;

import CourseInfo.Degree;
import CourseInfo.Course;
import java.util.ArrayList;

public class AddDegreeInteractor implements AddDegreeInputBoundary {
    private final AddDegreeDataAccessInterface addDegreeDataAccessObject;
    private final AddDegreeOutputBoundary addDegreePresenter;

    public AddDegreeInteractor(AddDegreeDataAccessInterface addDegreeDataAccessObject,
                               AddDegreeOutputBoundary addDegreeOutputBoundary) {
        this.addDegreeDataAccessObject = addDegreeDataAccessObject;
        this.addDegreePresenter = addDegreeOutputBoundary;
    }

    @Override
    public void execute(AddDegreeInputData addDegreeInputData) {
        if (addDegreeDataAccessObject.degreeAlreadyAdded(addDegreeInputData.getDegree())) {
            addDegreePresenter.prepareAddDegreeFailureView("Degree already selected");
        }
        else {
            final Degree degree = new Degree(addDegreeInputData.getDegree(), null);
            addDegreeDataAccessObject.add(degree);

            ArrayList<Course> requiredCourses = addDegreeDataAccessObject
                    .getRequiredCoursesForDegree(
                            addDegreeInputData.getDegree(),
                            addDegreeInputData.getYear()
                    );

            int coursesAdded = 0;
            for (Course course : requiredCourses) {
                addDegreeDataAccessObject.addCourse(course);
                coursesAdded++;
            }

            final AddDegreeOutputData addDegreeOutputData = new AddDegreeOutputData(
                    addDegreeInputData.getDegree(),
                    coursesAdded
            );
            addDegreePresenter.prepareAddDegreeSuccessView(addDegreeOutputData);
        }
    }
}
