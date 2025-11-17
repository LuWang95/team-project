package Generator.UseCase.sort_timetable;

import CourseInfo.Timetable;

import java.util.List;

/**
 * Strategy interface for sorting timetables.
 */
public interface TimetableSortStrategy {

    /**
     * Return a NEW list of timetables sorted according to this strategy.
     */
    List<Timetable> sort(List<Timetable> timetables);
}
