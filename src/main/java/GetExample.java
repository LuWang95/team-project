import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetExample {
    final OkHttpClient client = new OkHttpClient();

    String run(String start,String dest) throws IOException {
        String url = "https://api.openrouteservice.org/v2/directions/foot-walking?"
                + "start="+start + "&end=" + dest + "&api_key=" +
                "eyJvcmciOiI1YjNjZTM1OTc4NTExMTAwMDFjZjYyNDgiLCJpZCI6IjI5ZDc3ZGEyMDVkMDQ2NjliYzFiZjJhY2FhNTYxZmE2IiwiaCI6Im11cm11cjY0In0=";
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public static void main(String[] args) throws IOException {
        GetExample example = new GetExample();
        String response = example.run("-79.398,43.659","-79.395,43.662");
        System.out.println(response);
    }
}

