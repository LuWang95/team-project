
import Generator.DataAccess.JsonCourseDataAccess;
import Generator.InterfaceAdapter.DisplayTimeTable.DisplayTimeTableController;
import Generator.InterfaceAdapter.DisplayTimeTable.DisplayTimeTablePresenter;
import Generator.InterfaceAdapter.DisplayTimeTable.DisplayTimeTableState;
import Generator.InterfaceAdapter.DisplayTimeTable.DisplayTimeTableViewModel;
import Generator.UseCases.DisplayTimeTable.DisplayTimeTableInteractor;
import org.junit.Test;

import static org.junit.Assert.*;

public class DisplayTimeTableInteractorSecondTest {

    @Test
    public void testDisplayTimeTable_withRealJson() {
        JsonCourseDataAccess dao = new JsonCourseDataAccess("artsci_timetable.json");

        dao.saveCourses("CSC207H1"); // test course

        DisplayTimeTableViewModel vm = new DisplayTimeTableViewModel();
        DisplayTimeTablePresenter presenter = new DisplayTimeTablePresenter(vm);
        DisplayTimeTableInteractor interactor = new DisplayTimeTableInteractor(presenter, dao);
        DisplayTimeTableController controller = new DisplayTimeTableController(interactor);

        controller.execute();

        DisplayTimeTableState state = vm.getState();

        assertNotNull(state.getAllTimeTables());
        assertTrue(state.getAllTimeTables().size() > 0);
    }
}