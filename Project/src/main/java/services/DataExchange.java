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
