package generator.use_case;

import generator.data_access.FileUserDataAccessObject;
import generator.use_case.add_course.*;
import generator.use_case.add_degree.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class AddDegreeInteractorTest {


    /**
     * Tests if adding three distinct degrees successfully outputs to the presenter.
     */
    @Test
    public void testAddDegreeSuccess() {
        ArrayList<String> inputData = new ArrayList<>(Arrays.asList("ASSPE2676", "asspe1689", "asmin1689"));

        AddDegreeDataAccessInterface dataAccessInterface = new FileUserDataAccessObject("selectedPreferences.csv",
                "artsci_timetable.json",
                "Programs.json"
        );

        AddDegreeOutputBoundary addDegreePresenter = new AddDegreeOutputBoundary() {

            private int degreesAdded = 0;

            @Override
            public void prepareAddDegreeSuccessView(AddDegreeOutputData addDegreeOutputData) {
                degreesAdded++;

                switch(degreesAdded) {
                    case 1:
                        assertEquals("ASSPE2676", addDegreeOutputData.getDegreeCode());
                        assertTrue(dataAccessInterface.degreeAlreadyAdded("ASSPE2676"));
                        break;
                    case 2:
                        assertEquals("ASSPE1689", addDegreeOutputData.getDegreeCode());
                        assertTrue(dataAccessInterface.degreeAlreadyAdded("ASSPE1689"));
                        assertTrue(dataAccessInterface.degreeAlreadyAdded("ASSPE2676"));
                        break;
                    case 3:
                        assertEquals("ASMIN1689", addDegreeOutputData.getDegreeCode());
                        assertTrue(dataAccessInterface.degreeAlreadyAdded("ASSPE2676"));
                        assertTrue(dataAccessInterface.degreeAlreadyAdded("ASSPE1689"));
                        assertTrue(dataAccessInterface.degreeAlreadyAdded("ASMIN1689"));
                }

            }

            @Override
            public void prepareAddDegreeFailureView(String errorMessage) {
                fail("Should be able to add ASSPE2676");
            }
        };

        AddDegreeInputBoundary interactor = new AddDegreeInteractor(dataAccessInterface, addDegreePresenter,null,null);
        for (String degreeName: inputData) {
            AddDegreeInputData addDegreeInputData = new AddDegreeInputData(degreeName);
            interactor.execute(addDegreeInputData);
        }
    }

    /**
     * Tests if adding a degree already added results in an error.
     */
    @Test
    public void testAddDegreeFailureDuplicate() {
        ArrayList<String> inputData = new ArrayList<>(Arrays.asList("ASSPE2676", "ASSPE2676"));

        AddDegreeDataAccessInterface dataAccessInterface = new FileUserDataAccessObject("selectedPreferences.csv",
                "artsci_timetable.json",
                "Programs.json"
        );

        AddDegreeOutputBoundary addDegreePresenter = new AddDegreeOutputBoundary() {

            private int degreesAdded = 0;

            @Override
            public void prepareAddDegreeSuccessView(AddDegreeOutputData addDegreeOutputData) {
                degreesAdded++;

                switch(degreesAdded) {
                    case 1:
                        assertEquals("ASSPE2676", addDegreeOutputData.getDegreeCode());
                        assertTrue(dataAccessInterface.degreeAlreadyAdded("ASSPE2676"));
                        break;
                    case 2:
                        fail("There should not be any duplicate degrees.");
                }

            }

            @Override
            public void prepareAddDegreeFailureView(String errorMessage) {
                switch (degreesAdded) {
                    case 0:
                        fail("The first time ASSPE2676 is added should not fail.");
                        break;
                    case 1:
                        assertTrue(dataAccessInterface.degreeAlreadyAdded("ASSPE2676"));
                        assertEquals("Degree already selected", errorMessage);
                }
            }
        };

        AddDegreeInputBoundary interactor = new AddDegreeInteractor(dataAccessInterface, addDegreePresenter,null,null);
        for (String degreeName: inputData) {
            AddDegreeInputData addDegreeInputData = new AddDegreeInputData(degreeName);
            interactor.execute(addDegreeInputData);
        }
    }

    /**
     * Tests if putting nothing in the add degree field and attempting to add it results in an error.
     */
    @Test
    public void testAddDegreeFailureNoInput() {
        AddDegreeInputData addDegreeInputData = new AddDegreeInputData("");

        AddDegreeDataAccessInterface dataAccessInterface = new FileUserDataAccessObject("selectedPreferences.csv",
                "artsci_timetable.json",
                "Programs.json"
        );

        AddDegreeOutputBoundary addDegreePresenter = new AddDegreeOutputBoundary() {

            @Override
            public void prepareAddDegreeSuccessView(AddDegreeOutputData addDegreeOutputData) {
                fail("Attempting to add an empty string should not work.");
            }

            @Override
            public void prepareAddDegreeFailureView(String errorMessage) {
                assertEquals("Enter a degree code", errorMessage);
            }
        };

        AddDegreeInputBoundary interactor = new AddDegreeInteractor(dataAccessInterface, addDegreePresenter,null,null);
        interactor.execute(addDegreeInputData);
    }

    /**
     * Tests if inputting non degrees results in error.
     */
    @Test
    public void testAddDegreeFailureDegreeNotFound() {
        AddDegreeInputData addDegreeInputData = new AddDegreeInputData("lin228h1");

        AddDegreeDataAccessInterface dataAccessInterface = new FileUserDataAccessObject("selectedPreferences.csv",
                "artsci_timetable.json",
                "Programs.json"
        );

        AddDegreeOutputBoundary addDegreePresenter = new AddDegreeOutputBoundary() {

            @Override
            public void prepareAddDegreeSuccessView(AddDegreeOutputData addDegreeOutputData) {
                fail("lin228h1 is not a degree");

            }

            @Override
            public void prepareAddDegreeFailureView(String errorMessage) {
                assertEquals("Degree does not exist", errorMessage);
            }
        };

        AddDegreeInputBoundary interactor = new AddDegreeInteractor(dataAccessInterface, addDegreePresenter,null,null);
        interactor.execute(addDegreeInputData);
    }
}
