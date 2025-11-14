package Generator.InterfaceAdapter.set_preferences;

import java.util.ArrayList;

public class SetPreferencesState {
    private ArrayList<String> courses;
    private String courseError;
    private ArrayList<String> degrees;
    private String selectedCourse;
    private String selectedDegree;
    private int year;
    private boolean morning;
    private boolean afternoon;
    private boolean evening;

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

    public boolean isMorning() {
        return morning;
    }

    public void setMorning(boolean morning) {
        this.morning = morning;
    }

    public boolean isAfternoon() {
        return afternoon;
    }

    public void setAfternoon(boolean afternoon) {
        this.afternoon = afternoon;
    }

    public boolean isEvening() {
        return evening;
    }

    public void setEvening(boolean evening) {
        this.evening = evening;
    }

    @Override
    public String toString() {
        return "AddCourseState{"
                + "courses='" + courses + '\''
                + ", degrees='" + degrees + '\''
                + ", selectedCourse='" + selectedCourse + '\''
                + ", selectedDegree='" + selectedDegree + '\''
                + ", year='" + year + '\''
                + ", morning='" + morning + '\''
                + ", afternoon='" + afternoon + '\''
                + ", evening='" + evening + '\''
                + '}';
    }
}
