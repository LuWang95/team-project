package generator.use_case.generate_timetable;

import course_info.*;
import java.util.ArrayList;
import java.util.List;

import generator.use_case.sort_timetable.SortTimetableInputBoundary;
import generator.use_case.sort_timetable.SortTimetableInputData;
import generator.use_case.sort_timetable.SortTimetableOutputData;

public class GenerateTimetableInteractor implements GenerateTimetableInputBoundary {
    private static final int LIMIT = 5000;
    private final GenerateTimetableOutputBoundary presenter;
    private final GenerateTimetableDataAccessInterface dataAccess;
    private final List<Timetable> rawTimetables = new ArrayList<>();
    private final SortTimetableInputBoundary sortTimetableInteractor;

    public GenerateTimetableInteractor(GenerateTimetableDataAccessInterface dataAccess,
                                       GenerateTimetableOutputBoundary presenter,
                                       SortTimetableInputBoundary sortTimetableInteractor) {
        this.presenter = presenter;
        this.dataAccess = dataAccess;
        this.sortTimetableInteractor = sortTimetableInteractor;
    }

    @Override
    public void execute(GenerateTimetableInputData inputData) {
        List<Course> allCourses = dataAccess.getCourses();
        if (allCourses == null || allCourses.isEmpty()) {
            presenter.prepareGenerateTimetableFailureView("No added courses");
            return;
        }

        List<Course> fallCourses = new ArrayList<>();
        List<Course> winterCourses = new ArrayList<>();
        splitCoursesBySession(allCourses, fallCourses, winterCourses);

        ArrayList<TimetableDTO> fallList = (ArrayList<TimetableDTO>) generateTermTimetables(fallCourses);
        ArrayList<TimetableDTO> winterList = (ArrayList<TimetableDTO>) generateTermTimetables(winterCourses);

        // CONDITIONAL SORTING - Only sort if checkbox is enabled
        if (inputData.isSortEnabled()) {
            SortTimetableInputData sortInput =
                    new SortTimetableInputData(fallList, winterList);
            SortTimetableOutputData sorted =
                    sortTimetableInteractor.sort(sortInput);

            fallList = new ArrayList<>(sorted.getFallTimetables());
            winterList = new ArrayList<>(sorted.getWinterTimetables());
        }

        GenerateTimetableOutputData outputData =
                new GenerateTimetableOutputData(fallList, winterList);
        presenter.prepareGenerateTimetableSuccessView(outputData);
    }

    private void splitCoursesBySession(List<Course> courses, List<Course> fallCourses, List<Course> winterCourses) {
        for (Course course : courses) {
            String code = course.getCourseCode();
            if (code.charAt(6) == 'Y') {
                if (code.equals("CSC110Y1")){
                    fallCourses.add(course);
                }else{
                    fallCourses.add(course);
                    winterCourses.add(course);
                }
            } else if (course.getSessionCode() == 20259) {
                fallCourses.add(course);
            } else {
                winterCourses.add(course);
            }
        }
    }

    private List<TimetableDTO> generateTermTimetables(List<Course> termCourses) {
        int n = termCourses.size();
        List<ArrayList<Section>> lectureSections = new ArrayList<>(n);
        List<ArrayList<Section>> secondarySections = new ArrayList<>(n);

        for (Course course : termCourses) {
            lectureSections.add(course.getLectureSections());
            if (!course.getTutorialSections().isEmpty()) {
                secondarySections.add(course.getTutorialSections());
            } else if (!course.getPracticalSections().isEmpty()) {
                secondarySections.add(course.getPracticalSections());
            } else {
                secondarySections.add(new ArrayList<>());
            }
        }

        List<ArrayList<Section>> allSections = new ArrayList<>(2 * n);
        List<String> courseCodes = new ArrayList<>(2 * n);

        for (int i = 0; i < n; i++) {
            allSections.add(lectureSections.get(i));
            allSections.add(secondarySections.get(i));
            String code = termCourses.get(i).getCourseCode();
            courseCodes.add(code);
            courseCodes.add(code);
        }

        rawTimetables.clear();
        addAllCombination(allSections, new Timetable(), 0, courseCodes);

        List<TimetableDTO> result = new ArrayList<>(rawTimetables.size());
        for (Timetable timetable : rawTimetables) {
            result.add(TimetableDTO.fromEntity(timetable));
        }
        return result;
    }

    private void addAllCombination(List<ArrayList<Section>> allSections, Timetable curTimetable, int index, List<String> courseCodes) {
        if (rawTimetables.size() >= LIMIT) {
            return;
        }
        if (index == allSections.size()) {
            rawTimetables.add(new Timetable(curTimetable));
            return;
        }
        ArrayList<Section> sections = allSections.get(index);
        if (sections.isEmpty()) {
            addAllCombination(allSections, curTimetable, index + 1, courseCodes);
            return;
        }
        String courseCode = courseCodes.get(index);
        for (Section section : sections) {
            if (curTimetable.canAddBlock(section, courseCode)) {
                curTimetable.setBlocks(section, courseCode);
                addAllCombination(allSections, curTimetable, index + 1, courseCodes);
                curTimetable.removeBlocks(section, courseCode);
            }
        }
    }
}
