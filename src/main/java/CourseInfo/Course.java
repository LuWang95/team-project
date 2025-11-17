package CourseInfo;

import java.util.ArrayList;

public class Course {
    String courseCode;
    String courseTitle;
    int sessionCode;
    double credit;
    ArrayList<Section> lectureSections;
    ArrayList<Section> tutorialSections;
    ArrayList<Section> practicalSections;

    public Course(String courseCode, String courseTitle, int sessionCode, double credit,
                  ArrayList<Section> lectureSections, ArrayList<Section> tutorialSections,
                  ArrayList<Section> practicalSections) {
        this.courseCode = courseCode;
        this.courseTitle = courseTitle;
        this.sessionCode = sessionCode;
        this.credit = credit;
        this.lectureSections = lectureSections;
        this.tutorialSections = tutorialSections;
        this.practicalSections = practicalSections;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public int getSessionCode() {
        return sessionCode;
    }
    public double getCredit() {
        return credit;
    }

    public ArrayList<Section> getLectureSections() {
        return lectureSections;
    }

    public ArrayList<Section> getTutorialSections() {
        return tutorialSections;
    }

    public ArrayList<Section> getPracticalSections() {return practicalSections;}

    public String toString() {
        return courseCode + ": \"" + courseTitle + "\"";
    }
}
