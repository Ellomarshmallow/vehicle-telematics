package master;

<<<<<<< HEAD:src/main/java/es/upm/lsd/master/VehicleReport.java
import org.apache.flink.api.java.tuple.Tuple8;

public class VehicleReport extends Tuple8<Integer,String,Integer,Integer,Integer,Integer,Integer,Integer> {
=======
public class VehicleReport {
    public int time;
    public int vid;
    public int speed;
    public int highway;
    public int lane;
    public int direction;
    public int segment;
    public int position;
>>>>>>> main:src/main/java/master/VehicleReport.java

    public int getTime() {
        return f0;
    }

<<<<<<< HEAD:src/main/java/es/upm/lsd/master/VehicleReport.java
    public String getVid() {
        return f1;
=======
    public int getVid() {
        return vid;
>>>>>>> main:src/main/java/master/VehicleReport.java
    }

    public int getSpeed() {
        return f2;
    }

    public int getHighway() {
        return f3;
    }

    public int getLane() {
        return f4;
    }

    public int getDirection() {
        return f5;
    }

    public int getSegment() {
        return f6;
    }

    public int getPosition() {
        return f7;
    }

    public void setTime(int time) {
       f0 = time;
    }

<<<<<<< HEAD:src/main/java/es/upm/lsd/master/VehicleReport.java
    public void setVid(String vid) {
        f1 = vid;
=======
    public void setVid(int vid) {
        this.vid = vid;
>>>>>>> main:src/main/java/master/VehicleReport.java
    }

    public void setSpeed(int speed) {
        f2 = speed;
    }

    public void setHighway(int highway) {
        f3 = highway;
    }

    public void setLane(int lane) {
        f4 = lane;
    }

    public void setDirection(int direction) {
        f5 = direction;
    }

    public void setSegment(int segment) {
        f6 = segment;
    }

    public void setPosition(int position) {
        f7 = position;
    }

    String speedFineOutput(VehicleReport result) {
        return String.format("%s,%s,%s,%s,%s,%s", result.getTime(),result.getVid(), result.getDirection(),result.getHighway(), result.getLane(),result.getSegment(),result.getSpeed());
    }
}
