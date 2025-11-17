package Generator.UseCase.sort_timetable;

import CourseInfo.Timetable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Data from controller → use case.
 */
public class SortTimetableRequestModel {

    private final ArrayList<Timetable> timetables;

    public SortTimetableRequestModel(List<Timetable> timetables) {
        this.timetables = new ArrayList<>(timetables);
    }

    public List<Timetable> getTimetables() {
        return Collections.unmodifiableList(timetables);
    }
}
