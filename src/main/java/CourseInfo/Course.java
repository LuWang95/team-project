package CourseInfo;

import java.util.ArrayList;

public class Course {
    String courseCode;
    String courseTitle;
    int sessionCode;
    double credit;
    ArrayList<Section> lecture_sections;
    ArrayList<Section> tutorial_sections;
    ArrayList<Section> practical_sections;

    public Course(String courseCode, String courseTitle, int sessionCode, double credit,
                  ArrayList<Section> lectureSections, ArrayList<Section> tutorialSections,
                  ArrayList<Section> practicalSections) {
        this.courseCode = courseCode;
        this.courseTitle = courseTitle;
        this.sessionCode = sessionCode;
        this.credit = credit;
        this.lecture_sections = lectureSections;
        this.tutorial_sections = tutorialSections;
        this.practical_sections = practicalSections;
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
        return lecture_sections;
    }

    public ArrayList<Section> getTutorialSections() {
        return tutorial_sections;
    }

    public ArrayList<Section> getPracticalSections() {return practical_sections;}
}
