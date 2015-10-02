package logic;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;


/**
 * @author devinmcgloin
 * @version 10/1/15.
 */
public class ComparisonTest {

    @Test
    public void testGetProbability() throws Exception {

    }

    @Test
    public void testGetDistribution() throws Exception {

    }

    @Test
    public void testGetMedian() throws Exception {
        Random r = new Random();
        ArrayList<Double> values = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            values.add((double) i);
        }


        Assert.assertEquals(2, Comparison.getMedian(values), .00001);
    }
}