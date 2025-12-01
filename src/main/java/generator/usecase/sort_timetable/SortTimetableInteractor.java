package generator.usecase.sort_timetable;

import courseinfo.Course;
import courseinfo.Meeting;
import courseinfo.Section;
import generator.usecase.generate_timetable.GenerateTimetableDataAccessInterface;
import generator.usecase.generate_timetable.TimetableDTO;

import java.util.*;

public class SortTimetableInteractor implements SortTimetableInputBoundary {

    private final GenerateTimetableDataAccessInterface courseDataAccess;
    private final DistanceDataAccessInterface distanceDataAccess;

    public SortTimetableInteractor(GenerateTimetableDataAccessInterface courseDataAccess,
                                   DistanceDataAccessInterface distanceDataAccess) {
        this.courseDataAccess = courseDataAccess;
        this.distanceDataAccess = distanceDataAccess;
    }

    @Override
    public SortTimetableOutputData sort(SortTimetableInputData inputData) {
        List<TimetableDTO> fall = new ArrayList<>(inputData.getFallTimetables());
        List<TimetableDTO> winter = new ArrayList<>(inputData.getWinterTimetables());

        sortByWalkingDistance(fall);
        sortByWalkingDistance(winter);

        return new SortTimetableOutputData(fall, winter);
    }

    private void sortByWalkingDistance(List<TimetableDTO> timetables) {
        Map<TimetableDTO, Double> score = new HashMap<>();

        for (TimetableDTO dto : timetables) {
            try {
                score.put(dto, computeTotalWalkingDistance(dto));
            } catch (DistanceDataAccessInterface.DistanceLookupException e) {
                score.put(dto, Double.POSITIVE_INFINITY);
            }
        }

        timetables.sort(Comparator.comparingDouble(score::get));
    }

    private double computeTotalWalkingDistance(TimetableDTO dto)
            throws DistanceDataAccessInterface.DistanceLookupException {

        double total = 0.0;
        ArrayList<ArrayList<ArrayList<String>>> table = dto.getTable();

        for (int day = 0; day < table.size(); day++) {
            ArrayList<ArrayList<String>> dayRow = table.get(day);

            String prevBuilding = null;
            boolean havePrev = false;

            for (int slot = 0; slot < dayRow.size(); slot++) {
                ArrayList<String> cell = dayRow.get(slot);
                if (cell.isEmpty()) {
                    continue;
                }

                String block = cell.get(0);              // e.g. "CSC207H1FLEC0101"

                // Safely parse courseCode + sectionCode from block
                String courseCode;
                String sectionCode;

                // base code is always 8 chars like "CSC207H1" or "CSC110Y1"
                if (block.length() >= 8 && block.charAt(6) == 'H'
                        && block.length() >= 9
                        && (block.charAt(8) == 'F' || block.charAt(8) == 'S')) {
                    // half course with F/S: "CSC207H1F"
                    courseCode = block.substring(0, 9);
                    sectionCode = block.substring(9);
                } else {
                    // full-year or anything else: "CSC110Y1"
                    courseCode = block.substring(0, 8);
                    sectionCode = block.substring(8);
                }

                String building = findBuilding(courseCode, sectionCode);

                if (havePrev && prevBuilding != null && building != null
                        && !prevBuilding.equals(building)) {
                    total += distanceDataAccess.getWalkingDistance(prevBuilding, building);
                }
                prevBuilding = building;
                havePrev = true;
            }
        }

        return total;
    }

    private String findBuilding(String courseCode, String sectionCode) {
        Course course = courseDataAccess.getCoursebyCode(courseCode);
        if (course == null) {
            // Unknown course code in table – just skip distance for this block
            return null;
        }

        Section section = findSectionByCode(course.getLectureSections(), sectionCode);
        if (section == null) {
            section = findSectionByCode(course.getTutorialSections(), sectionCode);
        }
        if (section == null) {
            section = findSectionByCode(course.getPracticalSections(), sectionCode);
        }
        if (section == null || section.getMeetings().isEmpty()) {
            // Same idea: nothing to compute distance from
            return null;
        }

        Meeting firstMeeting = section.getMeetings().get(0);
        return firstMeeting.getBuildingCode();
    }

    private Section findSectionByCode(List<Section> sections, String code) {
        if (sections == null) return null;
        for (Section s : sections) {
            if (s.getSectionCode().equals(code)) {
                return s;
            }
        }
        return null;
    }
}
