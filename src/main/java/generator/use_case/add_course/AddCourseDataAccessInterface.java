package generator.use_case.add_course;
import course_info.Course;

public interface AddCourseDataAccessInterface {

    /**
     * checks if the course is already selected
     * @param course the course to look for
     * @return true if the course is already selected
     */
    boolean courseAlreadyAdded(String course);

    /**
     * adds the course to the list
     * @param course the course to be added
     */
    void add(Course course);

    boolean courseExists(String course);

    Course getCoursebyCode(String courseCode);
}
