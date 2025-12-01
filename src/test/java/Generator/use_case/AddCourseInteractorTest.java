/**
 * Tests the add course interactor.
 */

package Generator.UseCase;

import Generator.UseCase.add_course.*;
import Generator.DataAccess.FileUserDataAccessObject;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

public class AddCourseInteractorTest {

    /**
     * Tests if adding three distinct courses successfully outputs to the presenter.
     */
    @Test
    public void testAddCourseSuccess() {
        ArrayList<String> inputData = new ArrayList<>(Arrays.asList("CSC111H1S", "csc207h1f", "MAT137Y1"));

        AddCourseDataAccessInterface dataAccessInterface = new FileUserDataAccessObject("selectedPreferences.csv",
                "artsci_timetable.json",
                "Programs.json"
        );

        AddCourseOutputBoundary addCoursePresenter = new AddCourseOutputBoundary() {

            private int coursesAdded = 0;

            @Override
            public void prepareAddCourseSuccessView(AddCourseOutputData addCourseOutputData) {
                coursesAdded++;

                switch(coursesAdded) {
                    case 1:
                        assertEquals("CSC111H1S", addCourseOutputData.getCourseCode());
                        assertTrue(dataAccessInterface.courseAlreadyAdded("CSC111H1S"));
                        break;
                    case 2:
                        assertEquals("CSC207H1F", addCourseOutputData.getCourseCode());
                        assertTrue(dataAccessInterface.courseAlreadyAdded("CSC207H1F"));
                        assertTrue(dataAccessInterface.courseAlreadyAdded("CSC111H1S"));
                        break;
                    case 3:
                        assertEquals("MAT137Y1", addCourseOutputData.getCourseCode());
                        assertTrue(dataAccessInterface.courseAlreadyAdded("CSC111H1S"));
                        assertTrue(dataAccessInterface.courseAlreadyAdded("CSC207H1F"));
                        assertTrue(dataAccessInterface.courseAlreadyAdded("MAT137Y1"));
                }

            }

            @Override
            public void prepareAddCourseFailureView(String errorMessage) {
                fail("Adding CSC111H1S to a brand new list should not fail.");
            }
        };

        AddCourseInputBoundary interactor = new AddCourseInteractor(dataAccessInterface, addCoursePresenter);
        for (String courseName: inputData) {
            AddCourseInputData addCourseInputData = new AddCourseInputData(courseName);
            interactor.execute(addCourseInputData);
        }
    }

    /**
     * Tests if adding a course already added results in an error.
     */
    @Test
    public void testAddCourseFailureDuplicate() {
        ArrayList<String> inputData = new ArrayList<>(Arrays.asList("CSC111H1S", "CSC111H1S"));

        AddCourseDataAccessInterface dataAccessInterface = new FileUserDataAccessObject("selectedPreferences.csv",
                "artsci_timetable.json",
                "Programs.json"
        );

        AddCourseOutputBoundary addCoursePresenter = new AddCourseOutputBoundary() {

            private int coursesAdded = 0;

            @Override
            public void prepareAddCourseSuccessView(AddCourseOutputData addCourseOutputData) {
                coursesAdded++;

                switch(coursesAdded) {
                    case 1:
                        assertEquals("CSC111H1S", addCourseOutputData.getCourseCode());
                        assertTrue(dataAccessInterface.courseAlreadyAdded("CSC111H1S"));
                        break;
                    case 2:
                        fail("There should not be 2 courses added since a duplicate is attempted to be added.");
                }

            }

            @Override
            public void prepareAddCourseFailureView(String errorMessage) {
                switch (coursesAdded) {
                    case 0:
                        fail("The first time CSC111H1S is added should not fail.");
                        break;
                    case 1:
                        assertTrue(dataAccessInterface.courseAlreadyAdded("CSC111H1S"));
                        assertEquals("Course already selected", errorMessage);
                }
            }
        };

        AddCourseInputBoundary interactor = new AddCourseInteractor(dataAccessInterface, addCoursePresenter);
        for (String courseName: inputData) {
            AddCourseInputData addCourseInputData = new AddCourseInputData(courseName);
            interactor.execute(addCourseInputData);
        }
    }

    /**
     * Tests if putting nothing in the add course field and attempting to add it results in an error.
     */
    @Test
    public void testAddCourseFailureNoInput() {
        AddCourseInputData addCourseInputData = new AddCourseInputData("");

        AddCourseDataAccessInterface dataAccessInterface = new FileUserDataAccessObject("selectedPreferences.csv",
                "artsci_timetable.json",
                "Programs.json"
        );

        AddCourseOutputBoundary addCoursePresenter = new AddCourseOutputBoundary() {

            @Override
            public void prepareAddCourseSuccessView(AddCourseOutputData addCourseOutputData) {
               fail("Attempting to add an empty string should not result in adding a course.");

            }

            @Override
            public void prepareAddCourseFailureView(String errorMessage) {
                assertEquals("Enter a course", errorMessage);
            }
        };

        AddCourseInputBoundary interactor = new AddCourseInteractor(dataAccessInterface, addCoursePresenter);
        interactor.execute(addCourseInputData);
    }

    /**
     * Tests if putting nonsense in the add course field and attempting to add it results in an error.
     */
    @Test
    public void testAddCourseFailureCourseNotFound() {
        AddCourseInputData addCourseInputData = new AddCourseInputData("xcvnsdfgjjgkjk");

        AddCourseDataAccessInterface dataAccessInterface = new FileUserDataAccessObject("selectedPreferences.csv",
                "artsci_timetable.json",
                "Programs.json"
        );

        AddCourseOutputBoundary addCoursePresenter = new AddCourseOutputBoundary() {

            @Override
            public void prepareAddCourseSuccessView(AddCourseOutputData addCourseOutputData) {
                fail("Attempting to add xcvnsdfgjjgkjk should not result in adding a course.");

            }

            @Override
            public void prepareAddCourseFailureView(String errorMessage) {
                assertEquals("Course not found", errorMessage);
            }
        };

        AddCourseInputBoundary interactor = new AddCourseInteractor(dataAccessInterface, addCoursePresenter);
        interactor.execute(addCourseInputData);
    }
}
