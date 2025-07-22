import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;

public class CurrencyConverter {

    private static final String API_KEY = "cur_live_umPtMVGBILfEDqaKx1mBklQiSW2qvccFxLToXJ5r";

    public static double getExchangeRate(String fromCurrency, String toCurrency) {
        try {
            String url_str = "https://api.currencyapi.com/v3/latest"
                    + "?apikey=" + API_KEY
                    + "&base_currency=" + fromCurrency
                    + "&currencies=" + toCurrency;

            URL url = new URL(url_str);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.setRequestMethod("GET");
            request.connect();

            int responseCode = request.getResponseCode();
            if (responseCode != 200) {
                System.out.println("HTTP Error: " + responseCode);
                return -1;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                json.append(line);
            }
            in.close();

            JSONObject data = new JSONObject(json.toString());

            if (data.has("data") && data.getJSONObject("data").has(toCurrency)) {
                return data.getJSONObject("data").getJSONObject(toCurrency).getDouble("value");
            } else {
                System.out.println("❌ Error: 'data' or currency code not found.");
                return -1;
            }

        } catch (Exception e) {
            System.out.println("Exception while fetching exchange rate: " + e.getMessage());
            return -1;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter source currency code (e.g., USD, INR): ");
        String from = sc.next().toUpperCase();

        System.out.print("Enter target currency code (e.g., EUR, JPY): ");
        String to = sc.next().toUpperCase();

        System.out.print("Enter amount in " + from + ": ");
        double amount = sc.nextDouble();

        double rate = getExchangeRate(from, to);

        if (rate != -1) {
            double converted = amount * rate;
            System.out.printf("✅ Live Rate: 1 %s = %.4f %s%n", from, rate, to);
            System.out.printf("💱 Converted Amount: %.2f %s = %.2f %s%n", amount, from, converted, to);
        } else {
            System.out.println("❌ Failed to retrieve exchange rate.");
        }

        sc.close();
    }
}
