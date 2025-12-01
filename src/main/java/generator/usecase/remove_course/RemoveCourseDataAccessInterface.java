package generator.usecase.remove_course;
import courseinfo.Course;

public interface RemoveCourseDataAccessInterface {
    void remove(Course course);

    Course getCoursebyCode(String courseCode);
}
