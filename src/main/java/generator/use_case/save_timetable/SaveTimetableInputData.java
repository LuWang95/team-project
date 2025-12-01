package generator.use_case.save_timetable;

import generator.use_case.generate_timetable.TimetableDTO;

/**
 * Input data for saving a timetable.  Contains the fall and winter timetable
 * DTOs and the filename to which they should be written.
 */
public class SaveTimetableInputData {
    private final TimetableDTO fallTimetable;
    private final TimetableDTO winterTimetable;
    private final String fileName;

    public SaveTimetableInputData(
            TimetableDTO fallTimetable,
            TimetableDTO winterTimetable,
            String fileName) {
        this.fallTimetable = fallTimetable;
        this.winterTimetable = winterTimetable;
        this.fileName = fileName;
    }

    public TimetableDTO getFallTimetable() {
        return fallTimetable;
    }

    public TimetableDTO getWinterTimetable() {
        return winterTimetable;
    }

    public String getFileName() {
        return fileName;
    }
}
