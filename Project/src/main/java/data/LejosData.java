package data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "lejos_data")
public class LejosData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int leftMotor;
    private int rightMotor;
    private int securityDistance;
    private int lineColor;

    public LejosData() {
        super();
    }

    public LejosData(int leftMotor, int rightMotor, int securityDistance, int lineColor) {
        this.leftMotor = leftMotor;
        this.rightMotor = rightMotor;
        this.securityDistance = securityDistance;
        this.lineColor = lineColor;
    }

    public int getLeftMotor() {
        return leftMotor;
    }

    public void setLeftMotor(int leftMotor) {
        this.leftMotor = leftMotor;
    }

    public int getRightMotor() {
        return rightMotor;
    }

    public void setRightMotor(int rightMotor) {
        this.rightMotor = rightMotor;
    }

    public int getSecurityDistance() {
        return securityDistance;
    }

    public void setSecurityDistance(int securityDistance) {
        this.securityDistance = securityDistance;
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    @Override
    public String toString() {
        return "LejosData{" +
               "leftMotor=" + leftMotor +
               ", rightMotor=" + rightMotor +
               ", securityDistance=" + securityDistance +
               ", lineColor=" + lineColor +
               '}';
    }
}
