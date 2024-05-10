import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import lejos.pc.comm.NXTCommLogListener;
import lejos.pc.comm.NXTConnector;

public class RobotDataHandler {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/robotdb";
    private static final String USER = "root";
    private static final String PASS = "Dherniza2!";

    public static void main(String[] args) {
        RobotDataHandler handler = new RobotDataHandler();
        LejosData data = handler.retrieveDataFromDatabase();
        if (data != null) {
            handler.sendDataToRobot(data);
        }
    }

    private LejosData retrieveDataFromDatabase() {
        String query = "SELECT leftMotor, rightMotor, securityDistance, lineColor FROM lejos_data ORDER BY id DESC LIMIT 1";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return new LejosData(rs.getInt("leftMotor"), rs.getInt("rightMotor"),
                        rs.getInt("securityDistance"), rs.getInt("lineColor"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void sendDataToRobot(LejosData data) {
        NXTConnector conn = new NXTConnector();



        conn.addLogListener(new NXTCommLogListener() {
            public void logEvent(String message) {
                System.out.println("Log.listener: " + message);
            }

            public void logEvent(Throwable throwable) {
                System.out.println("Log.listener - stack trace: ");
                throwable.printStackTrace();
            }
        });

