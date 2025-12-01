package generator.interfaceadapter.save_timetable;

import generator.interfaceadapter.display_timetable.DisplayTimetableState;
import generator.interfaceadapter.display_timetable.DisplayTimetableViewModel;
import generator.usecase.generate_timetable.TimetableDTO;
import generator.usecase.save_timetable.SaveTimetableInputBoundary;
import generator.usecase.save_timetable.SaveTimetableInputData;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Unit tests for SaveTimetableController.
 */
public class SaveTimetableControllerTest {

    /**
     * Fake interactor that just captures the last input data.
     */
    private static final class CapturingInteractor implements SaveTimetableInputBoundary {
        private SaveTimetableInputData lastInput;

        @Override
        public void saveTimetable(SaveTimetableInputData inputData) {
            lastInput = inputData;
        }
    }

    /**
     * Helper to build a minimal TimetableDTO.
     * The controller never inspects the contents, so we only need
     * a non-null object with a tiny table.
     */
    private TimetableDTO createDummyTimetable(final String marker) {
        final ArrayList<ArrayList<ArrayList<String>>> table = new ArrayList<>();
        final ArrayList<ArrayList<String>> row = new ArrayList<>();
        final ArrayList<String> cell = new ArrayList<>();
        cell.add(marker);
        row.add(cell);
        table.add(row);
        return new TimetableDTO(table);
    }

    @Test
    public void saveTimetable_usesCurrentStateAndCallsInteractor() {
        // Arrange: view model + state
        final DisplayTimetableViewModel viewModel = new DisplayTimetableViewModel();
        final DisplayTimetableState state = new DisplayTimetableState();

        final TimetableDTO fall = createDummyTimetable("FALL");
        final TimetableDTO winter = createDummyTimetable("WINTER");

        final ArrayList<TimetableDTO> fallList = new ArrayList<>();
        final ArrayList<TimetableDTO> winterList = new ArrayList<>();
        fallList.add(fall);
        winterList.add(winter);

        // indices are 0 by default, so we only need to set the lists
        state.setFallTimetables(fallList);
        state.setWinterTimetables(winterList);

        viewModel.setState(state);

        // Fake interactor to capture what the controller passes in
        final CapturingInteractor interactor = new CapturingInteractor();

        final SaveTimetableController controller =
                new SaveTimetableController(interactor, viewModel);

        // Act
        controller.saveTimetable("my-timetable.csv");

        // Assert: interactor was called with correct data
        assertNotNull("Interactor should have been called", interactor.lastInput);
        assertEquals("my-timetable.csv", interactor.lastInput.getFileName());
        assertSame("Fall timetable from state should be passed through",
                fall, interactor.lastInput.getFallTimetable());
        assertSame("Winter timetable from state should be passed through",
                winter, interactor.lastInput.getWinterTimetable());
    }
}
