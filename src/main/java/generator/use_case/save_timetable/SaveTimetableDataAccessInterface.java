package generator.data_access;

import generator.use_case.generate_timetable.TimetableDTO;
import java.io.IOException;

/**
 * Interface for writing timetables to storage.
 */
public interface SaveTimetableDataAccessInterface {
    void saveTimetable(
            TimetableDTO fallTimetable,
            TimetableDTO winterTimetable,
            String fileName) throws IOException;
}
