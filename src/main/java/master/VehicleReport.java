package master;

import org.apache.flink.api.java.tuple.Tuple8;

public class VehicleReport extends Tuple8<Integer,Integer,Integer,Integer,Integer,Integer,Integer,Integer> {

    public VehicleReport (){
        
    }

    public Integer getTime() {
        return f0;
    }

    public Integer getVid() {
        return f1;
    }

    public Integer getSpeed() {
        return f2;
    }

    public Integer getHighway() {
        return f3;
    }

    public Integer getLane() {
        return f4;
    }

    public Integer getDirection() {
        return f5;
    }

    public Integer getSegment() {
        return f6;
    }

    public Integer getPosition() {
        return f7;
    }

    public void setTime(int time) {
       f0 = time;
    }

    public void setVid(int vid) {
        f1 = vid;
    }

    public void setHighway(int highway) {
        f2 = highway;
    }
    public void setSegment(int segment) {
        f3 = segment;
    }
    public void setDirection(int direction) {
        f4 = direction;
    }
    public void setSpeed(int speed) {
        f5 = speed;
    }
}
