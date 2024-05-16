package LeJos;

import lejos.hardware.Button;

/**
 * <p>Date 05.04.2024
 * 
 * <p>This class serves as the control point of the robot program, operating the start and stop 
 * of all threads involved in the robot's application
 * 
 * <p>The main function of this class is it controls the lifecycle of all threads in the program.
 */
public class Main {
    private static DataExchange DE = new DataExchange();
    private static linefollower car = new linefollower(DE);
    private static ColorSensor ref = new ColorSensor(DE);
    private static ObstacleAvoid obj = new ObstacleAvoid(DE);
 
    private static data.Connection conn = new data.Connection(DE);

    public static void main(String[] args) {
        Thread connThread = new Thread(conn, "ConnectionThread");
        Thread carThread = new Thread(car, "linefollowerThread");
        Thread refThread = new Thread(ref, "ColorSensorThread");
        Thread objThread = new Thread(obj, "ObstacleAvoidThread");

        carThread.start();
        refThread.start();
        objThread.start();
        connThread.start();

        
        while (Button.ESCAPE.isUp()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        DE.setLooping(false);

        try {
            carThread.join();
            refThread.join();
            objThread.join();
            connThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace(); 
        }

        System.out.println("Shutdown completed, all threads stopped.");
    }
}
