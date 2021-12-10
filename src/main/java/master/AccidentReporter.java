package master;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple5;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.util.Collector;

import java.util.Iterator;

public class AccidentReporter {
    public static SingleOutputStreamOperator detectAccident(SingleOutputStreamOperator<VehicleReport> output) {
        return output
                // We filter those cars that have speed 0 (stopped)
                .filter(new FilterFunction<VehicleReport>() {
                    @Override
                    public boolean filter(VehicleReport v) {
                        if (v.getSpeed() == 0) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                })

                //We gather the data that we need from each car that stopped, in this case vid,highway,segment,direction and position.
                //We use a countwindow with 4 as is the number of tuples we want and set the other parameter to 1 since one car can report more than one accident.
                .keyBy(new KeySelector<VehicleReport,Tuple5<Integer, Integer, Integer, Integer, Integer>>() {
                    @Override
                    public Tuple5<Integer, Integer, Integer, Integer, Integer> getKey(
                            VehicleReport v) {
                        return new Tuple5<>(
                                v.getVid(),
                                v.getHighway(),
                                v.getSegment(),
                                v.getDirection(),
                                v.getPosition());
                    }
                })
                .countWindow(4, 1)
                .apply(new AccidentReportWindow());
    }
    //In this method the cars that have stopped more or 4 times will be selected included in the output results
    private static class AccidentReportWindow
            implements WindowFunction<
                    VehicleReport,
                    AccidentReport,
                    Tuple5<Integer, Integer, Integer, Integer, Integer>,
                    GlobalWindow> {

        @Override
        public void apply(
                Tuple5<Integer, Integer, Integer, Integer, Integer> key,
                GlobalWindow window,
                Iterable<VehicleReport> report,
                Collector<AccidentReport> col) {
            
            //We initialize the number of times a car has been stopped
            int numEvents = 1;

            //We use an iterator over the tuples from the vehicle report
            Iterator<VehicleReport> it = report.iterator();
            Integer firstEvent = it.next().getTime();

            //We iterate and get the lastEvent with a maximum of that has been set in the window
            while (it.hasNext()) {
                numEvents++;
                Integer lastEvent = it.next().getTime();
                //When the car has been stopped 4 times or more we gather the data and include it in the output
                if (numEvents >= 4) {
                    AccidentReport accidentReport =
                            new AccidentReport(
                                    firstEvent, lastEvent, key.f0, key.f1, key.f2, key.f3, key.f4);
                    col.collect(accidentReport);
                }
            }
        }
    }
}
