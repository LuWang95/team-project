package Generator.UseCase.generate_timetable;

import java.util.ArrayList;

public class GenerateTimetableInputData {
    private ArrayList<String> timePreferences;

    public GenerateTimetableInputData(ArrayList<String> timePreferences) {
        this.timePreferences = timePreferences;
    }

    public ArrayList<String> getTimePreferences() {
        return this.timePreferences;
    }
}
