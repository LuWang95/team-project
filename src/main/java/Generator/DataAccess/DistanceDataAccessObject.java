package Generator.DataAccess;

import Generator.UseCase.sort_timetable.DistanceDataAccessInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DistanceDataAccessObject implements DistanceDataAccessInterface {

    private final GeoapifyDAO geoapifyDAO;
    private final OpenRouteServiceDAO routeServiceDAO;

    // Cache: buildingCode -> [lon, lat]
    private final Map<String, List<Double>> coordinateCache = new HashMap<>();
    // Cache: "FROM->TO" -> distance in meters
    private final Map<String, Double> distanceCache = new HashMap<>();

    public DistanceDataAccessObject() {
        this.geoapifyDAO = new GeoapifyDAO();
        this.routeServiceDAO = new OpenRouteServiceDAO();
    }

    @Override
    public double getWalkingDistance(String fromBuilding, String toBuilding)
            throws DistanceLookupException {

        // same building or empty => no walking
        if (fromBuilding == null || toBuilding == null
                || fromBuilding.isEmpty() || toBuilding.isEmpty()
                || fromBuilding.equals(toBuilding)) {
            return 0.0;
        }

        String key = fromBuilding + "->" + toBuilding;
        if (distanceCache.containsKey(key)) {
            return distanceCache.get(key);
        }

        try {
            List<Double> startCoords = getCoordinates(fromBuilding);
            List<Double> endCoords = getCoordinates(toBuilding);

            // OpenRouteServiceDAO expects ArrayList<Double>, so wrap them
            double distance = routeServiceDAO.getDistance(
                    new ArrayList<>(startCoords),
                    new ArrayList<>(endCoords)
            );
            distanceCache.put(key, distance);
            return distance;

        } catch (GeoapifyDAO.CoordinateNotFoundException |
                 OpenRouteServiceDAO.NoDistanceFoundException e) {

            throw new DistanceLookupException(
                    "Unable to fetch walking distance between "
                            + fromBuilding + " and " + toBuilding, e);
        }
    }

    private List<Double> getCoordinates(String buildingCode)
            throws GeoapifyDAO.CoordinateNotFoundException {

        if (coordinateCache.containsKey(buildingCode)) {
            return coordinateCache.get(buildingCode);
        }

        List<Double> coords = geoapifyDAO.getCoordinates(buildingCode);
        coordinateCache.put(buildingCode, coords);
        return coords;
    }
}
