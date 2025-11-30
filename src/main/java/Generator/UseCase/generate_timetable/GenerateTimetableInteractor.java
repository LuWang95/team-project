package Generator.UseCase.generate_timetable;

import java.util.ArrayList;
import java.util.List;

import CourseInfo.*;

public class  GenerateTimetableInteractor implements GenerateTimetableInputBoundary {
    private static final int LIMIT = 5000;
    private final GenerateTimetableOutputBoundary presenter;
    private final GenerateTimetableDataAccessInterface dataAccess;
    private final List<Timetable> rawTimetables = new ArrayList<>();

    public GenerateTimetableInteractor(GenerateTimetableDataAccessInterface dataAccess,
                                       GenerateTimetableOutputBoundary presenter) {
        this.presenter = presenter;
        this.dataAccess = dataAccess;
    }

    @Override
    public void execute(GenerateTimetableInputData generateTimetableInputData) {
        final List<Course> allCourses = dataAccess.getCourses();
        if (allCourses == null || allCourses.isEmpty()) {
            presenter.prepareGenerateTimetableFailureView("No added courses");
            return;
        }

        final List<Course> fallCourses = new ArrayList<>();
        final List<Course> winterCourses = new ArrayList<>();
        splitCoursesBySession(allCourses, fallCourses, winterCourses);

        ArrayList<TimetableDTO> fallList = (ArrayList<TimetableDTO>) generateTermTimetables(fallCourses);
        ArrayList<TimetableDTO> winterList = (ArrayList<TimetableDTO>) generateTermTimetables(winterCourses);

        final ArrayList<String> timePreferences = generateTimetableInputData.getTimePreferences();
        if (timePreferences != null && !timePreferences.isEmpty()) {
            fallList = filterByTimePreference(fallList, timePreferences, fallCourses);
            winterList = filterByTimePreference(winterList, timePreferences, winterCourses);
        }

        final GenerateTimetableOutputData outputData = new GenerateTimetableOutputData(fallList, winterList);
        presenter.prepareGenerateTimetableSuccessView(outputData);
    }

    private void splitCoursesBySession(List<Course> courses, List<Course> fallCourses, List<Course> winterCourses) {
        for (Course course : courses) {
            final String code = course.getCourseCode();
            if (code.charAt(6) == 'Y') {
                if (code.equals("CSC110Y1")) {
                    fallCourses.add(course);
                }
                else {
                    fallCourses.add(course);
                    winterCourses.add(course);
                }
            }
            else if (course.getSessionCode() == 20259) {
                fallCourses.add(course);
            }
            else {
                winterCourses.add(course);
            }
        }
    }

    private List<TimetableDTO> generateTermTimetables(List<Course> termCourses) {
        final int n = termCourses.size();
        final List<ArrayList<Section>> lectureSections = new ArrayList<>(n);
        final List<ArrayList<Section>> secondarySections = new ArrayList<>(n);

        for (Course course : termCourses) {
            lectureSections.add(course.getLectureSections());
            if (!course.getTutorialSections().isEmpty()) {
                secondarySections.add(course.getTutorialSections());
            }
            else if (!course.getPracticalSections().isEmpty()) {
                secondarySections.add(course.getPracticalSections());
            }
            else {
                secondarySections.add(new ArrayList<>());
            }
        }

        final List<ArrayList<Section>> allSections = new ArrayList<>(2 * n);
        final List<String> courseCodes = new ArrayList<>(2 * n);

        for (int i = 0; i < n; i++) {
            allSections.add(lectureSections.get(i));
            allSections.add(secondarySections.get(i));
            final String code = termCourses.get(i).getCourseCode();
            courseCodes.add(code);
            courseCodes.add(code);
        }

        rawTimetables.clear();
        addAllCombination(allSections, new Timetable(), 0, courseCodes);

        final List<TimetableDTO> result = new ArrayList<>(rawTimetables.size());
        for (Timetable timetable : rawTimetables) {
            result.add(TimetableDTO.fromEntity(timetable));
        }
        return result;
    }

    private void addAllCombination(List<ArrayList<Section>> allSections, Timetable curTimetable, int index,
                                   List<String> courseCodes) {
        if (rawTimetables.size() >= LIMIT) {
            return;
        }
        if (index == allSections.size()) {
            rawTimetables.add(new Timetable(curTimetable));
            return;
        }
        final ArrayList<Section> sections = allSections.get(index);
        if (sections.isEmpty()) {
            addAllCombination(allSections, curTimetable, index + 1, courseCodes);
            return;
        }
        final String courseCode = courseCodes.get(index);
        for (Section section : sections) {
            if (curTimetable.canAddBlock(section, courseCode)) {
                curTimetable.setBlocks(section, courseCode);
                addAllCombination(allSections, curTimetable, index + 1, courseCodes);
                curTimetable.removeBlocks(section, courseCode);
            }
        }
    }

