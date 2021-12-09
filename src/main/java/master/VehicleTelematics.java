package master;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.nio.file.Paths;

public class VehicleTelematics {

    public static void main(String[] args) throws Exception {
        // Set up the streaming execution environment
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        env.setParallelism(1); // todo: check how to use properly

        final String INPUT_FILE_PATH = args[0];
        final String OUTPUT_FOLDER_PATH = args[1];

        // Create DataStream[SensorReading] from a stream source
        DataStreamSource<String> stream = env.readTextFile(INPUT_FILE_PATH);

        // Initialize vehicle report from stream
        SingleOutputStreamOperator<VehicleReport> vehicleReport =
                stream.map(
                        new MapFunction<String, VehicleReport>() {
                            VehicleReport vr = new VehicleReport();

                            // Expected data format: [Time, VID, Spd, XWay, Lane, Dir, Seg, Pos]
                            public VehicleReport map(String in) throws Exception {
                                String[] fieldArray = in.split(",");
                                vr.setTime(Integer.parseInt(fieldArray[0]));
                                // vr.setVid((fieldArray[1]));
                                // xxx: Use int type for vid to key on it??
                                vr.setVid(Integer.parseInt(fieldArray[1]));
                                vr.setSpeed(Integer.parseInt(fieldArray[2]));
                                vr.setHighway(Integer.parseInt(fieldArray[3]));
                                vr.setLane(Integer.parseInt(fieldArray[4]));
                                vr.setDirection(Integer.parseInt(fieldArray[5]));
                                vr.setSegment(Integer.parseInt(fieldArray[6]));
                                vr.setPosition(Integer.parseInt(fieldArray[7]));

                                return vr;
                            }
                        });

        // Apply functionality
        SingleOutputStreamOperator speedFines = SpeedRadar.detectSpeeding(vehicleReport);
        SingleOutputStreamOperator avgSpeedFines =
                AverageSpeedControl.detectAvgSpeeding(vehicleReport);
        SingleOutputStreamOperator accidents = AccidentReporter.detectAccident(vehicleReport);

        // Create output CSVs
        speedFines.writeAsCsv(String.valueOf(Paths.get(OUTPUT_FOLDER_PATH, "speedfines.csv")));
        avgSpeedFines.writeAsCsv(
                String.valueOf(Paths.get(OUTPUT_FOLDER_PATH, "avgspeedfines.csv")));
        accidents.writeAsCsv(String.valueOf(Paths.get(OUTPUT_FOLDER_PATH, "accidents.csv")));

        // Execute application
        try {
            env.execute("VehicleTelematics");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
