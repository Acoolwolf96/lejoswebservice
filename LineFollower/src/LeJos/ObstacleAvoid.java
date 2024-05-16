package LeJos;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.lcd.LCD;
import lejos.robotics.SampleProvider;

/**
 * <p>Date 05.04.2024
 * 
 * <p>The ObstacleAvoid class is tasked with detecting obstacles using the ultrasonic sensor.
 * <p>It continuously runs with other robot operations 
 * 
 * <p>The {@code run} method, continuously checks distances using the ultrasonic sensor,
 * signalling the main control logic whenever an obstacle is detected close to the robot
 * 
 */
public class ObstacleAvoid extends Thread {
    private DataExchange DEObj;
    private EV3UltrasonicSensor ultrasonicSensor;
    private SampleProvider distanceProvider;
    private float[] sample;
    

    public ObstacleAvoid(DataExchange DE) {
        this.DEObj = DE;
        ultrasonicSensor = new EV3UltrasonicSensor(SensorPort.S2);
        distanceProvider = ultrasonicSensor.getDistanceMode();
        sample = new float[distanceProvider.sampleSize()];
    }

    @Override
    public void run() {
        while (DEObj.getLooping()) {
            distanceProvider.fetchSample(sample, 0);
            float distance = sample[0] * 100;
            int distanceInt = (int) distance; 

          
            LCD.drawString("Distance: " + distanceInt + " cm", 0, 3);

            if (distance < DEObj.getSecurityDistance()) { 
                DEObj.setObstacleDetected(true);
            } else {
                DEObj.setObstacleDetected(false);
            }

            try {
                Thread.sleep(100); 
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
