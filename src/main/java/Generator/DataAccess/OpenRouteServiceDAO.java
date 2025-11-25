package Generator.DataAccess;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.util.ArrayList;
import org.json.JSONObject;

public class OpenRouteServiceDAO {
    public double getDistance(ArrayList<Double> startCoords, ArrayList<Double> endCoords) {
        Client client = ClientBuilder.newClient();

        // Takes in longtitude then latitude
        Response response = client.target(OpenRouteServiceConfig.BASE_URL)
                .queryParam("api_key", OpenRouteServiceConfig.API_KEY)
                .queryParam("start", startCoords.get(0) + "," + startCoords.get(1))
                .queryParam("end", endCoords.get(0) + "," + endCoords.get(1))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("ACCEPT", OpenRouteServiceConfig.HEADER)
                .get();

        int status = response.getStatus();
        MultivaluedMap<String, Object> headers = response.getHeaders();
        String body = response.readEntity(String.class);
        JSONObject jsonBody = new JSONObject(body);

        return jsonBody.getJSONArray("features")
                .getJSONObject(0)
                .getJSONObject("properties")
                .getJSONArray("segments")
                .getJSONObject(0)
                .getDouble("distance");
    }

    public static void main(String[] args) {
        ArrayList<Double> start = new ArrayList<>();
        ArrayList<Double> end = new ArrayList<>();

        start.add(-79.390613);
        start.add(43.666898);

        end.add(-79.3975116);
        end.add(43.6592043);

        Client client = ClientBuilder.newClient();
        Response response = client.target(OpenRouteServiceConfig.BASE_URL)
                .queryParam("api_key", OpenRouteServiceConfig.API_KEY)
                .queryParam("start", start.get(0) + "," + start.get(1))
                .queryParam("end", end.get(0) + "," + end.get(1))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("ACCEPT", OpenRouteServiceConfig.HEADER)
                .get();


        String body = response.readEntity(String.class);
        JSONObject jsonBody = new JSONObject(body);
        System.out.println(body);

        System.out.println(jsonBody.getJSONArray("features")
                .getJSONObject(0)
                .getJSONObject("properties")
                .getJSONArray("segments")
                .getJSONObject(0)
                .getDouble("distance"));
    }
}
