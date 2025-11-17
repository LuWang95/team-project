package Generator.UseCase.sort_timetable;

import CourseInfo.Timetable;
import CourseInfo.TimetableMetrics;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Sort timetables by the total gap between lectures (in slots), ascending.
 */
public class IntervalBetweenLecturesStrategy implements TimetableSortStrategy {

    @Override
    public List<Timetable> sort(List<Timetable> timetables) {
        List<Timetable> copy = new ArrayList<>(timetables);
        copy.sort(Comparator.comparingInt(TimetableMetrics::totalGapSlots));
        return copy;
    }
}
