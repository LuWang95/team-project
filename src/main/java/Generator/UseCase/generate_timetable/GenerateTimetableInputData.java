package Generator.UseCase.generate_timetable;

public class GenerateTimetableInputData {
    private final boolean sortEnabled;

    public GenerateTimetableInputData(boolean sortEnabled) {
        this.sortEnabled = sortEnabled;
    }

    public boolean isSortEnabled() {
        return sortEnabled;
    }
}
