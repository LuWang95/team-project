/**
 * Tests the remove course interactor.
 */

package generator.UseCase;

import generator.DataAccess.FileUserDataAccessObject;
import generator.UseCase.add_course.AddCourseDataAccessInterface;
import generator.UseCase.remove_course.*;
import org.junit.jupiter.api.Test;

public class RemoveCourseInteractorTest {

    /**
     * Tests if removing two courses in succession from an already filled list successfully removes them.
     */
    @Test
    public void testAddCourseSuccess() {
        RemoveCourseDataAccessInterface dataAccessInterface = new FileUserDataAccessObject("selectedPreferences.csv",
                "artsci_timetable.json",
                "Programs.json"
        );
        AddCourseDataAccessInterface addCourseDataAccessInterface = (AddCourseDataAccessInterface) dataAccessInterface;
        addCourseDataAccessInterface.add(dataAccessInterface.getCoursebyCode("CSC111H1S"));
        addCourseDataAccessInterface.add(dataAccessInterface.getCoursebyCode("MAT137Y1"));
        addCourseDataAccessInterface.add(dataAccessInterface.getCoursebyCode("CSC207H1F"));

        RemoveCourseOutputBoundary removeCoursePresenter = new RemoveCourseOutputBoundary() {

            private int coursesAdded = 2;

            @Override
            public void prepareRemoveCourseSuccessView(RemoveCourseOutputData removeCourseOutputData) {
                coursesAdded--;

                switch(coursesAdded) {
                    case 0:
                        assertFalse(addCourseDataAccessInterface.courseAlreadyAdded("CSC111H1S"));
                        assertFalse(addCourseDataAccessInterface.courseAlreadyAdded("MAT137Y1"));
                        assertTrue(addCourseDataAccessInterface.courseAlreadyAdded("CSC207H1F"));
                        break;
                    case 1:
                        assertFalse(addCourseDataAccessInterface.courseAlreadyAdded("CSC111H1S"));
                        assertTrue(addCourseDataAccessInterface.courseAlreadyAdded("MAT137Y1"));
                        assertTrue(addCourseDataAccessInterface.courseAlreadyAdded("CSC207H1F"));
                        break;
                }
            }
        };

        RemoveCourseInteractor removeCourseInteractor = new RemoveCourseInteractor(dataAccessInterface,
                removeCoursePresenter);

        RemoveCourseInputData removeCourseInputData = new RemoveCourseInputData("CSC111H1S");
        removeCourseInteractor.execute(removeCourseInputData);

        removeCourseInputData = new RemoveCourseInputData("MAT137Y1");
        removeCourseInteractor.execute(removeCourseInputData);
    }
}
