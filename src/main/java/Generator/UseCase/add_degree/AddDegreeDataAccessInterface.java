package Generator.UseCase.add_degree;
import CourseInfo.Degree;
import CourseInfo.Course;
import java.util.ArrayList;

public interface AddDegreeDataAccessInterface {

    /**
     * checks if the degree is already selected
     * @param degree the degree to look for
     * @return true if the degree is already selected
     */
    boolean degreeAlreadyAdded(String degree);

    /**
     * adds the degree to the list
     * @param degree the degree to be added
     */
    void add(Degree degree);
    /**
     * gets the list of required courses for a given degree and year
     * @param degreeName the name of the degree
     * @param year the year level (1, 2, 3, or 4)
     * @return list of courses required for that degree and year
     */
    ArrayList<Course> getRequiredCoursesForDegree(String degreeName, int year);

    /**
     * adds a course to the user's selected courses list
     * @param course the course to be added
     */
    void addCourse(Course course);
}
