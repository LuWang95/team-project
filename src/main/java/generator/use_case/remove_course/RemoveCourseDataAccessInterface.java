package generator.use_case.remove_course;
import course_info.Course;

public interface RemoveCourseDataAccessInterface {
    void remove(Course course);

    Course getCoursebyCode(String courseCode);
}
