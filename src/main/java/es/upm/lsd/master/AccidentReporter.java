package es.upm.lsd.master;

import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;

public class AccidentReporter {
    public static SingleOutputStreamOperator detectAccident(
            SingleOutputStreamOperator<VehicleReport> tuples) {
        return none;
    }
}
