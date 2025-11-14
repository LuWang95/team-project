package Generator.UseCase.add_course;
import CourseInfo.Course;

public interface AddCourseDataAccessInterface {

    /**
     * checks if the course is already selected
     * @param course the course to look for
     * @return true if the course is already selected
     */
    boolean existsByName(String course);

    /**
     * adds the course to the list
     * @param course the course to be added
     */
    void add(Course course);
}
