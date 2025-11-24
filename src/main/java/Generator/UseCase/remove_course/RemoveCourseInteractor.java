package Generator.UseCase.remove_course;

import CourseInfo.Course;

public class RemoveCourseInteractor implements RemoveCourseInputBoundary {
    private final RemoveCourseDataAccessInterface removeCourseDataAccessObject;
    private final RemoveCourseOutputBoundary removeCoursePresenter;

    public RemoveCourseInteractor(RemoveCourseDataAccessInterface removeCourseDataAccessObject,
                                  RemoveCourseOutputBoundary removeCourseOutputBoundary) {
        this.removeCourseDataAccessObject = removeCourseDataAccessObject;
        this.removeCoursePresenter = removeCourseOutputBoundary;
    }

    @Override
    public void execute(RemoveCourseInputData removeCourseInputData) {
        final Course course = removeCourseDataAccessObject.getCoursebyCode(removeCourseInputData.getCourse());
        removeCourseDataAccessObject.remove(course);

        final RemoveCourseOutputData removeCourseOutputData = new RemoveCourseOutputData(
                removeCourseInputData.getCourse());
        removeCoursePresenter.prepareRemoveCourseSuccessView(removeCourseOutputData);

    }
}
