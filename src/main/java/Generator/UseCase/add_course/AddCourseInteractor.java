package Generator.UseCase.add_course;

import CourseInfo.Course;

public class AddCourseInteractor implements AddCourseInputBoundary {
    private final AddCourseDataAccessInterface addCourseDataAccessObject;
    private final AddCourseOutputBoundary addCoursePresenter;

    public AddCourseInteractor(AddCourseDataAccessInterface addCourseDataAccessObject,
                               AddCourseOutputBoundary addCourseOutputBoundary) {
        this.addCourseDataAccessObject = addCourseDataAccessObject;
        this.addCoursePresenter = addCourseOutputBoundary;
    }

    // checks if the course to be added is already selected
    // if so, then sends an error message to be displayed
    // if not, then records the added course in the files and then tells the presenter to display the added course
    @Override
    public void execute(AddCourseInputData addCourseInputData) {
        if (addCourseDataAccessObject.courseExistsByName(addCourseInputData.getCourse())) {
            addCoursePresenter.prepareAddCourseFailureView("Course already selected");
        }
        else {
            final Course course = new Course(addCourseInputData.getCourse(), null, 0, 0, null, null, null);
            addCourseDataAccessObject.add(course);

            final AddCourseOutputData addCourseOutputData = new AddCourseOutputData(addCourseInputData.getCourse());
            addCoursePresenter.prepareAddCourseSuccessView(addCourseOutputData);
        }
    }
}
