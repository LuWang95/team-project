package Generator.UseCase.remove_course;
import CourseInfo.Course;

public interface RemoveCourseDataAccessInterface {
    void remove(Course course);

    Course getCoursebyCode(String courseCode);
}
