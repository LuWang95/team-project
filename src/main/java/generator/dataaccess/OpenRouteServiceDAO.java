package generator.dataaccess;

import java.util.ArrayList;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class OpenRouteServiceDAO {
    private final int SUCCESS_CODE = 200;

    /**
     * Calls directions service of OpenRouteService API and then returns the distance between the two coordinates.
     * @param startCoords starting coordinates
     * @param endCoords end coordinates
     * @return the distance between the two coordinates
     * @throws NoDistanceFoundException if the distance can't be found (or if the API fails)
     */
    public double getDistance(ArrayList<Double> startCoords, ArrayList<Double> endCoords)
            throws NoDistanceFoundException {
        final Client client = ClientBuilder.newClient();

        // Takes in longtitude then latitude
        final Response response = client.target(OpenRouteServiceConfig.BASE_URL)
                .queryParam("api_key", OpenRouteServiceConfig.API_KEY)
                .queryParam("start", startCoords.get(0) + "," + startCoords.get(1))
                .queryParam("end", endCoords.get(0) + "," + endCoords.get(1))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("ACCEPT", OpenRouteServiceConfig.HEADER)
                .get();

        try {
            final int status = response.getStatus();
            final MultivaluedMap<String, Object> headers = response.getHeaders();
            final String body = response.readEntity(String.class);
            final JSONObject jsonBody = new JSONObject(body);

            if (status == SUCCESS_CODE) {
                return jsonBody.getJSONArray("features")
                        .getJSONObject(0)
                        .getJSONObject("properties")
                        .getJSONArray("segments")
                        .getJSONObject(0)
                        .getDouble("distance");
            }
            else {
                throw new NoDistanceFoundException(startCoords, endCoords);
            }
        }
        catch (JSONException exception) {
            throw new NoDistanceFoundException(startCoords, endCoords);
        }
    }

    static final class NoDistanceFoundException extends Exception {
        private NoDistanceFoundException(ArrayList<Double> startCoords, ArrayList<Double> endCoords) {
            super("No distance found for coordinates: " + startCoords + " and " + endCoords);
        }
    }
}
