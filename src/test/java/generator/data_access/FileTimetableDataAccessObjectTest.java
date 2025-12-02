package generator.data_access;

import generator.use_case.generate_timetable.TimetableDTO;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for FileTimetableDataAccessObject.
 */
class FileTimetableDataAccessObjectTest {

    /**
     * Creates a tiny 2x2 timetable with one course block in [0][1].
     */
    private TimetableDTO createSmallTimetable() {
        ArrayList<ArrayList<ArrayList<String>>> table = new ArrayList<>();

        for (int row = 0; row < 2; row++) {
            ArrayList<ArrayList<String>> rowList = new ArrayList<>();
            for (int col = 0; col < 2; col++) {
                ArrayList<String> cell = new ArrayList<>();
                if (row == 0 && col == 1) {
                    cell.add("CSC207H1F LEC0101");
                }
                rowList.add(cell);
            }
            table.add(rowList);
        }

        return new TimetableDTO(table);
    }

    @Test
    void saveTimetable_writesExpectedContentToFile() throws Exception {

        // Arrange
        FileTimetableDataAccessObject dao = new FileTimetableDataAccessObject();
        TimetableDTO fall = createSmallTimetable();
        TimetableDTO winter = createSmallTimetable();

        File tempFile = Files.createTempFile("timetable-", ".csv").toFile();
        tempFile.deleteOnExit();

        // Act
        dao.saveTimetable(fall, winter, tempFile.getAbsolutePath());

        // Assert that the file exists
        assertTrue(tempFile.exists(), "File should exist after saving");

        boolean sawFallHeader = false;
        boolean sawWinterHeader = false;
        boolean sawCourse = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(tempFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Fall Timetable")) {
                    sawFallHeader = true;
                }
                if (line.contains("Winter Timetable")) {
                    sawWinterHeader = true;
                }
                if (line.contains("CSC207H1F")) {
                    sawCourse = true;
                }
            }
        }

        // Assert contents
        assertTrue(sawFallHeader, "Output should contain 'Fall Timetable' header");
        assertTrue(sawWinterHeader, "Output should contain 'Winter Timetable' header");
        assertTrue(sawCourse, "Output should contain the course code");
    }
}
