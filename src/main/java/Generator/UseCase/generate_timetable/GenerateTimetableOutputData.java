package Generator.UseCase.generate_timetable;


import java.util.ArrayList;

public class GenerateTimetableOutputData {
    private ArrayList<TimetableDTO> allTimeTables  = new ArrayList<>();


    public GenerateTimetableOutputData(ArrayList<TimetableDTO>  timeTables) {
        this.allTimeTables = timeTables;
    }

    public ArrayList<TimetableDTO>  getAllTimetables() {
        return allTimeTables;
    }

    public void setAllTimeTables (ArrayList<TimetableDTO>  timeTables) {
        this.allTimeTables = timeTables;
    }
}
