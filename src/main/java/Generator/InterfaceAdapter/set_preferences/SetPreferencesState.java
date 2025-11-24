package Generator.InterfaceAdapter.set_preferences;

import java.util.ArrayList;

public class SetPreferencesState {
    private ArrayList<String> courses;
    private String courseError;
    private ArrayList<String> degrees;
    private String degreeError;
    private String selectedCourse = "";
    private String selectedDegree = "";
    private String noSelectedCoursesError;
    private int year;
    private ArrayList<String> times;

    public ArrayList<String> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<String> courses) {
        this.courses = courses;
    }

    public String getCourseError() {
        return courseError;
    }

    public void setCourseError(String courseError) {
        this.courseError = courseError;
    }

    public ArrayList<String> getDegrees() {
        return degrees;
    }

    public void setDegrees(ArrayList<String> degrees) {
        this.degrees = degrees;
    }

    public String getDegreeError() {
        return degreeError;
    }

    public void setDegreeError(String degreeError) {
        this.degreeError = degreeError;
    }

    public String getSelectedCourse() {
        return selectedCourse;
    }

    public void setSelectedCourse(String selectedCourse) {
        this.selectedCourse = selectedCourse;
    }

    public String getSelectedDegree() {
        return selectedDegree;
    }

    public void setSelectedDegree(String selectedDegree) {
        this.selectedDegree = selectedDegree;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public ArrayList<String> getTimes() {
        return times;
    }

    public void setTimes(ArrayList<String> times) {
        this.times = times;
    }

    public String getNoSelectedCoursesError() {
        return noSelectedCoursesError;
    }

    public void setNoSelectedCoursesError(String noSelectedCoursesError) {
        this.noSelectedCoursesError = noSelectedCoursesError;
    }

    @Override
    public String toString() {
        return "AddCourseState{"
                + "courses='" + courses + '\''
                + ", degrees='" + degrees + '\''
                + ", selectedCourse='" + selectedCourse + '\''
                + ", selectedDegree='" + selectedDegree + '\''
                + ", year='" + year + '\''
                + ", morning='" + times.contains("Morning") + '\''
                + ", afternoon='" + times.contains("Afternoon") + '\''
                + ", evening='" + times.contains("Evening") + '\''
                + '}';
    }
}
