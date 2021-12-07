package es.upm.lsd.master;

public class VehicleReport {
    public int time;
    public String vid;
    public int speed;
    public int highway;
    public int lane;
    public int direction;
    public int segment;
    public int position;

    public int getTime() {
        return time;
    }

    public String getVid() {
        return vid;
    }

    public int getSpeed() {
        return speed;
    }

    public int getHighway() {
        return highway;
    }

    public void getLane(int lane) {
        this.lane = lane;
    }

    public int getDirection() {
        return direction;
    }

    public int getSegment() {
        return segment;
    }

    public int getPosition() {
        return position;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setHighway(int highway) {
        this.highway = highway;
    }

    public void setLane(int lane) {
        this.lane = lane;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void setSegment(int segment) {
        this.segment = segment;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
