package main;


import pa.PA;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

/**
 * Alright bub,
 * This will be the "main". Nothin' fancy. No thinking. Just the terminal for talking to PA.
 */
public class Main {

    public static void main(String[] args) {
        //PA pa = new PA();
        //pa.blaze();
//        REPL repl = new REPL();
//        repl.cycle();
//        PA.save();

        Double[] items = {12.3,123.32,213.3,35.5,545.8,234.4,24.9,76.8};
        DescriptiveStatistics stats = new DescriptiveStatistics();
        for(Double item : items){
            stats.addValue(item);
        }

    }

}