    /**
     * Filters timetables based on time preferences with prioritization.
     * Prioritizes timetables where the first (most important) courses are in the preferred time slots.
     * @param timetables the list of timetables to filter
     * @param timePreferences the time preferences selected by the user (e.g., "Morning", "Afternoon", "Evening")
     * @param courses the courses in order of priority (first courses are most important)
     * @return sorted list of timetables with best matches first
     */
    private ArrayList<TimetableDTO> filterByTimePreference(ArrayList<TimetableDTO> timetables,
                                                            ArrayList<String> timePreferences,
                                                            List<Course> courses) {
        final ArrayList<ScoredTimetable> scoredTimetables = new ArrayList<>();

        for (TimetableDTO timetable : timetables) {
            final int score = calculateTimePreferenceScore(timetable, timePreferences, courses);
            scoredTimetables.add(new ScoredTimetable(timetable, score));
        }

        scoredTimetables.sort((first, second) -> Integer.compare(second.score, first.score));

        final ArrayList<TimetableDTO> result = new ArrayList<>();
        for (ScoredTimetable scored : scoredTimetables) {
            result.add(scored.timetable);
        }

        return result;
    }

    /**
     * Calculates a score for how well a timetable matches time preferences.
     * Higher weight is given to earlier courses (more important).
     * @param timetable the timetable to score
     * @param timePreferences the preferred time slots
     * @param courses the courses in priority order
     * @return score where higher is better
     */
    private int calculateTimePreferenceScore(TimetableDTO timetable,
                                              ArrayList<String> timePreferences,
                                              List<Course> courses) {
        int score = 0;
        final ArrayList<ArrayList<ArrayList<String>>> table = timetable.getTable();

        for (int courseIndex = 0; courseIndex < courses.size(); courseIndex++) {
            final String courseCode = courses.get(courseIndex).getCourseCode();
            final int weight = courses.size() - courseIndex;

            if (isCourseInPreferredTime(table, courseCode, timePreferences)) {
                score += weight * 100;
            }
        }

        return score;
    }

    /**
     * Checks if a specific course appears in any of the preferred time slots.
     * A course gets credit if ANY of its sessions (lecture, tutorial, etc.) are in preferred times.
     * @param table the timetable grid
     * @param courseCode the course code to check (e.g., "CSC207H1")
     * @param timePreferences the preferred time slots
     * @return true if at least one session of the course is in a preferred time slot
     */
    private boolean isCourseInPreferredTime(ArrayList<ArrayList<ArrayList<String>>> table,
                                             String courseCode,
                                             ArrayList<String> timePreferences) {
        boolean foundCourse = false;
        boolean foundInPreferredTime = false;

        for (int day = 0; day < 5; day++) {
            final ArrayList<ArrayList<String>> daySchedule = table.get(day);

            for (int hour = 0; hour < 12; hour++) {
                final ArrayList<String> slot = daySchedule.get(hour);

                for (String entry : slot) {
                    if (entry.startsWith(courseCode)) {
                        foundCourse = true;
                        if (isTimeSlotAllowed(hour, timePreferences)) {
                            foundInPreferredTime = true;
                        }
                    }
                }
            }
        }

        return foundCourse && foundInPreferredTime;
    }

    /**
     * Helper class to associate a timetable with its preference score.
     */
    private static class ScoredTimetable {
        private final TimetableDTO timetable;
        private final int score;

        ScoredTimetable(TimetableDTO timetable, int score) {
            this.timetable = timetable;
            this.score = score;
        }
    }

    /**
     * Checks if a specific hour slot is allowed based on time preferences.
     * @param hour the hour index (0 = 9-10am, 11 = 8-9pm) based on the 2d array.
     * @param timePreferences the list of allowed time preferences.
     * @return true if the hour falls within any of the preferred time ranges.
     */
    private boolean isTimeSlotAllowed(int hour, ArrayList<String> timePreferences) {
        for (String preference : timePreferences) {
            if (preference.equals("Morning") && hour >= 0 && hour < 3) {
                return true;
            }
            else if (preference.equals("Afternoon") && hour >= 3 && hour < 8) {
                return true;
            }
            else if (preference.equals("Evening") && hour >= 8 && hour < 12) {
                return true;
            }
        }
        return false;
    }
}
