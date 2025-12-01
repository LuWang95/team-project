package generator.usecase.load_timetable;

import generator.usecase.generate_timetable.TimetableDTO;
import java.util.ArrayList;

public class LoadTimetableOutputData {
    private final ArrayList<TimetableDTO> fallTimetables;
    private final ArrayList<TimetableDTO> winterTimetables;
    private final ArrayList<String> courseCodes;
    private final ArrayList<String> courseNames;
    private final ArrayList<Double> credits;

    public LoadTimetableOutputData(ArrayList<TimetableDTO> fallTimetables,
                                   ArrayList<TimetableDTO> winterTimetables,
                                   ArrayList<String> courseCodes,
                                   ArrayList<String> courseNames,
                                   ArrayList<Double> credits) {
        this.fallTimetables = fallTimetables;
        this.winterTimetables = winterTimetables;
        this.courseCodes = courseCodes;
        this.courseNames = courseNames;
        this.credits = credits;
    }

    public ArrayList<TimetableDTO> getFallTimetables() {
        return fallTimetables;
    }

    public ArrayList<TimetableDTO> getWinterTimetables() {
        return winterTimetables;
    }

    public ArrayList<String> getCourseCodes() {
        return courseCodes;
    }

    public ArrayList<String> getCourseNames() {
        return courseNames;
    }

    public ArrayList<Double> getCredits() {
        return credits;
    }
}
