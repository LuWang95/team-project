package CourseInfo;

import java.util.ArrayList;

public class Course {
    String courseCode;
    String courseTitle;
    String sessionCode;
    double credit;
    ArrayList<LectureSection> lecture_sections;
    ArrayList<TutorialSection> tutorial_sections;
    ArrayList<PracticalSection> practical_sections;

    public Course(String courseCode, String courseTitle, String sessionCode, double credit,
                  ArrayList<LectureSection> lectureSections, ArrayList<TutorialSection> tutorialSections,ArrayList<PracticalSection> practicalSections) {
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
    public String getSessionCode() {
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
    public ArrayList<PracticalSection> getPracticalSections() {return practical_sections;}


}
