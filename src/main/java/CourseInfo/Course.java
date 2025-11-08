package CourseInfo;

import java.util.ArrayList;

public class Course {
    String courseCode;
    String courseTitle;
    int sessionCode;
    double credit;
    ArrayList<LectureSection> lecture_sections;
    ArrayList<TutorialSection> tutorial_sections;

    public Course(String courseCode, String courseTitle, int sessionCode, double credit,
                  ArrayList<LectureSection> lectureSections, ArrayList<TutorialSection> tutorialSections) {
        this.courseCode = courseCode;
        this.courseTitle = courseTitle;
        this.sessionCode = sessionCode;
        this.credit = credit;
        this.lecture_sections = lectureSections;
        this.tutorial_sections = tutorialSections;
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

    public ArrayList<LectureSection> getLectureSections() {
        return lecture_sections;
    }

    public ArrayList<TutorialSection> getTutorialSections() {
        return tutorial_sections;
    }


}
