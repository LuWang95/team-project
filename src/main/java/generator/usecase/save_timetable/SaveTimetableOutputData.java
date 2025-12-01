package generator.usecase.save_timetable;

/**
 * Output data for saving a timetable.  Contains the path used
 * and a boolean indicating success.
 */
public class SaveTimetableOutputData {
    private final String fileName;
    private final boolean success;

    public SaveTimetableOutputData(String fileName, boolean success) {
        this.fileName = fileName;
        this.success = success;
    }

    public String getFileName() {
        return fileName;
    }

    public boolean isSuccess() {
        return success;
    }
}
