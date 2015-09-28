package funct;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.stat.inference.TTest;

/**
 * @author devinmcgloin
 * @author balzepoop109
 * @version 8/20/15.
 */
public class Stats {

    public static double tcdf(double sigma, double df) {
        TTest gosset = new TTest();
        TDistribution t = new TDistribution(df);

        //System.out.println( "Student T's Cumulative Probability Distribution Function: " + t.cumulativeProbability(1.0589) );


        return t.cumulativeProbability(sigma);

    }
}
