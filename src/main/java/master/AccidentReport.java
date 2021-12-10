package master;

import org.apache.flink.api.java.tuple.Tuple7;

public class AccidentReport
        extends Tuple7<Integer, Integer, Integer, Integer, Integer, Integer, Integer> {

    public AccidentReport(
            Integer firstEvent,
            Integer lastEvent,
            Integer f0,
            Integer f1,
            Integer f2,
            Integer f3,
            Integer f4) {
        super(firstEvent, lastEvent, f0, f1, f2, f3, f4);
    }

    public AccidentReport() {}

    public void setTime1(int time1) {
        f0 = time1;
    }

    public void setTime2(int time2) {
        f1 = time2;
    }

    public void setVid(int vid) {
        f2 = vid;
    }

    public void setHighway(int highway) {
        f3 = highway;
    }

    public void setSegment(int segment) {
        f4 = segment;
    }

    public void setDirection(int direction) {
        f5 = direction;
    }

    public void setPosition(int position) {
        f6 = position;
    }
}
