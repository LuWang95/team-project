package generator.usecase;

import courseinfo.Degree;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class AddDegreeInteractorTest {
    @Test


    public void testAddDegree() {
        ArrayList<String> TestCourseCodes = new ArrayList<String>();
        TestCourseCodes.add("CSC108H1");
        TestCourseCodes.add("CSC148H1");
        TestCourseCodes.add("CSC165H1");
        Degree TestMaj = new Degree("ASMAJ1689"
                , "Computer Science Major (Science Program"
                , TestCourseCodes);


    }
}
