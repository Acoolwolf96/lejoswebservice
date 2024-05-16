package LeJos;


/**
 * <p>Date 05.04.2024
 * <p>The DataExchange class facilitates communication and sharing between threads.
 * <p>It is not a thread itself but a shared resource accessed by various threads for coordination.
 * 
 * <p>It holds runtime information and system flags that influence decision making across threads
 * 
 * <p>It does not have a {@code run} method, but its getters and setters are important elements to the 
 * robot's operation.
 */
public class DataExchange {
    private int lineValue;
    private boolean looping = true;
    private boolean obstacleDetected = false;
    private int COLOR_INTENSITY_THRESHOLD;
    private int securityDistance;
    private int leftMotorSpeed;
    private int rightMotorSpeed;
    
    
    //Line Follower
    public void setLineValue(int value) {
        this.lineValue = value;
    }

    public int getLineValue() {
        return this.lineValue;
    }
    // Getters and Setters for Obstacle
    public void setObstacleDetected(boolean detected) {
    	this.obstacleDetected = detected;
    }
    
    public boolean isObstacleDetected() {
    	return obstacleDetected;
    }
    // boolean condition for robot program
    public void setLooping(boolean looping) {
        this.looping = looping;
    }

    public boolean getLooping() {
        return this.looping;
    }

	public int getSecurityDistance() {
		return securityDistance;
	}

	public void setSecurityDistance(int securityDistance) {
		this.securityDistance = securityDistance;
	}

	public int getCOLOR_INTENSITY_THRESHOLD() {
		return COLOR_INTENSITY_THRESHOLD;
	}

	public void setCOLOR_INTENSITY_THRESHOLD(int cOLOR_INTENSITY_THRESHOLD) {
		COLOR_INTENSITY_THRESHOLD = cOLOR_INTENSITY_THRESHOLD;
	}

	public int getLeftMotorSpeed() {
		return leftMotorSpeed;
	}

	public void setLeftMotorSpeed(int leftMotorSpeed) {
		this.leftMotorSpeed = leftMotorSpeed;
	}

	public int getRightMotorSpeed() {
		return rightMotorSpeed;
	}

	public void setRightMotorSpeed(int rightMotorSpeed) {
		this.rightMotorSpeed = rightMotorSpeed;
	}


}
