package Generator.InterfaceAdapter;
import CourseInfo.Course;

import java.util.ArrayList;

public class DisplayTimeTableState {
    private ArrayList<String> courses = new ArrayList<>();
    private String noCourseError;

    public DisplayTimeTableState() {

    }

    public ArrayList<String> getCourses() {
        return courses;
    }
    public void setCourses(ArrayList<String> courses) {
        this.courses = courses;
    }
}


