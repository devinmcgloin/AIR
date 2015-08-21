package funct;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.stat.inference.TTest;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import java.util.ArrayList;

/**
 * Created by devinmcgloin on 8/20/15.
 * Written by balzepoop109 on the/same/date.
 * comment to push
 */
public class Stats {

    public static double stdDev(ArrayList<Double> values){

        double[] tmp = new double[values.size()];
        for(int i =0; i<values.size(); i++){
            tmp[i] = values.get(i);
            //System.out.println(values.get(i));
        }

        StandardDeviation obj = new StandardDeviation();
        return obj.evaluate(tmp);
    }

    public static double tcdf(double sigma, int df){
        TTest gosset = new TTest();
        TDistribution t = new TDistribution(df);

        //System.out.println( "Student T's Cumulative Probability Distribution Function: " + t.cumulativeProbability(1.0589) );


        return t.cumulativeProbability(sigma);

    }


}
