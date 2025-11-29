package Generator.DataAccess;

import Generator.UseCase.generate_timetable.TimetableDTO;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Writes the current fall and winter timetables to a CSV-like file.
 */
public class FileTimetableDataAccessObject implements Generator.DataAccess.SaveTimetableDataAccessInterface {

    @Override
    public void saveTimetable(
            TimetableDTO fallTimetable,
            TimetableDTO winterTimetable,
            String fileName) throws IOException {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("Fall Timetable\n");
            writeTimetable(writer, fallTimetable);

            writer.write("\nWinter Timetable\n");
            writeTimetable(writer, winterTimetable);
        }
    }

    private void writeTimetable(BufferedWriter writer, TimetableDTO timetable)
            throws IOException {

        ArrayList<ArrayList<ArrayList<String>>> table = timetable.getTable();
        // rows = time slots, columns = time + days
        for (ArrayList<ArrayList<String>> row : table) {
            for (int col = 0; col < row.size(); col++) {
                ArrayList<String> cell = row.get(col);
                String cellText = String.join("/", cell);
                writer.write(cellText);
                if (col < row.size() - 1) {
                    writer.write(",");
                }
            }
            writer.newLine();
        }
    }
}
