package master;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.functions.timestamps.AscendingTimestampExtractor;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.assigners.EventTimeSessionWindows;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.util.Collector;
import org.apache.flink.api.java.tuple.Tuple6;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;

public class AverageSpeedControl {
    public static SingleOutputStreamOperator detectAvgSpeeding(SingleOutputStreamOperator<VehicleReport> output) {
        return output

        // We create a filter to select the tuples that are in the segment from 52 to 56
        .filter(new FilterFunction<VehicleReport>() {
            @Override
            public boolean filter(VehicleReport v){
                if(v.getSegment() >= 52 && v.getSegment() <= 56){
                    return true;
                }
                else{
                    return false;
                }
            }         
        })

        //We assign the timestamp and watermarks with time
        .assignTimestampsAndWatermarks(new AscendingTimestampExtractor<VehicleReport>() {

            @Override
            public long extractAscendingTimestamp(VehicleReport v) {
                return v.getTime()*1000;
            }
        })

        //We convert into KeyStream to use the same report
        .keyBy(new KeySelector<VehicleReport, Tuple3<Integer,Integer,Integer>>(){

            @Override
            public Tuple3<Integer,Integer,Integer> getKey(VehicleReport v) {
                return Tuple3.of( v.getHighway(), v.getVid(), v.getDirection());
                
            }

        })
    

    .window(EventTimeSessionWindows.withGap(Time.seconds(31)))
    .apply(new AverageSpeedControlWindow());
        
    }
   

    private static class AverageSpeedControlWindow implements WindowFunction<VehicleReport,Tuple6<Double, Integer, Integer, Integer, Integer, Integer>,Tuple3<Integer, Integer, Integer>,TimeWindow>{
        
        @Override
        public void apply(Tuple3<Integer, Integer, Integer> key, TimeWindow window, Iterable<VehicleReport> report, Collector<Tuple6<Double, Integer, Integer, Integer, Integer, Integer>> col) {

            int initialPos = Integer.MAX_VALUE;
            int initialTime = Integer.MAX_VALUE;
            int finalPos = -1;
            int finalTime = -1;
            int firstSegment = Integer.MAX_VALUE;
            int lastSegment = -1;


            for(VehicleReport v: report){
                initialTime = Integer.min(initialTime, v.getTime());
                initialPos = Integer.min(initialPos, v.getPosition());
                finalPos = Integer.max(finalPos, v.getPosition());
                finalTime = Integer.max(finalTime, v.getTime());
                firstSegment = Integer.min(firstSegment, v.getSegment());
                lastSegment = Integer.max(lastSegment, v.getSegment());

            }
            // We calculate the average speed. We conver the position to Double and convert the unit of measurement  
            Double averageSpeed = (finalPos - initialPos) * 1.0 / (finalTime - initialTime) * 2.23694;

            //Now we take the values that exceed the speed limit and completed the segment
            if( averageSpeed > 60 && firstSegment == 52 && lastSegment == 56){
                Tuple6<Double, Integer, Integer, Integer, Integer, Integer> result = new Tuple6(averageSpeed, initialTime, finalTime, key.f0, key.f1, key.f2);
                col.collect(result);

            }
        }
        
    }
    
}