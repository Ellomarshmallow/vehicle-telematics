package master;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;

public class SpeedRadar {
    static final int SPEED_LIMIT = 90;

    // Detect cars which go over the speed limit of 90
    public static SingleOutputStreamOperator detectSpeeding(
            SingleOutputStreamOperator<VehicleReport> output) {
        return output.filter(
                        new FilterFunction<VehicleReport>() {
                            @Override
                            public boolean filter(VehicleReport v) throws Exception {
                                if (v.getSpeed() > SPEED_LIMIT) {
                                    return true;
                                } else {
                                    return false;
                                }
                            }
                        })
                // We convert into KeyStream to use the same report
                .keyBy(
                        new KeySelector<VehicleReport, Tuple2<Integer, Integer>>() {

                            @Override
                            public Tuple2<Integer, Integer> getKey(VehicleReport v) {
                                return Tuple2.of(v.getVid(), v.getDirection());
                            }
                        })
                .map(
                        new MapFunction<VehicleReport, SpeedRadarReport>() {
                            SpeedRadarReport sre = new SpeedRadarReport();

                            @Override
                            public SpeedRadarReport map(VehicleReport vehicleReport)
                                    throws Exception {
                                sre.setTime(vehicleReport.getTime());
                                sre.setVid(vehicleReport.getVid());
                                sre.setHighway(vehicleReport.getHighway());
                                sre.setSegment(vehicleReport.getSegment());
                                sre.setDirection(vehicleReport.getDirection());
                                sre.setSpeed(vehicleReport.getSpeed());
                                return sre;
                            }
                        });
    }
}
