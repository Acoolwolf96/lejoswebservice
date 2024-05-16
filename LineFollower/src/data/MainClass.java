package data;

import LeJos.DataExchange;

import lejos.hardware.Button;
import LeJos.linefollower;
import LeJos.ColorSensor;
import LeJos.ObstacleAvoid;

public class MainClass {
    public static DataExchange dataExchange = new DataExchange();

    public static void main(String[] args) {
        ColorSensor colorSensor = new ColorSensor(dataExchange); // Assuming constructors to accept DataExchange
        linefollower motorControl = new linefollower(dataExchange);
        ObstacleAvoid ultrasonic = new ObstacleAvoid(dataExchange);
        Connection connection = new Connection(dataExchange);
        
        // Initialize threads for each component
        Thread colorSensorThread = new Thread(colorSensor, "ColorSensorThread");
        Thread motorControlThread = new Thread(motorControl, "MotorControlThread");
        Thread ultrasonicSensorThread = new Thread(ultrasonic, "UltrasonicSensorThread");
        Thread connectionThread = new Thread(connection, "ConnectionThread");

        // Start all threads
        colorSensorThread.start();
        motorControlThread.start();
        ultrasonicSensorThread.start();
        connectionThread.start();

        // Monitor the ESCAPE button to stop the application
        while (Button.ESCAPE.isUp()) {
            if (!dataExchange.getLooping()) {
                System.exit(0);
            }
        }

        System.exit(0);
    }
}
