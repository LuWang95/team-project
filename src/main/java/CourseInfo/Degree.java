package CourseInfo;

import java.util.ArrayList;

// mostly a whatever implementation that works well enough for the set preferences ui.
// i'm leaving it to franklin or smo else to make it better i believe in u lol
public class Degree {
    private final String degreeCode;
    private final String degreeTitle;
    private final ArrayList<Course> courses;

    public Degree(String degreeCode, String degreeTitle) {
        this.degreeCode = degreeCode;
        this.degreeTitle = degreeTitle;
        this.courses = new ArrayList<>();
    }

    public String getDegreeCode() {
        return degreeCode;
    }

    public String getDegreeTitle() {
        return degreeTitle;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void addCourse(Course course) {
        this.courses.add(course);
    }

}
