package data;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import LeJos.DataExchange;

public class SendData implements Runnable {
    private DataExchange dataExchange;

    public SendData(DataExchange dataExchange) {
        this.dataExchange = dataExchange;
    }

    @Override
    public void run() {
        while (dataExchange.getLooping()) { 
            HttpURLConnection conn = null;

            try {
                URL url = new URL("http://172.20.10.7:8080/rest/team19/sendData");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                conn.setDoOutput(true);

                // Create form data
                StringBuilder formData = new StringBuilder();
                formData.append("leftMotor=").append(URLEncoder.encode(String.valueOf(dataExchange.getLeftMotorSpeed()), StandardCharsets.UTF_8.name()));
                formData.append("&rightMotor=").append(URLEncoder.encode(String.valueOf(dataExchange.getRightMotorSpeed()), StandardCharsets.UTF_8.name()));
                formData.append("&securityDistance=").append(URLEncoder.encode(String.valueOf(dataExchange.getSecurityDistance()), StandardCharsets.UTF_8.name()));
                formData.append("&lineColor=").append(URLEncoder.encode(String.valueOf(dataExchange.getCOLOR_INTENSITY_THRESHOLD()), StandardCharsets.UTF_8.name()));

//                System.out.println("Sending Form Data: " + formData.toString());

                // Send form data
                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = formData.toString().getBytes(StandardCharsets.UTF_8.name());
                    os.write(input, 0, input.length);
                }

                int responseCode = conn.getResponseCode();
                System.out.println("Response Code: " + responseCode);
                if (responseCode == HttpURLConnection.HTTP_OK) {
//                    System.out.println("Data sent successfully");
                } else {
                    System.out.println("Failed to send data. HTTP error code: " + responseCode);
                }

                Thread.sleep(1000);

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error sending data to server: " + e.getMessage());
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }
    }
}
