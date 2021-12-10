package master;

import java.util.Iterator;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.api.java.tuple.Tuple5;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.util.Collector;

public class AccidentReporter {
    public static SingleOutputStreamOperator detectAccident(SingleOutputStreamOperator<VehicleReport> output) {
        return output

        .filter(new FilterFunction<VehicleReport>() {
            @Override
            public boolean filter(VehicleReport v){
                if(v.getSpeed() == 0){
                    return true;
                }
                else{
                    return false;
                }
            }       
        })
        .keyBy(new KeySelector<VehicleReport, Tuple5<Integer,Integer,Integer,Integer,Integer>>(){

            @Override
            public Tuple5<Integer,Integer,Integer, Integer, Integer> getKey(VehicleReport v) {
                return Tuple5.of(v.getVid(),v.getHighway(),v.getSegment(), v.getDirection(), v.getPosition());
                
            }

        }).countWindow(4,1)
        .apply(new AccidentReportWindow());
    }
    private static class AccidentReportWindow implements WindowFunction<VehicleReport, AccidentReport, Tuple5<Integer, Integer, Integer, Integer, Integer>,GlobalWindow>{
    
    @Override
    public void apply(Tuple5<Integer, Integer, Integer, Integer, Integer> key, GlobalWindow window, Iterable<VehicleReport> report, Collector<AccidentReport> col) {
        
        int numEvents= 1;
        Iterator<VehicleReport>  it = report.iterator();

        int firstEvent= it.next().getTime();
        while(it.hasNext()){
            numEvents++;
            int lastEvent = it.next().getTime();
            if(numEvents > 4){
                AccidentReport accidentReport = new AccidentReport();
                accidentReport.setTime1(firstEvent);
                accidentReport.setTime2(lastEvent);
                accidentReport.setVid(key.f0);
                accidentReport.setHighway(key.f1);
                accidentReport.setSegment(key.f2);
                accidentReport.setDirection(key.f3);
                accidentReport.setPosition(key.f4);
                col.collect(accidentReport);

            }



        }






        
        




    }
} 
}
