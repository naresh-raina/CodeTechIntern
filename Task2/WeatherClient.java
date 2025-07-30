import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class WeatherClient {
    public static void main(String[] args) {
        try {
            String apiUrl = "https://api.open-meteo.com/v1/forecast?latitude=28.6139&longitude=77.2090&current_weather=true";

            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Read response
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parse JSON
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONObject weather = jsonResponse.getJSONObject("current_weather");

            // Display data
            System.out.println("🌤️ Current Weather in Tokyo:");
            System.out.println("Temperature: " + weather.getDouble("temperature") + "°C");
            System.out.println("Windspeed: " + weather.getDouble("windspeed") + " km/h");
            System.out.println("Time: " + weather.getString("time"));

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
