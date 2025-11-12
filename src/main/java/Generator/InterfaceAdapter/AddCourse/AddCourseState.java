package Generator.InterfaceAdapter.AddCourse;

import java.util.ArrayList;

public class AddCourseState {
    private String coursesCode = "";
    private ArrayList<String> curCourses = new ArrayList<>();
    private String courseNotFoundError;

    private AddCourseState(AddCourseState copy) {
        this.coursesCode = copy.coursesCode;
        this.courseNotFoundError = copy.courseNotFoundError;
        this.curCourses = copy.curCourses;
    }

    AddCourseState() {

    }
    public String getCoursesCode() {
        return coursesCode;
    }
    public String getCourseNotFoundError() {
        return courseNotFoundError;
    }
    public ArrayList<String> getCurCourses() {
        return curCourses;
    }
    public void setCoursesCode(String coursesCode) {
        this.coursesCode = coursesCode;
    }
    public void setCourseNotFoundError(String courseNotFoundError) {
        this.courseNotFoundError = courseNotFoundError;
    }
    public void setCurCourses(ArrayList<String> curCourses) {
        this.curCourses = curCourses;
    }
}
