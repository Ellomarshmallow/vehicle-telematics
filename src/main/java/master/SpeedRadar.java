package master;

import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;

public class SpeedRadar {
    static int threshold = 90;

    // Detect cars which go over the speed limit of 90
    public static SingleOutputStreamOperator detectSpeeding(
            SingleOutputStreamOperator<VehicleReport> tuples) {
        // return tuples.filter(a -> a >= threshold);
        return tuples;
    }
}
//     compute the sum of the tips per hour for each driver
//    DataStream<Tuple3<Long, Long, Float>> hourlyTips = fares
//            .keyBy((TaxiFare fare) -> fare.driverId)
//            .process(new PseudoWindow(Time.hours(1)));
