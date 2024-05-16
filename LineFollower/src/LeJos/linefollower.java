package LeJos;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.utility.Delay;

/**
 * @author Nelson Njoku 
 * @author Keshav Sapkota 
 * @author Abraham Atenidkoye
 * 
 * @version 1.7.0_80
 *<p> Date 05.04.2024
 * 
 * <p>This Linefollower class is responsible for controlling the robot's movement to follow the line.
 * The class extends Thread, enabling parallel execution of other tasks
 * 
 * <p>The {@code run} method continuously  checks the color sensor reading to adjust the movement of the robot, aiming to keep it aligned
 * with the line. it also monitors for obstacle and initiate the avoidObstacle method
 */
public class linefollower extends Thread {
    private DataExchange DEObj;


    private EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(MotorPort.A); // Left motor
    private EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(MotorPort.B); // Right motor

    public linefollower(DataExchange DE) {
        this.DEObj = DE;
    }

    @Override
    public void run() {
        System.out.println("Line Follower Started");
        Button.waitForAnyPress();
        
        //Play Song
        SoundPlayer song = new SoundPlayer();
        song.start();
        
        long startTime = System.currentTimeMillis();
        while (DEObj.getLooping() && Button.ESCAPE.isUp()) {
            int lineValue = DEObj.getLineValue();

            if (DEObj.isObstacleDetected()) {
                avoidObstacle();
                DEObj.setObstacleDetected(false); 
            } else {
                followLine(lineValue);
            }
        }
        
        song.stopPlaying();
        try {
            song.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        long totalTime = System.currentTimeMillis() - startTime;
        LCD.clear();
        LCD.drawString("Total Time: " + (totalTime / 1000.0) + "s", 0, 4);
        System.out.println("Total Time: " + (totalTime / 1000.0) + "s");
        
        Button.waitForAnyPress();
        leftMotor.stop();
        rightMotor.stop();
        
        
    }

    private void followLine(int lineValue) {
    	if (lineValue < DEObj.getCOLOR_INTENSITY_THRESHOLD()) {
            // Use the speed settings from DataExchange
            leftMotor.setSpeed(DEObj.getLeftMotorSpeed());
            rightMotor.setSpeed(DEObj.getRightMotorSpeed());
        } else {
            // Use reversed speeds or other logic as necessary
            leftMotor.setSpeed(DEObj.getRightMotorSpeed());
            rightMotor.setSpeed(DEObj.getLeftMotorSpeed());
        }
        leftMotor.forward();
        rightMotor.forward();
        Delay.msDelay(100); 
    }

    private void avoidObstacle() {
    	
    	Sound.beep();
    	Sound.beep();
        
        // Turn left 
        leftMotor.setSpeed(220); 
        rightMotor.setSpeed(-220); 
        leftMotor.forward();
        rightMotor.forward();
        Delay.msDelay(500);

        // Move forward a bit to clear the obstacle
        leftMotor.setSpeed(20);
        rightMotor.setSpeed(20);
        leftMotor.forward();
        rightMotor.forward();
        Delay.msDelay(500);

        // Right Turn
        leftMotor.setSpeed(-220);
        rightMotor.setSpeed(220); 
        leftMotor.backward(); 
        rightMotor.forward();
        Delay.msDelay(500);
        
         
    }

}
