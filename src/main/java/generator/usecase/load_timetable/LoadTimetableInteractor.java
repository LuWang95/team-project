package generator.usecase.load_timetable;

import generator.usecase.generate_timetable.TimetableDTO;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class LoadTimetableInteractor implements LoadTimetableInputBoundary {

    private final LoadTimetableOutputBoundary presenter;

    public LoadTimetableInteractor(LoadTimetableOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void loadTimetable(LoadTimetableInputData inputData) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputData.getFilePath()));

            ArrayList<TimetableDTO> fallTimetables = new ArrayList<>();
            ArrayList<TimetableDTO> winterTimetables = new ArrayList<>();
            ArrayList<String> courseCodes = new ArrayList<>();
            ArrayList<String> courseNames = new ArrayList<>();
            ArrayList<Double> credits = new ArrayList<>();

            // Read all lines first
            ArrayList<String> allLines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                allLines.add(line);
            }
            reader.close();

            // Find section indices
            int fallIndex = -1;
            int winterIndex = -1;

            for (int i = 0; i < allLines.size(); i++) {
                String currentLine = allLines.get(i).trim();
                if (currentLine.contains("Fall") && currentLine.contains("Timetable")) {
                    fallIndex = i;
                } else if (currentLine.contains("Winter") && currentLine.contains("Timetable")) {
                    winterIndex = i;
                }
            }

            if (fallIndex == -1) {
                presenter.prepareFailView("Invalid CSV format: Missing 'Fall Timetable' header");
                return;
            }

            if (winterIndex == -1) {
                presenter.prepareFailView("Invalid CSV format: Missing 'Winter Timetable' header");
                return;
            }

            // Parse Fall Timetable (5 days, each with 12 hours)
            ArrayList<ArrayList<ArrayList<String>>> fallTable = initializeTable();
            parseTimetableSection(allLines, fallIndex + 1, winterIndex, fallTable);

            // Parse Winter Timetable (5 days, each with 12 hours)
            ArrayList<ArrayList<ArrayList<String>>> winterTable = initializeTable();
            parseTimetableSection(allLines, winterIndex + 1, allLines.size(), winterTable);

            // Extract unique courses from both timetables
            HashSet<String> uniqueCodes = new HashSet<>();
            extractCoursesFromTable(fallTable, uniqueCodes);
            extractCoursesFromTable(winterTable, uniqueCodes);

            // Add to lists
            for (String code : uniqueCodes) {
                if (code.length() >= 8) {
                    String courseCode = code.substring(0, 8);
                    if (!courseCodes.contains(courseCode)) {
                        courseCodes.add(courseCode);
                        courseNames.add(extractCourseName(courseCode));
                        credits.add(0.5);
                    }
                }
            }

            // Create DTOs
            fallTimetables.add(new TimetableDTO(fallTable));
            winterTimetables.add(new TimetableDTO(winterTable));

            LoadTimetableOutputData outputData = new LoadTimetableOutputData(
                    fallTimetables, winterTimetables, courseCodes, courseNames, credits
            );
            presenter.prepareSuccessView(outputData);

        } catch (IOException e) {
            presenter.prepareFailView("Error reading file: " + e.getMessage());
        } catch (Exception e) {
            presenter.prepareFailView("Error parsing CSV: " + e.getMessage());
        }
    }

    private ArrayList<ArrayList<ArrayList<String>>> initializeTable() {
        ArrayList<ArrayList<ArrayList<String>>> table = new ArrayList<>();
        for (int day = 0; day < 5; day++) {
            table.add(new ArrayList<>());
            for (int hour = 0; hour < 12; hour++) {
                table.get(day).add(new ArrayList<>());
            }
        }
        return table;
    }

    private void parseTimetableSection(ArrayList<String> allLines, int startIndex,
                                       int endIndex, ArrayList<ArrayList<ArrayList<String>>> table) {
        int dayIndex = 0;

        for (int i = startIndex; i < endIndex && dayIndex < 5; i++) {
            String line = allLines.get(i).trim();

            // Skip empty lines
            if (line.isEmpty()) {
                continue;
            }

            // This line represents one day's schedule (12 hours)
            String[] hourCells = line.split(",", -1);

            for (int hour = 0; hour < Math.min(12, hourCells.length); hour++) {
                String cell = hourCells[hour].trim();
                if (!cell.isEmpty()) {
                    // Cell might contain multiple courses separated by "/"
                    String[] courses = cell.split("/");
                    for (String course : courses) {
                        course = course.trim();
                        if (!course.isEmpty()) {
                            table.get(dayIndex).get(hour).add(course);
                        }
                    }
                }
            }

            dayIndex++;
        }
    }

    private void extractCoursesFromTable(ArrayList<ArrayList<ArrayList<String>>> table,
                                         HashSet<String> uniqueCodes) {
        for (ArrayList<ArrayList<String>> day : table) {
            for (ArrayList<String> hour : day) {
                for (String cell : hour) {
                    if (!cell.isEmpty()) {
                        uniqueCodes.add(cell);
                    }
                }
            }
        }
    }

    private String extractCourseName(String courseCode) {
        if (courseCode.startsWith("CSC")) {
            return "Computer Science Course";
        } else if (courseCode.startsWith("MAT")) {
            return "Mathematics Course";
        } else if (courseCode.startsWith("STA")) {
            return "Statistics Course";
        } else if (courseCode.startsWith("ECO")) {
            return "Economics Course";
        } else {
            return "Course";
        }
    }
}
