package Generator.InterfaceAdapter.save_timetable;

import Generator.UseCase.save_timetable.SaveTimetableOutputData;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for SaveTimetablePresenter.
 */
public class SaveTimetablePresenterTest {

    /**
     * Test view model that lets us detect whether firePropertyChange()
     * was called by the presenter.
     */
    private static class TestSaveTimetableViewModel extends SaveTimetableViewModel {
        private boolean propertyChangeFired = false;

        @Override
        public void firePropertyChange() {
            propertyChangeFired = true;
            super.firePropertyChange();
        }

        boolean isPropertyChangeFired() {
            return propertyChangeFired;
        }

        void resetFlag() {
            propertyChangeFired = false;
        }
    }

    @Test
    public void prepareSuccessView_updatesStateAndFiresPropertyChange() {
        // Arrange
        TestSaveTimetableViewModel viewModel = new TestSaveTimetableViewModel();
        SaveTimetablePresenter presenter = new SaveTimetablePresenter(viewModel);

        SaveTimetableOutputData outputData =
                new SaveTimetableOutputData("success.csv", true);

        // Act
        presenter.prepareSuccessView(outputData);

        // Assert
        SaveTimetableState state = viewModel.getState();
        assertTrue("State should be marked as success", state.isSuccess());
        assertEquals("success.csv", state.getFileName());
        assertEquals("Timetable saved successfully to success.csv",
                state.getMessage());
        assertTrue("firePropertyChange() should be called",
                viewModel.isPropertyChangeFired());
    }

    @Test
    public void prepareFailView_updatesStateAndFiresPropertyChange() {
        // Arrange
        TestSaveTimetableViewModel viewModel = new TestSaveTimetableViewModel();
        SaveTimetablePresenter presenter = new SaveTimetablePresenter(viewModel);

        viewModel.resetFlag();

        String error = "Failed to save timetable: disk full";

        // Act
        presenter.prepareFailView(error);

        // Assert
        SaveTimetableState state = viewModel.getState();
        assertFalse("State should not be marked as success", state.isSuccess());
        assertEquals("Error message should be stored in state",
                error, state.getMessage());
        assertTrue("firePropertyChange() should be called on failure as well",
                viewModel.isPropertyChangeFired());
    }
}
