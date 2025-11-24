package Generator.UseCase.generate_timetable;


import java.util.ArrayList;

public class GenerateTimetableOutputData {
    private ArrayList<TimetableDTO> fallTimeTables = new ArrayList<>();
    private ArrayList<TimetableDTO> winterTimeTables = new ArrayList<>();


    public GenerateTimetableOutputData(ArrayList<TimetableDTO>  fallTimeTables, ArrayList<TimetableDTO> winterTimeTables) {
        this.fallTimeTables = fallTimeTables;
        this.winterTimeTables = winterTimeTables;
    }

    public ArrayList<TimetableDTO>  getWinterTimeTables() {
        return winterTimeTables;
    }
    public ArrayList<TimetableDTO> getFallTimeTables() {
        return fallTimeTables;
    }

    public void setAllTimeTables (ArrayList<TimetableDTO>  timeTables) {
        this.winterTimeTables = timeTables;
        this.fallTimeTables = timeTables;
    }
}
