package Generator.UseCase.generate_timetable;

import CourseInfo.*;
import java.util.ArrayList;

public class GenerateTimetableInteractor implements GenerateTimetableInputBoundary {
    final GenerateTimetableOutputBoundary generateTimeTablePresenter;
    final GenerateTimetableDataAccessInterface generateTimeTableDataAccessObject;
    final private static int LIMIT = 5000;
    ArrayList<Timetable> rawTimetables = new ArrayList<>();

    public GenerateTimetableInteractor(GenerateTimetableDataAccessInterface generateTimeTableDataAccessObject,
                                       GenerateTimetableOutputBoundary generateTimeTablePresenter) {
        this.generateTimeTablePresenter = generateTimeTablePresenter;
        this.generateTimeTableDataAccessObject = generateTimeTableDataAccessObject;
    }

    public void execute() {
        if(generateTimeTableDataAccessObject.getCourses() == null || generateTimeTableDataAccessObject.getCourses().isEmpty()) {
            generateTimeTablePresenter.prepareGenerateTimetableFailureView("No added courses");
            return;
        }else{
            ArrayList<Course> fallCourses = new ArrayList<>();
            ArrayList<Course> winterCourses = new ArrayList<>();
            ArrayList<Course> addedCourses = generateTimeTableDataAccessObject.getCourses();
            ArrayList<ArrayList<Section>> lectureSections = new ArrayList<>();
            ArrayList<ArrayList<Section>> secondarySections = new ArrayList<>();

            for (Course course : addedCourses) {
                String CourseCode = course.getCourseCode();
                if (CourseCode.charAt(6) == 'Y') {
                    fallCourses.add(course);
                    winterCourses.add(course);
                }else{
                    int sessionCode = course.getSessionCode();
                    if (sessionCode == 20259) {
                        fallCourses.add(course);
                    }else{
                        winterCourses.add(course);
                    }
                }
            }


            for(Course course: fallCourses){
                lectureSections.add(course.getLectureSections());
                if(course.getPracticalSections().isEmpty() && course.getTutorialSections().isEmpty()){
                    secondarySections.add(new ArrayList<>());
                }else if(! course.getTutorialSections().isEmpty()){
                    secondarySections.add(course.getTutorialSections());
                }else if(! course.getPracticalSections().isEmpty()){
                    secondarySections.add(course.getPracticalSections());
                }
            }

            int n = fallCourses.size();
            ArrayList<ArrayList<Section>> allSections = new ArrayList<>();
            ArrayList<String> respectiveCourseCode = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                allSections.add(lectureSections.get(i));
                allSections.add(secondarySections.get(i));
                respectiveCourseCode.add(fallCourses.get(i).getCourseCode());
                respectiveCourseCode.add(fallCourses.get(i).getCourseCode());
            }

            rawTimetables.clear();
            Timetable emptyTimetable = new Timetable();
            addAllCombination(allSections, emptyTimetable,0,respectiveCourseCode);
            ArrayList<TimetableDTO> fallList = new ArrayList<>();
            for (Timetable timetable : rawTimetables) {
                fallList.add(TimetableDTO.fromEntity(timetable));
            }


            //winterlist
            ArrayList<TimetableDTO> winterList = new ArrayList<>();
            lectureSections.clear();
            secondarySections.clear();
            allSections.clear();
            respectiveCourseCode.clear();
            for(Course course: winterCourses){
                lectureSections.add(course.getLectureSections());
                if(course.getPracticalSections().isEmpty() && course.getTutorialSections().isEmpty()){
                    secondarySections.add(new ArrayList<>());
                }else if(! course.getTutorialSections().isEmpty()){
                    secondarySections.add(course.getTutorialSections());
                }else if(! course.getPracticalSections().isEmpty()){
                    secondarySections.add(course.getPracticalSections());
                }
            }
            n = winterCourses.size();
            for (int i = 0; i < n; i++) {
                allSections.add(lectureSections.get(i));
                allSections.add(secondarySections.get(i));
                respectiveCourseCode.add(winterCourses.get(i).getCourseCode());
                respectiveCourseCode.add(winterCourses.get(i).getCourseCode());
            }
            rawTimetables.clear();
            emptyTimetable = new Timetable();
            addAllCombination(allSections, emptyTimetable,0,respectiveCourseCode);
            for (Timetable timetable : rawTimetables) {
                winterList.add(TimetableDTO.fromEntity(timetable));
            }

            GenerateTimetableOutputData outputData = new GenerateTimetableOutputData(fallList,winterList);
            generateTimeTablePresenter.prepareGenerateTimetableSuccessView(outputData);
        }
    }

    private void addAllCombination(ArrayList<ArrayList<Section>> allSections, Timetable curTimetable, Integer index, ArrayList<String> courseCodes) {
        if (rawTimetables.size() >= LIMIT) {
            return;
        }
        if(index == allSections.size()){
            rawTimetables.add(new Timetable(curTimetable));
            return;
        }
        if(allSections.get(index).isEmpty()){
            addAllCombination(allSections, curTimetable,index+1,courseCodes);
            return;
        }
        for (Section section : allSections.get(index)) {
            if(curTimetable.canAddBlock(section, courseCodes.get(index))){
                curTimetable.setBlocks(section, courseCodes.get(index));
                addAllCombination(allSections, curTimetable,index+1,courseCodes);
                curTimetable.removeBlocks(section, courseCodes.get(index));
            }
        }
    }

}
