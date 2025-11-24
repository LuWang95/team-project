package Generator.UseCase.save_timetable;

public class SaveTimetableOutputData {
    private final String filePath;

    public SaveTimetableOutputData(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }
}
