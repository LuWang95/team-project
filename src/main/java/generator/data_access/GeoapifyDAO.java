package generator.data_access;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class GeoapifyDAO {
    protected static final Map<String, String> BuildingCodeToAddress = new HashMap<>();
    private final int SUCCESS_CODE = 200;

    static {
        BuildingCodeToAddress.put("DA", "1 Spadina Crescent");
        BuildingCodeToAddress.put("C1", "30 Charles Street West");
        BuildingCodeToAddress.put("C2", "35 Charles Street West");
        BuildingCodeToAddress.put("ML", "39 Queen's Park Crescent East");
        BuildingCodeToAddress.put("SX", "40 Sussex Avenue");
        BuildingCodeToAddress.put("PI", "59 Queen's Park Crescent East");
        BuildingCodeToAddress.put("ZC", "88 College Street");
        BuildingCodeToAddress.put("JP", "90 Wellesley Street West");
        BuildingCodeToAddress.put("IR", "121 St. George Street");
        BuildingCodeToAddress.put("CO", "162 St. George Street");
        BuildingCodeToAddress.put("HU", "215 Huron Street");
        BuildingCodeToAddress.put("CZ", "229 College Street");
        BuildingCodeToAddress.put("SK", "246 Bloor Street West");
        BuildingCodeToAddress.put("RM", "256 McCaul Street");
        BuildingCodeToAddress.put("OA", "263 McCaul Street");
        BuildingCodeToAddress.put("CA", "370 Huron Street");
        BuildingCodeToAddress.put("TT", "455 Spadina Avenue");
        BuildingCodeToAddress.put("XG", "665 Spadina Avenue");
        BuildingCodeToAddress.put("UY", "700 University Avenue");
        BuildingCodeToAddress.put("IA", "703 University Avenue");
        BuildingCodeToAddress.put("AN", "95 Queen's Park");
        BuildingCodeToAddress.put("AP", "19 Ursula Franklin Street");
        BuildingCodeToAddress.put("AB", "50 St. George Street");
        BuildingCodeToAddress.put("BA", "40 St. George Street");
        BuildingCodeToAddress.put("BF", "4 Bancroft Avenue");
        BuildingCodeToAddress.put("BC", "95 Charles Street West");
        BuildingCodeToAddress.put("SB", "487 Spadina Crescent");
        BuildingCodeToAddress.put("NB", "563 Spadina Crescent");
        BuildingCodeToAddress.put("BR", "81 St. Mary Street");
        BuildingCodeToAddress.put("NL", "6 Queen's Park Crescent West");
        BuildingCodeToAddress.put("CG", "14 Queen's Park Crescent West");
        BuildingCodeToAddress.put("CR", "100 St. Joseph Street");
        BuildingCodeToAddress.put("BN", "Clara Benson Building");
        BuildingCodeToAddress.put("BL", "140 St. George Street");
        BuildingCodeToAddress.put("CH", "Convocation Hall");
        BuildingCodeToAddress.put("CU", "33 St. George Street");
        BuildingCodeToAddress.put("PT", "6 King's College Road");
        BuildingCodeToAddress.put("DN", "124 Edward Street");
        BuildingCodeToAddress.put("WR", "45 Walmer Road");
        BuildingCodeToAddress.put("ER", "7 Glen Morris Street");
        BuildingCodeToAddress.put("ES", "22 Ursula Franklin Street");
        BuildingCodeToAddress.put("EJ", "80 Queens Park");
        BuildingCodeToAddress.put("EM", "75 Queen's Park Crescent East");
        BuildingCodeToAddress.put("EA", "11 King's College Road");
        BuildingCodeToAddress.put("EX", "255 McCaul Street");
        BuildingCodeToAddress.put("FC", "41 Willcocks Street");
        BuildingCodeToAddress.put("FH", "84 Queens Park");
        BuildingCodeToAddress.put("FI", "222 College Street");
        BuildingCodeToAddress.put("FS", "59 Queen's Park Crescent East");
        BuildingCodeToAddress.put("FG", "150 College Street");
        BuildingCodeToAddress.put("LW", "78 Queens Park");
        BuildingCodeToAddress.put("BH", "1 Elmsley Place");
        BuildingCodeToAddress.put("GA", "223 College Street");
        BuildingCodeToAddress.put("GB", "35 St. George Street");
        BuildingCodeToAddress.put("LA", "15 Devonshire Place");
        BuildingCodeToAddress.put("SM", "9 King's College Circle");
        BuildingCodeToAddress.put("GH", "8 Elmsley Place");
        BuildingCodeToAddress.put("GO", "100 Devonshire Place");
        BuildingCodeToAddress.put("GC", "150 Charles Street West");
        BuildingCodeToAddress.put("GU", "16 Bancroft Avenue");
        BuildingCodeToAddress.put("HA", "170 College Street");
        BuildingCodeToAddress.put("HS", "155 College Street");
        BuildingCodeToAddress.put("IN", "2 Sussex Avenue");
        BuildingCodeToAddress.put("BT", "93 Charles Street West");
        BuildingCodeToAddress.put("DR", "21 King's College Circle");
        BuildingCodeToAddress.put("JH", "170 St. George Street");
        BuildingCodeToAddress.put("KL", "113 St. Joseph Street");
        BuildingCodeToAddress.put("KX", "59 St. George Street");
        BuildingCodeToAddress.put("KP", "569 Spadina Crescent");
        BuildingCodeToAddress.put("KS", "214 College Street");
        BuildingCodeToAddress.put("LM", "80 St. George Street");
        BuildingCodeToAddress.put("MB", "170 College Street");
        BuildingCodeToAddress.put("PB", "144 College Street");
        BuildingCodeToAddress.put("LI", "125 Queen's Park");
        BuildingCodeToAddress.put("SO", "12 Hart House Circle");
        BuildingCodeToAddress.put("GM", "4 Glen Morris Street");
        BuildingCodeToAddress.put("MM", "63 St. Geroge Street");
        BuildingCodeToAddress.put("GE", "150 St. George Street");
        BuildingCodeToAddress.put("MP", "255 Huron Street");
        BuildingCodeToAddress.put("MR", "12 Queen's Park Crescent West");
        BuildingCodeToAddress.put("MC", "5 King's College Road");
        BuildingCodeToAddress.put("MS", "1 King's College Circle");
        BuildingCodeToAddress.put("MH", "59 Queen's Park Crescent East");
        BuildingCodeToAddress.put("MK", "315 Bloor Street West");
        BuildingCodeToAddress.put("MU", "1 Devonshire Place");
        BuildingCodeToAddress.put("AH", "121 St. Joseph Street");
        BuildingCodeToAddress.put("MY", "55 St. George Street");
        BuildingCodeToAddress.put("NF", "73 Queen's Park Crescent East");
        BuildingCodeToAddress.put("OH", "50 St. Joseph Street");
        BuildingCodeToAddress.put("OI", "252 Bloor Street West");
        BuildingCodeToAddress.put("PH", "3 Elmsley Place");
        BuildingCodeToAddress.put("PG", "45 St. George Street");
        BuildingCodeToAddress.put("RW", "25 Harbord Street");
        BuildingCodeToAddress.put("RG", "100 Wellesley Street West");
        BuildingCodeToAddress.put("RU", "500 University Avenue");
        BuildingCodeToAddress.put("RL", "130 St. George Street");
        BuildingCodeToAddress.put("RS", "164 College Street");
        BuildingCodeToAddress.put("RT", "105 St. George Street");
        BuildingCodeToAddress.put("SF", "10 King's College Road");
        BuildingCodeToAddress.put("CS", "158 St. George Street");
        BuildingCodeToAddress.put("GS", "65 St. George Street");
        BuildingCodeToAddress.put("SH", "5 Elmsley Place");
        BuildingCodeToAddress.put("SS", "100 St. George Street");
        BuildingCodeToAddress.put("SI", "27 King's College Circle");
        BuildingCodeToAddress.put("TR", "7 Hart House Circle");
        BuildingCodeToAddress.put("BS", "50 St. Joseph Street");
        BuildingCodeToAddress.put("SN", "63 Charles Street West");
        BuildingCodeToAddress.put("EP", "149 College Street");
        BuildingCodeToAddress.put("SU", "230 College Street");
        BuildingCodeToAddress.put("TF", "57 Queen's Park Crescent Esat");
        BuildingCodeToAddress.put("DC", "160 College Street");
        BuildingCodeToAddress.put("RB", "120 St. George Street");
        BuildingCodeToAddress.put("TH", "47 Queen's Park Crescent East");
        BuildingCodeToAddress.put("TC", "6 Hoskin Avenue");
        BuildingCodeToAddress.put("UC", "15 King's College Circle");
        BuildingCodeToAddress.put("UP", "79, St. George Street");
        BuildingCodeToAddress.put("FE", "371 Bloor Street West");
        BuildingCodeToAddress.put("VC", "91 Charles Street West");
        BuildingCodeToAddress.put("WB", "184 College Street");
        BuildingCodeToAddress.put("WS", "55 Harbord Street");
        BuildingCodeToAddress.put("WW", "119 St. George Street");
        BuildingCodeToAddress.put("WY", "5 Hoskin Avenue");
    }

    /**
     * Calls the geocoding API of Geoapify and return the coordinates of the building specified.
     * @param buildingCode buliding code
     * @return coordinates of the building
     * @throws CoordinateNotFoundException if the coordinate can't be found (or if the API fails)
     */
    public List<Double> getCoordinates(String buildingCode) throws CoordinateNotFoundException {
        final Client client = ClientBuilder.newClient();
        final String address = BuildingCodeToAddress.get(buildingCode);

        final Response response = client.target(GeoapifyConfig.BASE_URL)
                .queryParam("text", address)
                .queryParam("city", "Toronto")
                .queryParam("country", "Canada")
                .queryParam("apiKey", GeoapifyConfig.API_KEY)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        try {
            final int status = response.getStatus();
            final String body = response.readEntity(String.class);
            final JSONObject jsonBody = new JSONObject(body);

            if (status == SUCCESS_CODE) {
                final ArrayList<Double> coordinates = new ArrayList<>();
                coordinates.add(
                        jsonBody.getJSONArray("features")
                                .getJSONObject(0)
                                .getJSONObject("properties")
                                .getDouble("lon")
                );
                coordinates.add(
                        jsonBody.getJSONArray("features")
                                .getJSONObject(0)
                                .getJSONObject("properties")
                                .getDouble("lat")
                );
                return coordinates;
            }
            else {
                throw new CoordinateNotFoundException(buildingCode);
            }
        }
        catch (JSONException exception) {
            throw new CoordinateNotFoundException(buildingCode);
        }
    }

    static final class CoordinateNotFoundException extends Exception {
        private CoordinateNotFoundException(String buildingCode) {
            super("Coordinate not found for " + buildingCode);
        }
    }
}
