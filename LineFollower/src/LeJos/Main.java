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
    private static data.SendData snd = new data.SendData(DE);
 
    private static data.Connection conn = new data.Connection(DE);

    public static void main(String[] args) {
        Thread connThread = new Thread(conn, "ConnectionThread");
        Thread sndThread = new Thread(snd, "sendDataThread");
        Thread carThread = new Thread(car, "carThread");
        
        carThread.start();
        ref.start();
        obj.start();
        sndThread.start();
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
            ref.join();
            obj.join();
            connThread.join();
            sndThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace(); 
        }

        System.out.println("Shutdown completed, all threads stopped.");
    }
}
