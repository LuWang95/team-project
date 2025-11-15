package Generator.UseCases.DisplayTimeTable;

import CourseInfo.*;

import java.util.ArrayList;
import java.util.Collection;

public class DisplayTimeTableInteractor implements DisplayTimeTableInputBoundary {
    final DisplayTimeTableOutputBoundary displayTimeTablePresenter;
    final DisplayTimeTableDataAccessInterface displayTimeTableDataAccessObject;
    ArrayList<TimeTable> rawTimeTables = new ArrayList<>();
    ArrayList<TimeTable> filteredTimeTables = new ArrayList<>();

    public DisplayTimeTableInteractor(DisplayTimeTableOutputBoundary displayTimeTablePresenter,
                                      DisplayTimeTableDataAccessInterface displayTimeTableDataAccessObject) {
        this.displayTimeTablePresenter = displayTimeTablePresenter;
        this.displayTimeTableDataAccessObject = displayTimeTableDataAccessObject;
    }

    public void execute() {
        if(displayTimeTableDataAccessObject.getAddedCourses() == null || displayTimeTableDataAccessObject.getAddedCourses().isEmpty()) {
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


            int n = lectureSections.size();


            ArrayList<ArrayList<Section>> allSections = new ArrayList<>();
            ArrayList<String> respectiveCourseCode = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                allSections.add(lectureSections.get(i));
                allSections.add(secondarySections.get(i));
                respectiveCourseCode.add(addedCourses.get(i).getCourseCode());
                respectiveCourseCode.add(addedCourses.get(i).getCourseCode());
            }


            rawTimeTables.clear();
            filteredTimeTables.clear();
            TimeTable emptyTimeTable = new TimeTable();
            addAllCombination(allSections,emptyTimeTable,0,respectiveCourseCode);


            for (TimeTable timetable:rawTimeTables) {
                if (timetable.isValid()){
                    filteredTimeTables.add(timetable);
                }
            }

            ArrayList<TimeTableDTO> dtoList = new ArrayList<>();
            for (TimeTable timetable : filteredTimeTables) {
                dtoList.add(TimeTableDTO.fromEntity(timetable));
            }

            DisplayTimeTableOutputData outputData = new DisplayTimeTableOutputData(dtoList);
            displayTimeTablePresenter.prepareSuccessView(outputData);
        }


        }

    private void addAllCombination(ArrayList<ArrayList<Section>> allSections, TimeTable curTimeTable, Integer index, ArrayList<String> courseCodes) {
        if(index == allSections.size()){
            rawTimeTables.add(curTimeTable);
            return;
        }
        if(allSections.get(index).isEmpty()){
            addAllCombination(allSections,curTimeTable,index+1,courseCodes);
            return;
        }
        for (Section section : allSections.get(index)) {
            TimeTable newTimeTable = new TimeTable(curTimeTable);
            newTimeTable.setBlocks(section, courseCodes.get(index));
            addAllCombination(allSections, newTimeTable,index+1, courseCodes);
        }




    }


}
