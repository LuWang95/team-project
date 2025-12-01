package generator.use_case.generate_timetable;

import java.util.ArrayList;

public class GenerateTimetableInputData {
    private final boolean sortEnabled;
    private ArrayList<String> timePreferences;

    public GenerateTimetableInputData(boolean sortEnabled, ArrayList<String> timePreferences) {
        this.sortEnabled = sortEnabled;
        this.timePreferences = timePreferences;
    }

    public boolean isSortEnabled() {
        return sortEnabled;
    }

    public ArrayList<String> getTimePreferences() {
        return this.timePreferences;
    }
}
