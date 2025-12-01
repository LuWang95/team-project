package generator.use_case.sort_timetable;

public interface DistanceDataAccessInterface {

    /**
     * Returns walking distance (in meters) between two building codes.
     */
    double getWalkingDistance(String fromBuilding, String toBuilding)
            throws DistanceLookupException;

    class DistanceLookupException extends Exception {
        public DistanceLookupException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
