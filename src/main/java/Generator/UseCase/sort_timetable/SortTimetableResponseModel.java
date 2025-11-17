package Generator.UseCase.sort_timetable;

import CourseInfo.Timetable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Data from use case → presenter.
 */
public class SortTimetableResponseModel {

    private final ArrayList<Timetable> sortedTimetables;

    public SortTimetableResponseModel(List<Timetable> sortedTimetables) {
        this.sortedTimetables = new ArrayList<>(sortedTimetables);
    }

    public List<Timetable> getSortedTimetables() {
        return Collections.unmodifiableList(sortedTimetables);
    }
}
