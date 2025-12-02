package Generator.DataAccess;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GeoapifyDAOTest {

    @Test
    void testValidBuildingCode() throws GeoapifyDAO.CoordinateNotFoundException {
        GeoapifyDAO geoapifyDAO = new GeoapifyDAO();
        List<Double> coordinates = geoapifyDAO.getCoordinates("MY");
        ArrayList<Double> expected =  new ArrayList<>(List.of(-79.3975116, 43.6592043));
        assertEquals(expected, coordinates);
    }

    @Test
    void testInvalidBuildingCode() {
        GeoapifyDAO geoapifyDAO = new GeoapifyDAO();
        assertThrows(GeoapifyDAO.CoordinateNotFoundException.class, () -> geoapifyDAO.getCoordinates("ZZ"));
    }

    @Test
    void testEmptyBuildingCode (){
        GeoapifyDAO geoapifyDAO = new GeoapifyDAO();
        assertThrows(GeoapifyDAO.CoordinateNotFoundException.class, () -> geoapifyDAO.getCoordinates(""));
    }
}
