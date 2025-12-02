package Generator.DataAccess;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpenRouteServiceDAOTest {

    @Test
    void testValidCoordinates() throws OpenRouteServiceDAO.NoDistanceFoundException {
        OpenRouteServiceDAO openRouteServiceDAO = new OpenRouteServiceDAO();
        ArrayList<Double> start = new ArrayList<>(List.of(-79.3966447, 43.6605006));
        ArrayList<Double> end = new ArrayList<>(List.of(-79.3975116, 43.6592043));
        double distance = openRouteServiceDAO.getDistance(start, end);
        double expected = 314.5;
        assertEquals(expected, distance);
    }

    @Test
    void testInvalidCoordinates() {
        OpenRouteServiceDAO openRouteServiceDAO = new OpenRouteServiceDAO();
        ArrayList<Double> start = new ArrayList<>(List.of(100.0, 200.0));
        ArrayList<Double> end = new ArrayList<>(List.of(-100.0, -200.0));
        assertThrows(OpenRouteServiceDAO.NoDistanceFoundException.class, () ->
                openRouteServiceDAO.getDistance(start, end));
    }

    @Test
    void testEmptyCoordinates() {
        OpenRouteServiceDAO openRouteServiceDAO = new OpenRouteServiceDAO();
        ArrayList<Double> start = new ArrayList<>(List.of());
        ArrayList<Double> end = new ArrayList<>(List.of());
        assertThrows(OpenRouteServiceDAO.NoDistanceFoundException.class, () ->
                openRouteServiceDAO.getDistance(start, end));
    }
}
