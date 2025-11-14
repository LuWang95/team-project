package Generator.UseCases.AddCourse;

import CourseInfo.LectureSection;
import CourseInfo.PracticalSection;
import CourseInfo.TutorialSection;

import java.util.ArrayList;

public class AddCourseOutputData {
    private final String courseName;
    private final String courseCode;
    private final ArrayList<LectureSection> lectureSection;
    private final ArrayList<TutorialSection> tutorialSection;
    private final ArrayList<PracticalSection> practicalSection;
    private final double credit;
    private final String sessionCode;

    public AddCourseOutputData(String courseCode, String courseName,ArrayList<LectureSection> lectureSection,ArrayList<TutorialSection> tutorialSection,
                               ArrayList<PracticalSection> practicalSection, double credit, String sessionCode) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.lectureSection = lectureSection;
        this.tutorialSection = tutorialSection;
        this.practicalSection = practicalSection;
        this.credit = credit;
        this.sessionCode = sessionCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }
    public ArrayList<LectureSection> getLectureSection() {
        return lectureSection;
    }
    public ArrayList<TutorialSection> getTutorialSection() {
        return tutorialSection;
    }
    public ArrayList<PracticalSection> getPracticalSection() {
        return practicalSection;
    }

    public double getCredit() {
        return credit;
    }
    public String getSessionCode() {
        return sessionCode;
    }

}
