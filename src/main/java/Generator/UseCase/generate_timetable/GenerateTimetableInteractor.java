package Generator.UseCase.generate_timetable;

import CourseInfo.*;
import java.util.ArrayList;

public class GenerateTimetableInteractor implements GenerateTimetableInputBoundary {
    final GenerateTimetableOutputBoundary generateTimeTablePresenter;
    final GenerateTimetableDataAccessInterface generateTimeTableDataAccessObject;
    ArrayList<Timetable> rawTimetables = new ArrayList<>();
    ArrayList<Timetable> filteredTimetables = new ArrayList<>();

    public GenerateTimetableInteractor(GenerateTimetableDataAccessInterface generateTimeTableDataAccessObject,
                                       GenerateTimetableOutputBoundary generateTimeTablePresenter) {
        this.generateTimeTablePresenter = generateTimeTablePresenter;
        this.generateTimeTableDataAccessObject = generateTimeTableDataAccessObject;
    }

    public void execute() {
        if(generateTimeTableDataAccessObject.getCourses() == null ||
                generateTimeTableDataAccessObject.getCourses().isEmpty()) {
            generateTimeTablePresenter.prepareGenerateTimetableFailureView("No added courses");
            return;
        }

        ArrayList<Course> addedCourses = generateTimeTableDataAccessObject.getCourses();
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

        rawTimetables.clear();
        filteredTimetables.clear();
        Timetable emptyTimetable = new Timetable();
        addAllCombination(allSections, emptyTimetable, 0, respectiveCourseCode);

        for (Timetable timetable: rawTimetables) {
            if (timetable.isValid()){
                filteredTimetables.add(timetable);
            }
        }

        ArrayList<TimetableDTO> dtoList = new ArrayList<>();
        for (Timetable timetable : filteredTimetables) {
            dtoList.add(TimetableDTO.fromEntity(timetable));
        }

        GenerateTimetableOutputData outputData = new GenerateTimetableOutputData(dtoList);
        generateTimeTablePresenter.prepareGenerateTimetableSuccessView(outputData);
    }

    private void addAllCombination(ArrayList<ArrayList<Section>> allSections,
                                   Timetable curTimetable,
                                   Integer index,
                                   ArrayList<String> courseCodes) {
        if(index == allSections.size()){
            rawTimetables.add(curTimetable);
            return;
        }
        if(allSections.get(index).isEmpty()){
            addAllCombination(allSections, curTimetable, index+1, courseCodes);
            return;
        }
        for (Section section : allSections.get(index)) {
            Timetable newTimetable = new Timetable(curTimetable);
            newTimetable.setBlocks(section, courseCodes.get(index));
            if (newTimetable.isValid()) {
                addAllCombination(allSections, newTimetable, index + 1, courseCodes);
            }
        }
    }
}
