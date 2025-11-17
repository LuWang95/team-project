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
        String code = addCourseInputData.getCourse().trim().toUpperCase();
        if (code.isEmpty()) {
            addCoursePresenter.prepareAddCourseFailureView("Enter a course");
        }
        else if (addCourseDataAccessObject.courseAlreadyAdded(code)) {
            addCoursePresenter.prepareAddCourseFailureView("Course already selected");
        }
        else if (!addCourseDataAccessObject.courseExists(code)) {
            addCoursePresenter.prepareAddCourseFailureView("Course not found");
        }
        else {
            final Course course = addCourseDataAccessObject.getCoursebyCode(code);
            addCourseDataAccessObject.add(course);

            final AddCourseOutputData addCourseOutputData = new AddCourseOutputData(course.getCourseCode(),
                    course.getCourseTitle(), course.getLectureSections(), course.getTutorialSections(),
                    course.getPracticalSections(), course.getCredit(), String.valueOf(course.getSessionCode()));
            addCoursePresenter.prepareAddCourseSuccessView(addCourseOutputData);
        }
    }
}
