/**
 * Tests the remove course interactor.
 */

package generator.use_case;

import generator.data_access.FileUserDataAccessObject;
import generator.use_case.add_degree.AddDegreeDataAccessInterface;
import generator.use_case.remove_degree.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RemoveDegreeInteractorTest {

    /**
     * Tests if removing two degrees in succession from an already filled list successfully removes them.
     */
    @Test
    public void testRemoveDegreeSuccess() {
        RemoveDegreeDataAccessInterface dataAccessInterface = new FileUserDataAccessObject(
                "selectedPreferences.csv",
                "artsci_timetable.json",
                "Programs.json"
        );
        AddDegreeDataAccessInterface addDegreeDataAccessInterface = (AddDegreeDataAccessInterface) dataAccessInterface;
        addDegreeDataAccessInterface.add(((AddDegreeDataAccessInterface) dataAccessInterface).getDegreeByCode(
                "ASSPE2676"));
        addDegreeDataAccessInterface.add(((AddDegreeDataAccessInterface) dataAccessInterface).getDegreeByCode(
                "ASSPE1689"));
        addDegreeDataAccessInterface.add(((AddDegreeDataAccessInterface) dataAccessInterface).getDegreeByCode(
                "ASMIN1689"));

        RemoveDegreeOutputBoundary removeDegreePresenter = new RemoveDegreeOutputBoundary() {

            private int degreesAdded = 2;

            @Override
            public void prepareRemoveDegreeSuccessView(RemoveDegreeOutputData removeDegreeOutputData) {
                degreesAdded--;

                switch(degreesAdded) {
                    case 0:
                        assertFalse(addDegreeDataAccessInterface.degreeAlreadyAdded("ASSPE2676"));
                        assertFalse(addDegreeDataAccessInterface.degreeAlreadyAdded("ASSPE1689"));
                        assertTrue(addDegreeDataAccessInterface.degreeAlreadyAdded("ASMIN1689"));
                        break;
                    case 1:
                        assertFalse(addDegreeDataAccessInterface.degreeAlreadyAdded("ASSPE2676"));
                        assertTrue(addDegreeDataAccessInterface.degreeAlreadyAdded("ASSPE1689"));
                        assertTrue(addDegreeDataAccessInterface.degreeAlreadyAdded("ASMIN1689"));
                        break;
                }
            }
        };

        RemoveDegreeInteractor removeDegreeInteractor = new RemoveDegreeInteractor(dataAccessInterface,
                removeDegreePresenter);

        RemoveDegreeInputData removeDegreeInputData = new RemoveDegreeInputData("ASSPE2676");
        removeDegreeInteractor.execute(removeDegreeInputData);

        removeDegreeInputData = new RemoveDegreeInputData("ASSPE1689");
        removeDegreeInteractor.execute(removeDegreeInputData);
    }
}
