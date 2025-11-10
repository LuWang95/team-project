package Generator.UseCases;
import CourseInfo.*;


public interface AddCourseDataAccessInterface {
    Course getCourse(String coursesCode);

    boolean getCourseByCode(String coursesCode);

    void saveCourses(String coursesCode);



}
