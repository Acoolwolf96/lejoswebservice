package data;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import LeJos.DataExchange;

public class Connection implements Runnable {
    private DataExchange dataExchange;

    public Connection(DataExchange dataExchange) {
        this.dataExchange = dataExchange;
    }

    @Override
    public void run() {
        while (dataExchange.getLooping()) {
            HttpURLConnection conn = null;
            try {
                URL url = new URL("http://172.20.10.7:8080/rest/team19/lejosdata/5");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            response.append(line);
                        }
                        parseAndSetData(response.toString());
                    }
                } else {
                    System.out.println("HTTP error code: " + responseCode);
                }
            } catch (Exception e) {
                System.out.println("Exception occurred: " + e.getMessage());
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
            try {
                Thread.sleep(1000); 
            } catch (InterruptedException e) {
                System.out.println("InterruptedException occurred: " + e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }

    private void parseAndSetData(String jsonString) {
        try {
            jsonString = jsonString.trim().replace("{", "").replace("}", "").replace("\"", "");
            String[] keyValuePairs = jsonString.split(",");
            int leftMotorSpeed = 0, rightMotorSpeed = 0, securityDistance = 0, lineColor = 0;

            for (String pair : keyValuePairs) {
                String[] entry = pair.split(":");
                String key = entry[0].trim();
                int value = Integer.parseInt(entry[1].trim());

                switch (key) {
                    case "leftMotor":
                        leftMotorSpeed = value;
                        break;
                    case "rightMotor":
                        rightMotorSpeed = value;
                        break;
                    case "securityDistance":
                        securityDistance = value;
                        break;
                    case "lineColor":
                        lineColor = value;
                        break;
                    default:
                        System.out.println("Unknown key: " + key);
                        break;
                }
            }

//            System.out.println("Parsed values: LeftMotorSpeed=" + leftMotorSpeed +
//                               ", RightMotorSpeed=" + rightMotorSpeed +
//                               ", SecurityDistance=" + securityDistance +
//                               ", LineColor=" + lineColor);

            dataExchange.setLeftMotorSpeed(leftMotorSpeed);
            dataExchange.setRightMotorSpeed(rightMotorSpeed);
            dataExchange.setSecurityDistance(securityDistance);
            dataExchange.setCOLOR_INTENSITY_THRESHOLD(lineColor);

        } catch (Exception e) {
            System.out.println("Error parsing JSON: " + jsonString);
            e.printStackTrace();
        }
    }
}
