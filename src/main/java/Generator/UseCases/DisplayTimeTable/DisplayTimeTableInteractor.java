package Generator.UseCases.DisplayTimeTable;

import CourseInfo.*;

import java.util.ArrayList;
import java.util.Collection;

public class DisplayTimeTableInteractor implements DisplayTimeTableInputBoundary {
    final DisplayTimeTableOutputBoundary displayTimeTablePresenter;
    final DisplayTimeTableDataAccessInterface displayTimeTableDataAccessObject;

    public DisplayTimeTableInteractor(DisplayTimeTableOutputBoundary displayTimeTablePresenter,
                                      DisplayTimeTableDataAccessInterface displayTimeTableDataAccessObject) {
        this.displayTimeTablePresenter = displayTimeTablePresenter;
        this.displayTimeTableDataAccessObject = displayTimeTableDataAccessObject;
    }

    public void execute() {
        if(displayTimeTableDataAccessObject.getAddedCourses() == null){
            displayTimeTablePresenter.prepareErrorView("No added courses");
            return;
        }else{
            ArrayList<Course> addedCourses = displayTimeTableDataAccessObject.getAddedCourses();
            ArrayList<ArrayList<Section>> lectureSections = new ArrayList<>();
            ArrayList<ArrayList<Section>> secondarySections = new ArrayList<>();

            for(Course course: addedCourses){
                lectureSections.add(course.getLectureSections());
                if(course.getPracticalSections().isEmpty() && course.getTutorialSections().isEmpty()){
                    secondarySections.add(new ArrayList<>());
                }else if(! course.getTutorialSections().isEmpty()){
                    secondarySections.add(course.getTutorialSections());
                }else if(! course.getPracticalSections().isEmpty()){
                    secondarySections.add(course.getPracticalSections());
                }
            }

            ArrayList<String>[][] timetable = new ArrayList[5][12];
            for (int day = 0; day < 5; day++) {
                for (int hour = 0; hour < 12; hour++) {
                    timetable[day][hour] = new ArrayList<>();
                }
            }

            int n = lectureSections.size();

            ArrayList<ArrayList<Section>> allSections = new ArrayList<>();
            ArrayList<String> respectiveCourseCode = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                allSections.add(lectureSections.get(i));
                allSections.add(secondarySections.get(i));
                respectiveCourseCode.add(addedCourses.get(i).getCourseCode());
                respectiveCourseCode.add(addedCourses.get(i).getCourseCode());
            }









        }



    }


}
