

import Generator.DataAccess.JsonCourseDataAccess;
import Generator.InterfaceAdapter.DisplayTimeTable.DisplayTimeTableController;
import Generator.InterfaceAdapter.DisplayTimeTable.DisplayTimeTablePresenter;
import Generator.InterfaceAdapter.DisplayTimeTable.DisplayTimeTableState;
import Generator.InterfaceAdapter.DisplayTimeTable.DisplayTimeTableViewModel;
import CourseInfo.TimeTable;

import Generator.UseCases.DisplayTimeTable.DisplayTimeTableInteractor;
import Generator.UseCases.DisplayTimeTable.TimeTableDTO;
import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class DisplayTimeTableIntegrationTest {

    /**
     * End-to-end test:
     * JsonCourseDataAccess (reads JSON) ->
     * DisplayTimeTableInteractor ->
     * DisplayTimeTablePresenter ->
     * DisplayTimeTableViewModel / State.
     */
    @Test
    public void testFullDisplayTimeTableFlow() throws IOException {
        String jsonContent = "[\n" +
                "  {\n" +
                "    \"course_code\": \"ABP100Y1\",\n" +
                "    \"course_title\": \"Introduction to Academic Studies\",\n" +
                "    \"campus\": \"St. George\",\n" +
                "    \"session\": \"20259\",\n" +
                "    \"department_code\": \"WDW\",\n" +
                "    \"faculty_code\": \"ARTSC\",\n" +
                "    \"credit\": 0.0,\n" +
                "    \"section_code\": \"LEC0101\",\n" +
                "    \"component\": \"LEC\",\n" +
                "    \"instructors\": [\"Joseph Sproule\"],\n" +
                "    \"meetings\": [\n" +
                "      {\n" +
                "        \"day_of_week\": 2,\n" +
                "        \"day_abbr\": \"TUE\",\n" +
                "        \"start\": \"10:00\",\n" +
                "        \"end\": \"13:00\",\n" +
                "        \"start_min\": 600,\n" +
                "        \"end_min\": 780,\n" +
                "        \"building_code\": \"WW\",\n" +
                "        \"room\": \"\",\n" +
                "        \"session_code\": \"20259\",\n" +
                "        \"repetition\": \"ONCE_A_WEEK\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "]";

        java.nio.file.Path tempFile = Files.createTempFile("courses", ".json");
        try (FileWriter fw = new FileWriter(tempFile.toFile())) {
            fw.write(jsonContent);
        }

        JsonCourseDataAccess dao = new JsonCourseDataAccess(tempFile.toString());

        dao.saveCourses("ABP100Y1");

        DisplayTimeTableViewModel viewModel = new DisplayTimeTableViewModel();
        DisplayTimeTablePresenter presenter = new DisplayTimeTablePresenter(viewModel);

        DisplayTimeTableInteractor interactor =
                new DisplayTimeTableInteractor(presenter, dao);
        DisplayTimeTableController controller =
                new DisplayTimeTableController(interactor);

        controller.execute();

        DisplayTimeTableState state = viewModel.getState();

        assertNull("No error should be set when there is at least one course",
                state.getNoCourseError());

        ArrayList<TimeTableDTO> timetables = state.getAllTimeTables();
        assertNotNull("Timetable list should not be null", timetables);
        assertFalse("There should be at least one generated timetable", timetables.isEmpty());

        TimeTableDTO dto = timetables.get(0);
        int tueIndex = 1;
        int tenToElevenIndex = 1;

        assertTrue(dto.getTable().get(tueIndex).get(1).contains("ABP100Y1LEC0101"));
        assertTrue(dto.getTable().get(tueIndex).get(2).contains("ABP100Y1LEC0101"));
        assertTrue(dto.getTable().get(tueIndex).get(3).contains("ABP100Y1LEC0101"));
    }
}