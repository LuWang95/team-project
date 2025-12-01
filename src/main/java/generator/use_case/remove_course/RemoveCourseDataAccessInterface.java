package generator.use_case.remove_course;
import courseinfo.Course;

public interface RemoveCourseDataAccessInterface {
    void remove(Course course);

    Course getCoursebyCode(String courseCode);
}
