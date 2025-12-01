package generator.use_case.load_timetable;

public class LoadTimetableInputData {
    private final String filePath;

    public LoadTimetableInputData(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }
}
