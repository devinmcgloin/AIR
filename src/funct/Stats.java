package funct;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.stat.inference.TTest;

/**
 * Created by devinmcgloin on 8/20/15.
 * Written by balzepoop109 on the/same/date.
 */
public class Stats {

    public static double tcdf(double sigma, int df){
        TTest gosset = new TTest();
        TDistribution t = new TDistribution(df);

        //System.out.println( "Student T's Cumulative Probability Distribution Function: " + t.cumulativeProbability(1.0589) );


        return t.cumulativeProbability(sigma);

    }
}
