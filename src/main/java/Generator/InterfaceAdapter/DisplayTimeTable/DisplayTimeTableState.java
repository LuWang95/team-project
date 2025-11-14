package Generator.InterfaceAdapter.DisplayTimeTable;
import CourseInfo.LectureSection;
import CourseInfo.PracticalSection;
import CourseInfo.TutorialSection;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DisplayTimeTableState {
    private ArrayList<String> courseCodes = new ArrayList<>();
    private ArrayList<String> courseNames = new ArrayList<>();
    private ArrayList<ArrayList<LectureSection>> lectureSections = new ArrayList<>();
    private ArrayList<ArrayList<TutorialSection>> tutorialSections = new ArrayList<>();
    private ArrayList<ArrayList<PracticalSection>> practicalSections = new ArrayList<>();
    private ArrayList<Double> credit = new ArrayList<>();
    private ArrayList<String> sessionCode = new ArrayList<>();
    private String noCourseError;
    private ArrayList<String[][]> timeTable;

    public DisplayTimeTableState() {
    }

    public ArrayList<String> getCourses() {
        return courseCodes;
    }
    public ArrayList<String > getCourseNames() {
        return courseNames;
    }
    public ArrayList<ArrayList<LectureSection>> getLectureSections() {
        return lectureSections;
    }
    public ArrayList<ArrayList<TutorialSection>> getTutorialSections() {
        return tutorialSections;
    }
    public ArrayList<ArrayList<PracticalSection>> getPracticalSections() {
        return practicalSections;
    }
    public ArrayList<Double> getCredit() {
        return credit;
    }
    public ArrayList<String> getSessionCode() {
        return sessionCode;
    }
    public void setCourses(ArrayList<String> courses) {
        this.courseCodes = courses;
    }
    public void setCourseNames(ArrayList<String> courseNames) {
        this.courseNames = courseNames;
    }
    public void setLectureSections(ArrayList<ArrayList<LectureSection>> lectureSections) {
        this.lectureSections = lectureSections;
    }
    public void setTutorialSections(ArrayList<ArrayList<TutorialSection>> tutorialSections) {
        this.tutorialSections = tutorialSections;
    }
    public void setPracticalSections(ArrayList<ArrayList<PracticalSection>> practicalSections) {
        this.practicalSections = practicalSections;
    }
    public void setCredit(ArrayList<Double> credit) {
        this.credit = credit;
    }
    public void setSessionCode(ArrayList<String> sessionCode) {
        this.sessionCode = sessionCode;
    }
}


