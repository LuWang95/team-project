package generator.use_case.save_timetable;

import generator.data_access.SaveTimetableDataAccessInterface;
import generator.use_case.generate_timetable.TimetableDTO;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SaveTimetableInteractor.
 */
class SaveTimetableInteractorTest {

    /**
     * Fake DAO that lets us control whether an IOException is thrown.
     */
    private static final class FakeTimetableDAO implements SaveTimetableDataAccessInterface {

        boolean called;
        boolean throwIOException;
        String lastFileName;

        @Override
        public void saveTimetable(
                TimetableDTO fallTimetable,
                TimetableDTO winterTimetable,
                String fileName) throws IOException {

            called = true;
            lastFileName = fileName;

            if (throwIOException) {
                throw new IOException("disk full");
            }
        }
    }

    /**
     * Fake presenter capturing success or error calls.
     */
    private static final class FakePresenter implements SaveTimetableOutputBoundary {

        SaveTimetableOutputData lastSuccess;
        String lastError;

        @Override
        public void prepareSuccessView(SaveTimetableOutputData outputData) {
            lastSuccess = outputData;
        }

        @Override
        public void prepareFailView(String error) {
            lastError = error;
        }
    }

    /**
     * Helper to build a minimal input object.
     * The interactor doesn't inspect the timetables themselves,
     * so we can just pass null for them.
     */
    private SaveTimetableInputData buildInput(String fileName) {
        return new SaveTimetableInputData(
                null,   // fallTimetable
                null,   // winterTimetable
                fileName
        );
    }

    @Test
    void saveTimetable_success_callsDaoAndSuccessView() {
        // Arrange
        FakeTimetableDAO dao = new FakeTimetableDAO();
        FakePresenter presenter = new FakePresenter();
        SaveTimetableInteractor interactor =
                new SaveTimetableInteractor(dao, presenter);

        SaveTimetableInputData input = buildInput("success.csv");

        // Act
        interactor.saveTimetable(input);

        // Assert – DAO was used correctly
        assertTrue(dao.called, "DAO should be called");
        assertEquals("success.csv", dao.lastFileName);

        // Assert – success path taken
        assertNotNull(presenter.lastSuccess, "Presenter should receive success output");
        assertEquals("success.csv", presenter.lastSuccess.getFileName());
        assertTrue(presenter.lastSuccess.isSuccess(), "Output should indicate success");
        assertNull(presenter.lastError, "Error message should not be set on success");
    }

    @Test
    void saveTimetable_whenDaoThrowsIOException_callsFailView() {
        // Arrange
        FakeTimetableDAO dao = new FakeTimetableDAO();
        dao.throwIOException = true;

        FakePresenter presenter = new FakePresenter();
        SaveTimetableInteractor interactor =
                new SaveTimetableInteractor(dao, presenter);

        SaveTimetableInputData input = buildInput("failure.csv");

        // Act
        interactor.saveTimetable(input);

        // Assert – DAO still called
        assertTrue(dao.called, "DAO should still be called even on failure");

        // Assert – failure path taken
        assertNull(presenter.lastSuccess, "Success output should not be set on failure");
        assertNotNull(presenter.lastError, "Error message should be set on failure");
        assertTrue(
                presenter.lastError.startsWith("Failed to save timetable: "),
                "Error message should start with the interactor's prefix"
        );
    }
}
