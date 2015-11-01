package funct;

import org.junit.Assert;
import org.junit.Test;
import parse.Parser;

import java.util.Random;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

/**
 * @author devinmcgloin
 * @version 10/3/15.
 */
public class PredicateTest {

    @Test
    public void testIsExpression() throws Exception {
        //count tests
        assertThat("height < 13", Parser.isExpression.apply("height < 13"), is(true));
        assertThat("height > 1.3", Parser.isExpression.apply("height > 1.3"), is(true));
        assertThat("height < 13v", Parser.isExpression.apply("height < 13v"), not(is(true)));
        assertThat("height < .13", Parser.isExpression.apply("height < .13"), not(is(true)));
        assertThat("height <- 13", Parser.isExpression.apply("height <- 13"), not(is(true)));
        assertThat("height =< 13v", Parser.isExpression.apply("height =< 13v"), not(is(true)));
        assertThat("height =< 13", Parser.isExpression.apply("height =< 13"), not(is(true)));
        assertThat("height <. 13v", Parser.isExpression.apply("height <. 13v"), not(is(true)));
        assertThat("height <= 13.0", Parser.isExpression.apply("height <= 13.0"), is(true));

        //todo measurment tests cant complete now due to lack of unit types in the db
    }


    @Test
    public void testIsLDATA() throws Exception {

    }

    @Test
    public void testIsLDATA1() throws Exception {

    }

    @Test
    public void testIsExpression1() throws Exception {

    }

    @Test
    public void testIsUnit() throws Exception {

    }

    @Test
    public void testIsUnitsEqual() throws Exception {

    }

    @Test
    public void testIsNumeric() throws Exception {
        Assert.assertTrue("0.901", Parser.isNumeric.apply("0.901"));
        Assert.assertTrue("12.901", Parser.isNumeric.apply("12.901"));
        Assert.assertTrue("0.90100", Parser.isNumeric.apply("0.90100"));
        Assert.assertTrue("0", Parser.isNumeric.apply("0"));
        Assert.assertTrue("901", Parser.isNumeric.apply("901"));
        Assert.assertTrue("901231231", Parser.isNumeric.apply("901231231"));

        Assert.assertFalse(".123", Parser.isNumeric.apply(".123"));
        Assert.assertFalse(".12.3", Parser.isNumeric.apply(".12.3"));
        Assert.assertFalse("v.123", Parser.isNumeric.apply("v.123"));
        Assert.assertFalse("v3", Parser.isNumeric.apply("v3"));
        Assert.assertFalse("vaskj", Parser.isNumeric.apply("vakjsa"));

        Random r = new Random();
        String baseString = "";

        for (int i = 0; i < 50000; i++) {
            String testString = baseString
                    + Character.getName(r.nextInt(Character.MAX_CODE_POINT))
                    + Character.getName(r.nextInt(Character.MAX_CODE_POINT))
                    + Character.getName(r.nextInt(Character.MAX_CODE_POINT))
                    + Character.getName(r.nextInt(Character.MAX_CODE_POINT))
                    + Character.getName(r.nextInt(Character.MAX_CODE_POINT));

            Assert.assertFalse(testString, Parser.isNumeric.apply(testString));
        }


    }

}